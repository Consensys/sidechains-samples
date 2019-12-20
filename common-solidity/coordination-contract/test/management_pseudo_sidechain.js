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
 * This file contains tests around the management pseudo sidechain.
 *
 */

contract('Management Pseduo Sidechain', function(accounts) {
    let common = require('./common');

    const twoSidechainId = "0x2";

    it("getSidechainExists for management sidechain", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        const exists = await coordInterface.getSidechainExists.call(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID);

        assert.equal(exists, true);
    });


    it("attempting to recreate management sidechain id fails", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        let didNotTriggerError = false;
        try {
            await coordInterface.addSidechain(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, await common.getValidVotingContractAddress(), common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);
            didNotTriggerError = true;
        } catch(err) {
            assert.equal(err.message, common.REVERT);
            //console.log("ERROR! " + err.message);
        }

        assert.equal(didNotTriggerError, false);
    });


    it("check that the account which deployed the contract can call addSidechain", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await coordInterface.addSidechain(twoSidechainId, await common.getValidVotingContractAddress(), common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);
    });

    it("check that accounts other than the one which deployed the contract can not call addSidechain", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        let didNotTriggerError = false;
        try {
            await coordInterface.addSidechain(twoSidechainId, await common.getValidVotingContractAddress(), common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY, {from: accounts[1]});
            didNotTriggerError = true;
        } catch(err) {
            assert.equal(err.message, common.REVERT);
            //console.log("ERROR! " + err.message);
        }

        assert.equal(didNotTriggerError, false);
    });


    it("add an account to the management sidechain id", async function() {
        let secondParticipant = accounts[1];
        let coordInterface = await await common.getNewCrosschainCoordination();

        await coordInterface.proposeVote(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, common.VOTE_ADD_UNMASKED_PARTICIPANT, secondParticipant, "1", "0x0");
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        await coordInterface.actionVotes(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, secondParticipant);

       let isParticipant = await coordInterface.isUnmaskedSidechainParticipant.call(common.MANAGEMENT_PSEUDO_SIDECHAIN_ID, secondParticipant);
       assert.equal(isParticipant, true, "unexpectedly, Second Participant: isSidechainParticipant == false");
    });


});
