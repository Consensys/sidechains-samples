/* SPDX-License-Identifier: Apache-2.0 */
pragma solidity >=0.5;
import "./Sc2Contract2Interface.sol";

contract Sc2Contract2 is Sc2Contract2Interface {

    bool private val;
    uint256 private val2;

    function set() external {
        val = true;
    }

    function clear() external {
        val = false;
    }

    function get() external view returns(bool) {
        return val;
    }

    function setUint256(uint256 v) external {
        val2 = v;
    }

    function getUint256() external view returns(uint256) {
        return val2;
    }
}
