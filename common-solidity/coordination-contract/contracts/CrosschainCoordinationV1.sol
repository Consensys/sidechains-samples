/*
 * Copyright 2019 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
pragma solidity >=0.4.23;

import "./CrosschainCoordinationInterface.sol";
import "./VotingAlgInterface.sol";


/**
 * Contract to manage multiple sidechains.
 *
 * Please see the interface for documentation on all topics except for the constructor.
 *
 */
contract CrosschainCoordinationV1 is CrosschainCoordinationInterface {
    // Implementation version of the of the Crosschain Coordination Contract.
    uint16 constant private VERSION_ONE = 1;

    // The management sidechain is the sidechain with ID 0x00. It is used solely to restrict which
    // users can create a new sidechain. Only members of this sidechain can call addSidechain().
    uint256 public constant MANAGEMENT_PSEUDO_SIDECHAIN_ID = 0;


    // Indications that a vote is underway.
    // VOTE_NONE indicates no vote is underway. Also matches the deleted value for integers.
    enum VoteType {
        VOTE_NONE,                          // 0: MUST be the first value so it is the zero / deleted value.
        VOTE_ADD_MASKED_PARTICIPANT,        // 1
        VOTE_REMOVE_MASKED_PARTICIPANT,     // 2
        VOTE_ADD_UNMASKED_PARTICIPANT,      // 3
        VOTE_REMOVE_UNMASKED_PARTICIPANT,   // 4
        VOTE_CHANGE_PUBLIC_KEY              // 5
    }

    struct Votes {
        // The type of vote being voted on.
        VoteType voteType;
        // The block number when voting will cease.
        uint endOfVotingBlockNumber;
        // Additional info contain additional values which are type
        // of vote specific.
        uint256 additionalInfo1;
        bytes additionalInfo2;

        // Have map as well as array to ensure constant time / constant =cost look-up,
        // independent of number of participants.
        mapping(address=>bool) hasVoted;
        // The number of participants who voted for the proposal.
        uint64 numVotedFor;
        // The number of participants who voted against the proposal.
        uint64 numVotedAgainst;
    }

    struct SidechainRecord {
        // The algorithm for assessing the votes.
        address votingAlgorithmContract;
        // Voting period in blocks. This is the period in which participants can vote. Must be greater than 0.
        uint64 votingPeriod;

        // The number of unmasked participants.
        // Note that this value could be less than the size of the unmasked array as some of the participants
        // may have been removed.
        uint64 numUnmaskedParticipants;
        // Array of participants who can vote.
        // Note that this array could contain empty values, indicating that the participant has been removed.
        address[] unmasked;
        // Have map as well as array to ensure constant time / constant cost look-up, independent of number of participants.
        mapping(address=>bool) inUnmasked;

        // Array of masked participant. These participants can not vote.
        // Note that this array could contain empty values, indicating that the masked participant has been removed
        // or has been unmasked.
        uint256[] masked;
        // Have map as well as array to ensure constant time / constant cost look-up, independent of number of participants.
        mapping(uint256=>bool) inMasked;

        // Votes for adding and removing participants, for changing voting algorithm and voting period.
        mapping(uint256=>Votes) votes;

        bytes publicKey;
    }

    mapping(uint256=>SidechainRecord) private sidechains;


    enum XTX_STATE {
        NOT_STARTED,        // 0
        STARTED,            // 1
        COMMITTED,          // 2
        IGNORED             // 3

    }

    struct CrosschainTransaction {
        XTX_STATE state;
        uint256 timeoutBlockNumber;
    }
    mapping(uint256=>CrosschainTransaction) private txMap;





    /**
     * Function modifier to ensure only unmasked sidechain participants can call the function.
     *
     * @param _sidechainId The 256 bit identifier of the Sidechain.
     * @dev Throws if the message sender isn't a participant in the sidechain, or if the sidechain doesn't exist.
     */
    modifier onlySidechainParticipant(uint256 _sidechainId) {
        require(sidechains[_sidechainId].inUnmasked[msg.sender]);
        _;
    }

    /**
     * Set the management pseudo chain configuration and global configuration.
     *
     * @param _votingAlg Management pseudo chain voting algorithm.
     * @param _votingPeriod Management pseudo chain voting period in blocks.
     */
    constructor (address _votingAlg, uint32 _votingPeriod) public {
        bytes memory pubKey = "";
        addSidechainInternal(MANAGEMENT_PSEUDO_SIDECHAIN_ID, _votingAlg, _votingPeriod, pubKey);
    }


    // TODO an Add Sidechain method where an array of masked and unmasked participants are added.
    //      Instead of using the addSidechain Internal function, code up a function that takes an array of participants (masked & unmasked) - add to interface

    function addSidechain(uint256 _sidechainId, address _votingAlgorithmContract, uint64 _votingPeriod, bytes calldata _publicKey) external onlySidechainParticipant(MANAGEMENT_PSEUDO_SIDECHAIN_ID) {
        bytes memory pubKey = _publicKey;
        addSidechainInternal(_sidechainId, _votingAlgorithmContract, _votingPeriod, pubKey);
    }

    function addSidechainInternal(uint256 _sidechainId, address _votingAlgorithmContract, uint64 _votingPeriod, bytes memory _pubKey) private {
        // The sidechain can not exist prior to creation.
        require(sidechains[_sidechainId].votingPeriod == 0);
        // The voting period must be greater than 0.
        require(_votingPeriod > 0);
        emit AddedSidechain(_sidechainId);

        // Create the entry in the map by assigning values to the structure.
        sidechains[_sidechainId].votingPeriod = _votingPeriod;
        sidechains[_sidechainId].votingAlgorithmContract = _votingAlgorithmContract;

        // The creator of the sidechain is always an unmasked participant. Anyone who analysed the
        // transaction history would be able determine this account as the one which instigated the
        // transaction.
        sidechains[_sidechainId].unmasked.push(msg.sender);
        sidechains[_sidechainId].inUnmasked[msg.sender] = true;
        sidechains[_sidechainId].numUnmaskedParticipants++;

        sidechains[_sidechainId].publicKey = _pubKey;
    }


    function unmask(uint256 _sidechainId, uint256 _index, uint256 _salt) external {
        uint256 maskedParticipantActual = sidechains[_sidechainId].masked[_index];
        uint256 maskedParticipantCalculated = uint256(keccak256(abi.encodePacked(msg.sender, _salt)));
        // An account can only unmask itself.
        require(maskedParticipantActual == maskedParticipantCalculated);
        // If the unmasked participant already exists, then remove the participant
        // from the masked list and don't add it to the unmasked list.
        if (sidechains[_sidechainId].inUnmasked[msg.sender] == false) {
            emit AddingSidechainUnmaskedParticipant(_sidechainId, msg.sender);
            sidechains[_sidechainId].unmasked.push(msg.sender);
            sidechains[_sidechainId].inUnmasked[msg.sender] = true;
            sidechains[_sidechainId].numUnmaskedParticipants++;
        }
        delete sidechains[_sidechainId].masked[_index];
        delete sidechains[_sidechainId].inMasked[maskedParticipantActual];
    }


    function proposeVote(uint256 _sidechainId, uint16 _action, uint256 _voteTarget, uint256 _additionalInfo1, bytes calldata _additionalInfo2) external onlySidechainParticipant(_sidechainId) {
        // This will throw an error if the action is not a valid VoteType.
        VoteType action = VoteType(_action);

        // Can't start a vote if a vote is already underway.
        require(sidechains[_sidechainId].votes[_voteTarget].voteType == VoteType.VOTE_NONE);

        // If the action is to add a masked participant, then they shouldn't be a participant already.
        if (action == VoteType.VOTE_ADD_MASKED_PARTICIPANT) {
            require(sidechains[_sidechainId].inMasked[_voteTarget] == false);
        }
        // If the action is to remove a masked participant, then they should be a participant already.
        // Additionally, they must supply the offset into the masked array of the participant to be removed.
        if (action == VoteType.VOTE_REMOVE_MASKED_PARTICIPANT) {
            require(sidechains[_sidechainId].inMasked[_voteTarget] == true);
            require(sidechains[_sidechainId].masked[_additionalInfo1] == _voteTarget);
        }
        // If the action is to add an unmasked participant, then they shouldn't be a participant already.
        if (action == VoteType.VOTE_ADD_UNMASKED_PARTICIPANT) {
            require(sidechains[_sidechainId].inUnmasked[address(_voteTarget)] == false);
        }
        // If the action is to remove an unmasked participant, then they should be a participant
        // already and they can not be the sender. That is, the sender can not vote to remove
        // themselves.
        // Additionally, they must supply the offset into the unmasked array of the participant to be removed.
        if (action == VoteType.VOTE_REMOVE_UNMASKED_PARTICIPANT) {
            address voteTargetAddr = address(_voteTarget);
            require(sidechains[_sidechainId].inUnmasked[voteTargetAddr] == true);
            require(voteTargetAddr != msg.sender);
            require(sidechains[_sidechainId].unmasked[_additionalInfo1] == voteTargetAddr);
        }

//  TODO - Add change of public key to interface, work out what other actions need to be taken when changing the public key
//        if (action == VoteType.VOTE_CHANGE_PUBLIC_KEY) {
            // The public key has been assumed to be valid.
            // TODO are there any checks which need to be done?
  //      }


        // Set-up the vote.
        sidechains[_sidechainId].votes[_voteTarget].voteType = action;
        sidechains[_sidechainId].votes[_voteTarget].endOfVotingBlockNumber = block.number + sidechains[_sidechainId].votingPeriod;
        sidechains[_sidechainId].votes[_voteTarget].additionalInfo1 = _additionalInfo1;
        sidechains[_sidechainId].votes[_voteTarget].additionalInfo2 = _additionalInfo2;

        // The proposer is deemed to be voting for the proposal.
        voteNoChecks(_sidechainId, _action, _voteTarget, true);
    }


    function vote(uint256 _sidechainId, uint16 _action, uint256 _voteTarget, bool _voteFor) external onlySidechainParticipant(_sidechainId) {
        // This will throw an error if the action is not a valid VoteType.
        VoteType action = VoteType(_action);

        // The type of vote must match what is currently being voted on.
        // Note that this will catch the case when someone is voting when there is no active vote.
        require(sidechains[_sidechainId].votes[_voteTarget].voteType == action);
        // Ensure the account has not voted yet.
        require(sidechains[_sidechainId].votes[_voteTarget].hasVoted[msg.sender] == false);

        // Check voting period has not expired.
        require(sidechains[_sidechainId].votes[_voteTarget].endOfVotingBlockNumber >= block.number);

        voteNoChecks(_sidechainId, _action, _voteTarget, _voteFor);
    }


    function actionVotes(uint256 _sidechainId, uint256 _voteTarget) external onlySidechainParticipant(_sidechainId) {
        // If no vote is underway, then there is nothing to action.
        VoteType action = sidechains[_sidechainId].votes[_voteTarget].voteType;
        require(action != VoteType.VOTE_NONE);
        // Can only action vote after voting period has ended.
        require(sidechains[_sidechainId].votes[_voteTarget].endOfVotingBlockNumber < block.number);

        VotingAlgInterface voteAlg = VotingAlgInterface(sidechains[_sidechainId].votingAlgorithmContract);
        bool result = voteAlg.assess(
                sidechains[_sidechainId].numUnmaskedParticipants,
                sidechains[_sidechainId].votes[_voteTarget].numVotedFor,
                sidechains[_sidechainId].votes[_voteTarget].numVotedAgainst);
        emit VoteResult(_sidechainId, uint16(action), _voteTarget, result);

        if (result) {
            // The vote has been decided in the affimative.
            uint256 additionalInfo1 = sidechains[_sidechainId].votes[_voteTarget].additionalInfo1;
            address participantAddr = address(_voteTarget);
            if (action == VoteType.VOTE_ADD_UNMASKED_PARTICIPANT) {
                sidechains[_sidechainId].unmasked.push(participantAddr);
                sidechains[_sidechainId].inUnmasked[participantAddr] = true;
                sidechains[_sidechainId].numUnmaskedParticipants++;
            }
            else if (action == VoteType.VOTE_ADD_MASKED_PARTICIPANT) {
                sidechains[_sidechainId].masked.push(_voteTarget);
                sidechains[_sidechainId].inMasked[_voteTarget] = true;
            }
            else if (action == VoteType.VOTE_REMOVE_UNMASKED_PARTICIPANT) {
                delete sidechains[_sidechainId].unmasked[additionalInfo1];
                delete sidechains[_sidechainId].inUnmasked[participantAddr];
                sidechains[_sidechainId].numUnmaskedParticipants--;
            }
            else if (action == VoteType.VOTE_REMOVE_MASKED_PARTICIPANT) {
                delete sidechains[_sidechainId].masked[additionalInfo1];
                delete sidechains[_sidechainId].inMasked[_voteTarget];
            }
            else if (action == VoteType.VOTE_CHANGE_PUBLIC_KEY) {
                //TODO we should allow the old public key to be used for a limited amount of time,
                // so there is some change over period from the old to the new
                sidechains[_sidechainId].publicKey = sidechains[_sidechainId].votes[_voteTarget].additionalInfo2;
            }
        }


        // The vote is over. Now delete the voting arrays and indicate there is no vote underway.
        // Remove all values from the map: Maps can't be deleted in Solidity.
        // NOTE: The code below has used values directly, rather than a local variable due to running
        // out of local variables.
        for (uint i = 0; i < sidechains[_sidechainId].unmasked.length; i++) {
            if( sidechains[_sidechainId].unmasked[i] != address(0)) {
                delete sidechains[_sidechainId].votes[_voteTarget].hasVoted[sidechains[_sidechainId].unmasked[i]];
            }
        }
        // This will recursively delete everything in the structure, except for the map, which was
        // deleted in the for loop above.
        delete sidechains[_sidechainId].votes[_voteTarget];
    }








    /**
    * This function is used to indicate that an entity has voted. It has been created so that
    * calls to proposeVote do not have to incur all of the value checking in the vote call.
    *
    * TODO: Compare gas usage of keeping this integrated with the value checking.
    *       What is the trade-off of having one large structure, as opposed to separate structure (low priority)
    */
    function voteNoChecks(uint256 _sidechainId, uint16 _action, uint256 _voteTarget, bool _voteFor) private {
        // Indicate msg.sender has voted.
        emit ParticipantVoted(_sidechainId, msg.sender, _action, _voteTarget, _voteFor);
        sidechains[_sidechainId].votes[_voteTarget].hasVoted[msg.sender] = true;

        if (_voteFor) {
            sidechains[_sidechainId].votes[_voteTarget].numVotedFor++;
        } else {
            sidechains[_sidechainId].votes[_voteTarget].numVotedAgainst++;
        }
    }




    /**
    * Start the Crosschain Transaction.
    *
    *
    */
    // TODO should anyone be able to start, commit or ignore? Should it be limited to sidechain unmasked participants?
    function start(uint256 _originatingSidechainId, uint256 _crosschainTransactionId,
        bytes calldata /*_signedStartMessage */, uint256 _transactionTimeoutBlock) external {
        uint256 index = uint256(keccak256(abi.encodePacked(_originatingSidechainId, _crosschainTransactionId)));

        // The transaction id can not be re-used and the time-out block must be in the future.
        require(txMap[index].state == XTX_STATE.NOT_STARTED);
        require(_transactionTimeoutBlock > block.number);

        // TODO: validate the signed start message. Need to work out exact structure of message still.
        // TODO check signature verifies, given the public key of the originating sidechain. (could be a private method) Callout to precompile of a BLS signature. Use the functions coded by Peter & John. 
        // TODO check that the originating sidechain id in the message matches the parameter value.
        // TODO check that the Coordination Blockchain Identifier in the message matches the parameter value.
        // TODO check that the Crosschain Coordination Contract address in the message matches the parameter value.
        //   use   address(this)
        // TODO check that the time-out block number matches.
        // TODO check that the it is a start message.


        txMap[index].state = XTX_STATE.STARTED;
        txMap[index].timeoutBlockNumber = _transactionTimeoutBlock;

//        emit Dump2(index, block.number, txMap[index].timeoutBlockNumber, uint256(txMap[index].state));
    }

    /**
     * Commit the Crosschain Transaction.
     */
    function commit(uint256 _originatingSidechainId, uint256 _crosschainTransactionId, bytes calldata /*_signedCommitMessage*/) external {
        uint256 index = uint256(keccak256(abi.encodePacked(_originatingSidechainId, _crosschainTransactionId)));
        // The transaction must be started and the time-out block must be in the future.
        require(txMap[index].state == XTX_STATE.STARTED);
        require(txMap[index].timeoutBlockNumber >= block.number);


        // TODO validate the signed commit message.
        // TODO check signature verifies, given the public key of the originating sidechain.
        // TODO check that the originating sidechain id in the message matches the parameter value.
        // TODO check that the Coordination Blockchain Identifier in the message matches the parameter value.
        // TODO check that the Crosschain Coordination Contract address in the message matches the parameter value.
        //   use   address(this)
        // TODO check that the it is a commit message.


        txMap[index].state = XTX_STATE.COMMITTED;
    }


    /**
     * Ignore the Crosschain Transaction.
     */
    function ignore(uint256 _originatingSidechainId, uint256 _crosschainTransactionId, bytes calldata /*_signedIgnoreMessage*/) external {
        uint256 index = uint256(keccak256(abi.encodePacked(_originatingSidechainId, _crosschainTransactionId)));
        // The transaction must be started and the time-out block must be in the future.
        require(txMap[index].state == XTX_STATE.STARTED);
        require(txMap[index].timeoutBlockNumber >= block.number);

        // TODO validate the signed ignore message.
        // TODO check signature verifies, given the public key of the originating sidechain.
        // TODO check that the originating sidechain id in the message matches the parameter value.
        // TODO check that the Coordination Blockchain Identifier in the message matches the parameter value.
        // TODO check that the Crosschain Coordination Contract address in the message matches the parameter value.
        //   use   address(this)
        // TODO check that the it is a commit message.


        txMap[index].state = XTX_STATE.IGNORED;
    }



    function getCrosschainTransactionStatus(uint256 _originatingSidechainId, uint256 _crosschainTransactionId) external view returns (uint32) {
        uint256 index = uint256(keccak256(abi.encodePacked(_originatingSidechainId, _crosschainTransactionId)));
        if (txMap[index].state == XTX_STATE.COMMITTED) {
            return uint32(XTX_STATE.COMMITTED);
        }
        // Note that "not started" means that this Crosschain Coordination Contract has not
        // heard of this Atomic Crosschain Transaction.
        if (txMap[index].state == XTX_STATE.NOT_STARTED) {
            return uint32(XTX_STATE.NOT_STARTED);
        }
        if (txMap[index].timeoutBlockNumber < block.number) {
            return uint32(XTX_STATE.IGNORED);
        }
        return uint32(txMap[index].state);
    }

    function getCrosschainTransactionTimeout(uint256 _originatingSidechainId, uint256 _crosschainTransactionId) external view returns (uint256) {
        uint256 index = uint256(keccak256(abi.encodePacked(_originatingSidechainId, _crosschainTransactionId)));
        return txMap[index].timeoutBlockNumber;
    }

    function getBlockNumber() external view returns (uint256) {
        return block.number;
    }



    function getSidechainExists(uint256 _sidechainId) external view returns (bool) {
        return sidechains[_sidechainId].votingPeriod != 0;
    }


    function getVotingPeriod(uint256 _sidechainId) external view returns (uint64) {
        return sidechains[_sidechainId].votingPeriod;
    }


    function isUnmaskedSidechainParticipant(uint256 _sidechainId, address _participant) external view returns(bool) {
        return sidechains[_sidechainId].inUnmasked[_participant];
    }


    function getUnmaskedSidechainParticipantsSize(uint256 _sidechainId) external view returns(uint256) {
        return sidechains[_sidechainId].unmasked.length;
    }


    function getUnmaskedSidechainParticipant(uint256 _sidechainId, uint256 _index) external view returns(address) {
        return sidechains[_sidechainId].unmasked[_index];
    }


    function getMaskedSidechainParticipantsSize(uint256 _sidechainId) external view returns(uint256) {
        return sidechains[_sidechainId].masked.length;
    }


    function getMaskedSidechainParticipant(uint256 _sidechainId, uint256 _index) external view returns(uint256) {
        return sidechains[_sidechainId].masked[_index];
    }

    function getPublicKey(uint256 _sidechainId) external view returns (bytes memory) {
        bytes memory pubKey = sidechains[_sidechainId].publicKey;
        return pubKey;
    }

    function getVersion() external pure returns (uint16) {
        return VERSION_ONE;
    }

}