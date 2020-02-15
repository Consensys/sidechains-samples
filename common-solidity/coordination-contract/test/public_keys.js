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

contract('Public Keys:', function(accounts) {
    let common = require('./common');

    const A_BLOCKCHAIN_ID = "0x0000000000000000000000000000000000000000000000000000000000000002";


    async function addBlockchain(coordInterface) {
        await coordInterface.addBlockchain(A_BLOCKCHAIN_ID, (await VotingAlgMajority.deployed()).address, common.VOTING_PERIOD);
    }

    it("public key decode", async function() {
        let coordInterface = await common.getNewCrosschainCoordination();
        await addBlockchain(coordInterface);

        let keyVersion = "1";
        let targetOfVoteIsNone = "0";

        await coordInterface.proposeVote(A_BLOCKCHAIN_ID, common.VOTE_CHANGE_PUBLIC_KEY, targetOfVoteIsNone, keyVersion, common.PUBLIC_KEY);
        await common.mineBlocks(parseInt(common.VOTING_PERIOD));
        let actionResult = await coordInterface.actionVotes(A_BLOCKCHAIN_ID, targetOfVoteIsNone);
        const result = await common.checkVotingResult(actionResult.logs);
        assert.equal(true, result, "incorrect result reported in event");

        let exists = await coordInterface.publicKeyExists(A_BLOCKCHAIN_ID, keyVersion);
        assert.equal(true, exists, "unexpectedly, the key is not in the contract.");

        var {blockNumber, algorithm, key} = await coordInterface.getPublicKey(A_BLOCKCHAIN_ID, keyVersion)
        //console.log("block number: " + blockNumber);
        //console.log("algorithm: " + algorithm);
        //console.log("key: " + key);

        // These values are decimal equivalents to the hexadecimal public key values in common.PUBLIC_KEY
        assert.equal(key[0], "15708193363243225275616394410035962949243193317474330111734786682038951469099", "key[0] incorrect");
        assert.equal(key[1], "1188331353279655925534827725772442412689505989247595413767014200443655972901", "key[1] incorrect");
        assert.equal(key[2], "190304280072117358474082803523199131933615869822817420523710391421051176208", "key[2] incorrect");
        assert.equal(key[3], "17678420073345995323628877228661749409444380174258249952808734399169055452901", "key[3] incorrect");

        assert.equal(algorithm, "1", "Algorithm should be 1");
    });

});