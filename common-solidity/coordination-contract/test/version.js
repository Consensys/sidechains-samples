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
 * Version number tests.
 *
 */

contract('Version tests', function(accounts) {
    let common = require('./common');

    const V1 = 1;
    const NON_EXISTANT_CROSSCHAIN_TRANSACTION = "0x01";

    it("getVersion: One", async function() {
        let coordInterface = await await common.getDeployedCrosschainCoordination();
        const ver = await coordInterface.getVersion.call();

        assert.equal(V1, ver);
    });


});
