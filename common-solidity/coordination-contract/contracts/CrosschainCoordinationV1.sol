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

    // Public key version number
    uint64 constant private FIRST_PUBLIC_KEY = 0;

    // Public key & status
    uint64 private publicKeyStatus = uint64(PublicKeyStatus.KEY_CURRENT);  // 0 -> Current key
    bytes private newPublicKey;
    bytes private sidechainPublicKey;
    bytes constant private NULL_KEY = "";

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

    // The public key may be in a state of flux.
    // So it is important to understand the 'status' of the public key.
    enum PublicKeyStatus {
        KEY_CURRENT,                        // 0: This is the active public key for the sidechain which is currently in use
        KEY_PROPOSED,                       // 1: The public key that has been returned is flagged as a proposed changed key, so it is dependent on the voting before it can become active
        KEY_PREVIOUS                        // 2: This is a public that has been used previously, but is currently not in use. Note that this key could be the prior key, or an historic key
    }

    struct PublicKey {
        uint64 versionNumber;
        uint64 status;
        uint blockNumber;
        bytes key;
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

        // Public keys for this sidechain, containing additional information regarding version, status & block number
        uint64 numberOfKeys;
        mapping(uint256 => PublicKey) publicKeys;
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
    modifier onlyUnmaskedSidechainParticipant(uint256 _sidechainId) {
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

    function addSidechain(uint256 _sidechainId, address _votingAlgorithmContract, uint64 _votingPeriod, bytes calldata _publicKey) external onlyUnmaskedSidechainParticipant (MANAGEMENT_PSEUDO_SIDECHAIN_ID) {
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
        uint64 keystatus = uint64(PublicKeyStatus.KEY_CURRENT);
        sidechainPublicKey = _pubKey;
        // Version number 0 is the version number assigned when the sidechain is created
        setPublicKey(_sidechainId, FIRST_PUBLIC_KEY, keystatus, sidechainPublicKey);
        sidechains[_sidechainId].numberOfKeys = 1;
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


    function proposeVote(uint256 _sidechainId, uint16 _action, uint256 _voteTarget, uint256 _additionalInfo1, bytes calldata _additionalInfo2) external onlyUnmaskedSidechainParticipant (_sidechainId) {
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

        if (action == VoteType.VOTE_CHANGE_PUBLIC_KEY) {
            // The proposed public key is assumed to be valid.
            // Set up the detail for the proposed key to be associated with this sidechain
            uint64 sidechainsNoOfKeys = sidechains[_sidechainId].numberOfKeys;
            uint64 keyVersion = sidechains[_sidechainId].publicKeys[sidechainsNoOfKeys-1].versionNumber;      // Mapping starts at 0 not 1
          //  sidechains[_sidechainId].publicKeys[sidechainsNoOfKeys].versionNumber = keyVersion + 1;
          //  sidechains[_sidechainId].publicKeys[sidechainsNoOfKeys].status = uint64(PublicKeyStatus.KEY_PROPOSED);
          //  sidechains[_sidechainId].publicKeys[sidechainsNoOfKeys].blockNumber = block.number;
          //  sidechains[_sidechainId].publicKeys[sidechainsNoOfKeys].key = _additionalInfo2;
          //  sidechains[_sidechainId].numberOfKeys += 1;
            setPublicKey(_sidechainId, (keyVersion + 1), uint64(PublicKeyStatus.KEY_PROPOSED), _additionalInfo2);
      }

        // Set-up the vote.
        sidechains[_sidechainId].votes[_voteTarget].voteType = action;
        sidechains[_sidechainId].votes[_voteTarget].endOfVotingBlockNumber = block.number + sidechains[_sidechainId].votingPeriod;
        sidechains[_sidechainId].votes[_voteTarget].additionalInfo1 = _additionalInfo1;
        sidechains[_sidechainId].votes[_voteTarget].additionalInfo2 = _additionalInfo2;

        // The proposer is deemed to be voting for the proposal.
        voteNoChecks(_sidechainId, _action, _voteTarget, true);
    }


    function vote(uint256 _sidechainId, uint16 _action, uint256 _voteTarget, bool _voteFor) external onlyUnmaskedSidechainParticipant (_sidechainId) {
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


    function actionVotes(uint256 _sidechainId, uint256 _voteTarget) external onlyUnmaskedSidechainParticipant(_sidechainId) {
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
                newPublicKey = sidechains[_sidechainId].votes[_voteTarget].additionalInfo2;  // new public key
                publicKeyStatus = uint64(PublicKeyStatus.KEY_CURRENT);
                // Change the current active public key to the one voted on
                changePublicKey(_sidechainId, newPublicKey);

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


     /**
     * Get blockchain's public key, version number, status and block number
     *
     * @param _sidechainId The 256 bit sidechain identifier to which this public key belongs
     * @return public key information for the currently active public key for the blockchain
     *         Value      Status
     *           0        This is the active public key for the sidechain which is currently in use
     *           1        The public key that has been returned is flagged as a proposed changed key, so it is dependent on the voting before it can become active
     *           2        This is a public key that has been used previously, but is currently not in use. Note that this key could be the prior key, or an historic key
     */
    function getPublicKey(uint256 _sidechainId) external view returns ( uint64 _versionNumber, uint64 _status, uint _blockNumber, bytes memory _key){

        (uint64 vsn, uint64 stat, uint256 blk, bytes memory key) = getLastActivePublicKey(_sidechainId);

        return (vsn, stat, blk, key);
    }


    /**
      * Return the details of the public key for a specific version number: status, block number and public key
      */
    function getVersionOfPublicKey(uint256 _sidechainId, uint64 _versionNumber) external view returns (uint64 _status, uint _blockNumber, bool _keyFound, bytes memory _key) {

        (uint64 stat, uint256 blk, bool found, bytes memory key) = checkPublicKeyVersion(_sidechainId,_versionNumber);

        return (stat, blk, found, key);
    }


    function checkPublicKeyVersion (uint256 _sidechainId, uint64 _versionNumber) private view returns (uint64 _status, uint _blockNumber, bool keyFound,  bytes memory _key) {
        uint64 sidechainsNoOfKeys = sidechains[_sidechainId].numberOfKeys;
        uint64 stat;
        uint blk;
        bool found = false;
        bytes memory key = NULL_KEY;
        uint64 i = 0;

        while (!found && i < sidechainsNoOfKeys){
            if (sidechains[_sidechainId].publicKeys[i].versionNumber == _versionNumber) {
                found = true;
                stat = sidechains[_sidechainId].publicKeys[i].status;
                blk = sidechains[_sidechainId].publicKeys[i].blockNumber;
                key = sidechains[_sidechainId].publicKeys[i].key;
            }
            i++;
        }

        return (stat, blk, found, key);
    }

    /**
     * Get array of Sidechain's public key, version number, status and block number
     *
     * @param _sidechainId The 256 bit sidechain identifier to which this public key belongs
     * @return an array of public keys for the sidechain corresponding to the 3 different states it can be in:
     *         Value      Status
     *           0        This is the active public key for the sidechain which is currently in use
     *           1        The public key that has been returned is flagged as a proposed changed key, so it is dependent on the voting before it can become active
     *           2        This is a public key that has been used previously, but is currently not in use. Note that this key could be the prior key, or an historic key
     */
    function getPublicKeyArray (uint256 _sidechainId) private view returns ( PublicKey [] memory _publicKeyArray){
        uint64 sidechainsNoOfKeys = sidechains[_sidechainId].numberOfKeys;  // If sidechain does not yet exist, then the number of keys will have the default value of 0.
        PublicKey [] memory publicKeyArray;                                 // This should initialise all fields in the struct to default values, e.g. 0 for uint
        for (uint i = 0; i < sidechainsNoOfKeys; i++) {
            publicKeyArray[i] = sidechains[_sidechainId].publicKeys[i];
        }
        return(publicKeyArray);
    }

    /**
     * Get the last active key for the sidechain
     *
     * @param _sidechainId The 256 bit sidechain identifier to which this public key belongs
     * @return _activeKey latest active public key
     */
    function getLastActivePublicKey(uint256 _sidechainId) private view returns ( uint64 _versionNumber, uint64 _status, uint _blockNumber, bytes memory _key){
        // If there is exactly one public key then this is the one assigned when the sidechain was created & we would expect it to have a status of active
        PublicKey [] memory publicKeyArray = getPublicKeyArray(_sidechainId);
        uint64 sidechainsNoOfKeys = sidechains[_sidechainId].numberOfKeys;

        // If the sidechain does not exist, return null and zero values, and indicate in the status that the public key is not a proposed or current key
        if (sidechainsNoOfKeys == 0 || publicKeyArray.length == 0){
            return(0,uint64(PublicKeyStatus.KEY_PREVIOUS),0,NULL_KEY);
        } else {
            bool active = false;
            uint i;

            for ( i = sidechainsNoOfKeys; (i == 0 || active); i--) {
                publicKeyArray[i-1] = sidechains[_sidechainId].publicKeys[i-1];
                if (publicKeyArray[i-1].status == uint64(PublicKeyStatus.KEY_CURRENT)) {
                    active = true;
                }
            }
            // using i, not (i-1) since i would have been decreased at the end of the for loop
            return(publicKeyArray[i].versionNumber, publicKeyArray[i].status, publicKeyArray[i].blockNumber, publicKeyArray[i].key);
        }
    }

    /**
     * Set the Sidechain's public key, version, status & block number
     *
     * @param  _sidechainId    The 256 bit sidechain identifier to which this public key belongs
     * @param  _publicKey      The new public key for the sidechain
     */
    function setPublicKey(uint256 _sidechainId, uint64 _versionNumber, uint64 _status, bytes memory _publicKey) private {
       // Check if we are adding or replacing a public key entry
        uint64 index = sidechains[_sidechainId].numberOfKeys;
        // Need to replace entry and not add an entry - assume that we are updating the block number too
        //TODO - CHANGE THE CHECK HERE, SINCE VERSION NUMBER IS NOT NECESSARILY EQUIVALENT TO THE NO OF KEYS - PR SAYS IT IS PASSED IN THROUGH ADDITIONALINFO
        if (index == _versionNumber){
            sidechains[_sidechainId].publicKeys[index-1].status = _status;
            sidechains[_sidechainId].publicKeys[index-1].blockNumber = block.number;
        } else {
            sidechains[_sidechainId].publicKeys[index].versionNumber = _versionNumber;
            sidechains[_sidechainId].publicKeys[index].status = _status;
            sidechains[_sidechainId].publicKeys[index].blockNumber = block.number;
            sidechains[_sidechainId].publicKeys[index].key = _publicKey;
            sidechains[_sidechainId].numberOfKeys = index + 1;
        }
    }

    /**
        * There was a majority vote to change the public key of the Sidechain, so change the public key as has been voted on
        *
        * @param  _sidechainId     The 256 bit sidechain identifier to which this public key belongs
        * @param  _newPublicKey    The new public key that has been proposed for the sidechain
        */
    function changePublicKey(uint256 _sidechainId, bytes memory _newPublicKey) private {
        // TODO - Ensure that the current key matches what we expect it to be before we get a change underway
        // Note: If the latest key for the sidechain is one that is not yet active, we need to replace it, signifying it is now active, rather than adding to the array of keys

        (uint64 vsn, uint64 stat, uint256 blk, bytes memory activePublickey) = getLastActivePublicKey(_sidechainId);

        uint64 totKeys = sidechains[_sidechainId].numberOfKeys;

        // If the getActivePublicKey returns a null key, then we know that no public kry exists for this sidechain
        if (publicKeysEqual(activePublickey, NULL_KEY)){
            setPublicKey(_sidechainId, FIRST_PUBLIC_KEY, uint64 (PublicKeyStatus.KEY_CURRENT), _newPublicKey);
        } else {
            require(totKeys >=1, "Inconsistent information: total no of keys for sidechain is = 0, but the public key returned as being active, is not null.");
            bytes memory currentKey = sidechains[_sidechainId].publicKeys[totKeys-1].key;
            uint currentKeyBlkNo = sidechains[_sidechainId].publicKeys[totKeys-1].blockNumber;
            uint64 currentKeyStatus = sidechains[_sidechainId].publicKeys[totKeys-1].status;
            uint64 currentVersionNumber = sidechains[_sidechainId].publicKeys[totKeys-1].versionNumber;

            // -----------------------------------------------------------------------------------------------------------------------------------------------
            // The commented out code below has been replaced by checking the public keys. If that doesn't work, then some of the code below may again be used
            // -----------------------------------------------------------------------------------------------------------------------------------------------
            // Check if the most current key in the array has a status of active. If it has then its status is set to previous & a new key is added
    //        if (currentKeyStatus == uint64 (PublicKeyStatus.KEY_CURRENT)){
    //            require(currentKeyBlkNo == blk);           // If the current key is active, then we expect the last active key to be the current key
    //            sidechains[_sidechainId].publicKeys[totKeys-1].status = uint64 (PublicKeyStatus.KEY_PREVIOUS); // current key becomes a historic key
    //            setPublicKey(_sidechainId, currentVersionNumber + 1, uint64 (PublicKeyStatus.KEY_CURRENT), _newPublicKey);
            // If the most current key is not active, then we expect it to be in the process of being changed, as you would expect the latest one to either be active or in the process of being changed
    //        } else {
    //           require(currentKeyStatus == uint256(PublicKeyStatus.KEY_PROPOSED));
    //            setPublicKey(_sidechainId, currentVersionNumber, uint64 (PublicKeyStatus.KEY_CURRENT), _newPublicKey);
    //       }
            // -----------------------------------------------------------------------------------------------------------------------------------------------


            // There are two possible scenarios: the active key is the current key, or the current key is 'in progress'.
            // Anything else is an error, e.g. Would not expect it to be set to previous, unless it is an empty key, i.e. no key has been added for sidechain yet

            // TODO - How do you compare public keys in the dynamic bytes arrays????
            // TODO   I've created a function, but need to test if it works as expected
            // --------------------------------------------------------------------------
            // If the current public key is the active public key then we need to add the new key and set the current one to being a previous key
            if (publicKeysEqual(activePublickey, currentKey)) {
                require(currentKeyBlkNo == blk,"The active key equals the current key, but the block numbers differ.");  // If the current key is active, then we expect the last active key to be the current key
                sidechains[_sidechainId].publicKeys[totKeys-1].status = uint64 (PublicKeyStatus.KEY_PREVIOUS);           // current key becomes a historic key
                setPublicKey(_sidechainId, currentVersionNumber + 1, uint64 (PublicKeyStatus.KEY_CURRENT), _newPublicKey);
            } else {
                // The new public key is the latest entry in the public key array, so we expect the status to be 'proposed'
                if (publicKeysEqual(currentKey, _newPublicKey)) {
                    require(currentKeyStatus == uint64(PublicKeyStatus.KEY_PROPOSED), "Expected current public key to have a status of proposed.");
                    setPublicKey(_sidechainId, currentVersionNumber, uint64 (PublicKeyStatus.KEY_CURRENT), _newPublicKey);
                } else {
                    if (publicKeysEqual(currentKey, NULL_KEY)){
                        setPublicKey(_sidechainId, currentVersionNumber, uint64 (PublicKeyStatus.KEY_CURRENT), _newPublicKey);
                    }
                }
            }
        }
    }


    function getVersion() external pure returns (uint16) {
        return VERSION_ONE;
    }

    function publicKeysEqual(bytes memory _firstKey,bytes memory _secondKey ) private pure returns (bool){

        uint256 keyLength_1 = _firstKey.length;
        uint256 keyLength_2 = _secondKey.length;
        bool equal = false;
        uint64 i = 0;

        if (keyLength_1 != keyLength_2) {
          return (false);
        } else {
            while (!equal && i < keyLength_1){
                if (_firstKey[i] != _secondKey[i]) {
                    equal = false ;
                }
                i++;
            }
            return(equal);
        }
    }

}