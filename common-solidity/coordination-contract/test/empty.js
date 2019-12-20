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
 * Uninitialised tests. Check that the contract operates
 * correctly when there are no sidechains in the contract - except for the management pseudo-sidechain.
 *
 */

contract('Empty Tests', function(accounts) {
    let common = require('./common');

    const NON_EXISTANT_BLOCKCHAIN = "0x2";
    const NON_EXISTANT_CROSSCHAIN_TRANSACTION = "0x01";

    it("getBlockchainExists for management pseudo-sidechain", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        const exists = await coordInterface.getBlockchainExists.call(common.MANAGEMENT_PSEUDO_BLOCKCHAIN_ID);

        assert.equal(exists, true);
    });

    it("getVotingPeriod for management pseudo-sidechain", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        const votingPeriod = await coordInterface.getVotingPeriod.call(common.MANAGEMENT_PSEUDO_BLOCKCHAIN_ID);
        assert.equal(votingPeriod, common.VOTING_PERIOD);
    });

    it("isBlockchainParticipant for management pseudo-sidechain: valid participant", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        const isParticipant = await coordInterface.isUnmaskedBlockchainParticipant.call(common.MANAGEMENT_PSEUDO_BLOCKCHAIN_ID, accounts[0]);
        assert.equal(isParticipant, true);
    });

    it("isBlockchainParticipant for management pseudo-sidechain: non-participant", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        const isParticipant = await coordInterface.isUnmaskedBlockchainParticipant.call(common.MANAGEMENT_PSEUDO_BLOCKCHAIN_ID, accounts[1]);
        assert.equal(isParticipant, false);
    });

    it("getUnmaskedBlockchainParticipantsSize for management pseudo-sidechain", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        const numParticipants = await coordInterface.getUnmaskedBlockchainParticipantsSize.call(common.MANAGEMENT_PSEUDO_BLOCKCHAIN_ID);
        assert.equal(numParticipants, "1");
    });

    it("getUnmaskedBlockchainParticipant for management pseudo-sidechain: valid participant", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        const participant = await coordInterface.getUnmaskedBlockchainParticipant.call(common.MANAGEMENT_PSEUDO_BLOCKCHAIN_ID, 0);
        assert.equal(participant, accounts[0]);
    });

    it("getMaskedBlockchainParticipantsSize for management pseudo-sidechain", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        const numUnmaskedParticipants = await coordInterface.getMaskedBlockchainParticipantsSize.call(common.MANAGEMENT_PSEUDO_BLOCKCHAIN_ID);
        assert.equal(numUnmaskedParticipants, "0");
    });



    it("getBlockchainExists for non-existent sidechain", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        const exists = await coordInterface.getBlockchainExists.call(NON_EXISTANT_BLOCKCHAIN);
        assert.equal(exists, false);
    });

    it("getVotingPeriod for non-existent sidechain", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        const votingPeriod = await coordInterface.getVotingPeriod.call(NON_EXISTANT_BLOCKCHAIN);
        assert.equal(votingPeriod, "0");
    });

    it("isBlockchainParticipant for non-existent sidechain", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        const isParticipant = await coordInterface.isUnmaskedBlockchainParticipant.call(NON_EXISTANT_BLOCKCHAIN, accounts[0]);
        assert.equal(isParticipant, false);
    });

    it("getUnmaskedBlockchainParticipantsSize for non-existent sidechain", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        const numUnMaskedParticipants = await coordInterface.getUnmaskedBlockchainParticipantsSize.call(NON_EXISTANT_BLOCKCHAIN);
        assert.equal(numUnMaskedParticipants, "0");
    });

    it("getUnmaskedBlockchainParticipant for non-existent sidechain TODO", async function() {
        // TODO: This appears to cause a revert. This could be due to attempting to reference a map
        //  inside a non-existant map. Need to determine if this is expected behaviour.
        // let coordInterface = await await common.getDeployedCrosschainCoordination();
        // const unmaskedParticipant = await coordInterface.getUnmaskedBlockchainParticipant.call(NON_EXISTANT_BLOCKCHAIN, 0);
        // assert.equal(unmaskedParticipant, "0");
    });

    it("getMaskedBlockchainParticipantsSize for non-existent sidechain", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        const numMaskedParticipants = await coordInterface.getMaskedBlockchainParticipantsSize.call(NON_EXISTANT_BLOCKCHAIN);
        assert.equal(numMaskedParticipants, "0");
    });

    it("getMaskedBlockchainParticipant for non-existent sidechain TODO", async function() {
        // TODO: This appears to cause a revert. This could be due to attempting to reference a map
        //  inside a non-existant map. Need to determine if this is expected behaviour.
        //let coordInterface = await await common.getDeployedCrosschainCoordination();
        //const maskedParticipant = await coordInterface.getMaskedBlockchainParticipant.call(NON_EXISTANT_BLOCKCHAIN, 0);
        //assert.equal(maskedParticipant, "0");
    });

    it("get public key when not valid sidechain", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        // The getPublicKey function now returns several values, not only the public key
          const pubKeyInfo = await coordInterface.getPublicKey.call(NON_EXISTANT_BLOCKCHAIN, common.KEY_VERSION);
          const {0: status, 1: blockNumber, 2: keyFound, 3: pubKey} = pubKeyInfo;
          assert.equal(pubKey, null);
    });

//TODO get this to work!
    it("get crosschain coordination status for invalid call", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        const status = await coordInterface.getCrosschainTransactionStatus.call(NON_EXISTANT_BLOCKCHAIN, NON_EXISTANT_CROSSCHAIN_TRANSACTION);
        assert.equal(0, status);
    });



});
