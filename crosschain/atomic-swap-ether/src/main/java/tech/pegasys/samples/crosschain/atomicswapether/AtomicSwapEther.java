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
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import tech.pegasys.samples.sidechains.common.coordination.CrosschainCoordinationContractSetup;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Main Class for sample code.
 *
 * Swap Ether on one blockchain for Ether on another blockchain.
 */
public class AtomicSwapEther {
    private static final Logger LOG = LogManager.getLogger(AtomicSwapEther.class);

    private static final String ENTITY_ACCEPTING_ALLOCATION = "100";
    private static final String ENTITY_OFFERING_ALLOCATION = "101";


    // For this sample to work, two Hyperledger Besu Ethereum Clients which represent
    // the sidechains / blockchains need to be deployed at the addresses shown below,
    // with the blockchain IDs indicated.
    private static final BigInteger SC1_SIDECHAIN_ID = BigInteger.valueOf(11);
    private static final String SC1_IP_PORT = "127.0.0.1:8110";
    private static final String SC1_URI = "http://" + SC1_IP_PORT + "/";
    private static final BigInteger SC2_SIDECHAIN_ID = BigInteger.valueOf(22);
    private static final String SC2_IP_PORT = "127.0.0.1:8220";
    private static final String SC2_URI = "http://" + SC2_IP_PORT + "/";

    // Have the polling interval equal to the block time.
    private static final int POLLING_INTERVAL = 2000;
    // Retry reqests to Ethereum Clients up to five times.
    private static final int RETRY = 5;

    // Time-out for Crosschain Transactions in terms of block numbers on SC0.
    private static final int CROSSCHAIN_TRANSACTION_TIMEOUT = 10;

    // Web services for each blockchain / sidechain.
    private Besu web3jSc1;
    private Besu web3jSc2;


    private Faucet faucet;
    private RegistrationContractOwner registrationContractOwner;
    private EntityAcceptingOffer entityAcceptingOffer;
    private EntityOfferingEther entityOfferingEther;

    static boolean automatedRun = false;

    public static void main(final String args[]) throws Exception {
        LOG.info("Atomic Swap Ether - started");
        new AtomicSwapEther().run();
    }

    public static void automatedRun() throws Exception {
      // Delete all properties files as a starting point. This will ensure all contracts are redeployed.
      deleteAllPropertiesFile();

      // Run the samples in a way that does not require input from the keyboard.
      automatedRun = true;
      new AtomicSwapEther().run();

      // Clean-up.
      deleteAllPropertiesFile();
    }

    private static void deleteAllPropertiesFile() throws IOException {
        // Delete all properties files as a starting point. This will ensure all contracts are redeployed.
        (new EntityAcceptingOffer.EntityAcceptingProperties()).deletePropertiesFile();
        (new EntityOfferingEther.EntityOfferingProperties()).deletePropertiesFile();
        (new Faucet.FaucetProperties()).deletePropertiesFile();
        (new RegistrationContractOwner.RegistrationProperties()).deletePropertiesFile();
    }

    private AtomicSwapEther() throws Exception {
        this.web3jSc1 = Besu.build(new HttpService(SC1_URI), POLLING_INTERVAL);
        this.web3jSc2 = Besu.build(new HttpService(SC2_URI), POLLING_INTERVAL);

        // Note that the multi-chain node is assumed to be configured.
        // If this is not the case, please use the Multichain Manager sample with the options "config auto".
        CrosschainCoordinationContractSetup coordinationContractSetup = new CrosschainCoordinationContractSetup(this.web3jSc1);

        this.faucet = new Faucet(this.web3jSc1, SC1_SIDECHAIN_ID, this.web3jSc2, SC2_SIDECHAIN_ID, RETRY, POLLING_INTERVAL);
        this.registrationContractOwner = new RegistrationContractOwner(this.web3jSc1, SC1_SIDECHAIN_ID, RETRY, POLLING_INTERVAL);
        this.entityOfferingEther = new EntityOfferingEther(this.web3jSc1, SC1_SIDECHAIN_ID, this.web3jSc2, SC2_SIDECHAIN_ID, RETRY, POLLING_INTERVAL,
            coordinationContractSetup.getCrosschainCoordinationWeb3J(),
            coordinationContractSetup.getCrosschainCoordinationContractBlockcainId(),
            coordinationContractSetup.getCrosschainCoordinationContractAddress(),
            CROSSCHAIN_TRANSACTION_TIMEOUT);
        this.entityAcceptingOffer = new EntityAcceptingOffer(this.web3jSc1, SC1_SIDECHAIN_ID, this.web3jSc2, SC2_SIDECHAIN_ID, RETRY, POLLING_INTERVAL,
            coordinationContractSetup.getCrosschainCoordinationWeb3J(),
            coordinationContractSetup.getCrosschainCoordinationContractBlockcainId(),
            coordinationContractSetup.getCrosschainCoordinationContractAddress(),
            CROSSCHAIN_TRANSACTION_TIMEOUT);

        initialFunding();
        if (this.registrationContractOwner.getRegistrationContractAddress() == null) {
            deployRegistrationContract();
        }
    }

    private void initialFunding() throws Exception {
        LOG.info("Faucet funding of Entity Offering Ether on sidechain 2 and Entity Accepting Offer / Offering Ether on sidechain 1");
        this.faucet.sendEtherSc1(this.entityAcceptingOffer.accountAddress(), Convert.toWei(ENTITY_ACCEPTING_ALLOCATION, Convert.Unit.ETHER).toBigInteger());
        LOG.info(" Faucet sent {} Ether to {} (entity accepting offer) on SC1", ENTITY_ACCEPTING_ALLOCATION, this.entityAcceptingOffer.accountAddress());
        this.faucet.sendEtherSc2(this.entityOfferingEther.accountAddress(), Convert.toWei(ENTITY_OFFERING_ALLOCATION, Convert.Unit.ETHER).toBigInteger());
        LOG.info(" Faucet sent {} Ether to {} (entity offering Ether) on SC2", ENTITY_OFFERING_ALLOCATION, this.entityOfferingEther.accountAddress());
    }

    private void deployRegistrationContract() throws Exception {
        this.registrationContractOwner.deployRegistrationContract();
    }

    private void run() throws Exception {
        boolean runOnce = false;
        Scanner myInput = new Scanner( System.in );
        while (true) {
            System.out.println("What do you want to do next?");
            System.out.println("0  Run entire sample.");
            System.out.println("1  Deploy new Sender and Receiver contracts and set-up for an Atomic Swap of Ether.");
            System.out.println("2  Swap Ether.");
            System.out.println("3  Show balances.");
            System.out.println("4  Display offers.");
            System.out.println("5  Entity Offering, withdraw funds on SC1");
            System.out.println("6  Entity Offering, deposit funds on SC2");
            System.out.println("7  Deploy new registration contract");

            int option = 0;
            if (automatedRun) {
              System.out.println("Executing automated run. Executing option 0.");
              runOnce = true;
            }
            else {
              if (myInput.hasNext()) {
                option = myInput.nextInt();
              }
              else {
                System.out.println("No input device available. Executing option 0.");
                runOnce = true;
              }
            }
            switch (option) {
                case 0:
                    final double sidechain1Ether = 5;
                    final double sidechain2Ether = 10;
                    final double exchangeRate = 2.0;
                    final int offerNumberOfLatest = -1;
                    LOG.info("Exchange {} Sidechain 2 Ether for {} Sidechain 1 Ether. Exchange rate: {}",
                        sidechain2Ether, sidechain1Ether, exchangeRate);
                    showBalances();
                    setupOffer(null, exchangeRate, sidechain2Ether);
                    acceptOffer(null, sidechain1Ether, offerNumberOfLatest);
                    entityOfferingWithdrawlSc1();
                    showBalances();
                    break;
                case 1:
                    setupOffer(myInput, 0, 0);
                    break;
                case 2:
                    acceptOffer(myInput, 0, 0);
                    break;
                case 3:
                    showBalances();
                    break;
                case 4:
                    this.entityAcceptingOffer.showOffers(this.registrationContractOwner.getRegistrationContractAddress());
                    break;
                case 5:
                    entityOfferingWithdrawlSc1();
                    break;
                case 6:
                    entityOfferingDepositSc2(myInput, 0);
                    break;
                case 7:
                    deployRegistrationContract();
                    break;
                default:
                    LOG.error("Unknown option {}", option);
                    break;
            }

            if (runOnce) {
                return;
            }
        }
    }

    private void setupOffer(final Scanner in, double exchangeRate, double transferAmountEther) throws Exception {
        if (in != null) {
            System.out.println("What exchange rate do you want to use? (specify a double, for example 1.5)");
            exchangeRate = in.nextDouble();
            System.out.println("How much Sidechain 2 Ether do you want to offer? (specify a double, for example 0.1)");
            transferAmountEther = in.nextDouble();
        }
        LOG.info(" Deploying Sender and Receiver contracts and registering with Registration Contract");
        LOG.info(" Specifying exchange rate of {}. Funding the Receiver with {} Ether", exchangeRate, transferAmountEther);
        BigInteger adjustedExchangeRate = getAdjustedExchangeRate(exchangeRate);
        BigInteger transferAmountWei = Convert.toWei(new BigDecimal(transferAmountEther), Convert.Unit.ETHER).toBigInteger();
        this.entityOfferingEther.setUpAndDeployContracts(this.registrationContractOwner.getRegistrationContractAddress(),
            adjustedExchangeRate, transferAmountWei);
    }

    private void acceptOffer(final Scanner in, double transferAmountEther, int offerNumber) throws Exception {
        if (in != null) {
            System.out.println("How much Sidechain 1 Ether do you want to exchange? (specify a double, for example 1.5)");
            transferAmountEther = in.nextDouble();
            System.out.println("Which offer do you wish to accept? (for example 0 or 1)");
            offerNumber = in.nextInt();
        }
        BigInteger transferAmountWei = Convert.toWei(new BigDecimal(transferAmountEther), Convert.Unit.ETHER).toBigInteger();
        if (this.entityAcceptingOffer.prepareForExchange(this.registrationContractOwner.getRegistrationContractAddress(), offerNumber)) {
            // There was an error. We can't continue.
            return;
        }
        this.entityAcceptingOffer.swapEther(transferAmountWei);
    }

    private void showBalances() throws Exception {
        LOG.info("Balances");
        LOG.info(" Faucet Account {} on SC1: {}",
            this.faucet.getFaucetAddress(), getBalanceAsString(this.web3jSc1, this.faucet.getFaucetAddress()));
        LOG.info(" Faucet Account {} on SC2: {}",
            this.faucet.getFaucetAddress(), getBalanceAsString(this.web3jSc2, this.faucet.getFaucetAddress()));
        LOG.info(" Offering Account {} on SC1: {}",
            this.entityOfferingEther.accountAddress(), getBalanceAsString(this.web3jSc1, this.entityOfferingEther.accountAddress()));
        LOG.info(" Offering Account {} on SC2: {}",
            this.entityOfferingEther.accountAddress(), getBalanceAsString(this.web3jSc2, this.entityOfferingEther.accountAddress()));
        LOG.info(" Accepting Account {} on SC1: {}",
            this.entityAcceptingOffer.accountAddress(), getBalanceAsString(this.web3jSc1, this.entityAcceptingOffer.accountAddress()));
        LOG.info(" Accepting Account {} on SC2: {}",
            this.entityAcceptingOffer.accountAddress(), getBalanceAsString(this.web3jSc2, this.entityAcceptingOffer.accountAddress()));
        if (this.entityOfferingEther.senderContractAddress != null) {
            LOG.info(" Sending Contract {} on SC1: {}",
                this.entityOfferingEther.senderContractAddress, getBalanceAsString(this.web3jSc1, this.entityOfferingEther.senderContractAddress));
            LOG.info(" Sending Contract Balance (offering): {}", this.entityOfferingEther.myAccountBalanceSenderContract());
            LOG.info(" Sending Contract Balance (accepting): {}", this.entityAcceptingOffer.myAccountBalanceSenderContract());
        }
        if (this.entityOfferingEther.receiverContractAddress != null) {
            LOG.info(" Receiving Contract {} on SC2: {}",
                this.entityOfferingEther.receiverContractAddress, getBalanceAsString(this.web3jSc2, this.entityOfferingEther.receiverContractAddress));
            LOG.info(" Receiving Contract Balance (offering): {}", this.entityOfferingEther.myAccountBalanceReceiverContract());
            LOG.info(" Receiving Contract Balance (accepting): {}", this.entityAcceptingOffer.myAccountBalanceReceiverContract());
        }
    }


    private void entityOfferingWithdrawlSc1() throws Exception {
        LOG.info("Entity Offering: Withdraw all Ether from sender contract on Sidechain 1");
        this.entityOfferingEther.withdrawEverythingSc1();
    }


    private void entityOfferingDepositSc2(Scanner in, double transferAmountEther) throws Exception {
        LOG.info("Entity Offering: Deposit Ether into receiver contract on Sidechain 2");
        if (in != null) {
            System.out.println("How much Sidechain 2 Ether do you want to deposit? (specify a double, for example 0.1)");
            transferAmountEther = in.nextDouble();
        }
        BigInteger transferAmountWei = Convert.toWei(new BigDecimal(transferAmountEther), Convert.Unit.ETHER).toBigInteger();
        this.entityOfferingEther.depositSc2(transferAmountWei);
    }


    private String getBalanceAsString(Besu besu, String address) throws InterruptedException, ExecutionException {
        BigInteger wei = getBalance(besu, address);
        java.math.BigDecimal tokenValue = Convert.fromWei(String.valueOf(wei), Convert.Unit.ETHER);
        return String.valueOf(tokenValue);
    }
    private BigInteger getBalance(Besu besu, String address) throws InterruptedException, ExecutionException {
        EthGetBalance ethGetBalance=
            besu.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get();
        return ethGetBalance.getBalance();
    }


    private static BigInteger getAdjustedExchangeRate(double exchangeRate) {
        BigDecimal exRate = new BigDecimal(exchangeRate);
        BigInteger scalingFactor = CallSimulator.DECIMAL_POINT;
        BigDecimal result = exRate.multiply(new BigDecimal(scalingFactor));
        return result.toBigInteger();
    }

}
