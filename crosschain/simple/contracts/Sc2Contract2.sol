/* SPDX-License-Identifier: Apache-2.0 */
pragma solidity >=0.5;
import "./Sc2Contract2Interface.sol";

contract Sc2Contract2 is Sc2Contract2Interface {

    bool private val;

    function set() external {
        val = true;
    }

    function clear() external {
        val = false;
    }

    function get() external view returns(bool) {
        return val;
    }
}
