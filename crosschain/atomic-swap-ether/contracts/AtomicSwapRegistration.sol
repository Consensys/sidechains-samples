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

contract AtomicSwapRegistration {
    // Implementation version of this contract.
    uint16 constant public VERSION = 1;


    uint256 private constant TEN = 10;


    struct ExchangeOffer {
        address owner;
        uint256 exchangeRate;
    }
    struct ExchangeOffers {
        mapping(address=>ExchangeOffer) offers;
        address[] offerAddresses;
    }
    mapping(uint256=>ExchangeOffers) private exchangeOffers;


    /**
  * Function modifier to ensure only unmasked sidechain participants can call the function.
  *
  * @param _sidechainId The 256 bit identifier of the Sidechain.
  * @dev Throws if the message sender isn't a participant in the sidechain, or if the sidechain doesn't exist.
  */
    modifier onlyOfferOwner(uint256 _sidechainId, address payable _executionContract) {
        require(exchangeOffers[_sidechainId].offers[_executionContract].owner == msg.sender);
        _;
    }

    function register(uint256 _sidechainId, address payable _executionContract, uint256 _exchangeRate) external {
        // TODO
    }
    function changeExchangeRate(uint256 _sidechainId, address payable _executionContract, uint256 _exchangeRate)
        external onlyOfferOwner(_sidechainId, _executionContract){
        // TODO
    }

    function deregister(uint256 _sidechainId, address payable _executionContract, uint256 _exchangeRate)
        external onlyOfferOwner(_sidechainId, _executionContract) {
        // TODO

    }

    function compactOffers(uint256 _sidechainId) {
        // TODO

    }

    function getOfferAddressesSize(uint256 _sidechainId) external view returns(uint256) {
        return exchangeOffers[_sidechainId].offerAddresses.length;
    }

    function getOfferExchangeRate(uint256 _sidechainId, uint256 offset) external view returns(uint256) {
        address addr = exchangeOffers[_sidechainId].offerAddresses[offset];
        if (addr == 0) {
            return 0;
        }
        return exchangeOffers[_sidechainId].offers[addr].exchangeRate;
    }
    function getOfferOwner(uint256 _sidechainId, uint256 offset) external view returns(address) {
        address addr = exchangeOffers[_sidechainId].offerAddresses[offset];
        if (addr == 0) {
            return 0;
        }
        return exchangeOffers[_sidechainId].offers[addr].owner;
    }

    function getTenOfferExchangeRates(uint256 _sidechainId, uint256 offset) external view returns(uint256) {

        /* memory */ uint256[] result = new uint256[TEN];
        for (int i = 0; i < TEN; i++) {
            result[i] = getOfferExchangeRate(_sidechainId, offset+i);
        }
        return result;
    }



}
