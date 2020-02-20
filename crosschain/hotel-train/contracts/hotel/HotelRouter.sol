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
import "../erc20/ERC20Router.sol";
import "../../../../common-solidity/crosschain-precompile-calls/contracts/Crosschain.sol";

contract HotelRouter is HotelRouterInterface, Crosschain {
    HotelRoom[] private rooms;

    address owner;
    uint256 today;
    uint256 eventHorizon;

    ERC20Router erc20;

    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }

    constructor(uint256 _eventHorizon, address _erc20Router) public {
        owner = msg.sender;
        eventHorizon = _eventHorizon;
        erc20 = ERC20Router(_erc20Router);
    }

    function changeDate(uint256 _date) external {
        today = _date;
    }

    function addRooms(address[] calldata _roomsToAdd) onlyOwner external {
        // TODO: there is no protection against the same hotel room being added twice!
        // TODO: this will affect functions trying to determine how many rooms are available.
        for (uint i=0; i < _roomsToAdd.length; i++) {
            rooms.push(HotelRoom(_roomsToAdd[i]));
        }
    }

    // TODO improve data structures so for loop not needed.
    function bookRoom(uint256 _date, uint256 _uniqueId, uint256 _maxAmountToPay) external {
        require(_date >=today, "Booking date must be in the future");
        require(_date <= today+eventHorizon, "Booking date can not be beyond the event horizon");

        for (uint i=0; i<rooms.length; i++) {
            if (!crosschainIsLocked(address(rooms[i]))) {
                uint256 rate = rooms[i].roomRate();
                if (rate <= _maxAmountToPay && rooms[i].isAvailable(_date)) {
                    rooms[i].bookRoom(_date, _uniqueId);
                    erc20.transferFrom(tx.origin, owner, rate);
                    return;
                }
            }
        }
        require(false, "No rooms available");
    }

    function getRoomInformation(uint256 _date, uint256 _uniqueId) external view returns (uint256 amountPaid, uint256 roomId) {
        for (uint i=0; i<rooms.length; i++) {
            // TODO skip if isLocked.
            (uint256 uniqueId, address whoBooked) = rooms[i].getBookingInfo(_date);
            if (_uniqueId == uniqueId && whoBooked == tx.origin) {
                roomId = i;
                amountPaid = rooms[i].roomRate();
                return (amountPaid, roomId);
            }
        }
        return (0,0);
    }

    function getRoomRates() external view returns (uint256 lowestRate, uint256 highestRate) {
        lowestRate = 0;
        highestRate = 0;
    }

    function getNumberRoomsAvailable(uint256 _date) external view returns (uint256 numRoomsAvailable) {
        for (uint i=0; i<rooms.length; i++) {
            // TODO skip if isLocked.
            (uint256 uniqueId, ) = rooms[i].getBookingInfo(_date);
            if (uniqueId == 0) {
                numRoomsAvailable++;
            }
        }
    }
}
