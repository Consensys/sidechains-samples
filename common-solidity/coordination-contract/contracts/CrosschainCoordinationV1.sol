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
import "../../crosschain-precompile-calls/contracts/Crosschain.sol";
import "../../signature-verification/contracts/SignatureVerification.sol";


/**
 * Contract to manage multiple blockchains.
 *
 * Please see the interface for documentation on all topics except for the constructor.
 *
 */
contract CrosschainCoordinationV1 is CrosschainCoordinationInterface, Crosschain, SignatureVerification {
    // Implementation version of the of the Crosschain Coordination Contract.
    uint16 constant private VERSION_ONE = 1;

    // The management blockchain is the blockchain with ID 0x00. It is used solely to restrict which
    // users can create a new blockchain. Only members of this blockchain can call addBlockchain().
    // TODO this should probably be private.
    uint256 public constant MANAGEMENT_PSEUDO_BLOCKCHAIN_ID = 0;

    // Message types of threshold signed messages.
    uint256 private constant CROSSCHAIN_TRANSACTION_START = 1;
    uint256 private constant CROSSCHAIN_TRANSACTION_COMMIT = 2;
    uint256 private constant CROSSCHAIN_TRANSACTION_IGNORE = 3;

    // Size of BN128 public key and signature in bytes, assuming the public key is on the E2 curve
    // and the signature is on the E1 curve.
    uint256 constant private LENGTH_UINT32_IN_BYTES = 4;
    uint256 constant private NUMBER_ELEMENTS_PUBLIC_KEY = 4;
    uint256 constant private NUMBER_ELEMENTS_SIGNATURE_KEY = 2;
    uint256 constant private LENGTH_OF_ARRAY_LENGTH = 32;
    uint256 constant private BN128_FIELD_SIZE = 32;
    uint256 constant private BN128_PUBLIC_KEY_SIZE = NUMBER_ELEMENTS_PUBLIC_KEY * BN128_FIELD_SIZE + LENGTH_UINT32_IN_BYTES;
    uint256 constant private BN128_SIGNATURE_SIZE = 2 * BN128_FIELD_SIZE + LENGTH_UINT32_IN_BYTES;

    // Crypto systems.
    uint32 constant private ALT_BN_128_WITH_KECCAK256 = 1;

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
        uint32 algorithm;
        uint256[] publicKey;
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
        addBlockchainInternal(MANAGEMENT_PSEUDO_BLOCKCHAIN_ID, _votingAlg, _votingPeriod);
    }


    // TODO an Add Blockchain method where an array of masked and unmasked participants are added.
    //      Instead of using the addBlockchain Internal function, code up a function that takes an array of participants (masked & unmasked) - add to interface
    function addBlockchain(uint256 _blockchainId, address _votingAlgorithmContract, uint64 _votingPeriod) external onlyUnmaskedBlockchainParticipant (MANAGEMENT_PSEUDO_BLOCKCHAIN_ID) {
        addBlockchainInternal(_blockchainId, _votingAlgorithmContract, _votingPeriod);
    }

    function addBlockchainInternal(uint256 _blockchainId, address _votingAlgorithmContract, uint64 _votingPeriod) private {
        // The blockchain can not exist prior to creation.
        require(blockchains[_blockchainId].votingPeriod == 0, "Blockchain already added");
        // The voting period must be greater than 0.
        require(_votingPeriod > 0, "The voting period must be greater than zero");
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
            // Check that the proposed key version is greater than current version.
            require(blockchains[_blockchainId].publicKeyActiveVersion < _additionalInfo1);

            // Decode the public key in an attempt to check if it is valid.
            decodeEncodedPublicKey(_additionalInfo2);
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
                setPublicKey(
                    _blockchainId,
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
    * Start the Crosschain Transaction.
    *
    *
    */
    function start(uint256 _originatingBlockchainId, uint256 _crosschainTransactionId,
        uint256 _hashOfMessage, uint256 _transactionTimeoutBlock, uint64 _keyVersion, bytes calldata _signature) external {

        uint256 index = uint256(keccak256(abi.encodePacked(_originatingBlockchainId, _crosschainTransactionId)));
        // The transaction id can not be re-used and the time-out block must be in the future.
        require(txMap[index].state == XTX_STATE.NOT_STARTED);
        require(_transactionTimeoutBlock > block.number);

        uint256 myBlockchainId = crosschainGetInfoBlockchainId();

        // Signed message is:
        //  Message Type
        //  Coordination Blockchain Id
        //  Coordination Contract Address
        //  Originating Blockchain Id
        //  Crosschain Transaction Id
        //  Message Digest of the transaction
        //  Transaction time-out block number
        bytes memory dataToBeVerified = abi.encodePacked(
            CROSSCHAIN_TRANSACTION_START,
            myBlockchainId,
            address(this),
            _originatingBlockchainId,
            _crosschainTransactionId,
            _hashOfMessage,
            _transactionTimeoutBlock);
         emit Dump3(dataToBeVerified);


        verifySignature(
            blockchains[_originatingBlockchainId].publicKeys[_keyVersion],
            dataToBeVerified,
            _signature);

        txMap[index].state = XTX_STATE.STARTED;
        txMap[index].timeoutBlockNumber = _transactionTimeoutBlock;
        emit Start(_originatingBlockchainId, _crosschainTransactionId,
            _hashOfMessage, _transactionTimeoutBlock, _keyVersion);
    }

    function startDebug(uint256 _originatingBlockchainId, uint256 _crosschainTransactionId,
        uint256 _hashOfMessage, uint256 _transactionTimeoutBlock) external {

        uint256 myBlockchainId = crosschainGetInfoBlockchainId();

        // Signed message is:
        //  Message Type
        //  Coordination Blockchain Id
        //  Coordination Contract Address
        //  Originating Blockchain Id
        //  Crosschain Transaction Id
        //  Message Digest of the transaction
        //  Transaction time-out block number
        bytes memory dataToBeVerified = abi.encodePacked(
            CROSSCHAIN_TRANSACTION_START,
            myBlockchainId,
            address(this),
            _originatingBlockchainId,
            _crosschainTransactionId,
            _hashOfMessage,
            _transactionTimeoutBlock);
        emit Dump3(dataToBeVerified);
    }

    /**
     * Commit the Crosschain Transaction.
     */
    function commit(uint256 _originatingBlockchainId, uint256 _crosschainTransactionId,
        uint256 _hashOfMessage, uint64 _keyVersion, bytes calldata _signature) external {

        uint256 index = uint256(keccak256(abi.encodePacked(_originatingBlockchainId, _crosschainTransactionId)));
        // The transaction must be started and the time-out block must be in the future.
        require(txMap[index].state == XTX_STATE.STARTED);
        require(txMap[index].timeoutBlockNumber >= block.number);

        uint256 myBlockchainId = crosschainGetInfoBlockchainId();

        // Signed message is:
        //  Message Type
        //  Coordination Blockchain Id
        //  Coordination Contract Address
        //  Originating Blockchain Id
        //  Crosschain Transaction Id
        //  Message Digest of the transaction
        //  Transaction time-out block number
        bytes memory dataToBeVerified = abi.encodePacked(
            CROSSCHAIN_TRANSACTION_COMMIT,
            myBlockchainId,
            address(this),
            _originatingBlockchainId,
            _crosschainTransactionId,
            _hashOfMessage);
        emit Dump3(dataToBeVerified);


        verifySignature(
            blockchains[_originatingBlockchainId].publicKeys[_keyVersion],
            dataToBeVerified,
            _signature);

        // TODO allow for use of the active or previous key version

        txMap[index].state = XTX_STATE.COMMITTED;
    }


    /**
     * Ignore the Crosschain Transaction.
     */
    function ignore(uint256 _originatingBlockchainId, uint256 _crosschainTransactionId,
        uint256 _hashOfMessage, uint64 _keyVersion, bytes calldata _signature) external {

        uint256 index = uint256(keccak256(abi.encodePacked(_originatingBlockchainId, _crosschainTransactionId)));
        // The transaction must be started and the time-out block must be in the future.
        require(txMap[index].state == XTX_STATE.STARTED);
        require(txMap[index].timeoutBlockNumber >= block.number);

        uint256 myBlockchainId = crosschainGetInfoBlockchainId();

        // Signed message is:
        //  Message Type
        //  Coordination Blockchain Id
        //  Coordination Contract Address
        //  Originating Blockchain Id
        //  Crosschain Transaction Id
        //  Message Digest of the transaction
        //  Transaction time-out block number
        bytes memory dataToBeVerified = abi.encodePacked(
            CROSSCHAIN_TRANSACTION_IGNORE,
            myBlockchainId,
            address(this),
            _originatingBlockchainId,
            _crosschainTransactionId,
            _hashOfMessage);
        emit Dump3(dataToBeVerified);

        verifySignature(
            blockchains[_originatingBlockchainId].publicKeys[_keyVersion],
            dataToBeVerified,
            _signature);

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
    function getActivePublicKey(uint256 _blockchainId) external view returns (uint256 blockNumber, uint32 algorithm, uint256[] memory _key){
        return getPublicKey(_blockchainId, blockchains[_blockchainId].publicKeyActiveVersion);
    }

    function publicKeyExists(uint256 _blockchainId, uint64 _keyVersion) external view returns (bool) {
        return blockchains[_blockchainId].publicKeys[_keyVersion].blockNumber != 0;
    }

    function getPublicKey(uint256 _blockchainId, uint64 _keyVersion) public view returns (uint256 blockNumber, uint32 algorithm, uint256[] memory key){
        blockNumber = blockchains[_blockchainId].publicKeys[_keyVersion].blockNumber;
        algorithm = blockchains[_blockchainId].publicKeys[_keyVersion].algorithm;
        key = blockchains[_blockchainId].publicKeys[_keyVersion].publicKey;
    }

//    function getPublicKey(uint256 _blockchainId, uint64 _keyVersion) public view returns (uint64 keyVersion, uint256 blockNumber, bytes memory key){
//        uint256 blk = blockchains[_blockchainId].publicKeys[_keyVersion].blockNumber;
//        bytes memory encodedKey = blockchains[_blockchainId].publicKeys[_keyVersion].encodedKey;
//        return (_keyVersion, blk, encodedKey);
//    }



    function getVersion() external pure returns (uint16) {
        return VERSION_ONE;
    }


    /************************************* PRIVATE FUNCTIONS BELOW HERE *************************************
    /************************************* PRIVATE FUNCTIONS BELOW HERE *************************************
    /************************************* PRIVATE FUNCTIONS BELOW HERE *************************************

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
     * Set the Blockchain's public key, version, status & block number
     *
     * @param  _blockchainId    The 256 bit blockchain identifier to which this public key belongs
     * @param  _encodedPublicKey      The new public key for the blockchain.
     */
    function setPublicKey(uint256 _blockchainId, uint64 _versionNumber, bytes memory _encodedPublicKey) private {
        // There must not be an entry for this key.
        // TODO: Due to other checks, this probably can't happen, and hence is probably not required.
        require(blockchains[_blockchainId].publicKeys[_versionNumber].blockNumber == 0, "Public Key exists for the specified version");

        blockchains[_blockchainId].publicKeys[_versionNumber].blockNumber = block.number;
        uint32 algorithm;
        uint256[] memory publicKey;
        (algorithm, publicKey) = decodeEncodedPublicKey(_encodedPublicKey);

        blockchains[_blockchainId].publicKeys[_versionNumber].algorithm = algorithm;
        blockchains[_blockchainId].publicKeys[_versionNumber].publicKey = publicKey;
        blockchains[_blockchainId].publicKeyPreviousVersion = blockchains[_blockchainId].publicKeyActiveVersion;
        blockchains[_blockchainId].publicKeyActiveVersion = _versionNumber;
    }



    function decodeEncodedPublicKey(bytes memory _encodedPublicKey) private pure returns (uint32 algorithm, uint256[] memory publicKey) {
        // Extract the algorithm field from the key.
        uint256[] memory val = new uint256[](1);
        uint256 offset1 = LENGTH_OF_ARRAY_LENGTH;
        uint256 offset2 = LENGTH_UINT32_IN_BYTES;
        assembly { mstore(add(val, offset1), mload(add(_encodedPublicKey, offset2))) }
        algorithm = uint32(val[0]);

        require(algorithm == ALT_BN_128_WITH_KECCAK256, "Unknown crypto system1");
        require(_encodedPublicKey.length == BN128_PUBLIC_KEY_SIZE, "Public key wrong size for algorithm");

        publicKey = new uint256[](NUMBER_ELEMENTS_PUBLIC_KEY);
        uint256 j = LENGTH_OF_ARRAY_LENGTH;
        for (uint256 i=BN128_FIELD_SIZE+LENGTH_UINT32_IN_BYTES; i<=publicKey.length*BN128_FIELD_SIZE+LENGTH_UINT32_IN_BYTES; i+=BN128_FIELD_SIZE) {
            assembly { mstore(add(publicKey, j), mload(add(_encodedPublicKey, i))) }
            j+=BN128_FIELD_SIZE;
        }
    }

    function verifySignature(
        PublicKey storage _pubKeyInfo,
        bytes memory /* _message */,
        bytes memory /* _signature */   // an E1 point
    ) private view {
        require(_pubKeyInfo.algorithm == ALT_BN_128_WITH_KECCAK256, "Unknown crypto system2");

//        E2Point memory pub = E2Point(
//            {x: [_pubKeyInfo.publicKey[0], _pubKeyInfo.publicKey[1]],
//             y: [_pubKeyInfo.publicKey[2], _pubKeyInfo.publicKey[3]]});
//        E1Point memory sig = decodeSignature(_signature);
// TODO disable signature verification temporarily        bool verified = verify(pub, _message, sig);
        bool verified = true;
        require(verified, "Signature failed verification");
    }

    function decodeSignature(bytes memory _sig) private pure returns (E1Point memory signature) {
        uint256[] memory output = new uint256[](NUMBER_ELEMENTS_SIGNATURE_KEY);
        for (uint256 i=BN128_FIELD_SIZE; i<=output.length*BN128_FIELD_SIZE; i+=BN128_FIELD_SIZE) {
            assembly { mstore(add(output, i), mload(add(_sig, i))) }
        }

        signature = E1Point(0, 0);
        signature.x = output[0];
        signature.y = output[1];
    }

}