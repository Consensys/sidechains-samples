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
import org.web3j.protocol.besu.response.crosschain.CrossIsLockedResponse;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.CrosschainContext;
import org.web3j.tx.CrosschainContextGenerator;
import org.web3j.tx.CrosschainTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.cc.ERC20LockableAccount;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.ERC20Router;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.cc.HotelRouter;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.cc.TrainRouter;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.cc.TravelAgency;
import tech.pegasys.samples.sidechains.common.coordination.CrosschainCoordinationContractSetup;
import tech.pegasys.samples.sidechains.common.utils.BasePropertiesFile;
import tech.pegasys.samples.sidechains.common.utils.KeyPairGen;
import tech.pegasys.samples.sidechains.common.utils.PRNGSecureRandom;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;


/**
 * TODO
 */
public class EntityTravelAgency {
    private static final Logger LOG = LogManager.getLogger(EntityTravelAgency.class);

    private Credentials credentials;

    private CrosschainTransactionManager tmTravelAgency;
    private Besu web3jTravelAgency;
    private String agencyContractAddress;
    private BigInteger agencyBcId;

    private PRNGSecureRandom rand;

    private CrosschainTransactionManager tmTrain;
    private CrosschainTransactionManager tmHotel;

    TravelAgency agencyContract;
    HotelRouter hotelRouter;
    TrainRouter trainRouter;

    ERC20Router trainErc20;
    ERC20Router hotelErc20;

    CrosschainCoordinationContractSetup coord;

    // A gas provider which indicates no gas is charged for transactions.
    private ContractGasProvider freeGasProvider = new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);

    public EntityTravelAgency(
        final Besu web3j, BigInteger bcId, int retry, int pollingInterval,
        final Besu hotelWeb3j, final BigInteger hotelBcId,
        final Besu trainWeb3j, final BigInteger trainBcId,
        final Besu web3jCoordinationBlockchain,
        final BigInteger coordinationBlockchainId,
        final String coordinationContractAddress,
        final long crosschainTransactionTimeout) throws Exception {

        loadStoreProperties();
        this.web3jTravelAgency = web3j;
        this.tmTravelAgency = new CrosschainTransactionManager(web3j, this.credentials, bcId, retry, pollingInterval,
            web3jCoordinationBlockchain, coordinationBlockchainId, coordinationContractAddress, crosschainTransactionTimeout);
        this.agencyBcId = bcId;

        this.tmHotel = new CrosschainTransactionManager(hotelWeb3j, this.credentials, hotelBcId, retry, pollingInterval,
            web3jCoordinationBlockchain, coordinationBlockchainId, coordinationContractAddress, crosschainTransactionTimeout);
        this.tmTrain = new CrosschainTransactionManager(trainWeb3j, this.credentials, trainBcId, retry, pollingInterval,
            web3jCoordinationBlockchain, coordinationBlockchainId, coordinationContractAddress, crosschainTransactionTimeout);

        this.rand = new PRNGSecureRandom();

        this.coord = new CrosschainCoordinationContractSetup(
            web3jCoordinationBlockchain, coordinationContractAddress, coordinationBlockchainId);
    }

    public void deploy(final Besu trainWeb3j, final BigInteger trainBcId, final String trainContractAddress, String trainErc20Address,
                       final Besu hotelWeb3j, final BigInteger hotelBcId, final String hotelContractAddress, String hotelErc20Address) throws Exception {
        LOG.info(" Deploying travel agency contract");
        RemoteCall<TravelAgency> remoteCall =
            TravelAgency.deployLockable(this.web3jTravelAgency, this.tmTravelAgency, this.freeGasProvider,
                hotelBcId, hotelContractAddress, trainBcId, trainContractAddress);
        this.agencyContract = remoteCall.send();
        this.agencyContractAddress = agencyContract.getContractAddress();
        LOG.info("  Travel Agency Contract deployed on blockchain {}, at address: {}",
            this.agencyBcId, this.agencyContractAddress);

        this.trainRouter = TrainRouter.load(trainContractAddress, trainWeb3j, this.tmTrain, this.freeGasProvider);
        this.hotelRouter = HotelRouter.load(hotelContractAddress, hotelWeb3j, this.tmHotel, this.freeGasProvider);

        LOG.info(" Creating train lockable ERC account storage");
        this.trainErc20 = ERC20Router.load(trainErc20Address, trainWeb3j, this.tmTrain, this.freeGasProvider);
        ERC20Helper trainErc20Helper = new ERC20Helper(this.trainErc20);
        trainErc20Helper.createAccount(trainWeb3j, this.tmTrain, this.freeGasProvider, 1);
        LOG.info(" Checking train lockable ERC account storage");
        trainErc20Helper.dumpRouterInformation();
        trainErc20Helper.dumpAccountInformation(this.credentials.getAddress());

        LOG.info(" Creating hotel lockable ERC account storage");
        this.hotelErc20 = ERC20Router.load(hotelErc20Address, hotelWeb3j, this.tmHotel, this.freeGasProvider);
        ERC20Helper hotelErc20Helper = new ERC20Helper(this.hotelErc20);
        hotelErc20Helper.createAccount(hotelWeb3j, this.tmHotel, this.freeGasProvider, 1);
        LOG.info(" Checking hotel lockable ERC account storage");
        hotelErc20Helper.dumpRouterInformation();
        hotelErc20Helper.dumpAccountInformation(this.credentials.getAddress());

        storeContractAddress();
    }

    public void load() throws Exception {
        LOG.info(" Loading travel agency contract");
        this.agencyContract =
            TravelAgency.load(this.agencyContractAddress, this.web3jTravelAgency, this.tmTravelAgency, this.freeGasProvider);
        LOG.info("  Travel Agency Contract loaded on blockchain {}, at address: {}",
            this.agencyBcId, this.agencyContractAddress);
    }


    public BigInteger book(final int date) throws Exception {
        BigInteger dateBigInt = BigInteger.valueOf(date);
        byte[] randomBytes = new byte[32];
        rand.nextBytes(randomBytes);
        BigInteger uniqueBookingId = new BigInteger(1, randomBytes);

        CrosschainContextGenerator contextGenerator = new CrosschainContextGenerator(this.agencyBcId);
        // The same subordinate context applies to both calls as both calls have the same from blockchain and contract.
        CrosschainContext subordinateTransactionContext = contextGenerator.createCrosschainContext(this.agencyBcId, this.agencyContractAddress);
        byte[] subordinateTransHotel = this.hotelRouter.bookRoom_AsSignedCrosschainSubordinateTransaction(dateBigInt, uniqueBookingId, BigInteger.valueOf(100), subordinateTransactionContext);
        byte[] subordinateTransTrain = this.trainRouter.bookSeat_AsSignedCrosschainSubordinateTransaction(dateBigInt, uniqueBookingId, BigInteger.valueOf(100), subordinateTransactionContext);

        byte[][] subordinateTransactionsAndViews = new byte[][]{subordinateTransHotel, subordinateTransTrain};
        CrosschainContext originatingTransactionContext = contextGenerator.createCrosschainContext(subordinateTransactionsAndViews);

        LOG.info("  Executing Crosschain Transaction, using booking ID {}", uniqueBookingId);

        try {
            TransactionReceipt transactionReceipt = this.agencyContract.bookHotelAndTrain_AsCrosschainOriginatingTransaction(dateBigInt, uniqueBookingId, originatingTransactionContext).send();
            LOG.info("   Transaction Receipt: {}", transactionReceipt.toString());
            if (!transactionReceipt.isStatusOK()) {
                throw new Error(transactionReceipt.getStatus());
            }
        } catch (Throwable th) {
            LOG.info("Error reported during Crosschain Transaction: {}", th.toString());
        }



        boolean committed = this.coord.waitForCrosschainTransactionComplete(
            this.credentials, this.agencyBcId, originatingTransactionContext.getCrosschainTransactionId());
        // The contract will unlock up to a block or two after the crosschain transaction has been committed.
        waitForUnlock(this.web3jTravelAgency, this.agencyContractAddress);
        boolean bookingConfirmed = this.agencyContract.bookingConfirmed(uniqueBookingId).send();
        LOG.info(" Booking number {} confirmation status: {}", uniqueBookingId, bookingConfirmed);

        return uniqueBookingId;
    }

    public String getTravelAgencyAccount() {
        return this.credentials.getAddress();
    }

    public void showBookingInformation(int date) throws Exception {
        BigInteger bDate = BigInteger.valueOf(date);
        boolean done = false;
        BigInteger offset = BigInteger.ZERO;
        boolean noBookingsFound = true;
        while (!done) {
            Tuple2<BigInteger, BigInteger> retVal = this.agencyContract.findBookingIds(bDate, offset).send();
            offset = retVal.component1().add(BigInteger.ONE);
            BigInteger bookingId = retVal.component2();

            if (bookingId.equals(BigInteger.ZERO)) {
                done = true;
            }
            else {
                noBookingsFound = false;

                retVal = this.hotelRouter.getRoomInformation(bDate, bookingId).send();
                BigInteger amountPaid = retVal.component1();
                BigInteger roomId = retVal.component2();
                LOG.info(" Booked room: {} for amount: {} with booking ID: {}", roomId, amountPaid, bookingId);

                retVal = this.trainRouter.getSeatInformation(bDate, bookingId).send();
                amountPaid = retVal.component1();
                BigInteger seatId = retVal.component2();
                LOG.info(" Booked seat: {} for amount: {} with booking ID: {}", seatId, amountPaid, bookingId);
            }
        }

        if (noBookingsFound) {
            LOG.info(" No bookings found for date: {}", date);
        }

        BigInteger numberOfRoomsAvailable = this.hotelRouter.getNumberRoomsAvailable(bDate).send();
        LOG.info(" {} rooms are available on date {}", numberOfRoomsAvailable, date);

        BigInteger numberOfSeatsAvailable = this.trainRouter.getNumberSeatsAvailable(bDate).send();
        LOG.info(" {} seats available on date {}", numberOfSeatsAvailable, date);
    }

    public void waitForUnlock(Besu web3j, String address) throws Exception {
        boolean stillLocked;
        final int tooLong = 10;
        int longTimeCount = 0;
        StringBuffer graphicalCount = new StringBuffer();
        LOG.info("Wating for contract {} to unlock", address);
        do {
            longTimeCount++;
            if (longTimeCount > tooLong) {
                LOG.error("Contract {} did not unlock", address);
            }
            Thread.sleep(500);
            CrossIsLockedResponse isLockedObj = web3j.crossIsLocked(address, DefaultBlockParameter.valueOf("latest")).send();
            stillLocked = isLockedObj.isLocked();
            if (stillLocked) {
                graphicalCount.append(".");
                LOG.info("   Waiting for the contract to unlock{}", graphicalCount.toString());
            }
        } while (stillLocked);
    }


    // TODO need to persist hotel and train router contract addresses.
    private void loadStoreProperties() {
        AgencyProperties props = new AgencyProperties();
        if (props.propertiesFileExists()) {
            props.load();
            this.agencyContractAddress = props.agencyContractAddress;
        }
        else {
            // Generate a key and store it in the format required for Credentials.
            props.privateKey = new KeyPairGen().generateKeyPairGetPrivateKey();
            System.out.println("Priv2: " + props.privateKey);
            props.store();
        }
        this.credentials = Credentials.create(props.privateKey);
    }

    private void storeContractAddress() {
        AgencyProperties props = new AgencyProperties();
        props.load();
        props.agencyContractAddress = this.agencyContractAddress;
        props.store();
    }



    static class AgencyProperties extends BasePropertiesFile {
        private static final String PROP_PRIV_KEY = "privateKey";
        private static final String PROP_AGENCY_CONTRACT_ADDRESS = "AgencyContractAddress";
        String privateKey;
        String agencyContractAddress;

        AgencyProperties() {
            super("ragency");
        }

        void load() {
            loadProperties();
            this.privateKey = this.properties.getProperty(PROP_PRIV_KEY);
            this.agencyContractAddress = this.properties.getProperty(PROP_AGENCY_CONTRACT_ADDRESS);
        }

        void store() {
            this.properties.setProperty(PROP_PRIV_KEY, this.privateKey);
            if (agencyContractAddress != null) {
                this.properties.setProperty(PROP_AGENCY_CONTRACT_ADDRESS, this.agencyContractAddress);
            }
            storeProperties();
        }
    }
}
