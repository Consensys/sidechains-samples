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
import "./AtomicSwapReceiver.sol";

/*
 * This contract works with the AtomicSwapReceiver contract to affect
 * an atomic swap of Ether between blockchains / sidechains.
 *
 * The entity wishing to offer Ether on the Receiving chain deploys this contract
 * on the Sending chain.
 *
 * The entity wishing to execute the exchange calls the exchange function. This
 * function will call the exchange function on the AtomicSwapReceiver contract
 * on the other blockchain / sidechain which completes the exchange.
 *
 * The entity offering the Ether on the other sidechain will then have some Ether
 * available to them in this contract. They can withdraw the Ether using the withdraw()
 * function.
 */
contract AtomicSwapSender is Crosschain, DepositWithdrawl {
    uint256 private constant DECIMAL_POINT = 2**64;
    uint256 private constant MAX_VALUE = 2**127;

    uint256 public exchangeRate;
    uint256 public receiverSidechainId;
    AtomicSwapReceiverInterface public receiverContract;

    /**
    * Create the sender contract, connecting it with a receiver contract and specifying
    * the exchange rate.
    *
    * The exchange rate is specified as a number between 0 and 2**127, with 2**64 being
    * the virtual decimal point. To specify the following exchange rates use the following.
    *
    * Exchange Rate                                                            Value to Use
    * 1 Ether on this sidechain equals 1 Ether on the other sidechain.         2**64  = 0x10000000000000000 = 18446744073709551616
    * 1 Ether on this sidechain equals 2 Ether on the other sidechain.         2**65  = 0x20000000000000000 = 36893488147419103232
    * 2 Ether on this sidechain equals 1 Ether on the other sidechain.         2**63  = 0x08000000000000000 =  9223372036854775808
    *
    * @param _receiverSidechainId Sidechain identifier of sidechain receiver contract is on.
    * @param _receiverContract Contract address of receiver contract on other sidechain.
    * @param _exchangeRate Exchange rate as described above.
    */
    constructor(uint256 _receiverSidechainId, address _receiverContract, uint256 _exchangeRate) public {
        require(_exchangeRate < MAX_VALUE);
        require(_exchangeRate != 0);
        receiverSidechainId = _receiverSidechainId;
        receiverContract = AtomicSwapReceiverInterface(_receiverContract);
        exchangeRate = _exchangeRate;
    }

    /**
    * Entity wishing to accept offer calls this function to execute the Atomic Swap of Ether.
    */
    function exchange(uint256 _amount) external {
        require(_amount < MAX_VALUE);
        require(whoOwnsWhat[msg.sender] >= _amount);
        whoOwnsWhat[msg.sender] -= _amount;
        whoOwnsWhat[owner] += _amount;
        uint256 swapAmountOtherChain = _amount * exchangeRate / DECIMAL_POINT;
        crosschainTransaction(receiverSidechainId, address(receiverContract), abi.encodeWithSelector(receiverContract.exchange.selector, swapAmountOtherChain) );
    }
}