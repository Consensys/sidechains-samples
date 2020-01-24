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

import "./HotelRoom.sol";
import "./HotelRouterInterface.sol";

contract HotelRouter is HotelRouterInterface {
    HotelRoom[] private rooms;

    address owner;
    uint256 today;
    uint256 eventHorizon;

    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }

    constructor(uint256 _eventHorizon) public {
        owner = msg.sender;
        eventHorizon = _eventHorizon;
    }

    function changeDate(uint256 _date) external {
        today = _date;
    }

    function addRooms(address[] calldata _roomsToAdd) external {
        // TODO: there is no protection against the same hotel room being added twice!
        // TODO: this will affect functions trying to determine how many rooms are available.
        for (uint i=0; i < _roomsToAdd.length; i++) {
            rooms.push(HotelRoom(_roomsToAdd[i]));
        }
    }

    function bookRoom(uint256 _date, uint256 _uniqueId, uint256 _maxAmountToPay) external {
        require(_date >=today, "Booking date must be in the future");
        require(_date <= today+eventHorizon, "Booking date can not be beyond the event horizon");

        // TODO improve data structures so for loop not needed.
        for (uint i=0; i<rooms.length; i++) {
            // TODO if room locked  then skip

            if (rooms[i].isAvailableForLessThan(_date, _maxAmountToPay)) {
                rooms[i].bookRoom(_date, _uniqueId, _maxAmountToPay);
            }
        }
    }

    function getRoomInformation(uint256 /*_date*/, uint256 /*_uniqueId*/) external view returns (uint256 amountPaid, uint256 roomId) {
        // TODO find room
        amountPaid = 0;
        roomId = 0;
    }

    function getRoomRates() external view returns (uint256 lowestRate, uint256 highestRate) {
        lowestRate = 0;
        highestRate = 0;
    }

    function getNumberRoomsAvailable(uint256 /*_date*/) external view returns (uint256 numRoomsAvailable) {
        numRoomsAvailable = 0;
    }
}
