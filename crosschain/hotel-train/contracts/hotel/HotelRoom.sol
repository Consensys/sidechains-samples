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



contract HotelRoom {
    address public hotelRouterContract;
    uint256 public roomRate;
    mapping(uint256 => uint256) private bookings;
    mapping(uint256 => address) private bookedBy;

    // Only accept requests from the hotel router contract.
    modifier onlyHotelRouterContract() {
        require(msg.sender == hotelRouterContract);
        _;
    }


    constructor(address _hotelRouterContract, uint256 _roomRate) public {
        hotelRouterContract = _hotelRouterContract;
        roomRate = _roomRate;
    }

    function changeHotelRouterContract(address _hotelRouterContract) external onlyHotelRouterContract{
        hotelRouterContract = _hotelRouterContract;
    }

    function changeRoomRate(uint256 _roomRate) external onlyHotelRouterContract {
        roomRate = _roomRate;
    }

    function bookRoom(uint256 _date, uint256 _uniqueId) external onlyHotelRouterContract {
        // Check that the room isn't already booked on the specified date.
        require(isAvailable(_date));

        // Book the room.
        bookings[_date] = _uniqueId;
        bookedBy[_date] = tx.origin;
    }

    function isAvailable(uint256 _date) public view returns (bool) {
        return bookings[_date] == 0;
    }

    function getBookingInfo(uint256 _date) external view returns (uint256 uniqueId, address whoBooked) {
        uniqueId = bookings[_date];
        whoBooked = bookedBy[_date];
    }
}