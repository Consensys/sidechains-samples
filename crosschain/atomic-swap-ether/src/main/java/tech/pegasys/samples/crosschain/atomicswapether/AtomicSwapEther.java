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
package tech.pegasys.samples.crosschain.atomicswapether;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.CrosschainTransactionManager;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import tech.pegasys.samples.crosschain.atomicswapether.soliditywrappers.AtomicSwapReceiver;
import tech.pegasys.samples.crosschain.atomicswapether.soliditywrappers.AtomicSwapRegistration;
import tech.pegasys.samples.crosschain.atomicswapether.soliditywrappers.AtomicSwapSender;
import tech.pegasys.samples.crosschain.atomicswapether.utils.KeyPairGen;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

/**
 * Swap Ether on one blockchain for Ether on another blockchain.
 */
public class AtomicSwapEther {
    private static final Logger LOG = LogManager.getLogger(AtomicSwapEther.class);

    public static final String STARTING_POINT_ACCEPTING_ETHER_SC1 = "11";
    public static final String STARTING_POINT_OFFERING_ETHER_SC2 = "10";
    public static final double EXCHANGE_RATE = 2.0;


    // For this sample to work, three Hyperledger Besu Ethereum Clients which represent
    // the sidechains / blockchains need to be deployed at the addresses shown below,
    // with the blockchain IDs indicated.
    private static final BigInteger SC1_SIDECHAIN_ID = BigInteger.valueOf(11);
    private static final String SC1_URI = "http://127.0.0.1:8110/";
    private static final BigInteger SC2_SIDECHAIN_ID = BigInteger.valueOf(22);
    private static final String SC2_URI = "http://127.0.0.1:8220/";

    // Have the polling interval equal to the block time.
    private static final int POLLING_INTERVAL = 2000;
    // Retry reqests to Ethereum Clients up to five times.
    private static final int RETRY = 5;

    // Name of properties file which holds information for this sample code.
    private static final String SAMPLE_PROPERTIES_FILE_NAME = "sample.properties";
    // Properties in the file.
    private static final String ENTITY_OFFERING_ETHER_PRIVATE_KEY = "EntityOfferingEtherPrivateKey";
    private static final String ENTITY_ACCEPTING_OFFER_PRIVATE_KEY = "EntityAcceptingOfferPrivateKey";
    private static final String REGISTRATION_CONTRACT_ADDRESS = "RegistrationContractAddress";
    private static final String SENDER_CONTRACT_ADDRESS = "SenderContractAddress";
    private static final String RECEIVER_CONTRACT_ADDRESS = "ReceiverContractAddress";

    // Externally Owned Account key pairs and associated transaction managers.
    // Faucet is set-up in the genesis file to have lots of Ether.
    private Credentials faucetCredentials;
    private static final String FAUCET_PRIVATE_KEY = "6960b51fb7c56858d752c2faf781de045c6418d0b4b60d55ab853bcc194d3770";
    private static final String FAUCET_ADDRESS = "0x9c45dcb0be210bde43c3ffb84194ebf459f0acd1";
    private TransactionManager faucetTmSc1;
    private TransactionManager faucetTmSc2;

    private Credentials entityOfferingEtherCredentials;
    private CrosschainTransactionManager entityOfferingTmSc1;
    private CrosschainTransactionManager entityOfferingTmSc2;

    private Credentials entityAcceptingOfferCredentials;
    private CrosschainTransactionManager entityAcceptingOfferTmSc1;
    private CrosschainTransactionManager entityAcceptingOfferTmSc2;

    // Web services for each blockchain / sidechain.
    private Besu web3jSc1;
    private Besu web3jSc2;

    // A gas provider which indicates no gas is charged for transactions.
    private ContractGasProvider freeGasProvider;


    // Smart contract addresses and objected.of contracts.
    private String registrationContractAddress = null;
    private String senderContractAddress = null;
    private String receiverContractAddress = null;
    private AtomicSwapRegistration registrationContract;
    private AtomicSwapSender entityOfferingSenderContract;
    private AtomicSwapReceiver entityOfferingReceiverContract;
    private AtomicSwapSender entityAcceptingOfferSenderContract;
    private AtomicSwapReceiver entityAcceptingOfferReceiverContract;


    public static void main(final String args[]) throws Exception {
        LOG.info("Atomic Swap Ether - started");
        new AtomicSwapEther().run();
    }

    private void run() throws Exception {
        this.faucetCredentials = Credentials.create(FAUCET_PRIVATE_KEY);
        if (propertiesFileExists()) {
            // If the contracts have been deployed, then just load the contracts.
            loadProperties();
            setupBesuServiceTransactionManager();
            setupEther();
            loadContracts();
        }
        else {
            // If the contracts haven't been deployed, then create some demo credentials and deploy the contracts.
            this.entityOfferingEtherCredentials = Credentials.create(new KeyPairGen().generateKeyPairForWeb3J());
            this.entityAcceptingOfferCredentials = Credentials.create(new KeyPairGen().generateKeyPairForWeb3J());
            setupBesuServiceTransactionManager();
            setupEther();
            deployContracts();
            storeProperties();
        }
        loadContractsPart2();

        core();
    }

    private void setupBesuServiceTransactionManager() {
        LOG.info("Setting up Besu service and transaction managers");

        this.web3jSc1 = Besu.build(new HttpService(SC1_URI), POLLING_INTERVAL);
        this.web3jSc2 = Besu.build(new HttpService(SC2_URI), POLLING_INTERVAL);

        this.faucetTmSc1 = new RawTransactionManager(this.web3jSc1, this.faucetCredentials, SC1_SIDECHAIN_ID.longValue(), RETRY, POLLING_INTERVAL);
        this.faucetTmSc2 = new RawTransactionManager(this.web3jSc2, this.faucetCredentials, SC2_SIDECHAIN_ID.longValue(), RETRY, POLLING_INTERVAL);
        this.entityOfferingTmSc1 = new CrosschainTransactionManager(this.web3jSc1, this.entityOfferingEtherCredentials, SC1_SIDECHAIN_ID.longValue(), RETRY, POLLING_INTERVAL);
        this.entityOfferingTmSc2 = new CrosschainTransactionManager(this.web3jSc2, this.entityOfferingEtherCredentials, SC2_SIDECHAIN_ID.longValue(), RETRY, POLLING_INTERVAL);
        this.entityAcceptingOfferTmSc1 = new CrosschainTransactionManager(this.web3jSc1, this.entityAcceptingOfferCredentials, SC1_SIDECHAIN_ID.longValue(), RETRY, POLLING_INTERVAL);
        this.entityAcceptingOfferTmSc2 = new CrosschainTransactionManager(this.web3jSc2, this.entityAcceptingOfferCredentials, SC2_SIDECHAIN_ID.longValue(), RETRY, POLLING_INTERVAL);

        // Hyperledger Besu is configured as an IBFT2, free gas network. We need a free gas provider.
        this.freeGasProvider = new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);
    }

    private final static int LESS_THAN = -1;
    /*
     * Set-up the starting point for the demo.
     */
    private void setupEther() throws Exception {

        LOG.info("Set-up Ether");
        showBalances();

        TransactionReceipt transactionReceipt;
        // Transfer all Ether from Entity Accepting Ether on SC2 and from Entity Offering
        // Ether on SC1.
        BigInteger offeringSc1Wei = getBalance(this.web3jSc1, this.entityOfferingEtherCredentials.getAddress());
        if (offeringSc1Wei.compareTo(BigInteger.ZERO) != 0) {
            transactionReceipt = Transfer.sendFunds(this.web3jSc1, this.entityOfferingEtherCredentials,
                FAUCET_ADDRESS, new BigDecimal(offeringSc1Wei), Convert.Unit.WEI).send();
        }
        BigInteger acceptingSc2Wei = getBalance(this.web3jSc2, this.entityAcceptingOfferCredentials.getAddress());
        if (acceptingSc2Wei.compareTo(BigInteger.ZERO) == 0) {
            transactionReceipt = Transfer.sendFunds(this.web3jSc2, this.entityAcceptingOfferCredentials,
                FAUCET_ADDRESS, new BigDecimal(acceptingSc2Wei), Convert.Unit.WEI).send();
        }

        // Check the faucets have enough Ether.
        BigInteger faucetSc1Bal = getBalance(this.web3jSc1, FAUCET_ADDRESS);
        if (faucetSc1Bal.compareTo(Convert.toWei(STARTING_POINT_ACCEPTING_ETHER_SC1, Convert.Unit.ETHER).toBigInteger()) == LESS_THAN) {
            LOG.info("Faucet on sidechain 1 does not have enough Ether");
        }
        BigInteger faucetSc2Bal = getBalance(this.web3jSc2, FAUCET_ADDRESS);
        if (faucetSc2Bal.compareTo(Convert.toWei(STARTING_POINT_OFFERING_ETHER_SC2, Convert.Unit.ETHER).toBigInteger()) == LESS_THAN) {
            LOG.info("Faucet on sidechain 2 does not have enough Ether");
        }

        BigInteger targetStartingPoint = Convert.toWei(STARTING_POINT_ACCEPTING_ETHER_SC1, Convert.Unit.ETHER).toBigInteger();
        BigInteger acceptingSc1Wei = getBalance(this.web3jSc1, this.entityAcceptingOfferCredentials.getAddress());
        BigInteger amount = targetStartingPoint.subtract(acceptingSc1Wei);
        boolean transfersNeeded = false;
        if (amount.compareTo(BigInteger.ZERO) != 0) {
            transfersNeeded = true;
            if (targetStartingPoint.compareTo(acceptingSc1Wei) == LESS_THAN) {
                amount = amount.negate();
                LOG.info("sending from accepting to faucet {} Wei", amount.toString());
                transactionReceipt = Transfer.sendFunds(this.web3jSc1, this.entityAcceptingOfferCredentials,
                    FAUCET_ADDRESS, new BigDecimal(amount), Convert.Unit.WEI).send();
            } else {
                LOG.info("sending from faucet to accepting {} Wei", amount.toString());
                transactionReceipt = Transfer.sendFunds(this.web3jSc1, this.faucetCredentials,
                    this.entityAcceptingOfferCredentials.getAddress(), new BigDecimal(amount), Convert.Unit.WEI).send();
            }
        }

        targetStartingPoint = Convert.toWei(STARTING_POINT_OFFERING_ETHER_SC2, Convert.Unit.ETHER).toBigInteger();
        BigInteger offeringSc2Wei = getBalance(this.web3jSc2, this.entityOfferingEtherCredentials.getAddress());
        amount = targetStartingPoint.subtract(offeringSc2Wei);
        if (amount.compareTo(BigInteger.ZERO) != 0) {
            transfersNeeded = true;
            if (targetStartingPoint.compareTo(offeringSc2Wei) == LESS_THAN) {
                amount = amount.negate();
                LOG.info("sending from offering to faucet {} Wei", amount.toString());
                transactionReceipt = Transfer.sendFunds(this.web3jSc2, this.entityOfferingEtherCredentials,
                    FAUCET_ADDRESS, new BigDecimal(amount), Convert.Unit.WEI).send();
            } else {
                LOG.info("sending from faucet to offering {} Wei", amount.toString());
                transactionReceipt = Transfer.sendFunds(this.web3jSc2, this.faucetCredentials,
                    this.entityOfferingEtherCredentials.getAddress(), new BigDecimal(amount), Convert.Unit.WEI).send();
            }
        }

        if (transfersNeeded) {
            showBalances();
        }
    }

    private void showBalances() throws Exception {
        LOG.info("Balances");
        LOG.info(" Faucet Account on SC1: {}", getBalanceAsString(this.web3jSc1, FAUCET_ADDRESS));
        LOG.info(" Faucet Account on SC2: {}", getBalanceAsString(this.web3jSc2, FAUCET_ADDRESS));
        LOG.info(" Offering Account on SC1: {}", getBalanceAsString(this.web3jSc1, this.entityOfferingEtherCredentials.getAddress()));
        LOG.info(" Offering Account on SC2: {}", getBalanceAsString(this.web3jSc2, this.entityOfferingEtherCredentials.getAddress()));
        LOG.info(" Accepting Account on SC1: {}", getBalanceAsString(this.web3jSc1, this.entityAcceptingOfferCredentials.getAddress()));
        LOG.info(" Accepting Account on SC2: {}", getBalanceAsString(this.web3jSc2, this.entityAcceptingOfferCredentials.getAddress()));
        if (this.senderContractAddress != null) {
            LOG.info(" Sending Contract on SC1: {}", getBalanceAsString(this.web3jSc1, this.senderContractAddress));
        }
        if (this.receiverContractAddress != null) {
            LOG.info(" Receiving Contract on SC2: {}", getBalanceAsString(this.web3jSc2, this.receiverContractAddress));
        }
    }

    private String getBalanceAsString(Besu besu, String address) throws Exception {
        BigInteger wei = getBalance(besu, address);
        java.math.BigDecimal tokenValue = Convert.fromWei(String.valueOf(wei), Convert.Unit.ETHER);
        return String.valueOf(tokenValue);
    }
    private BigInteger getBalance(Besu besu, String address) throws Exception {
        EthGetBalance ethGetBalance=
            besu.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get();
        return ethGetBalance.getBalance();
    }



    private void loadContracts() {
        LOG.info("Loading contracts");
        LOG.info(" Registration Contract: {}", this.registrationContractAddress);
        LOG.info(" Sender Contract: {}", this.senderContractAddress);
        LOG.info(" Receiver Contract: {}", this.receiverContractAddress);

        this.registrationContract = AtomicSwapRegistration.load(this.registrationContractAddress, this.web3jSc1, this.entityOfferingTmSc1, this.freeGasProvider);
        this.entityOfferingSenderContract = AtomicSwapSender.load(this.senderContractAddress, this.web3jSc1, this.entityOfferingTmSc1, this.freeGasProvider);
        this.entityOfferingReceiverContract = AtomicSwapReceiver.load(this.receiverContractAddress, this.web3jSc2, this.entityOfferingTmSc2, this.freeGasProvider);
    }
    private void loadContractsPart2() {
        LOG.info("Loading contracts from accepting account perspective");
        this.entityAcceptingOfferSenderContract = AtomicSwapSender.load(this.senderContractAddress, this.web3jSc1, this.entityAcceptingOfferTmSc1, this.freeGasProvider);
        this.entityAcceptingOfferReceiverContract = AtomicSwapReceiver.load(this.receiverContractAddress, this.web3jSc2, this.entityAcceptingOfferTmSc2, this.freeGasProvider);
    }

    private void deployContracts() throws Exception {
        LOG.info("Deploying contracts");
        RemoteCall<AtomicSwapRegistration> remoteCallRegistrationContract =
            AtomicSwapRegistration.deploy(this.web3jSc1, this.faucetTmSc1, this.freeGasProvider);
        this.registrationContract = remoteCallRegistrationContract.send();
        this.registrationContractAddress = this.registrationContract.getContractAddress();
        LOG.info(" Registration Contract deployed on sidechain 1 (id={}), at address: {}",  SC1_SIDECHAIN_ID, this.registrationContractAddress);


        BigInteger sc2OfferValue = Convert.toWei(STARTING_POINT_OFFERING_ETHER_SC2, Convert.Unit.ETHER).toBigInteger();
        RemoteCall<AtomicSwapReceiver> remoteCallReceiverContract =
            AtomicSwapReceiver.deployLockable(this.web3jSc2, this.entityOfferingTmSc2, this.freeGasProvider, sc2OfferValue, SC1_SIDECHAIN_ID);
        this.entityOfferingReceiverContract = remoteCallReceiverContract.send();
        this.receiverContractAddress = this.entityOfferingReceiverContract.getContractAddress();
        LOG.info(" Receiver Contract deployed on sidechain 2 (id={}), at address: {}", SC2_SIDECHAIN_ID, this.receiverContractAddress);

        RemoteCall<AtomicSwapSender> remoteCallSenderContract =
            AtomicSwapSender.deployLockable(this.web3jSc1, this.entityOfferingTmSc1, this.freeGasProvider,
                SC2_SIDECHAIN_ID, this.receiverContractAddress, getAdjustedExchangeRate(EXCHANGE_RATE));
        this.entityOfferingSenderContract = remoteCallSenderContract.send();
        this.senderContractAddress = this.entityOfferingSenderContract.getContractAddress();
        LOG.info(" Sender Contract deployed on sidechain 1 (id={}), at address: {}", SC1_SIDECHAIN_ID, this.senderContractAddress);

        // Finally, link the sender contract to the receiver contract.
        TransactionReceipt txReceipt =  this.entityOfferingReceiverContract.setSenderContract(this.senderContractAddress).send();
        LOG.info(" Receiver Contract deployed on sidechain 2 (id={}), at address: {}", txReceipt.isStatusOK());
    }


    private void core() throws Exception {
        LOG.info("Running Core Part of Sample Code");

        CallSimulator sim = new CallSimulator(AtomicSwapEther.getAdjustedExchangeRate(AtomicSwapEther.EXCHANGE_RATE));
        LOG.info("   Simulator says: Scaled exchange rate is: 0x{}", sim.atomicSwapSender_Exchange_exchangeRate.toString(16));

        // TODO the sample should include using the registration contract to find the send contract.


        showBalances();

        Scanner myInput = new Scanner( System.in );
        while (true) {
            LOG.info("Exchange rate is {} Ether in sidechain 2 (id:{}) for 1 Ether in sidechain 1 (id:{})", EXCHANGE_RATE, SC2_SIDECHAIN_ID, SC1_SIDECHAIN_ID);
            String prompt = " Enter sidechain 1 amount to transfer in Ether (for example 0.5, 1.0, or 2.0): ";
            System.out.println(prompt);
            double transferAmountEther = myInput.nextDouble();
            LOG.info("{} {}", prompt, transferAmountEther);
            BigInteger transferAmountWei = Convert.toWei(new BigDecimal(transferAmountEther), Convert.Unit.ETHER).toBigInteger();

            LOG.info("  Executing call simulator to determine parameter values and expected results");
            BigInteger receiverBalanceInWei = getBalance(this.web3jSc2, this.receiverContractAddress);
            BigInteger senderBalanceInWei = getBalance(this.web3jSc1, this.senderContractAddress);
            BigInteger accepterBalanceInWei = getBalance(this.web3jSc2, this.entityAcceptingOfferCredentials.getAddress());
            sim.setValues(receiverBalanceInWei, accepterBalanceInWei, senderBalanceInWei);
            sim.exchange(transferAmountWei);
            if (sim.atomicSwapSenderError) {
                LOG.info("***Simulator detected error while processing request: SenderError");
                continue;
            }
            if (sim.atomicSwapReceiverError) {
                LOG.info("***Simulator detected error while processing request: ReceiverError");
                continue;
            }

            LOG.info("   Simulator says: Receive amount is: {} Wei", sim.atomicSwapReceiver_Exchange_amount);
            LOG.info("   Simulator says: Receive contract balance will be: {} Wei", sim.receiverBalanceInWei);
            LOG.info("   Simulator says: Send contract balance will be: {} Wei", sim.senderBalanceInWei);
            LOG.info("   Simulator says: Accept account balance will be: {} Wei", sim.accepterBalanceInWei);

            LOG.info("  Constructing Nested Crosschain Transaction");
            byte[] subordinateTrans = this.entityAcceptingOfferReceiverContract.exchange_AsSignedCrosschainSubordinateTransaction(sim.atomicSwapReceiver_Exchange_amount, null);

            // Call to contract 1
            byte[][] subordinateTransactionsAndViews = new byte[][]{subordinateTrans};
            LOG.info("  Executing Crosschain Transaction");
            TransactionReceipt transactionReceipt = this.entityAcceptingOfferSenderContract.exchange_AsCrosschainTransaction(subordinateTransactionsAndViews, transferAmountWei).send();
            LOG.info("   Transaction Receipt: {}", transactionReceipt.toString());
            assertTrue(transactionReceipt.isStatusOK());

            // TODO should check to see if contracts unlocked before fetching values.
            Thread.sleep(5000);

            LOG.info("  Executing single blockchain transaction to withdraw money from send contract");
            transactionReceipt = this.entityOfferingSenderContract.withdraw().send();
            LOG.info("   Transaction Receipt: {}", transactionReceipt.toString());
            assertTrue(transactionReceipt.isStatusOK());


            // TODO should check to see if contracts unlocked before fetching values.
            Thread.sleep(5000);

            showBalances();
        }


    }

    public static BigInteger getAdjustedExchangeRate(double exchangeRate) {
        BigDecimal exRate = new BigDecimal(exchangeRate);
        BigInteger scalingFactor = CallSimulator.DECIMAL_POINT;
        BigDecimal result = exRate.multiply(new BigDecimal(scalingFactor));
        return result.toBigInteger();
    }


    private static void assertTrue(boolean val) {
        if (!val) {
            throw new Error("Unexpected Result");
        }
    }




    private boolean propertiesFileExists() {
        return Files.exists(getSamplePropertiesPath());
    }

    // Load the private keys and contract addresses from disk.
    //
    // ******                                                                         ******
    // ****** NOTE that in a production environment, extreme care should be exercised ******
    // ****** with the storage of private keys.                                       ******
    // ******                                                                         ******
    private void loadProperties() {
        Path path = getSamplePropertiesPath();
        LOG.info("Loading Properties from {}", path.toString());
        try {
            FileInputStream fis = new FileInputStream(getSamplePropertiesPath().toFile());
            Properties properties = new Properties();
            properties.load(fis);

            String privateKey = properties.getProperty(ENTITY_OFFERING_ETHER_PRIVATE_KEY);
            this.entityOfferingEtherCredentials = Credentials.create(privateKey);
            privateKey = properties.getProperty(ENTITY_ACCEPTING_OFFER_PRIVATE_KEY);
            this.entityAcceptingOfferCredentials = Credentials.create(privateKey);
            this.registrationContractAddress = properties.getProperty(REGISTRATION_CONTRACT_ADDRESS);
            this.senderContractAddress = properties.getProperty(SENDER_CONTRACT_ADDRESS);
            this.receiverContractAddress = properties.getProperty(RECEIVER_CONTRACT_ADDRESS);

        } catch (IOException ioEx) {
            // By the time we have reached the loadProperties method, we should be sure the file
            // exists. As such, just throw an exception to stop.
            throw new RuntimeException(ioEx);
        }
    }

    // Store the private keys and contract addresses to disk.
    //
    // ******                                                                         ******
    // ****** NOTE that in a production environment, extreme care should be exercised ******
    // ****** with the storage of private keys.                                       ******
    // ******                                                                         ******
    private void storeProperties() {
        Path path = getSamplePropertiesPath();
        LOG.info("Storing Properties to {}", path.toString());
        try {
            final FileOutputStream fos = new FileOutputStream(path.toFile());
            final Properties properties = new Properties();
            String privateKey = this.entityOfferingEtherCredentials.getEcKeyPair().getPrivateKey().toString(16);
            properties.setProperty(ENTITY_OFFERING_ETHER_PRIVATE_KEY, privateKey);
            privateKey = this.entityAcceptingOfferCredentials.getEcKeyPair().getPrivateKey().toString(16);
            properties.setProperty(ENTITY_ACCEPTING_OFFER_PRIVATE_KEY, privateKey);
            properties.setProperty(REGISTRATION_CONTRACT_ADDRESS, this.registrationContractAddress);
            properties.setProperty(SENDER_CONTRACT_ADDRESS, this.senderContractAddress);
            properties.setProperty(RECEIVER_CONTRACT_ADDRESS, this.receiverContractAddress);
            properties.store(fos, "Sample code properties file");
            fos.close();

        } catch (IOException ioEx) {
            // By the time we have reached the loadProperties method, we should be sure the file
            // exists. As such, just throw an exception to stop.
            throw new RuntimeException(ioEx);
        }
    }

    private Path getSamplePropertiesPath() {
        return Paths.get(System.getProperty("user.dir"), SAMPLE_PROPERTIES_FILE_NAME);
    }
}
