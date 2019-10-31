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
 * Check the voting algorithm: majority
 *
 */
const VotingAlgMajority = artifacts.require("./VotingAlgMajority.sol");

contract('Voting: majority voting tests:', function(accounts) {
    let common = require('./common');

    const A_SIDECHAIN_ID = "0x2";


    async function addSidechain(coordInterface) {
        await coordInterface.addSidechain(A_SIDECHAIN_ID, (await VotingAlgMajority.deployed()).address, common.VOTING_PERIOD, common.A_VALID_PUBLIC_KEY);
    }

    async function addSecondParticipant(coordInterface) {
        let newParticipant = accounts[1];
        await coordInterface.proposeVote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, "1", "0x0");
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_SIDECHAIN_ID, newParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");
        let isParticipant = await coordInterface.isSidechainParticipant.call(A_SIDECHAIN_ID, newParticipant);
        assert.equal(isParticipant, true, "unexpectedly, New Participant: isSidechainParticipant == false");
    }
    async function addThirdParticipant(coordInterface) {
        let newParticipant = accounts[2];
        await coordInterface.proposeVote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, "1", "0x0");
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, true, {from: accounts[1]});
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_SIDECHAIN_ID, newParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");
        let isParticipant = await coordInterface.isSidechainParticipant.call(A_SIDECHAIN_ID, newParticipant);
        assert.equal(isParticipant, true, "unexpectedly, New Participant: isSidechainParticipant == false");
    }
    async function addFourthParticipant(coordInterface) {
        let newParticipant = accounts[3];
        await coordInterface.proposeVote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, "1", "0x0");
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, true, {from: accounts[1]});
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, true, {from: accounts[2]});
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_SIDECHAIN_ID, newParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");
        let isParticipant = await coordInterface.isSidechainParticipant.call(A_SIDECHAIN_ID, newParticipant);
        assert.equal(isParticipant, true, "unexpectedly, New Participant: isSidechainParticipant == false");
    }
    async function addFifthParticipant(coordInterface) {
        let newParticipant = accounts[4];
        await coordInterface.proposeVote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, "1", "0x0");
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, true, {from: accounts[2]});
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, true, {from: accounts[1]});
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, true, {from: accounts[3]});
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_SIDECHAIN_ID, newParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");
        let isParticipant = await coordInterface.isSidechainParticipant.call(A_SIDECHAIN_ID, newParticipant);
        assert.equal(isParticipant, true, "unexpectedly, New Participant: isSidechainParticipant == false");
    }

    it("one participant", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addSidechain(coordInterface);
        await addSecondParticipant(coordInterface);
    });

    it("two participants: unanimous vote yes", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addSidechain(coordInterface);
        await addSecondParticipant(coordInterface);
        await addThirdParticipant(coordInterface);
    });

    it("two participants: only one votes yes", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addSidechain(coordInterface);
        await addSecondParticipant(coordInterface);

        // There are now two participants. Only one votes yes. This should fail, as a majority have not voted yes.
        let newParticipant = accounts[2];
        await coordInterface.proposeVote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, "1", "0x0");
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_SIDECHAIN_ID, newParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(false, result, "incorrect result reported in event");
        let isParticipant = await coordInterface.isSidechainParticipant.call(A_SIDECHAIN_ID, newParticipant);
        assert.equal(isParticipant, false, "Majority did not vote yes. Unexpectedly, New Participant: isSidechainParticipant != false");
    });

    it("two participants: one votes yes, one votes no", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addSidechain(coordInterface);
        await addSecondParticipant(coordInterface);

        let newParticipant = accounts[2];
        await coordInterface.proposeVote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, "1", "0x0");
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, false, {from: accounts[1]});
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_SIDECHAIN_ID, newParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(false, result, "incorrect result reported in event");
        let isParticipant = await coordInterface.isSidechainParticipant.call(A_SIDECHAIN_ID, newParticipant);
        assert.equal(isParticipant, false, "Majority did not vote yes. Unexpectedly, New Participant: isSidechainParticipant != false");
    });

    it("three participants: unanimous vote yes", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addSidechain(coordInterface);
        await addSecondParticipant(coordInterface);
        await addThirdParticipant(coordInterface);
        await addFourthParticipant(coordInterface);
    });

    it("three participants: only one vote yes", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addSidechain(coordInterface);
        await addSecondParticipant(coordInterface);
        await addThirdParticipant(coordInterface);

        let newParticipant = accounts[3];
        await coordInterface.proposeVote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, "1", "0x0");
//        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, true, {from: accounts[1]});
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_SIDECHAIN_ID, newParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(false, result, "incorrect result reported in event");
        let isParticipant = await coordInterface.isSidechainParticipant.call(A_SIDECHAIN_ID, newParticipant);
        assert.equal(isParticipant, false, "Majority did not vote yes. Unexpectedly, New Participant: isSidechainParticipant != false");
    });

    it("three participants: one votes yes, one votes no", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addSidechain(coordInterface);
        await addSecondParticipant(coordInterface);
        await addThirdParticipant(coordInterface);

        let newParticipant = accounts[3];
        await coordInterface.proposeVote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, "1", "0x0");
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, false, {from: accounts[1]});
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_SIDECHAIN_ID, newParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(false, result, "incorrect result reported in event");
        let isParticipant = await coordInterface.isSidechainParticipant.call(A_SIDECHAIN_ID, newParticipant);
        assert.equal(isParticipant, false, "Majority did not vote yes. Unexpectedly, New Participant: isSidechainParticipant != false");
    });

    it("three participants: one votes yes, two vote no", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addSidechain(coordInterface);
        await addSecondParticipant(coordInterface);
        await addThirdParticipant(coordInterface);

        let newParticipant = accounts[3];
        await coordInterface.proposeVote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, "1", "0x0");
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, false, {from: accounts[1]});
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, false, {from: accounts[2]});
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_SIDECHAIN_ID, newParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(false, result, "incorrect result reported in event");
        let isParticipant = await coordInterface.isSidechainParticipant.call(A_SIDECHAIN_ID, newParticipant);
        assert.equal(isParticipant, false, "Majority did not vote yes. Unexpectedly, New Participant: isSidechainParticipant != false");
    });

    it("three participants: two vote yes", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addSidechain(coordInterface);
        await addSecondParticipant(coordInterface);
        await addThirdParticipant(coordInterface);

        let newParticipant = accounts[3];
        await coordInterface.proposeVote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, "1", "0x0");
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, true, {from: accounts[1]});
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_SIDECHAIN_ID, newParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");
        let isParticipant = await coordInterface.isSidechainParticipant.call(A_SIDECHAIN_ID, newParticipant);
        assert.equal(isParticipant, true, "Majority voted yes. Unexpectedly, New Participant: isSidechainParticipant == false");
    });

    it("three participants: two vote yes, one votes no", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addSidechain(coordInterface);
        await addSecondParticipant(coordInterface);
        await addThirdParticipant(coordInterface);

        let newParticipant = accounts[3];
        await coordInterface.proposeVote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, "1", "0x0");
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, true, {from: accounts[1]});
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, false, {from: accounts[2]});
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_SIDECHAIN_ID, newParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");
        let isParticipant = await coordInterface.isSidechainParticipant.call(A_SIDECHAIN_ID, newParticipant);
        assert.equal(isParticipant, true, "Majority voted yes. Unexpectedly, New Participant: isSidechainParticipant == false");
    });

    it("five participants: three vote yes, one votes no", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addSidechain(coordInterface);
        await addSecondParticipant(coordInterface);
        await addThirdParticipant(coordInterface);
        await addFourthParticipant(coordInterface);
        await addFifthParticipant(coordInterface);

        let newParticipant = accounts[6];
        await coordInterface.proposeVote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, "1", "0x0");
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, true, {from: accounts[1]});
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, true, {from: accounts[2]});
        await coordInterface.vote(A_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, newParticipant, false, {from: accounts[3]});
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_SIDECHAIN_ID, newParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");
        let isParticipant = await coordInterface.isSidechainParticipant.call(A_SIDECHAIN_ID, newParticipant);
        assert.equal(isParticipant, true, "Majority voted yes. Unexpectedly, New Participant: isSidechainParticipant == false");
    });

});