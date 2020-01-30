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
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.HotelRoom;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.HotelRouter;
import tech.pegasys.samples.sidechains.common.utils.BasePropertiesFile;
import tech.pegasys.samples.sidechains.common.utils.KeyPairGen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class EntityHotel {
    private static final Logger LOG = LogManager.getLogger(EntityHotel.class);


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
    private HotelRouter hotel;
    private String routerContractAddress;

    // A gas provider which indicates no gas is charged for transactions.
    private ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);


    public EntityHotel(final Besu web3j, final BigInteger bcId, final int retry, final int pollingInterval,
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
        LOG.info("Deploy and set-up hotel contracts to blockchain {}", this.bcId);
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

        LOG.info(" Deploy hotel router contract");
        RemoteCall<HotelRouter> remoteCall2 =
            HotelRouter.deploy(this.web3j, this.tm, this.freeGasProvider, EVENT_HORIZON, this.erc20.getContractAddress());
        this.hotel = remoteCall2.send();
        this.routerContractAddress = this.hotel.getContractAddress();

        LOG.info(" Deploy hotel room lockable contracts");
        addresses = new ArrayList<>();
        for (int i=0; i < NUM_SEATS; i++) {
            RemoteCall<HotelRoom> remoteCall3 =
                HotelRoom.deployLockable(this.web3j, this.xtm, this.freeGasProvider, this.hotel.getContractAddress(), STANDARD_RATE);
            HotelRoom room = remoteCall3.send();
            addresses.add(room.getContractAddress());
        }
        LOG.info(" Linking hotel room lockable contracts to hotel contract");
        receipt = this.hotel.addRooms(addresses).send();

        storeContractAddresses();
    }

    public String getRouterContractAddress() {
        return this.routerContractAddress;
    }

//    private Credentials credentials;
//    private CrosschainTransactionManager tmSc1;
//    private CrosschainTransactionManager tmSc2;
//    private Besu web3jSc1;
//    private Besu web3jSc2;
//    private BigInteger sc1Id;
//    private BigInteger sc2Id;
//
//    BigInteger exchangeRate;
//
//    // A gas provider which indicates no gas is charged for transactions.
//    private ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);
//
//
//    // Smart contract addresses and objected.of contracts.
//    private String senderContractAddress = null;
//    private String receiverContractAddress = null;
//    private AtomicSwapSender senderContract;
//    private AtomicSwapReceiver receiverContract;
//
//    public EntityHotel(final Besu web3jSc1, final BigInteger sc1Id, final Besu web3jSc2, final BigInteger sc2Id,
//                                final int retry, final int pollingInterval,
//                                final Besu web3jCoordinationBlockchain,
//                                final BigInteger coordinationBlockchainId,
//                                final String coordinationContractAddress,
//                                final long crosschainTransactionTimeout) {
//        loadStoreProperties();
//        this.web3jSc1 = web3jSc1;
//        this.web3jSc2 = web3jSc2;
//        this.tmSc1 = new CrosschainTransactionManager(this.web3jSc1, this.credentials, sc1Id, retry, pollingInterval,
//            web3jCoordinationBlockchain, coordinationBlockchainId, coordinationContractAddress, crosschainTransactionTimeout);
//        this.tmSc2 = new CrosschainTransactionManager(this.web3jSc2, this.credentials, sc2Id, retry, pollingInterval,
//            web3jCoordinationBlockchain, coordinationBlockchainId, coordinationContractAddress, crosschainTransactionTimeout);
//        this.sc1Id = sc1Id;
//        this.sc2Id = sc2Id;
//    }
//
//
//
//    public void showOffers(String registrationContractAddress) throws Exception {
//        LOG.info("Showing Offers registered with Registration Contract");
//        AtomicSwapRegistration registrationContract = AtomicSwapRegistration.load(registrationContractAddress, this.web3jSc1, this.tmSc1, this.freeGasProvider);
//        BigInteger size = registrationContract.getOfferAddressesSize(this.sc2Id).send();
//        LOG.info("Total number of sender contracts registered for sending Ether to sidechain {} is {}", this.sc2Id, size);
//
//        int sizeInt = size.intValue();
//        if (sizeInt == 0) {
//            LOG.info("No send contracts registered for sending Ether to");
//            return;
//        }
//
//        BigInteger exchangeRateOffered = null;
//        String sendContractAddress = null;
//        for (int i = 0; i < sizeInt; i++) {
//            exchangeRateOffered = registrationContract.getOfferExchangeRate(this.sc2Id, BigInteger.valueOf(i)).send();
//            sendContractAddress = registrationContract.getOfferSenderContract(this.sc2Id, BigInteger.valueOf(i)).send();
//            LOG.info(" Offer Number {} Exchange Rate {} offered at Send Contract {}", i, exchangeRateOffered.toString(16), sendContractAddress);
//        }
//    }
//
//
//    public boolean prepareForExchange(String registrationContractAddress, int offerNumber) throws Exception {
//        LOG.info("Set-up and load contracts");
//        AtomicSwapRegistration registrationContract = AtomicSwapRegistration.load(registrationContractAddress, this.web3jSc1, this.tmSc1, this.freeGasProvider);
//        BigInteger size = registrationContract.getOfferAddressesSize(this.sc2Id).send();
//        int sizeInt = size.intValue();
//        if (offerNumber == -1) {
//            if (sizeInt == 0) {
//                LOG.error("No offers available");
//                return true;
//            }
//            LOG.info("Using latest offer");
//            offerNumber = sizeInt-1;
//        }
//        else {
//            if (sizeInt <= offerNumber) {
//                LOG.error("No offer at {} offset available", offerNumber);
//                return true;
//            }
//        }
//
//        BigInteger exchangeRateOffered = registrationContract.getOfferExchangeRate(this.sc2Id, BigInteger.valueOf(offerNumber)).send();
//        String sendContractAddress = registrationContract.getOfferSenderContract(this.sc2Id, BigInteger.valueOf(offerNumber)).send();
//        LOG.info(" Exchange Rate {} offered at Send Contract {}", exchangeRateOffered.toString(16), sendContractAddress);
//
//        this.senderContract = AtomicSwapSender.load(sendContractAddress, this.web3jSc1, this.tmSc1, this.freeGasProvider);
//        this.senderContractAddress = this.senderContract.getContractAddress();
//        this.receiverContractAddress = this.senderContract.receiverContract().send();
//        this.receiverContract = AtomicSwapReceiver.load(this.receiverContractAddress, this.web3jSc2, this.tmSc2, this.freeGasProvider);
//        this.exchangeRate = exchangeRateOffered;
//        return false;
//    }
//
//
//    public void swapEther(BigInteger amountInWei) throws Exception {
//        LOG.info("Running Core Part of Sample Code");
//
//        LOG.info("  Depositing {} Wei into sender contract", amountInWei);
//        this.senderContract.deposit(amountInWei).send();
//
//        CallSimulator sim = new CallSimulator(this.exchangeRate);
//        LOG.info("   Scaled exchange rate is: 0x{}", this.exchangeRate.toString(16));
//
//        LOG.info("  Executing call simulator to determine parameter values and expected results");
////        BigInteger receiverBalanceInWei = getBalance(this.web3jSc2, this.receiverContractAddress);
////        BigInteger senderBalanceInWei = getBalance(this.web3jSc1, this.senderContractAddress);
////        BigInteger accepterBalanceInWei = getBalance(this.web3jSc2, this.credentials.getAddress());
//        String owner = this.receiverContract.owner().send();
//        BigInteger receiverBalanceInWei = this.receiverContract.getBalance(owner).send();
//        BigInteger senderBalanceInWei = this.senderContract.getBalance(owner).send();
//        BigInteger accepterBalanceInWei = this.senderContract.getMyBalance().send();
//        sim.setValues(receiverBalanceInWei, accepterBalanceInWei, senderBalanceInWei);
//        sim.exchange(amountInWei);
//        if (sim.atomicSwapSenderError) {
//            LOG.info("***Simulator detected error while processing request: Attempt to send too much Ether");
//            return;
//        }
//        if (sim.atomicSwapReceiverError) {
//            LOG.info("***Simulator detected error while processing request: Transfer amount exceeded Receiver Contract balance");
//            return;
//        }
//
//        LOG.info("   Simulator says: Receive amount is: {} Wei", sim.atomicSwapReceiver_Exchange_amount);
//        LOG.info("   Simulator says: Receive contract balance will be: {} Wei", sim.receiverBalanceInWei);
//        LOG.info("   Simulator says: Send contract balance will be: {} Wei", sim.senderBalanceInWei);
//        LOG.info("   Simulator says: Accept account balance will be: {} Wei", sim.accepterBalanceInWei);
//
//        LOG.info("  Constructing Nested Crosschain Transaction");
//        CrosschainContextGenerator contextGenerator = new CrosschainContextGenerator(this.sc1Id);
//        CrosschainContext subordinateTransactionContext = contextGenerator.createCrosschainContext(this.sc1Id, this.senderContractAddress);
//        byte[] subordinateTrans = this.receiverContract.exchange_AsSignedCrosschainSubordinateTransaction(sim.atomicSwapReceiver_Exchange_amount, subordinateTransactionContext);
//
//        // Call to contract 1
//        byte[][] subordinateTransactionsAndViews = new byte[][]{subordinateTrans};
//        CrosschainContext originatingTransactionContext = contextGenerator.createCrosschainContext(subordinateTransactionsAndViews);
//
//        LOG.info("  Executing Crosschain Transaction");
//        TransactionReceipt transactionReceipt = this.senderContract.exchange_AsCrosschainOriginatingTransaction(amountInWei, originatingTransactionContext).send();
//        LOG.info("   Transaction Receipt: {}", transactionReceipt.toString());
//        if (!transactionReceipt.isStatusOK()) {
//            throw new Error(transactionReceipt.getStatus());
//        }
//
//
//        boolean stillLocked;
//        final int tooLong = 10;
//        int longTimeCount = 0;
//        StringBuffer graphicalCount = new StringBuffer();
//        do {
//            longTimeCount++;
//            if (longTimeCount > tooLong) {
//                LOG.error("Sender contract {} did not unlock", this.senderContractAddress);
//            }
//            Thread.sleep(500);
//            CrossIsLockedResponse isLockedObj = this.web3jSc1.crossIsLocked(this.senderContractAddress, DefaultBlockParameter.valueOf("latest")).send();
//            stillLocked = isLockedObj.isLocked();
//            if (stillLocked) {
//                graphicalCount.append(".");
//                LOG.info("   Waiting for the sender contract to unlock{}", graphicalCount.toString());
//            }
//        } while (stillLocked);
//    }
//
//
//    public String accountAddress() {
//        return this.credentials.getAddress();
//    }
//
//    public String myAccountBalanceSenderContract() throws Exception {
//        if (this.senderContract == null) {
//            return "";
//        }
//        return this.senderContract.getMyBalance().send().toString();
//    }
//    public String myAccountBalanceReceiverContract() throws Exception {
//        if (this.receiverContract == null) {
//            return "";
//        }
//        return this.receiverContract.getMyBalance().send().toString();
//    }
//

    private void loadStoreProperties() {
        EntityHotel.EntityHotelProperties props = new EntityHotel.EntityHotelProperties();
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
        EntityHotel.EntityHotelProperties props = new EntityHotel.EntityHotelProperties();
        props.load();
        props.routerContractAddress = this.routerContractAddress;
        props.store();
    }



    static class EntityHotelProperties extends BasePropertiesFile {
        private static final String PROP_PRIV_KEY = "privateKey";
        private static final String PROP_CONTRACT_ADDRESS = "RouterContractAddress";
        String privateKey;
        String routerContractAddress;

        EntityHotelProperties() {
            super("hotel");
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
