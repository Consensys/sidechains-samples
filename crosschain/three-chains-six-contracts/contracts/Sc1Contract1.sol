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
import "./Sc2Contract2Interface.sol";
import "./Sc3Contract5Interface.sol";
import "./Sc2Contract3Interface.sol";

contract Sc1Contract1 is Crosschain {
    uint256 private sc2;
    uint256 private sc3;
    Sc2Contract2Interface private contract2;
    Sc2Contract3Interface private contract3;
    Sc3Contract5Interface private contract5;

    uint256 public val;

    constructor (uint256 _Sc2SidechainId, uint256 _Sc3SidechainId, address payable _contract2,
        address payable _contract3, address payable _contract5) public {
        sc2 = _Sc2SidechainId;
        sc3 = _Sc3SidechainId;
        contract2 = Sc2Contract2Interface(_contract2);
        contract3 = Sc2Contract3Interface(_contract3);
        contract5 = Sc3Contract5Interface(_contract5);
    }


    function setVal(uint256 _val) external {
        val = _val;
    }


    function doStuff(uint256 _val) external {
        uint256 sc2Val = crosschainViewUint256(sc2, address(contract2), abi.encodeWithSelector(contract2.get.selector));
        val = sc2Val;

        if (_val > sc2Val) {
            uint256 calc = crosschainViewUint256(sc3, address(contract5), abi.encodeWithSelector(contract5.calculate.selector, _val, sc2Val));
            crosschainTransaction(sc2, address(contract3), abi.encodeWithSelector(contract3.process.selector, calc) );
            val = calc;
        }
    }
}
