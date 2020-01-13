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

    const MESSAGE =       "0x01020304";
    const WRONG_MESSAGE = "0x010203";
    const PUBLIC_KEY =       "0x193b3c607827fcdf3a92562e9fc5713bb2905c575143c4a761e0a020331a45c330559f2fc40eca61b8a2fffce6958adb198e7e26949180960a116f09f13e80d12dd917bb96500fe7d8648217168d760699ced9e1a59bbeab1bdc0b329175bfec06417b1a4ad316d9cc4f3b3b352ae22f99c051c332af9ad2f959765237c7b3a8";
    const WRONG_PUBLIC_KEY = "0x093b3c607827fcdf3a92562e9fc5713bb2905c575143c4a761e0a020331a45c330559f2fc40eca61b8a2fffce6958adb198e7e26949180960a116f09f13e80d12dd917bb96500fe7d8648217168d760699ced9e1a59bbeab1bdc0b329175bfec06417b1a4ad316d9cc4f3b3b352ae22f99c051c332af9ad2f959765237c7b3a8";
    const SIGNATURE =       "0x04eeacfb720c98c34a4fca76602fd9acf498426738d987fbdeca837cb68252342bdd49e7943087f8db3b02bf658a33d0a53e3b112eaae33d96427ac726928c53";
    const WRONG_SIGNATURE = "0x00eeacfb720c98c34a4fca76602fd9acf498426738d987fbdeca837cb68252342bdd49e7943087f8db3b02bf658a33d0a53e3b112eaae33d96427ac726928c53";

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