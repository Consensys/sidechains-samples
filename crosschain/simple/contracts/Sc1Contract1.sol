/* SPDX-License-Identifier: Apache-2.0 */
pragma solidity ^0.5;

import "../../../common-solidity/crosschain-precompile-calls/contracts/Crosschain.sol";
import "./Sc2Contract2Interface.sol";

contract Sc1Contract1 is Crosschain {
    uint256 private sc2Id;
    uint256 public localValue;
    Sc2Contract2Interface private contract2;

    constructor (uint256 _Sc2SidechainId, address _contract2 ) public {
        sc2Id = _Sc2SidechainId;
        contract2 = Sc2Contract2Interface(_contract2);
    }

    function crosschain_setter() external {
        crosschainTransaction(sc2Id, address(contract2), abi.encodeWithSelector(contract2.set.selector));
    }

    function crosschain_getUint256_transaction() external {
        // We want this to be a transaction instead of a view, so we can call it as an Originating Transaction
        // Originating Views are an uninterestingly simple corner case that we haven't implemented yet
        localValue = crosschainViewUint256(sc2Id, address(contract2), abi.encodeWithSelector(contract2.getUint256.selector));
    }


}
