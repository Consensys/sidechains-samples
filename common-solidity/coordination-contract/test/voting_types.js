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
/**
 * Check that all of the things that can be voted on work.
 *
 */

// var Web3 = require('web3');
// var web3 = new Web3();

const VotingAlgMajority = artifacts.require("./VotingAlgMajority.sol");

contract('Voting: types of voting / things to vote on:', function(accounts) {
    let common = require('./common');

    const A_BLOCKCHAIN_ID = "0x0000000000000000000000000000000000000000000000000000000000000002";


    async function addBlockchain(coordInterface) {
        await coordInterface.addBlockchain(A_BLOCKCHAIN_ID, (await VotingAlgMajority.deployed()).address, common.VOTING_PERIOD);
    }


    it("add an unmasked participant", async function() {
        let coordInterface = await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);

        let newParticipant = accounts[1];
        await coordInterface.proposeVote(A_BLOCKCHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, "0", "0x0");
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_BLOCKCHAIN_ID, newParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");

        let isParticipant = await coordInterface.isUnmaskedBlockchainParticipant.call(A_BLOCKCHAIN_ID, newParticipant);
        assert.equal(isParticipant, true, "unexpectedly, New Participant: isUnmaskedBlockchainParticipant == false");

        let numUnmaskedParticipant = await coordInterface.getUnmaskedBlockchainParticipantsSize.call(A_BLOCKCHAIN_ID);
        assert.equal(numUnmaskedParticipant, 2, "unexpectedly, number of unmasked participants != 2");

        let newParticipantStored = await coordInterface.getUnmaskedBlockchainParticipant.call(A_BLOCKCHAIN_ID, "1");
        assert.equal(newParticipant, newParticipantStored, "unexpectedly, the stored participant did not match the value supplied.");
    });

    it("add a masked participant", async function() {
        let coordInterface = await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);

        let newParticipant = accounts[1];
        let salt = "0x123456789ABCDEF0123456789ABCDEF0";
        let maskedParticipant  = web3.utils.keccak256(newParticipant, salt);

        await coordInterface.proposeVote(A_BLOCKCHAIN_ID, common.VOTE_ADD_MASKED_PARTICIPANT, maskedParticipant, "0", "0x0");
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_BLOCKCHAIN_ID, maskedParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");

        // This code no longer works as the Truffle framework now picks up that maskedParticipant is the wrong size for address.
        // let isParticipant = await coordInterface.isUnmaskedBlockchainParticipant.call(A_BLOCKCHAIN_ID, maskedParticipant);
        // assert.equal(isParticipant, false, "unexpectedly, New masked participant: isUnmaskedBlockchainParticipant != false");

        let numMaskedParticipant = await coordInterface.getMaskedBlockchainParticipantsSize.call(A_BLOCKCHAIN_ID);
        assert.equal(numMaskedParticipant, 1, "unexpectedly, number of masked participants != 1");

        let maskedParticipantStored = await coordInterface.getMaskedBlockchainParticipant.call(A_BLOCKCHAIN_ID, "0");
        let maskedParticipantStoredHex = web3.utils.toHex(maskedParticipantStored);
        assert.equal(maskedParticipant, maskedParticipantStoredHex, "unexpectedly, the stored masked participant did not match the value supplied.");
    });

    it("remove an  unmasked participant", async function() {
        let coordInterface = await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);

        // Add the participant
        let newParticipant = accounts[1];
        await coordInterface.proposeVote(A_BLOCKCHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, "0", "0x0");
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResults = await coordInterface.actionVotes(A_BLOCKCHAIN_ID, newParticipant);
        const result1 = await common.checkVotingResult(actionResults.logs);
        assert.equal(true, result1, "incorrect result reported in event");

        let isParticipant = await coordInterface.isUnmaskedBlockchainParticipant.call(A_BLOCKCHAIN_ID, newParticipant);
        assert.equal(isParticipant, true, "unexpectedly, New Participant: isUnmaskedBlockchainParticipant == false");

        const EXPECTED_OFFSET = "1";
        let newParticipantStored = await coordInterface.getUnmaskedBlockchainParticipant.call(A_BLOCKCHAIN_ID, EXPECTED_OFFSET);
        assert.equal(newParticipant, newParticipantStored, "unexpectedly, the stored participant did not match the value supplied.");


        // Remove the participant
        let participantToRemove = newParticipant;
        await coordInterface.proposeVote(A_BLOCKCHAIN_ID, common.VOTE_REMOVE_UNMASKED_PARTICIPANT, participantToRemove, EXPECTED_OFFSET, "0x0");
        // NOTE that with just two unmasked participants, the unmasked participant being
        // removed has to agree to being removed.
        await coordInterface.vote(A_BLOCKCHAIN_ID, common.VOTE_REMOVE_UNMASKED_PARTICIPANT, participantToRemove, true, {from: accounts[1]});
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResults2 = await coordInterface.actionVotes(A_BLOCKCHAIN_ID, participantToRemove);
        const result2 = await common.checkVotingResult(actionResults2.logs);
        assert.equal(true, result2, "incorrect result reported in event");

        isParticipant = await coordInterface.isUnmaskedBlockchainParticipant.call(A_BLOCKCHAIN_ID, participantToRemove);
        assert.equal(isParticipant, false, "unexpectedly, New Participant: isUnmaskedBlockchainParticipant != false");

        let numUnmaskedParticipant = await coordInterface.getUnmaskedBlockchainParticipantsSize.call(A_BLOCKCHAIN_ID);
        assert.equal(numUnmaskedParticipant, 2, "unexpectedly, unmasked participants array size != 2");

        newParticipantStored = await coordInterface.getUnmaskedBlockchainParticipant.call(A_BLOCKCHAIN_ID, EXPECTED_OFFSET);
        assert.equal("0x0000000000000000000000000000000000000000", newParticipantStored, "unexpectedly, the stored participant was not zeroized.");
    });

    it("remove a masked participant", async function() {
        let coordInterface = await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);

        let newParticipant = accounts[1];
        let salt = "0x123456789ABCDEF0123456789ABCDEF0";
        let maskedParticipant  = web3.utils.keccak256(newParticipant, salt);

        // Add the participant.
        await coordInterface.proposeVote(A_BLOCKCHAIN_ID, common.VOTE_ADD_MASKED_PARTICIPANT, maskedParticipant, "0", "0x0");
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult1 = await coordInterface.actionVotes(A_BLOCKCHAIN_ID, maskedParticipant);
        const result1 = await common.checkVotingResult(actionResult1.logs);
        assert.equal(true, result1, "incorrect result reported in event");

        const EXPECTED_OFFSET = "0";
        let maskedParticipantStored = await coordInterface.getMaskedBlockchainParticipant.call(A_BLOCKCHAIN_ID, EXPECTED_OFFSET);
        let maskedParticipantStoredHex = web3.utils.toHex(maskedParticipantStored);
        assert.equal(maskedParticipant, maskedParticipantStoredHex, "unexpectedly, the stored masked participant did not match the value supplied.");

        // Remove the participant.
        await coordInterface.proposeVote(A_BLOCKCHAIN_ID, common.VOTE_REMOVE_MASKED_PARTICIPANT, maskedParticipant, EXPECTED_OFFSET, "0x0");
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult2 = await coordInterface.actionVotes(A_BLOCKCHAIN_ID, maskedParticipant);
        const result2 = await common.checkVotingResult(actionResult2.logs);
        assert.equal(true, result2, "incorrect result reported in event");

        maskedParticipantStored = await coordInterface.getMaskedBlockchainParticipant.call(A_BLOCKCHAIN_ID, EXPECTED_OFFSET);
        maskedParticipantStoredHex = web3.utils.toHex(maskedParticipantStored);
        assert.equal(0, maskedParticipantStoredHex, "unexpectedly, the stored masked participant was not zeroized.");
    });


    it("add public key", async function() {
        let coordInterface = await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);

        let newParticipant = accounts[1];
        await coordInterface.proposeVote(A_BLOCKCHAIN_ID, common.VOTE_CHANGE_PUBLIC_KEY, "0", "1", common.PUBLIC_KEY);
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_BLOCKCHAIN_ID, "0");
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");

        let exists = await coordInterface.publicKeyExists(A_BLOCKCHAIN_ID, "1");
        assert.equal(true, exists, "unexpectedly, the key is not in the contract.");
    });

});