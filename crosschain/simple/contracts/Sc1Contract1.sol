/* SPDX-License-Identifier: Apache-2.0 */
pragma solidity ^0.5;

import "./Crosschain.sol";
import "./Sc2Contract2Interface.sol";

contract Sc1Contract1 is Crosschain {
    uint256 private sc2;
    Sc2Contract2Interface private contract2;

    constructor (uint256 _Sc2SidechainId, address _contract2 ) public {
        sc2 = _Sc2SidechainId;
        contract2 = Sc2Contract2Interface(_contract2);
    }

    function crosschain_setter() external {
         crosschainTransaction(sc2, address(contract2), abi.encodeWithSelector(contract2.set.selector));
    }
}
