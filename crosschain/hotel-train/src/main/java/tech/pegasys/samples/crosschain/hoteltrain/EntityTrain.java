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
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.ERC20LockableAccount;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.ERC20Router;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.TrainRouter;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.TrainSeat;
import tech.pegasys.samples.sidechains.common.utils.BasePropertiesFile;
import tech.pegasys.samples.sidechains.common.utils.KeyPairGen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class EntityTrain {
    private static final Logger LOG = LogManager.getLogger(EntityTrain.class);

    // How far in the future can people book?
    public static final BigInteger EVENT_HORIZON = BigInteger.valueOf(365);

    // Total number of tokens issued for booking.
    public static final BigInteger TOKEN_SUPPLY = BigInteger.valueOf(100);

    public static final BigInteger STANDARD_RATE = BigInteger.valueOf(5);
    public static final int NUM_SEATS = 4;

    public static final int NUM_RECEIVING_ACCOUNTS = 3;

    private Credentials credentials;
    private TransactionManager tm;
    private CrosschainTransactionManager xtm;
    private Besu web3j;
    private BigInteger bcId;

    private ERC20Router erc20;
    private TrainRouter train;
    private String routerContractAddress;

    // A gas provider which indicates no gas is charged for transactions.
    private ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);


    public EntityTrain(final Besu web3j, final BigInteger bcId, final int retry, final int pollingInterval,
                       final Besu web3jCoordinationBlockchain,
                       final BigInteger coordinationBlockchainId,
                       final String coordinationContractAddress,
                       final long crosschainTransactionTimeout) {
        loadStoreProperties();
        this.web3j = web3j;
        this.tm = new RawTransactionManager(this.web3j, this.credentials, bcId.longValue(), retry, pollingInterval);
        this.xtm = new CrosschainTransactionManager(this.web3j, this.credentials, bcId, retry, pollingInterval,
            web3jCoordinationBlockchain, coordinationBlockchainId, coordinationContractAddress, crosschainTransactionTimeout);
        this.bcId = bcId;
    }

    public void deploy() throws Exception {
        LOG.info("Deploy and set-up train contracts to blockchain {}", this.bcId);
        LOG.info(" Deploy ERC20 router contract");
        RemoteCall<ERC20Router> remoteCall = ERC20Router.deploy(this.web3j, this.tm, this.freeGasProvider);
        this.erc20 = remoteCall.send();

        LOG.info(" Deploy ERC20 lockable account contracts for owner");
        List<String> addresses = new ArrayList<>();
        for (int i=0; i<NUM_RECEIVING_ACCOUNTS; i++) {
            RemoteCall<ERC20LockableAccount> remoteCall1 =
                ERC20LockableAccount.deployLockable(this.web3j, this.xtm, this.freeGasProvider, this.erc20.getContractAddress());
            ERC20LockableAccount acc = remoteCall1.send();
            addresses.add(acc.getContractAddress());
        }
        LOG.info(" Linking ERC20 lockable account contracts to main ERC20 contract");
        TransactionReceipt receipt = this.erc20.createAccount(addresses).send();

        LOG.info(" Setting total supply as {} tokens", TOKEN_SUPPLY);
        receipt = this.erc20.mint(TOKEN_SUPPLY).send();

        LOG.info(" Deploy train router contract");
        RemoteCall<TrainRouter> remoteCall2 =
            TrainRouter.deploy(this.web3j, this.tm, this.freeGasProvider, EVENT_HORIZON, this.erc20.getContractAddress());
        this.train = remoteCall2.send();
        this.routerContractAddress = this.train.getContractAddress();

        LOG.info(" Deploy train seat lockable contracts");
        addresses = new ArrayList<>();
        for (int i=0; i < NUM_SEATS; i++) {
            RemoteCall<TrainSeat> remoteCall3 =
                TrainSeat.deployLockable(this.web3j, this.xtm, this.freeGasProvider, this.train.getContractAddress(), STANDARD_RATE);
            TrainSeat seat = remoteCall3.send();
            addresses.add(seat.getContractAddress());
        }
        LOG.info(" Linking train seat lockable contracts to train contract");
        receipt = this.train.addSeats(addresses).send();

        storeContractAddresses();
    }

    public String getRouterContractAddress() {
        return this.routerContractAddress;
    }

    //
//    public void withdrawEverythingSc1() throws Exception {
//        LOG.info("Withdrawing funds from sender contract on Sidechain 1");
//        if (this.senderContract == null) {
//            loadContracts();
//        }
//        BigInteger amountInWei = this.senderContract.getMyBalance().send();
//        this.senderContract.withdraw(amountInWei).send();
//        LOG.info(" Withdrawl completed");
//    }
//
//    public void withdrawSc2(BigInteger amountInWei) throws Exception {
//        LOG.info("Withdrawing {} wei from receiver contract on Sidechain 2", amountInWei);
//        if (this.receiverContract == null) {
//            loadContracts();
//        }
//        this.receiverContract.withdraw(amountInWei).send();
//        LOG.info(" Withdrawl completed");
//    }
//
//    public void depositSc2(BigInteger amountInWei) throws Exception {
//        LOG.info("Deposit {} wei into receiver account", amountInWei);
//        if (this.receiverContract == null) {
//            loadContracts();
//        }
//        this.receiverContract.deposit(amountInWei).send();
//        LOG.info(" Deposit completed");
//    }
//
//    public String accountAddress() {
//        return this.credentials.getAddress();
//    }
//
//    public String myAccountBalanceSenderContract() throws Exception {
//        if (this.senderContract == null) {
//            loadContracts();
//        }
//        return this.senderContract.getMyBalance().send().toString();
//    }
//    public String myAccountBalanceReceiverContract() throws Exception {
//        if (this.receiverContract == null) {
//            loadContracts();
//        }
//        return this.receiverContract.getMyBalance().send().toString();
//    }
//
    private void loadStoreProperties() {
        EntityTrainProperties props = new EntityTrainProperties();
        if (props.propertiesFileExists()) {
            props.load();
            this.routerContractAddress = props.routerContractAddress;
        }
        else {
            // Generate a key and store it in the format required for Credentials.
            props.privateKey = new KeyPairGen().generateKeyPairGetPrivateKey();
            props.store();
        }
        this.credentials = Credentials.create(props.privateKey);
    }

    private void storeContractAddresses() {
        EntityTrainProperties props = new EntityTrainProperties();
        props.load();
        props.routerContractAddress = this.routerContractAddress;
        props.store();
    }



    static class EntityTrainProperties extends BasePropertiesFile {
        private static final String PROP_PRIV_KEY = "privateKey";
        private static final String PROP_CONTRACT_ADDRESS = "RouterContractAddress";
        String privateKey;
        String routerContractAddress;

        EntityTrainProperties() {
            super("train");
        }

        void load() {
            loadProperties();
            this.privateKey = this.properties.getProperty(PROP_PRIV_KEY);
            this.routerContractAddress = this.properties.getProperty(PROP_CONTRACT_ADDRESS);
        }

        void store() {
            this.properties.setProperty(PROP_PRIV_KEY, this.privateKey);
            if (this.routerContractAddress != null) {
                this.properties.setProperty(PROP_CONTRACT_ADDRESS, this.routerContractAddress);
            }
            storeProperties();
        }
    }
}
