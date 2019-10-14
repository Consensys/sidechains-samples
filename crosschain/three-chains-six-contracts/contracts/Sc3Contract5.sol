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
import "./Sc3Contract5Interface.sol";

contract Sc3Contract5 is Sc3Contract5Interface {

    uint256 public val;


    function setVal(uint256 _val) external {
        val = _val;
    }

    function calculate(uint256 _val1, uint256 _val2) external view returns(uint256) {
        return val + _val1 + _val2;
    }
}