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
 * Contract to manage funds in the contract
 */
contract DepositWithdrawl {
    address public owner;

    // Holds a mapping of address to Ether. That is, who owns what
    // Ether that is held by the contract.
    mapping(address=>uint256) internal whoOwnsWhat;

    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }

    constructor() internal payable {
        owner = msg.sender;
        whoOwnsWhat[msg.sender] = msg.value;
    }

    /**
    * Add funds to this contract.
    */
    function deposit() external payable {
        whoOwnsWhat[msg.sender] += msg.value;
    }

    /**
     * Allows _amount of Wei to be withdrawn from this contract.
     *
     * @param _amount The amount to withdraw.
     */
    function withdraw(uint256 _amount) external {
        require(whoOwnsWhat[msg.sender] >= _amount);
        whoOwnsWhat[msg.sender] -= _amount;
        msg.sender.transfer(_amount);
    }

    /**
     * Get the amount of Ether an address owns in this contract.
     *
     * @return amount of Ether in Wei msg.sender has stored in this contract.
     */
    function getMyBalance() external view returns (uint256) {
        return(whoOwnsWhat[msg.sender]);
    }
    /**
     * Get the amount of Ether an address owns in this contract.
     *
     * @return amount of Ether in Wei msg.sender has stored in this contract.
     */
    function getBalance(address account) external view returns (uint256) {
        return(whoOwnsWhat[account]);
    }
}