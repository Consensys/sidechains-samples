/*
 * Copyright 2020 ConsenSys AG.
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

interface TrainRouterInterface {

    function bookSeat(uint256 _date, uint256 _uniqueId, uint256 _maxAmountToPay) external;

    function getSeatInformation(uint256 _date, uint256 _uniqueId) external view returns (uint256 amountPaid, uint256 seatId);

    function getSeatRates() external view returns (uint256 lowestRate, uint256 highestRate);

    function getNumberSeatsAvailable(uint256 _date) external view returns (uint256 numRoomsAvailable);
}