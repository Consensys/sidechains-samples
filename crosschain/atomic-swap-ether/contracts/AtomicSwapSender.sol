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

contract AtomicSwapExecution is Crosschain {
    uint256 private constant DECIMAL_POINT = 2**128;

    address public owner;
    address public registrationContract;
    uint256 public exchangeRate;

    uint256 receiverSidechainId;
    AtomicSwapExecutionReceiverInterface receiverContract;

    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }

    modifier onlyRegistrationContract() {
        require(msg.sender == registrationContract);
        _;
    }


    constructor(address _registrationContract, uint256 _receiverSidechainId, address _receiverContract) {
        owner = msg.sender;
        registrationContract = _registrationContract;
        receiverContract = AtomicSwapExecutionReceiverInterface(_receiverContract);
        receiverSidechainId = _receiverSidechainId;
    }

    function changeExchangeRate(uint256 _newExchangeRate, uint256 _receiverSidechainId) external {
        require(msg.sender == registrationContract);
        require(_receiverSidechainId == receiverSidechainId);
        exchangeRate = _newExchangeRate;
    }

    function withdraw() external ownlyOwner {
        msg.sender.transfer(address(this).balance);
    }

    function exchange(uint256 expectedExchangeRate) payable external {
        // Check that the exchange rate hasn't changed unexpectedly.
        require(expectedExchangeRate == exchangeRate);

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

        uint256 swapAmoutOtherChain = swapAmountThisChain * exchangeRate / DECIMAL_POINT;

        crosschainTransaction(receiverSidechainId, address(receiverContract), abi.encodeWithSelector(receiverContract.exchange.selector, swapAmountOtherChain) );


        // TODO if too much Ether has been submitted, then the extra Ether needs to be returned.
        msg.sender.transfer(msg.value - swapAmountThisChain);

    }



    function setVal(uint256 _val) external {
        val = _val;
    }

    function get() external view returns(uint256) {
        return val;
    }
}