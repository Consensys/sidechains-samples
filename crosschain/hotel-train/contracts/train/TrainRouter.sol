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

import "./TrainSeat.sol";
import "./TrainRouterInterface.sol";
import "../erc20/ERC20Router.sol";
import "../../../../common-solidity/crosschain-precompile-calls/contracts/Crosschain.sol";

contract TrainRouter is TrainRouterInterface, Crosschain {
    TrainSeat[] private seats;

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

    function addSeats(address[] calldata _seatsToAdd) onlyOwner external {
        // TODO: there is no protection against the same hotel room being added twice!
        // TODO: this will affect functions trying to determine how many rooms are available.
        for (uint i=0; i < _seatsToAdd.length; i++) {
            seats.push(TrainSeat(_seatsToAdd[i]));
        }
    }


    function bookSeat(uint256 _date, uint256 _uniqueId, uint256 _maxAmountToPay) external {
        require(_date >=today, "Booking date must be in the future");
        require(_date <= today+eventHorizon, "Booking date can not be beyond the event horizon");

        // TODO improve data structures so for loop not needed.
        for (uint i=0; i<seats.length; i++) {
//            if (!crosschainIsLocked(address(seats[i]))) {
                uint256 rate = seats[i].seatRate();
                if (rate <= _maxAmountToPay && seats[i].isAvailable(_date)) {
                    seats[i].bookSeat(_date, _uniqueId);
                    erc20.transferFrom(tx.origin, owner, rate);
                    break;
                }
//            }
        }
    }

    function getSeatInformation(uint256 _date, uint256 _uniqueId) external view returns (uint256 amountPaid, uint256 seatId) {
        for (uint i=0; i<seats.length; i++) {
            // TODO skip if isLocked.
            (uint256 uniqueId, address whoBooked) = seats[i].getBookingInfo(_date);
            if (_uniqueId == uniqueId && whoBooked == tx.origin) {
                seatId = i;
                amountPaid = seats[i].seatRate();
                return (amountPaid, seatId);
            }
        }
        return (0,0);
    }

    function getSeatRates() external view returns (uint256 lowestRate, uint256 highestRate) {
        lowestRate = 0;
        highestRate = 0;
    }

    function getNumberSeatsAvailable(uint256 _date) external view returns (uint256 numSeatsAvailable) {
        for (uint i=0; i<seats.length; i++) {
            // TODO skip if isLocked.
            (uint256 uniqueId, ) = seats[i].getBookingInfo(_date);
            if (uniqueId == 0) {
                numSeatsAvailable++;
            }
        }
    }
}
