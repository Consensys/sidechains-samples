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

    // 11
    const A_BLOCKCHAIN_ID = "0xB";

    // Start message
    // Public key used when generating the start message signature.
    const START_MESSAGE_PUBLIC_KEY = "0x0000000118f2c53d47aa0f16c9c7db250e1b7ba0afb7c128cdaf4fbd11d489a8bbed6e6a181e2161471983f38c218fc02227ba0b7f39913430b91bf37a7de700972fdd8f17756093d51d43c42d6b08d221c3cd3c69fbdcfbb3a55096ad710b2d4bb232561ece4e57bbb726739f89346b23ae8fbf7db4b0d52d666c861952c7c6c681f5e5";
    //48165743513990300380988978454821709068278076602382423941659282846720703716301
    const A_CROSS_TX_ID = "306119615846880469765888303682228643010750279842122396031849905157685439122";
    //102665386630937920234814764809786284335987281145580768364507172654125582397871
    const HASH_OF_START_MESSAGE = "78568958157276484761162660378195871061994187815754976194416851188085214982154";
    // Time-out: 138
    const START_MESSAGE_TIMEOUT = "531";
    const KEY_VERSION = "1";
    const START_MESSAGE_SIGNATURE = "0x2d4aea78c76d517984086899bb5d5936cdc2e4f598132ff7bb4b5158359dc4ef1ac2546b9726a817adb07087ea9face58d5d493622c3ca3c62ec10b5fc6e6c2e";
    //Start message contents to be signed: 0xf85c01219459a9351913fa74e17d9c98ed57b3b8e46c21fea00b9fad41ef9e947c151cedd82bd4c51f497c2fd574902eaa274ceea35c1738ea92a0adb47454802f9203862c10261f7ce09f05f3b206840d5d69daea59f95808b00a820213
    0x0000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000000000000000000030753e4a8aad7f8597332e813735def5dd395028000000000000000000000000000000000000000000000000000000000000000b00ad41ef9e947c151cedd82bd4c51f497c2fd574902eaa274ceea35c1738ea92adb47454802f9203862c10261f7ce09f05f3b206840d5d69daea59f95808b00a0000000000000000000000000000000000000000000000000000000000000213


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
        await coordInterface.proposeVote(A_BLOCKCHAIN_ID, common.VOTE_CHANGE_PUBLIC_KEY, "0", "1", START_MESSAGE_PUBLIC_KEY);
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_BLOCKCHAIN_ID, "0");
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");

        let exists = await coordInterface.publicKeyExists(A_BLOCKCHAIN_ID, "1");
        assert.equal(true, exists, "unexpectedly, the key is not in the contract.");

    }

    async function start(coordInterface) {
        //let blockNumber = await common.getBlockNumber();
        // Ensure the time-out is long enough that the crosschain transaction won't expire prior to checking the status.
        let actionResult = await coordInterface.start(A_BLOCKCHAIN_ID, A_CROSS_TX_ID, HASH_OF_START_MESSAGE, START_MESSAGE_TIMEOUT, KEY_VERSION, START_MESSAGE_SIGNATURE);
    }

    // async function startShortTimeout(coordInterface) {
    //     let blockNumber = await common.getBlockNumber();
    //     let timeoutBlockNumber = blockNumber + SHORT_TIMEOUT_INT;
    //     // Ensure the time-out is long enough that the crosschain transaction won't expire prior to checking the status.
    //     await coordInterface.start(A_BLOCKCHAIN_ID, A_CROSS_TX_ID, common.TX_HASH1, timeoutBlockNumber.toString(), common.KEY_VERSION, common.SIGNATURE);
    // }

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


        assert.equal(0, 1);


    });

    // TODO need to generate test data for 0 time-out
    // it("start with zero timeout", async function() {
    //     let coordInterface = await await common.getNewCrosschainCoordination();
    //     await addBlockchain(coordInterface);
    //     await addBlockchainPublicKey(coordInterface);
    //
    //     let didNotTriggerError = false;
    //     try {
    //         await coordInterface.start(A_BLOCKCHAIN_ID, A_CROSS_TX_ID, common.TX_HASH1, "0", common.KEY_VERSION, common.SIGNATURE);
    //         didNotTriggerError = true;
    //     } catch(err) {
    //         assert.equal(err.message, common.REVERT);
    //         //console.log("ERROR! " + err.message);
    //     }
    //     assert.equal(didNotTriggerError, false);
    // });

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
        await start(coordInterface);
        await common.mineBlocks(parseInt(START_MESSAGE_TIMEOUT));
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
        await start(coordInterface);
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
        await start(coordInterface);
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