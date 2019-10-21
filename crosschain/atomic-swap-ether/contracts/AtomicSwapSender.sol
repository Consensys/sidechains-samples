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
import "./Crosschain.sol";
import "./AtomicSwapReceiver.sol";

contract AtomicSwapSender is Crosschain {
    uint256 private constant DECIMAL_POINT = 2**128;

    address public owner;
    uint256 public exchangeRate;
    uint256 public receiverSidechainId;
    AtomicSwapReceiverInterface public receiverContract;

    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }

    constructor(uint256 _receiverSidechainId, address _receiverContract, uint256 _exchangeRate) public {
        owner = msg.sender;
        receiverSidechainId = _receiverSidechainId;
        receiverContract = AtomicSwapReceiverInterface(_receiverContract);
        exchangeRate = _exchangeRate;
    }


    function withdraw() external onlyOwner {
        msg.sender.transfer(address(this).balance);
    }

    function exchange(uint256 _expectedExchangeRate) payable external {
        require(msg.value < DECIMAL_POINT);

        // Check that the exchange rate hasn't changed unexpectedly.
        require(0 != exchangeRate);
        require(_expectedExchangeRate == exchangeRate);

        // Determine the amount of Ether in the other contract.
        uint256 receiverBalance = crosschainViewUint256(receiverSidechainId, address(receiverContract), abi.encodeWithSelector(receiverContract.getBalance.selector));
        require(receiverBalance < DECIMAL_POINT);

        // Calculate the equivalent amount of Ether on this chain.
        uint256 receiverSwapBalance = receiverBalance * DECIMAL_POINT / exchangeRate;

        uint256 swapAmountThisChain;
        if (msg.value >= receiverSwapBalance) {
            swapAmountThisChain = receiverSwapBalance;
        }
        else {
            swapAmountThisChain = msg.value;
        }

        uint256 swapAmountOtherChain = swapAmountThisChain * exchangeRate / DECIMAL_POINT;

        crosschainTransaction(receiverSidechainId, address(receiverContract), abi.encodeWithSelector(receiverContract.exchange.selector, swapAmountOtherChain) );


        // TODO if too much Ether has been submitted, then the extra Ether needs to be returned.
        msg.sender.transfer(msg.value - swapAmountThisChain);

    }

}