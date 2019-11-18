/* SPDX-License-Identifier: Apache-2.0 */

pragma solidity ^0.5;

interface Sc2Contract2Interface {
    function set() external;
    function clear() external;
    function get() external view returns(bool);
}
