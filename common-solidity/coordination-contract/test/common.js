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
 * This file contains code which is common to many of the test files.
 */

const CrosschainCoordinationV1 = artifacts.require("./CrosschainCoordinationV1.sol");

// All tests of the public API must be tested via the interface. This ensures all functions
// which are assumed to be part of the public API actually are in the interface.
const CrosschainCoordinationInterface = artifacts.require("./CrosschainCoordinationInterface.sol");

const VotingAlgMajority = artifacts.require("./VotingAlgMajority.sol");


const MANAGEMENT_PSEUDO_BLOCKCHAIN_ID = "0";

// Note that these values need to match what is set in the 1_initial_migration.js file.
const VOTING_PERIOD = "3";
const VOTING_PERIOD_PLUS_ONE = "4";
const VOTING_PERIOD_MINUS_ONE = "2";

const VOTE_NONE = "0";
const VOTE_ADD_MASKED_PARTICIPANT = "1";
const VOTE_REMOVE_MASKED_PARTICIPANT = "2";
const VOTE_ADD_UNMASKED_PARTICIPANT = "3";
const VOTE_REMOVE_UNMASKED_PARTICIPANT = "4";
const VOTE_CHANGE_PUBLIC_KEY = "5";

const REVERT = "Returned error: VM Exception while processing transaction: revert";

//TODO create a valid public key.
const A_VALID_PUBLIC_KEY = "0x1234567";
const KEY_VERSION = "1";

const mineOneBlock = async function() {
    // Mine one or more blocks.
    await web3.currentProvider.send({
        jsonrpc: '2.0',
        method: 'evm_mine',
        params: [],
        id: 0,
    }, function(err, result) {
        // dummy call back
    })
};


const mineBlocks = async function(n) {
    for (let i = 0; i < n; i++) {
        await mineOneBlock()
    }
};








function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}


module.exports = {
    MANAGEMENT_PSEUDO_BLOCKCHAIN_ID: MANAGEMENT_PSEUDO_BLOCKCHAIN_ID,

    VOTE_NONE: VOTE_NONE,

    VOTE_ADD_MASKED_PARTICIPANT: VOTE_ADD_MASKED_PARTICIPANT,
    VOTE_REMOVE_MASKED_PARTICIPANT: VOTE_REMOVE_MASKED_PARTICIPANT,
    VOTE_ADD_UNMASKED_PARTICIPANT: VOTE_ADD_UNMASKED_PARTICIPANT,
    VOTE_REMOVE_UNMASKED_PARTICIPANT: VOTE_REMOVE_UNMASKED_PARTICIPANT,
    VOTE_CHANGE_PUBLIC_KEY: VOTE_CHANGE_PUBLIC_KEY,

    VOTING_PERIOD: VOTING_PERIOD,
    VOTING_PERIOD_PLUS_ONE: VOTING_PERIOD_PLUS_ONE,
    VOTING_PERIOD_MINUS_ONE: VOTING_PERIOD_MINUS_ONE,
    KEY_VERSION: KEY_VERSION,
    getValidVotingContractAddress: async function() {
        return (await VotingAlgMajority.deployed()).address;
    },

    REVERT: REVERT,

    A_VALID_PUBLIC_KEY: A_VALID_PUBLIC_KEY,

    getNewCrosschainCoordination: async function() {
        let instance = await CrosschainCoordinationV1.new((await VotingAlgMajority.deployed()).address, VOTING_PERIOD);
        let instanceAddress = instance.address;
        return await CrosschainCoordinationInterface.at(instanceAddress);
    },
    getDeployedCrosschainCoordination: async function() {
        let instance = await CrosschainCoordinationV1.deployed();
        let instanceAddress = instance.address;
        return await CrosschainCoordinationInterface.at(instanceAddress);
    },

    mineOneBlock: mineOneBlock,

    mineBlocks: mineBlocks,

    checkVotingResult: function(logs) {
        assert.equal(logs.length, 1);
        assert.equal(logs[0].event, "VoteResult");
        return logs[0].args._result;
    },

    getLatestBlock: async function() {
        let block = await web3.eth.getBlock("latest");
        return block;
    },
    getBlockNumber: async function() {
        let block = await web3.eth.getBlock("latest");
        return block.number;
    },

    dumpAllDump1Events: async function(anInterface) {
        console.log("ContractAddress                                 Event           BlkNum DomainHash                 val1             val2                val3");
        await anInterface.Dump1({}, {fromBlock: 0, toBlock: "latest"}).get(function(error, result){
            if (error) {
                console.log(error);
                throw error;

            }
            if (result.length === 0) {
                console.log("No events recorded");
            } else {
                var i;
                for (i = 0; i < result.length; i++) {
                    console.log(
                        result[i].address + " \t" +
                        result[i].event + " \t" +
                        result[i].blockNumber + " \t" +
                        result[i].args.a + " \t" +
                        result[i].args.b + " \t" +
                        result[i].args.c + " \t"
                        //+
//                        result[i].blockHash + "    " +
//                        result[i].logIndex + " " +
//                        result[i].transactionHash + "  " +
//                        result[i].transactionIndex
                    );

                }
            }
        });
        // If this sleep isn't here, the Ethereum Client is shutdown before the code above finished executing.
        await sleep(100);
    }



};

