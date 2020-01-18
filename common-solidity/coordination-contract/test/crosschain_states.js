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
 * Check crosschain transaction state logic.
 *
 */
const VotingAlgMajority = artifacts.require("./VotingAlgMajority.sol");

contract('Crosschain Transaction States:', function(accounts) {
    let common = require('./common');

    const A_BLOCKCHAIN_ID = "0x2";
    const A_CROSS_TX_ID = "0x123456789ABCDEF123455";

    const A_VALID_COMMIT_MESSAGE = "0x12345";
    const A_VALID_IGNORE_MESSAGE = "0x12345";

    const SHORT_TIMEOUT = "2";
    const SHORT_TIMEOUT_INT = 2;
    const SHORT_TIMEOUT_PLUS_ONE = "3";
    const LONG_TIMEOUT = "100";
    const LONG_TIMEOUT_INT = 100;


    // Crosschain transaction states.
    const NOT_STARTED = 0;
    const STARTED = 1;
    const COMMITTED = 2;
    const IGNORED = 3;



    async function addBlockchain(coordInterface) {
        await coordInterface.addBlockchain(A_BLOCKCHAIN_ID, (await VotingAlgMajority.deployed()).address, common.VOTING_PERIOD);
    }

    async function addBlockchainPublicKey(coordInterface) {
        await coordInterface.proposeVote(A_BLOCKCHAIN_ID, common.VOTE_CHANGE_PUBLIC_KEY, "0", "1", common.PUBLIC_KEY);
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_BLOCKCHAIN_ID, "0");
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");

        let exists = await coordInterface.publicKeyExists(A_BLOCKCHAIN_ID, "1");
        assert.equal(true, exists, "unexpectedly, the key is not in the contract.");

    }

    async function start(coordInterface) {
        let blockNumber = await common.getBlockNumber();
        let timeoutBlockNumber = blockNumber + LONG_TIMEOUT_INT;
        // Ensure the time-out is long enough that the crosschain transaction won't expire prior to checking the status.
        await coordInterface.start(A_BLOCKCHAIN_ID, A_CROSS_TX_ID, common.TX_HASH1, timeoutBlockNumber.toString(), common.KEY_VERSION, common.SIGNATURE);
    }

    async function startShortTimeout(coordInterface) {
        let blockNumber = await common.getBlockNumber();
        let timeoutBlockNumber = blockNumber + SHORT_TIMEOUT_INT;
        // Ensure the time-out is long enough that the crosschain transaction won't expire prior to checking the status.
        await coordInterface.start(A_BLOCKCHAIN_ID, A_CROSS_TX_ID, common.TX_HASH1, timeoutBlockNumber.toString(), common.KEY_VERSION, common.SIGNATURE);
    }

    it("notstart", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);
        let state = await coordInterface.getCrosschainTransactionStatus.call(A_BLOCKCHAIN_ID, A_CROSS_TX_ID);
        assert.equal(NOT_STARTED, state, "state for transaction which does not exist should be not started");
    });


    it("start", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);
        await addBlockchainPublicKey(coordInterface);
        await start(coordInterface);

        let state = await coordInterface.getCrosschainTransactionStatus.call(A_BLOCKCHAIN_ID, A_CROSS_TX_ID);
        assert.equal(STARTED, state, "state is no started");
    });

    it("start with zero timeout", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);
        await addBlockchainPublicKey(coordInterface);

        let didNotTriggerError = false;
        try {
            await coordInterface.start(A_BLOCKCHAIN_ID, A_CROSS_TX_ID, common.TX_HASH1, "0", common.KEY_VERSION, common.SIGNATURE);
            didNotTriggerError = true;
        } catch(err) {
            assert.equal(err.message, common.REVERT);
            //console.log("ERROR! " + err.message);
        }
        assert.equal(didNotTriggerError, false);
    });

    it("start same transaction a second time", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);
        await addBlockchainPublicKey(coordInterface);
        await start(coordInterface);

        let didNotTriggerError = false;
        try {
            await start(coordInterface);
            didNotTriggerError = true;
        } catch(err) {
            assert.equal(err.message, common.REVERT);
            //console.log("ERROR! " + err.message);
        }
        assert.equal(didNotTriggerError, false);
    });


    it("timed-out", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);
        await addBlockchainPublicKey(coordInterface);
        await startShortTimeout(coordInterface);
        await common.mineBlocks(parseInt(SHORT_TIMEOUT_PLUS_ONE));
        let state = await coordInterface.getCrosschainTransactionStatus.call(A_BLOCKCHAIN_ID, A_CROSS_TX_ID);

        assert.equal(IGNORED, state, "state is not ignored");
    });

    it("committed", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);
        await addBlockchainPublicKey(coordInterface);
        await start(coordInterface);

        await coordInterface.commit(A_BLOCKCHAIN_ID, A_CROSS_TX_ID, A_VALID_COMMIT_MESSAGE);
        let state = await coordInterface.getCrosschainTransactionStatus.call(A_BLOCKCHAIN_ID, A_CROSS_TX_ID);
        assert.equal(COMMITTED, state, "state is not committed");
    });

    it("commit without start", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);
        await addBlockchainPublicKey(coordInterface);

        let didNotTriggerError = false;
        try {
            await coordInterface.commit(A_BLOCKCHAIN_ID, A_CROSS_TX_ID, A_VALID_COMMIT_MESSAGE);
            didNotTriggerError = true;
        } catch(err) {
            assert.equal(err.message, common.REVERT);
            //console.log("ERROR! " + err.message);
        }
        assert.equal(didNotTriggerError, false);
    });

    it("commit after timeout", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);
        await addBlockchainPublicKey(coordInterface);
        await startShortTimeout(coordInterface);
        await common.mineBlocks(parseInt(SHORT_TIMEOUT_PLUS_ONE));

        let didNotTriggerError = false;
        try {
            await coordInterface.commit(A_BLOCKCHAIN_ID, A_CROSS_TX_ID, A_VALID_COMMIT_MESSAGE);
            didNotTriggerError = true;
        } catch(err) {
            assert.equal(err.message, common.REVERT);
            //console.log("ERROR! " + err.message);
        }
        assert.equal(didNotTriggerError, false);
    });


    it("ignored", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);
        await addBlockchainPublicKey(coordInterface);
        await start(coordInterface);

        await coordInterface.ignore(A_BLOCKCHAIN_ID, A_CROSS_TX_ID, A_VALID_IGNORE_MESSAGE);
        let state = await coordInterface.getCrosschainTransactionStatus.call(A_BLOCKCHAIN_ID, A_CROSS_TX_ID);
        assert.equal(IGNORED, state, "state is not ignored");
    });

    it("ignore without start", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);
        await addBlockchainPublicKey(coordInterface);

        let didNotTriggerError = false;
        try {
            await coordInterface.ignore(A_BLOCKCHAIN_ID, A_CROSS_TX_ID, A_VALID_COMMIT_MESSAGE);
            didNotTriggerError = true;
        } catch(err) {
            assert.equal(err.message, common.REVERT);
            //console.log("ERROR! " + err.message);
        }
        assert.equal(didNotTriggerError, false);
    });

    it("ignore after timeout", async function() {
        let coordInterface = await await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);
        await addBlockchainPublicKey(coordInterface);
        await startShortTimeout(coordInterface);
        await common.mineBlocks(parseInt(SHORT_TIMEOUT_PLUS_ONE));

        let didNotTriggerError = false;
        try {
            await coordInterface.ignore(A_BLOCKCHAIN_ID, A_CROSS_TX_ID, A_VALID_COMMIT_MESSAGE);
            didNotTriggerError = true;
        } catch(err) {
            assert.equal(err.message, common.REVERT);
            //console.log("ERROR! " + err.message);
        }
        assert.equal(didNotTriggerError, false);
    });




});