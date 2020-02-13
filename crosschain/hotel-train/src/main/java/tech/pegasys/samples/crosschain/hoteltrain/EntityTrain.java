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
import org.web3j.crypto.Credentials;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.CrosschainTransactionManager;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.cc.ERC20LockableAccount;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.ERC20Router;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.TrainRouter;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.cc.TrainSeat;
import tech.pegasys.samples.sidechains.common.utils.BasePropertiesFile;
import tech.pegasys.samples.sidechains.common.utils.KeyPairGen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class EntityTrain extends EntityBase {
    private static final Logger LOG = LogManager.getLogger(EntityTrain.class);

    private static final String NAME = "train";

    // How far in the future can people book?
    public static final BigInteger EVENT_HORIZON = BigInteger.valueOf(365);

    public static final BigInteger STANDARD_RATE = BigInteger.valueOf(5);
    public static final int NUM_SEATS = 4;

    private TrainRouter train;


    public EntityTrain(final Besu web3j, final BigInteger bcId, final int retry, final int pollingInterval,
                       final Besu web3jCoordinationBlockchain,
                       final BigInteger coordinationBlockchainId,
                       final String coordinationContractAddress,
                       final long crosschainTransactionTimeout) {
        super(NAME, web3j, bcId, retry, pollingInterval, web3jCoordinationBlockchain,
            coordinationBlockchainId, coordinationContractAddress, crosschainTransactionTimeout);
    }

    public void deploy() throws Exception {
        LOG.info("Deploy and set-up train contracts to blockchain {}", this.bcId);
        deployErc20();

        LOG.info(" Deploy train router contract");
        RemoteCall<TrainRouter> remoteCall2 =
            TrainRouter.deploy(this.web3j, this.tm, this.freeGasProvider, EVENT_HORIZON, this.erc20.getContractAddress());
        this.train = remoteCall2.send();
        this.routerContractAddress = this.train.getContractAddress();
        LOG.info("  Deployed train router contract to address: {}", this.routerContractAddress);

        LOG.info(" Deploy train seat lockable contracts");
        List<String> addresses = new ArrayList<>();
        for (int i=0; i < NUM_SEATS; i++) {
            RemoteCall<TrainSeat> remoteCall3 =
                TrainSeat.deployLockable(this.web3j, this.xtm, this.freeGasProvider, this.train.getContractAddress(), STANDARD_RATE);
            TrainSeat seat = remoteCall3.send();
            addresses.add(seat.getContractAddress());
            LOG.info("  Deployed train seat contract [{}] to address {}", i, seat.getContractAddress());
        }
        LOG.info(" Linking train seat lockable contracts to train contract");
        TransactionReceipt receipt = this.train.addSeats(addresses).send();

        storeContractAddresses(NAME);
    }
}
