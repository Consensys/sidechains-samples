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
import "../hotel/HotelRouterInterface.sol";
import "../train/TrainRouterInterface.sol";
import "../../../../common-solidity/crosschain-precompile-calls/contracts/Crosschain.sol";



contract TravelAgency is Crosschain {
    uint256 public hotelBlockchainId;
    HotelRouterInterface public hotelContract;

    uint256 public trainBlockchainId;
    TrainRouterInterface public trainContract;

    uint256[] public confirmedBookingIds;
    uint256[] public confirmedBookingDates;




    constructor(uint256 _hotelBlockchainId, address _hotelContract, uint256 _trainBlockchainId, address _trainContract) public {
        hotelBlockchainId = _hotelBlockchainId;
        hotelContract = HotelRouterInterface(_hotelContract);
        trainBlockchainId = _trainBlockchainId;
        trainContract = TrainRouterInterface(_trainContract);
    }

    function bookHotelAndTrain(uint256 _date, uint256 _uniqueId) public {
        crosschainTransaction(hotelBlockchainId, address(hotelContract), abi.encodeWithSelector(hotelContract.bookRoom.selector, _date, _uniqueId, 100) );
        crosschainTransaction(trainBlockchainId, address(trainContract), abi.encodeWithSelector(trainContract.bookSeat.selector, _date, _uniqueId, 100) );

        confirmedBookingIds.push(_uniqueId);
        confirmedBookingDates.push(_date);
    }

    function findBookingIds(uint256 _date, uint256 _offset) public view returns (uint256 offset, uint256 bookingId) {
        for (uint i = _offset; i < confirmedBookingDates.length; i++) {
            if (confirmedBookingDates[i] == _date) {
                return (i, confirmedBookingIds[i]);
            }
        }
        return (0, 0);
    }

    function bookingConfirmed(uint256 _bookingId) public view returns (bool) {
        // TODO use a maps as well as array
        for (uint i = 0; i < confirmedBookingDates.length; i++) {
            if (confirmedBookingIds[i] == _bookingId) {
                return true;
            }
        }
        return false;
    }
}