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



contract TrainSeat {
    address public trainRouterContract;
    uint256 public seatRate;
    mapping(uint256 => uint256) private bookings;
    mapping(uint256 => address) private bookedBy;

    // Only accept requests from the hotel router contract.
    modifier onlyTrainRouterContract() {
        require(msg.sender == trainRouterContract);
        _;
    }


    constructor(address _hotelRouterContract, uint256 _roomRate) public {
        trainRouterContract = _hotelRouterContract;
        seatRate = _roomRate;
    }

    function changeTrainRouterContract(address _trainRouterContract) external onlyTrainRouterContract {
        trainRouterContract = _trainRouterContract;
    }

    function changeSeatRate(uint256 _seatRate) external onlyTrainRouterContract {
        seatRate = _seatRate;
    }

    function bookSeat(uint256 _date, uint256 _uniqueId) external onlyTrainRouterContract {
        // Check that the seat isn't already booked on the specified date.
        // TODO require(isAvailable(_date));

        // Book the seat.
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