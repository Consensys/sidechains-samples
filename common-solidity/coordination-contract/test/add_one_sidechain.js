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
 * Add a single sidechain, and check the set-up of the sidechain entry is correct.
 *
 */
contract('Add One Sidechain', function(accounts) {
    let common = require('./common');

    const twoSidechainId = "0x2";
    const oneSidechainId = "0x1";


    it("addSidechain", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await coordInterface.addSidechain(twoSidechainId, await common.getValidVotingContractAddress(), common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);
    });

    it("getSidechainExists for valid sidechain", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await coordInterface.addSidechain(twoSidechainId, await common.getValidVotingContractAddress(), common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);

        const hasD1 = await coordInterface.getSidechainExists.call(twoSidechainId);
        assert.equal(hasD1, true, "Found sidechain 0");
    });


    it("getSidechainExists for invalid sidechain", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await coordInterface.addSidechain(twoSidechainId, await common.getValidVotingContractAddress(), common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);

        const hasD2 = await coordInterface.getSidechainExists.call(oneSidechainId);
        assert.equal(hasD2, false, "Unexpectedly found sidechain 1, which shouldn't exist");
    });

    it("getVotingPeriod", async function() {
        let votingPeriodTen = 10;
        let votingPeriodEleven = 11;
        let coordInterface = await common.getNewCrosschainCoordination();
        await coordInterface.addSidechain(twoSidechainId, await common.getValidVotingContractAddress(), votingPeriodTen, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);
        await coordInterface.addSidechain(oneSidechainId, await common.getValidVotingContractAddress(), votingPeriodEleven, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);

        const actualVotingPeriod = await coordInterface.getVotingPeriod.call(twoSidechainId);
        assert.equal(actualVotingPeriod, votingPeriodTen, "twoChainId returned unexpected voting period");

        const actualVotingPeriod1 = await coordInterface.getVotingPeriod.call(oneSidechainId);
        assert.equal(actualVotingPeriod1, votingPeriodEleven, "oneChainId returned unexpected voting period");
    });

    it("isSidechainParticipant", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await coordInterface.addSidechain(twoSidechainId, await common.getValidVotingContractAddress(), common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);

        const isPartBad = await coordInterface.isUnmaskedSidechainParticipant.call(twoSidechainId, accounts[1]);
        assert.equal(isPartBad, false, "unexpectedly, account which should not be part of the sidechain is");

        const isPartGood = await coordInterface.isUnmaskedSidechainParticipant.call(twoSidechainId, accounts[0]);
        assert.equal(isPartGood, true, "account which should be part of the sidechain is");
    });

    it("getUnmaskedSidechainParticipantsSize", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await coordInterface.addSidechain(twoSidechainId, await common.getValidVotingContractAddress(), common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);

        const numUnmasked = await coordInterface.getUnmaskedSidechainParticipantsSize.call(twoSidechainId);
        assert.equal(numUnmasked, 1, "unexpected number of unmasked participants");
    });

    it("getMaskedSidechainParticipantsSize", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await coordInterface.addSidechain(twoSidechainId, await common.getValidVotingContractAddress(), common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);

        const numMasked = await coordInterface.getMaskedSidechainParticipantsSize.call(twoSidechainId);
        assert.equal(numMasked, 0, "unexpected number of unmasked participants");
    });


});