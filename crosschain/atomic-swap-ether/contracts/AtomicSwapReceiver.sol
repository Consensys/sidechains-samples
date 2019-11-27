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

import "./AtomicSwapReceiverInterface.sol";
import "../../../common-solidity/crosschain-precompile-calls/contracts/Crosschain.sol";
import "./DepositWithdrawl.sol";


/*
 * This contract works with the AtomicSwapSender contract to affect
 * an atomic swap of Ether between blockchains / sidechains.
 *
 * The entity wishing to offer Ether on the Receiving chain deploys this contract
 * on the Receiving chain, funding it with some Ether. The entity can add more
 * Ether or withdraw Ether using the deposit() and withdraw() functions.
 *
 * The entity wishing to execute the exchange calls the AtomicSwapSender
 * contract's exchange function. This function will call the exchange function on this
 * sidechain which completes the exchange.
 */
contract AtomicSwapReceiver is AtomicSwapReceiverInterface, Crosschain, DepositWithdrawl {
    address public senderContract;
    uint256 public senderSidechainId;

    /**
    * Set-up the owner of this contract and the blockchain / sidechain that this contract will be
    * called from. Funds can be deposited in this constuctor.
    *
    * @param _senderSidechainId The id of the blockchain which will call the exchange function in
    *          this contract.
    */
    constructor(uint256 _senderSidechainId) public payable {
        senderSidechainId = _senderSidechainId;
    }

    /**
    * Set-up the address of the contract that this contract will be called from.
    *
    * @param _senderContract The address of the contract that will call the exchange function in
    *          this contract.
    */
    function setSenderContract(address _senderContract) external {
        senderContract = _senderContract;
    }

    /**
    * Transfer within the contract _amount of Wei to the message sender. The function call must be part of
    * an Atomic Crosschain Transaction. The caller must be the blockchain / sidechain
    * configured in the AtomicSwapReceiver contract.
    *
    * @param _amount The amount of Wei to transfer.
    */
    function exchange(uint256 _amount) external {
        require(senderContract != address(0));

        uint256 fromBlockchainId = crosschainGetInfoFromBlockchainId();
        address fromAddress = crosschainGetInfoFromAddress();
        uint256 originatingBlockchainId = crosschainGetInfoOriginatingBlockchainId();

        // Check that the transaction came from where we expect it to.
        require(senderSidechainId == fromBlockchainId);
        require(senderContract == fromAddress);

        // Check that the from blockchain is the originating blockchain. The implication is that
        // the entity operating this contract will ensure the from blockchain id is not being spoofed.
        require(senderSidechainId == originatingBlockchainId);

        // The amount transferred could be the same or less than the amount of Ether
        // in the contract.
        require(whoOwnsWhat[owner] >= _amount);
        whoOwnsWhat[owner] -= _amount;
        whoOwnsWhat[msg.sender] += _amount;
    }
}