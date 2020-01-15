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
const SignatureTest = artifacts.require("./SignatureTest.sol");

contract('Sig Verification:', function(accounts) {

    const MESSAGE =       "0x01";
    const WRONG_MESSAGE = "0x02";
    const PUBLIC_KEY =       "0x2793affd189198f88c86cc138ebe3f4186927e6fe75c7af746aed7f010ff304902d6a6e1deeaabd5071c95c85aeb2fe4b7556107046dc70b07f9f154c02daa7b1830bdc713c31dd67d27d9d66903766ff6ccaefde178a10160a6b154cb9a26f910e154d227fc6959744b05fa8f35fdd05cc21cb8f9f00daaacac041ea4679373";
    const WRONG_PUBLIC_KEY = "0x0f777cbd5d6f1243796d4bf95329f77f42e7de156c624f095a4b8a733bdc23a70e164294193245ac36a52c9685387a1ac9611c91ab6c9eb11882f24da1f241eb0a1d2ea19baf52738dd9f627ed0f99b8a55f850c6a621484f7eb7ef763d493dd1c6b0a980ae7d69c2b1cfa75b54ce0ba7b9575b41773236214b24492f2e7bd3e";
    const SIGNATURE =       "0x1c81e628e38d60eec85e8c12410aa318ccfa425c8777cf9703e0f8484977390c289f2c26cb20feefeced3f696644f1a0bb2b12025477a6963a8b7d42427edcd6";
    const WRONG_SIGNATURE = "0x0118502e3748759a82a84e9dedec8bc95bc57ca7f43b15d4f75d5a613800d72907613b6878296262e4c24247aeccee418a1b55563b52a64cfda9e4bfd0ebe5c1";

    it("happy case", async function() {
        let instance = await SignatureTest.deployed();
        await instance.verifySignature(PUBLIC_KEY, MESSAGE, SIGNATURE);
        let verified = await instance.verified.call();
        assert.equal(true, verified, "is verified");
    });

    it("wrong message", async function() {
        let instance = await SignatureTest.deployed();
        await instance.verifySignature(PUBLIC_KEY, WRONG_MESSAGE, SIGNATURE);
        let verified = await instance.verified.call();
        assert.equal(false, verified, "is verified");
    });

    it("wrong public key", async function() {
        let instance = await SignatureTest.deployed();
        await instance.verifySignature(WRONG_PUBLIC_KEY, MESSAGE, SIGNATURE);
        let verified = await instance.verified.call();
        assert.equal(false, verified, "is verified");
    });

    it("wrong signature", async function() {
        let instance = await SignatureTest.deployed();
        await instance.verifySignature(PUBLIC_KEY, MESSAGE, WRONG_SIGNATURE);
        let verified = await instance.verified.call();
        assert.equal(false, verified, "is verified");
    });

});