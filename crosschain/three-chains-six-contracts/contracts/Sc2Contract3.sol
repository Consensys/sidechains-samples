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
pragma solidity >=0.4.23;

import "../../../common-solidity/crosschain-precompile-calls/contracts/Crosschain.sol";
import "./Sc3Contract6Interface.sol";
import "./Sc2Contract3Interface.sol";

contract Sc2Contract3 is Crosschain, Sc2Contract3Interface {
    uint256 private sc3;
    Sc3Contract6Interface private contract6;

    uint256 public val;

    constructor (uint256 _Sc3SidechainId, address payable _contract6) public {
        sc3 = _Sc3SidechainId;
        contract6 = Sc3Contract6Interface(_contract6);

    }

    function setVal(uint256 _val) external {
        val = _val;
    }


    function process(uint256 _val) external {
        uint256 sc3Val = crosschainViewUint256(sc3, address(contract6), abi.encodeWithSelector(contract6.get.selector, val));
        val = _val + sc3Val;
    }
}
