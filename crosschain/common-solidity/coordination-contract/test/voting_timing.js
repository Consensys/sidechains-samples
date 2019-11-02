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
 * Check timing aspects of voting.
 *
 */

contract('Voting: timing tests', function(accounts) {
    let common = require('./common');

    it("finalise vote immediately after voting period", async function() {
        let secondParticipant = accounts[1];
        let coordInterface = await await common.getNewCrosschainCoordination();

        await coordInterface.proposeVote(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, secondParticipant, "1", "0x0");
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, secondParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");

        let isParticipant = await coordInterface.isSidechainParticipant.call(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, secondParticipant);
        assert.equal(isParticipant, true, "unexpectedly, Second Participant: isSidechainParticipant == false");
    });

    it("finalise vote after voting period", async function() {
        let secondParticipant = accounts[1];
        let coordInterface = await await common.getNewCrosschainCoordination();

        await coordInterface.proposeVote(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, secondParticipant, "1", "0x0");
        await common.mineBlocks(parseInt(common.VOTING_PERIOD_PLUS_ONE));
        let actionResult = await coordInterface.actionVotes(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, secondParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");

        let isParticipant = await coordInterface.isSidechainParticipant.call(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, secondParticipant);
        assert.equal(isParticipant, true, "unexpectedly, Second Participant: isSidechainParticipant == false");
    });

    it("finalise vote immediately: expect to fail", async function() {
        let secondParticipant = accounts[1];
        let coordInterface = await await common.getNewCrosschainCoordination();

        await coordInterface.proposeVote(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, secondParticipant, "1", "0x0");
        let didNotTriggerError = false;
        try {
            await coordInterface.actionVotes(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, secondParticipant);
            didNotTriggerError = true;
        } catch(err) {
            assert.equal(err.message, common.REVERT);
            //console.log("ERROR! " + err.message);
        }
        assert.equal(didNotTriggerError, false);

        let isParticipant = await coordInterface.isSidechainParticipant.call(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, secondParticipant);
        assert.equal(isParticipant, false, "unexpectedly, Second Participant: isSidechainParticipant != false");
    });

    it("dont finalise vote", async function() {
        let secondParticipant = accounts[1];
        let coordInterface = await await common.getNewCrosschainCoordination();

        await coordInterface.proposeVote(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, secondParticipant, "1", "0x0");

        let isParticipant = await coordInterface.isSidechainParticipant.call(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, secondParticipant);
        assert.equal(isParticipant, false, "unexpectedly, Second Participant: isSidechainParticipant != false");
    });

    it("finalise vote early: expect to fail", async function() {
        let secondParticipant = accounts[1];
        let coordInterface = await await common.getNewCrosschainCoordination();

        await coordInterface.proposeVote(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, secondParticipant, "1", "0x0");
        await common.mineBlocks(parseInt(common.VOTING_PERIOD_MINUS_ONE));
        let didNotTriggerError = false;
        try {
            await coordInterface.actionVotes(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, secondParticipant);
            didNotTriggerError = true;
        } catch(err) {
            assert.equal(err.message, common.REVERT);
            //console.log("ERROR! " + err.message);
        }
        assert.equal(didNotTriggerError, false);

        let isParticipant = await coordInterface.isSidechainParticipant.call(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, secondParticipant);
        assert.equal(isParticipant, false, "unexpectedly, Second Participant: isSidechainParticipant != false");
    });



});