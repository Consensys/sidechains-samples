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

import "./AtomicSwapSender.sol";

/*
 *
 */
contract AtomicSwapRegistration {
    struct ExchangeOffer {
        uint256 exchangeRate;
    }
    struct ExchangeOffers {
        mapping(address=>ExchangeOffer) offers;
        address[] offerAddresses;
    }
    mapping(uint256=>ExchangeOffers) private exchangeOffers;



    function register(address payable _senderContract) external {
        AtomicSwapSender senderContract = AtomicSwapSender(_senderContract);
        uint256 receiverSidechainId = senderContract.receiverSidechainId();
        exchangeOffers[receiverSidechainId].offers[_senderContract].exchangeRate = senderContract.exchangeRate();
        exchangeOffers[receiverSidechainId].offerAddresses.push(_senderContract);
    }

    function getOfferAddressesSize(uint256 _sidechainId) external view returns(uint256) {
        return exchangeOffers[_sidechainId].offerAddresses.length;
    }

    function getOfferSenderContract(uint256 _sidechainId, uint256 offset) public view returns(address) {
        return exchangeOffers[_sidechainId].offerAddresses[offset];
    }

    function getOfferExchangeRate(uint256 _sidechainId, uint256 offset) external view returns(uint256) {
        address addr = getOfferSenderContract(_sidechainId, offset);
        return exchangeOffers[_sidechainId].offers[addr].exchangeRate;
    }
}
