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



contract ERC20LockableAccount {
    address public owner;
    address public erc20RouterContract;
    uint256 public balance;

    // Only accept requests from the hotel router contract.
    modifier onlyERC20RouterContract() {
        require(msg.sender == erc20RouterContract);
        _;
    }
    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }


    constructor(address _routerContract) public {
        owner = msg.sender;
        erc20RouterContract = _routerContract;
    }

    function setERC20RouterContract(address _routerContract) external onlyOwner {
        erc20RouterContract = _routerContract;
    }

    function sub(uint256 _amount) external onlyERC20RouterContract {
        require(_amount <= balance, "ERC20: transfer amount exceeds balance");
        balance -= _amount;
    }

    function add(uint256 _amount) external onlyERC20RouterContract {
        uint256 temp = balance + _amount;
        require(temp >= balance, "ERC20: addition overflow");
        balance += _amount;
    }

    function withdrawAll() external onlyERC20RouterContract returns (uint256 all) {
        all = balance;
        balance = 0;
    }

}