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
package tech.pegasys.samples.crosschain.threechainssixcontracts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.besu.response.crosschain.CrossIsLockedResponse;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.CrosschainContext;
import org.web3j.tx.CrosschainContextGenerator;
import org.web3j.tx.CrosschainTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers.Sc1Contract1;
import tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers.Sc2Contract2;
import tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers.Sc2Contract3;
import tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers.Sc2Contract4;
import tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers.Sc3Contract5;
import tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers.Sc3Contract6;
import tech.pegasys.samples.sidechains.common.coordination.CrosschainCoordinationContractSetup;
import tech.pegasys.samples.sidechains.common.utils.KeyPairGen;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

/**
 * The main class.
 */
public class ThreeChainsSixContracts {
    private static final Logger LOG = LogManager.getLogger(ThreeChainsSixContracts.class);

    // For this sample to work, three Hyperledger Besu Ethereum Clients which represent
    // three sidechains / blockchains need to be deployed at the addresses shown below,
    // with the blockchain IDs indicated.
    private static final BigInteger SC1_SIDECHAIN_ID = BigInteger.valueOf(11);
    private static final String SC1_IP_PORT = "127.0.0.1:8110";
    private static final String SC1_URI = "http://" + SC1_IP_PORT + "/";
    private static final BigInteger SC2_SIDECHAIN_ID = BigInteger.valueOf(22);
    private static final String SC2_IP_PORT = "127.0.0.1:8220";
    private static final String SC2_URI = "http://" + SC2_IP_PORT + "/";
    private static final BigInteger SC3_SIDECHAIN_ID = BigInteger.valueOf(33);
    private static final String SC3_IP_PORT = "127.0.0.1:8330";
    private static final String SC3_URI = "http://" + SC3_IP_PORT + "/";

    // Have the polling interval equal to the block time.
    private static final int POLLING_INTERVAL = 2000;
    // Retry reqests to Ethereum Clients up to five times.
    private static final int RETRY = 5;

    // Time-out for Crosschain Transactions in terms of block numbers on SC0.
    private static final int CROSSCHAIN_TRANSACTION_TIMEOUT = 10;


    // Name of properties file which holds information for this sample code.
    private static final String SAMPLE_PROPERTIES_FILE_NAME = "sample.properties";
    // Properties in the file.
    private static final String PRIVATE_KEY = "PrivateKey";
    private static final String CONTRACT1_ADDRESS = "Contract1Address";
    private static final String CONTRACT2_ADDRESS = "Contract2Address";
    private static final String CONTRACT3_ADDRESS = "Contract3Address";
    private static final String CONTRACT4_ADDRESS = "Contract4Address";
    private static final String CONTRACT5_ADDRESS = "Contract5Address";
    private static final String CONTRACT6_ADDRESS = "Contract6Address";

    // Externally Owned Account key pair.
    private Credentials credentials;

    // Web services for each blockchain / sidechain.
    private Besu web3jSc1;
    private Besu web3jSc2;
    private Besu web3jSc3;

    // A gas provider which indicates no gas is charged for transactions.
    private ContractGasProvider freeGasProvider;

    // Transaction manager for each sidechain.
    private CrosschainTransactionManager tmSc1;
    private CrosschainTransactionManager tmSc2;
    private CrosschainTransactionManager tmSc3;

    // Smart contract addresses and objected.of contracts.
    private String contract1Address = null;
    private String contract2Address = null;
    private String contract3Address = null;
    private String contract4Address = null;
    private String contract5Address = null;
    private String contract6Address = null;
    private Sc1Contract1 contract1;
    private Sc2Contract2 contract2;
    private Sc2Contract3 contract3;
    private Sc2Contract4 contract4;
    private Sc3Contract5 contract5;
    private Sc3Contract6 contract6;

    private CrosschainCoordinationContractSetup coordinationContractSetup;


    private static boolean automatedRun = false;

    public static void main(final String args[]) throws Exception {
        LOG.info("Three Chains Six Contracts - started");
        new ThreeChainsSixContracts().run();
    }

    public static void automatedRun() throws Exception {
        // Delete all properties files as a starting point. This will ensure all contracts are redeployed.
        deleteAllPropertiesFile();

        // Run the samples in a way that does not require input from the keyboard.
        automatedRun = true;
        new ThreeChainsSixContracts().run();

        // Clean-up.
        deleteAllPropertiesFile();
    }

    private static void deleteAllPropertiesFile() throws IOException {
        Files.deleteIfExists(getSamplePropertiesPath());
    }



    private void run() throws Exception {
        if (propertiesFileExists()) {
            loadProperties();
            setupBesuServiceTransactionManager();
            loadContracts();
        }
        else {
            this.credentials = Credentials.create(new KeyPairGen().generateKeyPairGetPrivateKey());
            setupBesuServiceTransactionManager();
            deployContracts();
            storeProperties();
        }
        LOG.info("Using credentials which correspond to account: {}", this.credentials.getAddress());

        core();
    }

    private void setupBesuServiceTransactionManager() throws Exception {
        LOG.info("Setting up Besu service and transaction managers");

        this.web3jSc1 = Besu.build(new HttpService(SC1_URI), POLLING_INTERVAL);
        this.web3jSc2 = Besu.build(new HttpService(SC2_URI), POLLING_INTERVAL);
        this.web3jSc3 = Besu.build(new HttpService(SC3_URI), POLLING_INTERVAL);

        // Note that the multi-chain node is assumed to be configured.
        // If this is not the case, please use the Multichain Manager sample with the options "config auto".
        CrosschainCoordinationContractSetup coordinationContractSetup = new CrosschainCoordinationContractSetup(this.web3jSc1);

        this.tmSc1 = new CrosschainTransactionManager(this.web3jSc1, this.credentials, SC1_SIDECHAIN_ID, RETRY, POLLING_INTERVAL,
            coordinationContractSetup.getCrosschainCoordinationWeb3J(),
            coordinationContractSetup.getCrosschainCoordinationContractBlockcainId(),
            coordinationContractSetup.getCrosschainCoordinationContractAddress(),
            CROSSCHAIN_TRANSACTION_TIMEOUT);
        this.tmSc2 = new CrosschainTransactionManager(this.web3jSc2, this.credentials, SC2_SIDECHAIN_ID, RETRY, POLLING_INTERVAL,
            coordinationContractSetup.getCrosschainCoordinationWeb3J(),
            coordinationContractSetup.getCrosschainCoordinationContractBlockcainId(),
            coordinationContractSetup.getCrosschainCoordinationContractAddress(),
            CROSSCHAIN_TRANSACTION_TIMEOUT);
        this.tmSc3 = new CrosschainTransactionManager(this.web3jSc3, this.credentials, SC3_SIDECHAIN_ID, RETRY, POLLING_INTERVAL,
            coordinationContractSetup.getCrosschainCoordinationWeb3J(),
            coordinationContractSetup.getCrosschainCoordinationContractBlockcainId(),
            coordinationContractSetup.getCrosschainCoordinationContractAddress(),
            CROSSCHAIN_TRANSACTION_TIMEOUT);

        // Hyperledger Besu is configured as an IBFT2, free gas network. We need a free gas provider.
        this.freeGasProvider = new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);
    }

    private void loadContracts() {
        LOG.info("Loading contracts");
        LOG.info(" Contract 1: {}", this.contract1Address);
        LOG.info(" Contract 2: {}", this.contract2Address);
        LOG.info(" Contract 3: {}", this.contract3Address);
        LOG.info(" Contract 4: {}", this.contract4Address);
        LOG.info(" Contract 5: {}", this.contract5Address);
        LOG.info(" Contract 6: {}", this.contract6Address);

        this.contract1 = Sc1Contract1.load(this.contract1Address, this.web3jSc1, this.tmSc1, this.freeGasProvider);
        this.contract2 = Sc2Contract2.load(this.contract2Address, this.web3jSc2, this.tmSc2, this.freeGasProvider);
        this.contract3 = Sc2Contract3.load(this.contract3Address, this.web3jSc2, this.tmSc2, this.freeGasProvider);
        this.contract4 = Sc2Contract4.load(this.contract4Address, this.web3jSc2, this.tmSc2, this.freeGasProvider);
        this.contract5 = Sc3Contract5.load(this.contract5Address, this.web3jSc3, this.tmSc3, this.freeGasProvider);
        this.contract6 = Sc3Contract6.load(this.contract6Address, this.web3jSc3, this.tmSc3, this.freeGasProvider);
    }

    private void deployContracts() throws Exception {
        LOG.info("Deploying contracts");
        RemoteCall<Sc2Contract2> remoteCallContract2 =
            Sc2Contract2.deployLockable(this.web3jSc2, this.tmSc2, this.freeGasProvider);
        this.contract2 = remoteCallContract2.send();
        this.contract2Address = this.contract2.getContractAddress();
        LOG.info(" Contract 2 deployed on sidechain 2 (id={}), at address: {}",  SC2_SIDECHAIN_ID, contract2Address);

        RemoteCall<Sc2Contract4> remoteCallContract4 =
            Sc2Contract4.deployLockable(this.web3jSc2, this.tmSc2, this.freeGasProvider);
        this.contract4 = remoteCallContract4.send();
        this.contract4Address = this.contract4.getContractAddress();
        LOG.info(" Contract 4 deployed on sidechain 2 (id={}), at address: {}", SC2_SIDECHAIN_ID, contract4Address);

        RemoteCall<Sc3Contract5> remoteCallContract5 =
            Sc3Contract5.deployLockable(this.web3jSc3, this.tmSc3, this.freeGasProvider);
        this.contract5 = remoteCallContract5.send();
        this.contract5Address = this.contract5.getContractAddress();
        LOG.info(" Contract 5 deployed on sidechain 3 (id={}), at address: {}", SC3_SIDECHAIN_ID, contract5Address);

        RemoteCall<Sc3Contract6> remoteCallContract6 =
            Sc3Contract6.deployLockable(this.web3jSc3, this.tmSc3, this.freeGasProvider, SC2_SIDECHAIN_ID, contract4Address);
        this.contract6 = remoteCallContract6.send();
        this.contract6Address = contract6.getContractAddress();
        LOG.info(" Contract 6 deployed on sidechain 3 (id={}), at address: {}", SC2_SIDECHAIN_ID,  contract6Address);

        RemoteCall<Sc2Contract3> remoteCallContract3 =
            Sc2Contract3.deployLockable(this.web3jSc2, this.tmSc2, this.freeGasProvider, SC3_SIDECHAIN_ID, contract6Address);
        this.contract3 = remoteCallContract3.send();
        this.contract3Address = this.contract3.getContractAddress();
        LOG.info(" Contract 3 deployed on sidechain 2 (id={}), at address: {}", SC2_SIDECHAIN_ID, contract3Address);

        RemoteCall<Sc1Contract1> remoteCallContract1 =
            Sc1Contract1.deployLockable(this.web3jSc1, this.tmSc1, this.freeGasProvider, SC2_SIDECHAIN_ID, SC3_SIDECHAIN_ID, contract2Address, contract3Address, contract5Address);
        this.contract1 = remoteCallContract1.send();
        this.contract1Address = this.contract1.getContractAddress();
        LOG.info(" Contract 1 deployed on sidechain 1 (id={}), at address: {}", SC1_SIDECHAIN_ID, contract1Address);
    }




    private void core() throws Exception {
        LOG.info("Running Core Part of Sample Code");

        LOG.info(" Set state in each contract to known values that aren't zero.");
        LOG.info("  Single-chain transaction: Contract1.set(1)");
        TransactionReceipt transactionReceipt = this.contract1.setVal(BigInteger.valueOf(1)).send();
        assertTrue(transactionReceipt.isStatusOK());
        LOG.info("  Single-chain transaction: Contract2.set(2)");
        transactionReceipt = this.contract2.setVal(BigInteger.valueOf(2)).send();
        assertTrue(transactionReceipt.isStatusOK());
        LOG.info("  Single-chain transaction: Contract3.set(3)");
        transactionReceipt = this.contract3.setVal(BigInteger.valueOf(3)).send();
        assertTrue(transactionReceipt.isStatusOK());
        LOG.info("  Single-chain transaction: Contract4.set(4)");
        transactionReceipt = this.contract4.setVal(BigInteger.valueOf(4)).send();
        assertTrue(transactionReceipt.isStatusOK());
        LOG.info("  Single-chain transaction: Contract5.set(5)");
        transactionReceipt = this.contract5.setVal(BigInteger.valueOf(5)).send();
        assertTrue(transactionReceipt.isStatusOK());
        LOG.info("  Single-chain transaction: Contract6.set(6)");
        transactionReceipt = this.contract6.setVal(BigInteger.valueOf(6)).send();
        assertTrue(transactionReceipt.isStatusOK());

        checkExpectedValues(1,2,3,4,5,6);

        Scanner myInput = new Scanner( System.in );
        while (true) {
            String prompt = " Enter long value to call Contract1.doStuff with: ";
            System.out.println(prompt);

            boolean runOnce = automatedRun;
            final long VAL = 43;
            long val = VAL;
            if (automatedRun) {
                System.out.println("Executing automated run. Executing with value " + VAL + ".");
                runOnce = true;
            }
            else {
                if (myInput.hasNext()) {
                    val = myInput.nextLong();
                }
                else {
                    System.out.println("No input device available. Executing with value " + VAL + ".");
                    runOnce = true;
                }
            }
            if (val < 0) {
                System.out.println("  No negative numbers please!");
                continue;
            }
            LOG.info("{} {}", prompt, val);

            BigInteger c1Val = this.contract1.val().send();
            BigInteger c2Val = this.contract2.val().send();
            BigInteger c3Val = this.contract3.val().send();
            BigInteger c4Val = this.contract4.val().send();
            BigInteger c5Val = this.contract5.val().send();
            BigInteger c6Val = this.contract6.val().send();

            LOG.info("  Executing call simulator to determine parameter values and expected results");
            CallSimulator sim = new CallSimulator(c1Val.longValue(), c2Val.longValue(), c3Val.longValue(), c4Val.longValue(), c5Val.longValue(), c6Val.longValue());
            sim.c1DoStuff(val);

            LOG.info("  Constructing Nested Crosschain Transaction");
            // Originating sidechain is sidechain 1.
            CrosschainContextGenerator contextGenerator = new CrosschainContextGenerator(SC1_SIDECHAIN_ID);

            // Call to contract 2
            // Contract 2 is called by conract 1 on sidechain 1.
            CrosschainContext subordinateContext = contextGenerator.createCrosschainContext(SC1_SIDECHAIN_ID, this.contract1Address);
            byte[] subordinateViewC2 = this.contract2.get_AsSignedCrosschainSubordinateView(subordinateContext);

            // Call to contract 4
            // Contract 4 is called by contract 6 on sidechain 3.
            subordinateContext = contextGenerator.createCrosschainContext(SC3_SIDECHAIN_ID, this.contract6Address);
            byte[] subordinateViewC4 = this.contract4.get_AsSignedCrosschainSubordinateView(BigInteger.valueOf(sim.c4Get_val), subordinateContext);

            // Call to contract 5
            // Contract 5 is called by contract 1 on sidechain 1.
            subordinateContext = contextGenerator.createCrosschainContext(SC1_SIDECHAIN_ID, this.contract1Address);
            byte[] subordinateViewC5 = this.contract5.calculate_AsSignedCrosschainSubordinateView(
                BigInteger.valueOf(sim.c5Calculate_val1), BigInteger.valueOf(sim.c5Calculate_val2), subordinateContext);

            // Call to contract 6
            // Contract 6 is called by contract 3 on sidechain 2.
            byte[][] subordinateTransactionsAndViewsForC6 = new byte[][] {subordinateViewC4};
            subordinateContext = contextGenerator.createCrosschainContext(SC2_SIDECHAIN_ID, this.contract3Address, subordinateTransactionsAndViewsForC6);
            byte[] subordinateViewC6 = this.contract6.get_AsSignedCrosschainSubordinateView(
                BigInteger.valueOf(sim.c6Get_val), subordinateContext);

            // Call to contract 3
            // Contract 3 is called by contract 1 on sidechain 1.
            byte[][] subordinateTransactionsAndViewsForC3 = new byte[][] {subordinateViewC6};
            subordinateContext = contextGenerator.createCrosschainContext(SC1_SIDECHAIN_ID, this.contract1Address, subordinateTransactionsAndViewsForC3);
            byte[] subordinateTransC3 = this.contract3.process_AsSignedCrosschainSubordinateTransaction(BigInteger.valueOf(sim.c3Process_val),
                subordinateContext);

            // Call to contract 1
            byte[][] subordinateTransactionsAndViewsForC1;
            if (sim.c1IsIfTaken) {
                subordinateTransactionsAndViewsForC1 = new byte[][]{subordinateViewC2, subordinateViewC5, subordinateTransC3};
            }
            else {
                subordinateTransactionsAndViewsForC1 = new byte[][] {subordinateViewC2};
            }
            LOG.info("  Executing Crosschain Transaction");
            // Contract 1 is the originating transaction.
            subordinateContext = contextGenerator.createCrosschainContext(subordinateTransactionsAndViewsForC1);
            transactionReceipt = this.contract1.doStuff_AsCrosschainOriginatingTransaction(BigInteger.valueOf(val), subordinateContext).send();
            LOG.info("  Transaction Receipt: {}", transactionReceipt.toString());
            assertTrue(transactionReceipt.isStatusOK());

            boolean stillLocked;
            final int tooLong = 10;
            int longTimeCount = 0;
            StringBuffer graphicalCount = new StringBuffer();
            do {
                longTimeCount++;
                if (longTimeCount > tooLong) {
                    LOG.error("Contract {} did not unlock", this.contract1Address);
                }
                Thread.sleep(500);
                CrossIsLockedResponse isLockedObj = this.web3jSc1.crossIsLocked(this.contract1Address, DefaultBlockParameter.valueOf("latest")).send();
                stillLocked = isLockedObj.isLocked();
                if (stillLocked) {
                    graphicalCount.append(".");
                    LOG.info("   Waiting for the contract to unlock{}", graphicalCount.toString());
                }
            } while (stillLocked);
            // TODO until new unlock logic using crosschain coordination contract - just wait!
            Thread.sleep(5000);

            checkExpectedValues(sim.val1, sim.val2, sim.val3, sim.val4, sim.val5, sim.val6);

            if (runOnce) {
                return;
            }
        }
    }


    private void checkExpectedValues(long v1, long v2, long v3, long v4, long v5, long v6) throws Exception {
        LOG.info(" Check values have been set as expected");
        BigInteger result = this.contract1.val().send();
        LOG.info("  Contract1.val = {}, expecting {}", result.intValue(), v1);
        assertTrue(result.equals(BigInteger.valueOf(v1)));
        result = this.contract2.val().send();
        LOG.info("  Contract2.val = {}, expecting {}", result.intValue(), v2);
        assertTrue(result.equals(BigInteger.valueOf(v2)));
        result = this.contract3.val().send();
        LOG.info("  Contract3.val = {}, expecting {}", result.intValue(), v3);
        assertTrue(result.equals(BigInteger.valueOf(v3)));
        result = this.contract4.val().send();
        LOG.info("  Contract4.val = {}, expecting {}", result.intValue(), v4);
        assertTrue(result.equals(BigInteger.valueOf(v4)));
        result = this.contract5.val().send();
        LOG.info("  Contract5.val = {}, expecting {}", result.intValue(), v5);
        assertTrue(result.equals(BigInteger.valueOf(v5)));
        result = this.contract6.val().send();
        LOG.info("  Contract6.val = {}, expecting {}", result.intValue(), v6);
        assertTrue(result.equals(BigInteger.valueOf(v6)));
    }


    private static void assertTrue(boolean val) {
        if (!val) {
            throw new Error("Unexpected Result");
        }
    }




    private boolean propertiesFileExists() {
        return Files.exists(getSamplePropertiesPath());
    }

    // Load the private key and contract addresses from disk.
    //
    // ******                                                                         ******
    // ****** NOTE that in a production environment, extreme care should be exercised ******
    // ****** with the storage of the private key.                                    ******
    // ******                                                                         ******
    private void loadProperties() {
        Path path = getSamplePropertiesPath();
        LOG.info("Loading Properties from {}", path.toString());
        try {
            FileInputStream fis = new FileInputStream(getSamplePropertiesPath().toFile());
            Properties properties = new Properties();
            properties.load(fis);

            String privateKey = properties.getProperty(PRIVATE_KEY);
            this.credentials = Credentials.create(privateKey);
            this.contract1Address = properties.getProperty(CONTRACT1_ADDRESS);
            this.contract2Address = properties.getProperty(CONTRACT2_ADDRESS);
            this.contract3Address = properties.getProperty(CONTRACT3_ADDRESS);
            this.contract4Address = properties.getProperty(CONTRACT4_ADDRESS);
            this.contract5Address = properties.getProperty(CONTRACT5_ADDRESS);
            this.contract6Address = properties.getProperty(CONTRACT6_ADDRESS);

        } catch (IOException ioEx) {
            // By the time we have reached the loadProperties method, we should be sure the file
            // exists. As such, just throw an exception to stop.
            throw new RuntimeException(ioEx);
        }
    }

    // Store the private key and contract addresses to disk.
    //
    // ******                                                                         ******
    // ****** NOTE that in a production environment, extreme care should be exercised ******
    // ****** with the storage of the private key.                                    ******
    // ******                                                                         ******
    private void storeProperties() {
        Path path = getSamplePropertiesPath();
        LOG.info("Storing Properties to {}", path.toString());
        try {
            final FileOutputStream fos = new FileOutputStream(path.toFile());
            final Properties properties = new Properties();
            String privateKey = this.credentials.getEcKeyPair().getPrivateKey().toString(16);
            properties.setProperty(PRIVATE_KEY, privateKey);
            properties.setProperty(CONTRACT1_ADDRESS, this.contract1Address);
            properties.setProperty(CONTRACT2_ADDRESS, this.contract2Address);
            properties.setProperty(CONTRACT3_ADDRESS, this.contract3Address);
            properties.setProperty(CONTRACT4_ADDRESS, this.contract4Address);
            properties.setProperty(CONTRACT5_ADDRESS, this.contract5Address);
            properties.setProperty(CONTRACT6_ADDRESS, this.contract6Address);
            properties.store(fos, "Sample code properties file");

        } catch (IOException ioEx) {
            // By the time we have reached the loadProperties method, we should be sure the file
            // exists. As such, just throw an exception to stop.
            throw new RuntimeException(ioEx);
        }
    }

    private static Path getSamplePropertiesPath() {
        return Paths.get(System.getProperty("user.dir"), SAMPLE_PROPERTIES_FILE_NAME);
    }
}
