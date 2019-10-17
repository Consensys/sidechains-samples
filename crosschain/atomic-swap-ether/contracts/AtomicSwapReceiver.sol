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


/*
 * This contract works with the AtomicSwapExecutionSender contract to affect
 * an atomic swap of Ether between sidechains.
 *
 * The entity wishing to offer Ether on the Receiving chain deploys this contract
 * on the Receiving chain, funding it with some Ether. The entity can add more
 * Ether or withdraw Ether using the deposit() and withdraw() functions.
 *
 * The entity wishing to execute the exchange calls the AtomicSwapExecutionSender
 * contract's exchange function. This function will call the exchange function on this
 * sidechain which completes the exchange.
 */
contract AtomicSwapExecutionReceiver {
    address public owner;
    address public senderContract;
    uint256 public senderSidechainId;

    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }



    constructor(uint256 _senderSidechainId, address _senderContract) external payable {
        owner = msg.sender;
        senderContract = _senderContract;
        senderSidechainId = _senderSidechainId;
    }

    function deposit() external payable onlyOwner {
    }

    function withdraw(uint256 _amount) external ownlyOwner {
        msg.sender.transfer(_amount);
    }

    function withdraw() external ownlyOwner {
        msg.sender.transfer(address(this).balance);
    }

    function exchange(uint256 _amount) payable external {
        // TODO check that the sending Sidechain ID and Contract are the expected ones.
        // TODO this will achieved by two precompiles

        msg.sender.transfer(_amount);
    }

    function getBalance() external view returns (uint256) {
        return this.balance;
    }

}