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
 * Contract to manage multiple blockchains.
 *
 * Please see the interface for documentation on all topics except for the constructor.
 *
 */
contract CrosschainCoordinationV1 is CrosschainCoordinationInterface {
    // Implementation version of the of the Crosschain Coordination Contract.
    uint16 constant private VERSION_ONE = 1;

    // The management blockchain is the blockchain with ID 0x00. It is used solely to restrict which
    // users can create a new blockchain. Only members of this blockchain can call addBlockchain().
    uint256 public constant MANAGEMENT_PSEUDO_BLOCKCHAIN_ID = 0;

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

    struct PublicKey {
        // Block number this key became active.
        uint256 blockNumber;
        // RLP List containing the key and information about the key.
        // keyVersion
        // threshold
        // algorithm
        // Encoded publicKey point
        // blockchainId
        bytes encodedKey;
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

    struct BlockchainRecord {
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

        // Public keys for this blockchain.
        uint64 publicKeyActiveVersion;
        uint64 publicKeyPreviousVersion;
        mapping(uint64 => PublicKey) publicKeys;
    }

    mapping(uint256=>BlockchainRecord) private blockchains;


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
     * Function modifier to ensure only unmasked blockchain participants can call the function.
     *
     * @param _blockchainId The 256 bit identifier of the Blockchain.
     * @dev Throws if the message sender isn't a participant in the blockchain, or if the blockchain doesn't exist.
     */
    modifier onlyUnmaskedBlockchainParticipant(uint256 _blockchainId) {
        require(blockchains[_blockchainId].inUnmasked[msg.sender]);
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
        addBlockchainInternal(MANAGEMENT_PSEUDO_BLOCKCHAIN_ID, _votingAlg, _votingPeriod, 0, pubKey);
    }


    // TODO an Add Blockchain method where an array of masked and unmasked participants are added.
    //      Instead of using the addBlockchain Internal function, code up a function that takes an array of participants (masked & unmasked) - add to interface

    function addBlockchain(uint256 _blockchainId, address _votingAlgorithmContract, uint64 _votingPeriod, uint64 _keyVersion, bytes calldata _publicKey) external onlyUnmaskedBlockchainParticipant (MANAGEMENT_PSEUDO_BLOCKCHAIN_ID) {
        bytes memory pubKey = _publicKey;
        addBlockchainInternal(_blockchainId, _votingAlgorithmContract, _votingPeriod, _keyVersion, pubKey);
    }

    function addBlockchainInternal(uint256 _blockchainId, address _votingAlgorithmContract, uint64 _votingPeriod, uint64 _keyVersion, bytes memory _pubKey) private {
        // The blockchain can not exist prior to creation.
        require(blockchains[_blockchainId].votingPeriod == 0);
        // The voting period must be greater than 0.
        require(_votingPeriod > 0);
        emit AddedBlockchain(_blockchainId);

        // Create the entry in the map by assigning values to the structure.
        blockchains[_blockchainId].votingPeriod = _votingPeriod;
        blockchains[_blockchainId].votingAlgorithmContract = _votingAlgorithmContract;

        // The creator of the blockchain is always an unmasked participant. Anyone who analysed the
        // transaction history would be able determine this account as the one which instigated the
        // transaction.
        blockchains[_blockchainId].unmasked.push(msg.sender);
        blockchains[_blockchainId].inUnmasked[msg.sender] = true;
        blockchains[_blockchainId].numUnmaskedParticipants++;

        // Add the key - no voting required for the first key.
        setPublicKey(_blockchainId, _keyVersion, _pubKey);
    }


    function unmask(uint256 _blockchainId, uint256 _index, uint256 _salt) external {
        uint256 maskedParticipantActual = blockchains[_blockchainId].masked[_index];
        uint256 maskedParticipantCalculated = uint256(keccak256(abi.encodePacked(msg.sender, _salt)));
        // An account can only unmask itself.
        require(maskedParticipantActual == maskedParticipantCalculated);
        // If the unmasked participant already exists, then remove the participant
        // from the masked list and don't add it to the unmasked list.
        if (blockchains[_blockchainId].inUnmasked[msg.sender] == false) {
            emit AddingBlockchainUnmaskedParticipant(_blockchainId, msg.sender);
            blockchains[_blockchainId].unmasked.push(msg.sender);
            blockchains[_blockchainId].inUnmasked[msg.sender] = true;
            blockchains[_blockchainId].numUnmaskedParticipants++;
        }
        delete blockchains[_blockchainId].masked[_index];
        delete blockchains[_blockchainId].inMasked[maskedParticipantActual];
    }


    function proposeVote(uint256 _blockchainId, uint16 _action, uint256 _voteTarget, uint256 _additionalInfo1, bytes calldata _additionalInfo2) external onlyUnmaskedBlockchainParticipant (_blockchainId) {
        // This will throw an error if the action is not a valid VoteType.
        VoteType action = VoteType(_action);

        // Can't start a vote if a vote is already underway.
        require(blockchains[_blockchainId].votes[_voteTarget].voteType == VoteType.VOTE_NONE);

        // If the action is to add a masked participant, then they shouldn't be a participant already.
        if (action == VoteType.VOTE_ADD_MASKED_PARTICIPANT) {
            require(blockchains[_blockchainId].inMasked[_voteTarget] == false);
        }
        // If the action is to remove a masked participant, then they should be a participant already.
        // Additionally, they must supply the offset into the masked array of the participant to be removed.
        if (action == VoteType.VOTE_REMOVE_MASKED_PARTICIPANT) {
            require(blockchains[_blockchainId].inMasked[_voteTarget] == true);
            require(blockchains[_blockchainId].masked[_additionalInfo1] == _voteTarget);
        }
        // If the action is to add an unmasked participant, then they shouldn't be a participant already.
        if (action == VoteType.VOTE_ADD_UNMASKED_PARTICIPANT) {
            require(blockchains[_blockchainId].inUnmasked[address(_voteTarget)] == false);
        }
        // If the action is to remove an unmasked participant, then they should be a participant
        // already and they can not be the sender. That is, the sender can not vote to remove
        // themselves.
        // Additionally, they must supply the offset into the unmasked array of the participant to be removed.
        if (action == VoteType.VOTE_REMOVE_UNMASKED_PARTICIPANT) {
            address voteTargetAddr = address(_voteTarget);
            require(blockchains[_blockchainId].inUnmasked[voteTargetAddr] == true);
            require(voteTargetAddr != msg.sender);
            require(blockchains[_blockchainId].unmasked[_additionalInfo1] == voteTargetAddr);
        }

        if (action == VoteType.VOTE_CHANGE_PUBLIC_KEY) {
            // The proposed public key is assumed to be valid.
            // TODO We could attempt to RLP decode the encoded public key.

            // TODO ***** check version is greater than current version
        }

        // Set-up the vote.
        blockchains[_blockchainId].votes[_voteTarget].voteType = action;
        blockchains[_blockchainId].votes[_voteTarget].endOfVotingBlockNumber = block.number + blockchains[_blockchainId].votingPeriod;
        blockchains[_blockchainId].votes[_voteTarget].additionalInfo1 = _additionalInfo1;
        blockchains[_blockchainId].votes[_voteTarget].additionalInfo2 = _additionalInfo2;

        // The proposer is deemed to be voting for the proposal.
        voteNoChecks(_blockchainId, _action, _voteTarget, true);
    }


    function vote(uint256 _blockchainId, uint16 _action, uint256 _voteTarget, bool _voteFor) external onlyUnmaskedBlockchainParticipant (_blockchainId) {
        // This will throw an error if the action is not a valid VoteType.
        VoteType action = VoteType(_action);

        // The type of vote must match what is currently being voted on.
        // Note that this will catch the case when someone is voting when there is no active vote.
        require(blockchains[_blockchainId].votes[_voteTarget].voteType == action);
        // Ensure the account has not voted yet.
        require(blockchains[_blockchainId].votes[_voteTarget].hasVoted[msg.sender] == false);

        // Check voting period has not expired.
        require(blockchains[_blockchainId].votes[_voteTarget].endOfVotingBlockNumber >= block.number);

        voteNoChecks(_blockchainId, _action, _voteTarget, _voteFor);
    }


    function actionVotes(uint256 _blockchainId, uint256 _voteTarget) external onlyUnmaskedBlockchainParticipant(_blockchainId) {
        // If no vote is underway, then there is nothing to action.
        VoteType action = blockchains[_blockchainId].votes[_voteTarget].voteType;
        require(action != VoteType.VOTE_NONE);
        // Can only action vote after voting period has ended.
        require(blockchains[_blockchainId].votes[_voteTarget].endOfVotingBlockNumber < block.number);

        VotingAlgInterface voteAlg = VotingAlgInterface(blockchains[_blockchainId].votingAlgorithmContract);
        bool result = voteAlg.assess(
                blockchains[_blockchainId].numUnmaskedParticipants,
                blockchains[_blockchainId].votes[_voteTarget].numVotedFor,
                blockchains[_blockchainId].votes[_voteTarget].numVotedAgainst);
        emit VoteResult(_blockchainId, uint16(action), _voteTarget, result);

        if (result) {
            // The vote has been decided in the affimative.
            uint256 additionalInfo1 = blockchains[_blockchainId].votes[_voteTarget].additionalInfo1;
            address participantAddr = address(_voteTarget);
            if (action == VoteType.VOTE_ADD_UNMASKED_PARTICIPANT) {
                blockchains[_blockchainId].unmasked.push(participantAddr);
                blockchains[_blockchainId].inUnmasked[participantAddr] = true;
                blockchains[_blockchainId].numUnmaskedParticipants++;
            }
            else if (action == VoteType.VOTE_ADD_MASKED_PARTICIPANT) {
                blockchains[_blockchainId].masked.push(_voteTarget);
                blockchains[_blockchainId].inMasked[_voteTarget] = true;
            }
            else if (action == VoteType.VOTE_REMOVE_UNMASKED_PARTICIPANT) {
                delete blockchains[_blockchainId].unmasked[additionalInfo1];
                delete blockchains[_blockchainId].inUnmasked[participantAddr];
                blockchains[_blockchainId].numUnmaskedParticipants--;
            }
            else if (action == VoteType.VOTE_REMOVE_MASKED_PARTICIPANT) {
                delete blockchains[_blockchainId].masked[additionalInfo1];
                delete blockchains[_blockchainId].inMasked[_voteTarget];
            }
            else if (action == VoteType.VOTE_CHANGE_PUBLIC_KEY) {
                // Change the current active public key to the one voted on by
                // adding the new key to the list.
                setPublicKey(_blockchainId,
                uint64(blockchains[_blockchainId].votes[_voteTarget].additionalInfo1),
                    blockchains[_blockchainId].votes[_voteTarget].additionalInfo2);
            }
        }


        // The vote is over. Now delete the voting arrays and indicate there is no vote underway.
        // Remove all values from the map: Maps can't be deleted in Solidity.
        // NOTE: The code below has used values directly, rather than a local variable due to running
        // out of local variables.
        for (uint i = 0; i < blockchains[_blockchainId].unmasked.length; i++) {
            if( blockchains[_blockchainId].unmasked[i] != address(0)) {
                delete blockchains[_blockchainId].votes[_voteTarget].hasVoted[blockchains[_blockchainId].unmasked[i]];
            }
        }
        // This will recursively delete everything in the structure, except for the map, which was
        // deleted in the for loop above.
        delete blockchains[_blockchainId].votes[_voteTarget];
    }


    /**
    * This function is used to indicate that an entity has voted. It has been created so that
    * calls to proposeVote do not have to incur all of the value checking in the vote call.
    *
    * TODO: Compare gas usage of keeping this integrated with the value checking.
    *       What is the trade-off of having one large structure, as opposed to separate structure (low priority)
    */
    function voteNoChecks(uint256 _blockchainId, uint16 _action, uint256 _voteTarget, bool _voteFor) private {
        // Indicate msg.sender has voted.
        emit ParticipantVoted(_blockchainId, msg.sender, _action, _voteTarget, _voteFor);
        blockchains[_blockchainId].votes[_voteTarget].hasVoted[msg.sender] = true;

        if (_voteFor) {
            blockchains[_blockchainId].votes[_voteTarget].numVotedFor++;
        } else {
            blockchains[_blockchainId].votes[_voteTarget].numVotedAgainst++;
        }
    }


    /**
    * Start the Crosschain Transaction.
    *
    *
    */
    // TODO should anyone be able to start, commit or ignore? Should it be limited to blockchain unmasked participants?
    function start(uint256 _originatingBlockchainId, uint256 _crosschainTransactionId,
        bytes calldata /*_signedStartMessage */, uint256 _transactionTimeoutBlock) external {
        uint256 index = uint256(keccak256(abi.encodePacked(_originatingBlockchainId, _crosschainTransactionId)));

        // The transaction id can not be re-used and the time-out block must be in the future.
        require(txMap[index].state == XTX_STATE.NOT_STARTED);
        require(_transactionTimeoutBlock > block.number);

        // TODO: validate the signed start message. Need to work out exact structure of message still.
        // TODO check signature verifies, given the public key of the originating blockchain. (could be a private method) Callout to precompile of a BLS signature. Use the functions coded by Peter & John. 
        // TODO check that the originating blockchain id in the message matches the parameter value.
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
    function commit(uint256 _originatingBlockchainId, uint256 _crosschainTransactionId, bytes calldata /*_signedCommitMessage*/) external {
        uint256 index = uint256(keccak256(abi.encodePacked(_originatingBlockchainId, _crosschainTransactionId)));
        // The transaction must be started and the time-out block must be in the future.
        require(txMap[index].state == XTX_STATE.STARTED);
        require(txMap[index].timeoutBlockNumber >= block.number);


        // TODO validate the signed commit message.
        // TODO check signature verifies, given the public key of the originating blockchain.
        // TODO check that the originating blockchain id in the message matches the parameter value.
        // TODO check that the Coordination Blockchain Identifier in the message matches the parameter value.
        // TODO check that the Crosschain Coordination Contract address in the message matches the parameter value.
        //   use   address(this)
        // TODO check that the it is a commit message.

        // TODO allow for use of the active or previous key version


        txMap[index].state = XTX_STATE.COMMITTED;
    }


    /**
     * Ignore the Crosschain Transaction.
     */
    function ignore(uint256 _originatingBlockchainId, uint256 _crosschainTransactionId, bytes calldata /*_signedIgnoreMessage*/) external {
        uint256 index = uint256(keccak256(abi.encodePacked(_originatingBlockchainId, _crosschainTransactionId)));
        // The transaction must be started and the time-out block must be in the future.
        require(txMap[index].state == XTX_STATE.STARTED);
        require(txMap[index].timeoutBlockNumber >= block.number);

        // TODO validate the signed ignore message.
        // TODO check signature verifies, given the public key of the originating blockchain.
        // TODO check that the originating blockchain id in the message matches the parameter value.
        // TODO check that the Coordination Blockchain Identifier in the message matches the parameter value.
        // TODO check that the Crosschain Coordination Contract address in the message matches the parameter value.
        //   use   address(this)
        // TODO check that the it is a commit message.

        // TODO allow for use of the active or previous key version

        txMap[index].state = XTX_STATE.IGNORED;
    }


    function getCrosschainTransactionStatus(uint256 _originatingBlockchainId, uint256 _crosschainTransactionId) external view returns (uint32) {
        uint256 index = uint256(keccak256(abi.encodePacked(_originatingBlockchainId, _crosschainTransactionId)));
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

    function getCrosschainTransactionTimeout(uint256 _originatingBlockchainId, uint256 _crosschainTransactionId) external view returns (uint256) {
        uint256 index = uint256(keccak256(abi.encodePacked(_originatingBlockchainId, _crosschainTransactionId)));
        return txMap[index].timeoutBlockNumber;
    }

    function getBlockNumber() external view returns (uint256) {
        return block.number;
    }



    function getBlockchainExists(uint256 _blockchainId) external view returns (bool) {
        return blockchains[_blockchainId].votingPeriod != 0;
    }


    function getVotingPeriod(uint256 _blockchainId) external view returns (uint64) {
        return blockchains[_blockchainId].votingPeriod;
    }


    function isUnmaskedBlockchainParticipant(uint256 _blockchainId, address _participant) external view returns(bool) {
        return blockchains[_blockchainId].inUnmasked[_participant];
    }


    function getUnmaskedBlockchainParticipantsSize(uint256 _blockchainId) external view returns(uint256) {
        return blockchains[_blockchainId].unmasked.length;
    }


    function getUnmaskedBlockchainParticipant(uint256 _blockchainId, uint256 _index) external view returns(address) {
        return blockchains[_blockchainId].unmasked[_index];
    }


    function getMaskedBlockchainParticipantsSize(uint256 _blockchainId) external view returns(uint256) {
        return blockchains[_blockchainId].masked.length;
    }


    function getMaskedBlockchainParticipant(uint256 _blockchainId, uint256 _index) external view returns(uint256) {
        return blockchains[_blockchainId].masked[_index];
    }


     /**
     * Get blockchain's public key, version number, status and block number
     *
     * @param _blockchainId The 256 bit blockchain identifier to which this public key belongs
     * @return public key information for the currently active public key for the blockchain
     */
    function getActivePublicKey(uint256 _blockchainId) external view returns ( uint64 _versionNumber, uint _blockNumber, bytes memory _key){
        return getPublicKey(_blockchainId, blockchains[_blockchainId].publicKeyActiveVersion);
    }

    function publicKeyExists(uint256 _blockchainId, uint64 _keyVersion) external view returns (bool) {
        return blockchains[_blockchainId].publicKeys[_keyVersion].blockNumber != 0;
    }

    function getPublicKey(uint256 _blockchainId, uint64 _keyVersion) public view returns (uint64 keyVersion, uint256 blockNumber, bytes memory key){
        uint256 blk = blockchains[_blockchainId].publicKeys[_keyVersion].blockNumber;
        bytes memory encodedKey = blockchains[_blockchainId].publicKeys[_keyVersion].encodedKey;
        return (_keyVersion, blk, encodedKey);
    }


    /**
     * Set the Blockchain's public key, version, status & block number
     *
     * @param  _blockchainId    The 256 bit blockchain identifier to which this public key belongs
     * @param  _encodedPublicKey      The new public key for the blockchain.
     */
    function setPublicKey(uint256 _blockchainId, uint64 _versionNumber, bytes memory _encodedPublicKey) private {
        // There must not be an entry for this key.
        require(blockchains[_blockchainId].publicKeys[_versionNumber].blockNumber == 0);

        blockchains[_blockchainId].publicKeys[_versionNumber].blockNumber = block.number;
        blockchains[_blockchainId].publicKeys[_versionNumber].encodedKey = _encodedPublicKey;
        blockchains[_blockchainId].publicKeyPreviousVersion = blockchains[_blockchainId].publicKeyActiveVersion;
        blockchains[_blockchainId].publicKeyActiveVersion = _versionNumber;
    }



    function getVersion() external pure returns (uint16) {
        return VERSION_ONE;
    }
}