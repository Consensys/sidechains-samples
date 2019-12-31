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
 * Check that unmasking a masked participant works.
 *
 */
const VotingAlgMajority = artifacts.require("./VotingAlgMajority.sol");

contract('Unmasking masked participants:', function(accounts) {
    let common = require('./common');

    const A_BLOCKCHAIN_ID = "0x2";


    async function addBlockchain(coordInterface) {
        await coordInterface.addBlockchain(A_BLOCKCHAIN_ID, (await VotingAlgMajority.deployed()).address, common.VOTING_PERIOD, common.KEY_VERSION, common.A_VALID_PUBLIC_KEY);
    }

    async function addMaskedParticipant(coordInterface, participant, salt) {
        let maskedParticipant  = web3.utils.keccak256(participant + salt);

        await coordInterface.proposeVote(A_BLOCKCHAIN_ID, common.VOTE_ADD_MASKED_PARTICIPANT, maskedParticipant, "0", "0x0");
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_BLOCKCHAIN_ID, maskedParticipant);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");
        return maskedParticipant;
    }


    it("unmask", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);

        let newParticipant = accounts[1];
        let salt1 = "0000000000000000000000000000000000000000000000000000000000000001";
        let salt = "0x" + salt1;
        let maskedParticipant  = await addMaskedParticipant(coordInterface, newParticipant, salt1);

        // Check that the masked participant exists and the unmasked participant does not exist.
        let isParticipant = await coordInterface.isUnmaskedBlockchainParticipant.call(A_BLOCKCHAIN_ID, newParticipant);
        assert.equal(isParticipant, false, "unexpectedly, New masked participant: isUnmaskedBlockchainParticipant != false");

        const EXPECTED_OFFSET = "0";
        let maskedParticipantStored = await coordInterface.getMaskedBlockchainParticipant.call(A_BLOCKCHAIN_ID, EXPECTED_OFFSET);
        let maskedParticipantStoredHex = web3.utils.toHex(maskedParticipantStored);
        assert.equal(maskedParticipant, maskedParticipantStoredHex, "unexpectedly, the stored masked participant did not match the value supplied.");


        await coordInterface.unmask(A_BLOCKCHAIN_ID, EXPECTED_OFFSET, salt, {from: newParticipant});

        // Check that the masked participant doesn't exist and the unmasked participant does exist.
        isParticipant = await coordInterface.isUnmaskedBlockchainParticipant.call(A_BLOCKCHAIN_ID, newParticipant);
        assert.equal(isParticipant, true, "unexpectedly, unmasked participant doesnt exist: isUnmaskedBlockchainParticipant == false");

        maskedParticipantStored = await coordInterface.getMaskedBlockchainParticipant.call(A_BLOCKCHAIN_ID, EXPECTED_OFFSET);
        maskedParticipantStoredHex = web3.utils.toHex(maskedParticipantStored);
        assert.equal("0x0", maskedParticipantStoredHex, "unexpectedly, the stored masked participant not zeroized.");
    });


    // TODO: Test that unmasking when the sender address doesn't match fails.

    // TODO: Test that when the offset or salt are wrong the unmasking fails.

    // TODO: Test the behaviour when the masked participant is also an unmasked participant.

});