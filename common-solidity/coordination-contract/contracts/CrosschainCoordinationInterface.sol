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


/**
 * Contract to coordinate Atomic Crosschain Transactions.
 *
 *
 *
 */
interface CrosschainCoordinationInterface {

    /**
     * Add a blockchain which could be used as an Originating Blockchain in an Atomic Crosschain Transaction.
     *
     * @param _blockchainId The 256 bit identifier of the Blockchain.
     * @param _votingAlgorithmContract The address of the initial contract to be used for all votes.
     * @param _votingPeriod The number of blocks by which time a vote must be finalized.
     */
    function addBlockchain(uint256 _blockchainId, address _votingAlgorithmContract, uint64 _votingPeriod) external;


    /**
     * Convert from being a masked to an unmasked participant. The participant themselves is the only
     * entity which can do this change.
     *
     * @param _blockchainId The 256 bit identifier of the Blockchain.
     * @param _index The index into the list of blockchain masked participants.
     * @param  _salt Salt: 256 bit random data
     * Note: function doesn't return anything, instead it emits an event, AddingBlockchainUnmaskedParticipant(uint256 _blockchainId, address _participant);
     */
    function unmask(uint256 _blockchainId, uint256 _index, uint256 _salt) external;

    /**
     * Propose that a certain action be voted on.
     * The message sender must be an unmasked member of the blockchain.
     *
     * Types of votes:
     *
     * Value  Action                                 _target                                 _additionalInfo1                 _additionalInfo2
     * 1      Vote to add a masked participant.      Salted Hash of participant's address    Not used                         Not used
     * 2      Vote to remove a masked participant    Salted Hash of participant's address    Index into array of participant  Not used
     * 3      Vote to add an unmasked participant    Address of proposed participant         Not used                         Not used
     * 4      Vote to remove an unmasked participant Address of participant                  Index into array of participant  Not used.
     * 5      Vote to change public key.             0x00                                    Version number of public key     Public Key
     *
     * @param _blockchainId The 256 bit identifier of the Blockchain.
     * @param _action The type of vote: add or remove a masked or unmasked participant, or change a public key
     * @param _voteTarget What is being voted on: a masked address or the unmasked address of a participant to be added or removed.
     * @param _additionalInfo1 See above.
     * @param _additionalInfo2 See above.
     */
    function proposeVote(uint256 _blockchainId, uint16 _action, uint256 _voteTarget, uint256 _additionalInfo1, bytes calldata _additionalInfo2) external;

    /**
     * Vote for a proposal.
     *
     * If an account has already voted, they can not vote again or change their vote.
     * Only members of the blockchain can vote.
     *
     * @param _blockchainId The 256 bit identifier of the Blockchain.
     * @param _action The type of vote: add or remove a masked or unmasked participant, or change a public key
     * @param _voteTarget What is being voted on: a masked address or the unmasked address of a participant to be added or removed.
     * @param _voteFor True if the transaction sender wishes to vote for the action.
     */
    function vote(uint256 _blockchainId, uint16 _action, uint256 _voteTarget, bool _voteFor) external;

    /**
     * Action votes to affect the change.
     *
     * Only members of the blockchain can action votes.
     *
     * @param _blockchainId The 256 bit identifier of the Blockchain.
     * @param _voteTarget What is being voted on: a masked address or the unmasked address of a participant to be added or removed.
     */
    function actionVotes(uint256 _blockchainId, uint256 _voteTarget) external;


    /**
    * Start the Crosschain Transaction.
    *
    * @param _originatingBlockchainId The blockchain ID of the blockchain which the crosschain transaction starts on. This
    *         must match the value in the signed message.
    * @param _crosschainTransactionId The ID of the Atomic Crosschain Transaction. This value must match what is inside
    *          the signed message.
    * @param _transactionTimeoutBlock is the block number after which this crosschain transaction will be ignored if it
    *         has not been committed. Note: This is an absolute block number, and not a relative block number, and this number
    *         must match what is inside the signed message. The reason for requiring an absolute block number is to prevent
    *         possible mis-use of the message: say having it signed and one point but then not submitting it until some time later.
    */
    function start(uint256 _originatingBlockchainId, uint256 _crosschainTransactionId,
        uint256 _hashOfMessage, uint256 _transactionTimeoutBlock, uint64 /*_keyVersion*/, bytes calldata /*_signature*/) external;

    /*
     * Commit the Crosschain Transaction.
     */
    function commit(uint256 _originatingBlockchainId, uint256 _crosschainTransactionId,
        uint256 _hashOfMessage, uint64 _keyVersion, bytes calldata _signature) external;

    /**
     * Ignore the Crosschain Transaction.
     */
    function ignore(uint256 _originatingBlockchainId, uint256 _crosschainTransactionId,
        uint256 _hashOfMessage, uint64 _keyVersion, bytes calldata _signature) external;


    /**
     * Get the status of the crosschain transaction
     */
    function getCrosschainTransactionStatus(uint256 _originatingBlockchainId, uint256 _crosschainTransactionId) external view returns (uint32);

    /**
     * For a specific blockchain and crosschain transaction, return the block number that represents the crosschain transaction timeout block
     */
    function getCrosschainTransactionTimeout(uint256 _originatingBlockchainId, uint256 _crosschainTransactionId) external view returns (uint256);

    /**
     * Get the current block number in the Coordination blockchain
     */
    function getBlockNumber() external view returns (uint256);


    /**
     * Indicate if this contract manages a certain blockchain.
     *
     * @param _blockchainId The 256 bit identifier of the Blockchain.
     * @return true if the blockchain is managed by this contract.
    */
    function getBlockchainExists(uint256 _blockchainId) external view returns (bool);

    /**
     * Get the voting period being used in a blockchain.
     *
     * @param _blockchainId The 256 bit identifier of the Blockchain.
     * @return Length of voting period in blocks.
     */
    function getVotingPeriod(uint256 _blockchainId) external view returns (uint64);

    /**
     * Indicate if a certain account is an unmasked participant of a blockchain.
     *
     * @param _blockchainId The 256 bit identifier of the Blockchain.
     * @param _participant Account to check to see if it is a participant.
     * @return true if _participant is an unmasked member of the blockchain.
     */
    function isUnmaskedBlockchainParticipant(uint256 _blockchainId, address _participant) external view returns(bool);

    /**
     * Get the length of the unmasked blockchain participants array for a certain blockchain.
     *
     * @param _blockchainId The 256 bit identifier of the Blockchain.
     * @return number of unmasked blockchain participants.
     */
    function getUnmaskedBlockchainParticipantsSize(uint256 _blockchainId) external view returns(uint256);

    /**
     * Get address of a certain unmasked blockchain participant. If the participant has been removed
     * at the given index, this function will return the address 0x00.
     *
     * @param _blockchainId The 256 bit identifier of the Blockchain.
     * @param _index The index into the list of blockchain participants.
     * @return Address of the participant, or 0x00.
     */
    function getUnmaskedBlockchainParticipant(uint256 _blockchainId, uint256 _index) external view returns(address);

    /**
     * Get the length of the masked blockchain participants array for a certain blockchain.
     *
     * @param _blockchainId The 256 bit identifier of the Blockchain.
     * @return length of the masked blockchain participants array.
     */
    function getMaskedBlockchainParticipantsSize(uint256 _blockchainId) external view returns(uint256);

    /*
     * Get the salted hash of a masked blockchain participant. If the participant has been removed
     * or has been unmasked, at the given index, this function will return 0x00.
     *
     * @param _blockchainId The 256 bit identifier of the Blockchain.
     * @param _index The index into the list of blockchain masked participants.
     * @return Salted hash or the participant's address, or 0x00.
     */
    function getMaskedBlockchainParticipant(uint256 _blockchainId, uint256 _index) external view returns(uint256);


    /**
    * Get the active key for a blockchain.
    *
    * @param _blockchainId The 256 bit blockchain identifier to which this public key belongs
    * @return public key information for the current active public key
    */
    function getActivePublicKey(uint256 _blockchainId) external view returns (uint256 blockNumber, uint32 algorithm, uint256[] memory _key);

    /**
    * Get key information for a certain version of a key for a blockchain.
    *
    * @param _blockchainId The 256 bit blockchain identifier to which this public key belongs
    * @param _keyVersion The version of the key to check.
    * @return public key information for the current active public key
    */
//    function getPublicKey(uint256 _blockchainId, uint64 _keyVersion) external view returns (uint64 keyVersion, uint256 blockNumber, bytes memory key);
    function getPublicKey(uint256 _blockchainId, uint64 _keyVersion) external view returns (uint256 blockNumber, uint32 algorithm, uint256[] memory key);


    /**
     * Determine if a public key exists for a certain blockchain for a certain key version.
     *
     * @param _blockchainId    The 256 bit blockchain identifier to which this public key belongs
     * @param _keyVersion     The key version to check
     * @return                true if a key exists for this sidehcain and key version.
     */
    function publicKeyExists(uint256 _blockchainId, uint64 _keyVersion) external view returns (bool);



    /*
     * Return the implementation version.
     */
    function getVersion() external pure returns (uint16);


    /*
     * Events: adding a blockchain; adding a masked blockchain participant; adding an unmasked blockchain participant; voting; voting result; two dump events
     */
    event AddedBlockchain(uint256 _blockchainId);
    event AddingBlockchainMaskedParticipant(uint256 _blockchainId, bytes32 _participant);
    event AddingBlockchainUnmaskedParticipant(uint256 _blockchainId, address _participant);

    event ParticipantVoted(uint256 _blockchainId, address _participant, uint16 _action, uint256 _voteTarget, bool _votedFor);
    event VoteResult(uint256 _blockchainId, uint16 _action, uint256 _voteTarget, bool _result);
    event Start(uint256 _originatingBlockchainId, uint256 _crosschainTransactionId,
        uint256 _hashOfMessage, uint256 _transactionTimeoutBlock, uint64 _keyVersion);

    event Dump1(uint256 a, uint256 b, address c);
    event Dump2(uint256 a, uint256 b, uint256 c, uint256 d);
    event Dump3(bytes a);

    event Alg(uint32 alg);
}