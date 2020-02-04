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
package tech.pegasys.samples.crosschain.hoteltrain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.cc.HotelRoom;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.HotelRouter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class EntityHotel extends EntityBase {
    private static final Logger LOG = LogManager.getLogger(EntityHotel.class);

    private static final String NAME = "hotel";

    // How far in the future can people book?
    public static final BigInteger EVENT_HORIZON = BigInteger.valueOf(365);

    public static final BigInteger STANDARD_RATE = BigInteger.valueOf(5);
    public static final int NUM_SEATS = 4;

    private HotelRouter hotel;

    public EntityHotel(final Besu web3j, final BigInteger bcId, final int retry, final int pollingInterval,
                       final Besu web3jCoordinationBlockchain,
                       final BigInteger coordinationBlockchainId,
                       final String coordinationContractAddress,
                       final long crosschainTransactionTimeout) {
        super(NAME, web3j, bcId, retry, pollingInterval, web3jCoordinationBlockchain,
            coordinationBlockchainId, coordinationContractAddress, crosschainTransactionTimeout);
    }

    public void deploy() throws Exception {
        LOG.info("Deploy and set-up hotel contracts to blockchain {}", this.bcId);
        deployErc20();

        LOG.info(" Deploy hotel router contract");
        RemoteCall<HotelRouter> remoteCall2 =
            HotelRouter.deploy(this.web3j, this.tm, this.freeGasProvider, EVENT_HORIZON, this.erc20.getContractAddress());
        this.hotel = remoteCall2.send();
        this.routerContractAddress = this.hotel.getContractAddress();

        LOG.info(" Deploy hotel room lockable contracts");
        List<String> addresses = new ArrayList<>();
        for (int i=0; i < NUM_SEATS; i++) {
            RemoteCall<HotelRoom> remoteCall3 =
                HotelRoom.deployLockable(this.web3j, this.xtm, this.freeGasProvider, this.hotel.getContractAddress(), STANDARD_RATE);
            HotelRoom room = remoteCall3.send();
            addresses.add(room.getContractAddress());
        }
        LOG.info(" Linking hotel room lockable contracts to hotel contract");
        TransactionReceipt receipt = this.hotel.addRooms(addresses).send();

        storeContractAddresses(NAME);
    }
}
