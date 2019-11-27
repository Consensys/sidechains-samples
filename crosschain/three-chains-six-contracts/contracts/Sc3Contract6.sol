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
import "./Sc2Contract4Interface.sol";

contract Sc3Contract6 is Crosschain, Sc3Contract6Interface {
    uint256 private sc2;
    Sc2Contract4Interface private contract4;

    uint256 public val;

    constructor (uint256 _Sc2SidechainId, address payable _contract4) public {
        sc2 = _Sc2SidechainId;
        contract4 = Sc2Contract4Interface(_contract4);

    }

    function setVal(uint256 _val) external {
        val = _val;
    }


    function get(uint256 _val) external view returns(uint256) {
        uint256 sc2Val = crosschainViewUint256(sc2, address(contract4), abi.encodeWithSelector(contract4.get.selector, val));
        return _val + sc2Val;
    }
}
