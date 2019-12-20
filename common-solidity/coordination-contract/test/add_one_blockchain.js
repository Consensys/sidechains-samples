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
 * Add a single blockchain, and check the set-up of the blockchain entry is correct.
 *
 */
contract('Add One Blockchain', function(accounts) {
    let common = require('./common');

    const twoBlockchainId = "0x2";
    const oneBlockchainId = "0x1";


    it("addBlockchain", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await coordInterface.addBlockchain(twoBlockchainId, await common.getValidVotingContractAddress(), common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);
    });

    it("getBlockchainExists for valid blockchain", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await coordInterface.addBlockchain(twoBlockchainId, await common.getValidVotingContractAddress(), common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);

        const hasD1 = await coordInterface.getBlockchainExists.call(twoBlockchainId);
        assert.equal(hasD1, true, "Found blockchain 0");
    });


    it("getBlockchainExists for invalid blockchain", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await coordInterface.addBlockchain(twoBlockchainId, await common.getValidVotingContractAddress(), common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);

        const hasD2 = await coordInterface.getBlockchainExists.call(oneBlockchainId);
        assert.equal(hasD2, false, "Unexpectedly found blockchain 1, which shouldn't exist");
    });

    it("getVotingPeriod", async function() {
        let votingPeriodTen = 10;
        let votingPeriodEleven = 11;
        let coordInterface = await common.getNewCrosschainCoordination();
        await coordInterface.addBlockchain(twoBlockchainId, await common.getValidVotingContractAddress(), votingPeriodTen, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);
        await coordInterface.addBlockchain(oneBlockchainId, await common.getValidVotingContractAddress(), votingPeriodEleven, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);

        const actualVotingPeriod = await coordInterface.getVotingPeriod.call(twoBlockchainId);
        assert.equal(actualVotingPeriod, votingPeriodTen, "twoChainId returned unexpected voting period");

        const actualVotingPeriod1 = await coordInterface.getVotingPeriod.call(oneBlockchainId);
        assert.equal(actualVotingPeriod1, votingPeriodEleven, "oneChainId returned unexpected voting period");
    });

    it("isBlockchainParticipant", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await coordInterface.addBlockchain(twoBlockchainId, await common.getValidVotingContractAddress(), common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);

        const isPartBad = await coordInterface.isUnmaskedBlockchainParticipant.call(twoBlockchainId, accounts[1]);
        assert.equal(isPartBad, false, "unexpectedly, account which should not be part of the blockchain is");

        const isPartGood = await coordInterface.isUnmaskedBlockchainParticipant.call(twoBlockchainId, accounts[0]);
        assert.equal(isPartGood, true, "account which should be part of the blockchain is");
    });

    it("getUnmaskedBlockchainParticipantsSize", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await coordInterface.addBlockchain(twoBlockchainId, await common.getValidVotingContractAddress(), common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);

        const numUnmasked = await coordInterface.getUnmaskedBlockchainParticipantsSize.call(twoBlockchainId);
        assert.equal(numUnmasked, 1, "unexpected number of unmasked participants");
    });

    it("getMaskedBlockchainParticipantsSize", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await coordInterface.addBlockchain(twoBlockchainId, await common.getValidVotingContractAddress(), common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);

        const numMasked = await coordInterface.getMaskedBlockchainParticipantsSize.call(twoBlockchainId);
        assert.equal(numMasked, 0, "unexpected number of unmasked participants");
    });


});