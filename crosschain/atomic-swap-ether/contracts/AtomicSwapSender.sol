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
    uint256 private constant DECIMAL_POINT = 2**64;
    uint256 private constant MAX_VALUE = 2**127;

    address public owner;
    uint256 public exchangeRate;
    uint256 public receiverSidechainId;
    AtomicSwapReceiverInterface public receiverContract;

    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }

    constructor(uint256 _receiverSidechainId, address _receiverContract, uint256 _exchangeRate) public {
        require(_exchangeRate < MAX_VALUE);
        require(_exchangeRate != 0);
        owner = msg.sender;
        receiverSidechainId = _receiverSidechainId;
        receiverContract = AtomicSwapReceiverInterface(_receiverContract);
        exchangeRate = _exchangeRate;
    }


    function withdraw() external onlyOwner {
        msg.sender.transfer(address(this).balance);
    }

    function exchange() payable external {
        require(msg.value < MAX_VALUE);
        uint256 swapAmountOtherChain = msg.value * exchangeRate / DECIMAL_POINT;
        crosschainTransaction(receiverSidechainId, address(receiverContract), abi.encodeWithSelector(receiverContract.exchange.selector, swapAmountOtherChain) );
    }

}