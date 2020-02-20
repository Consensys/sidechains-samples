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
import org.web3j.protocol.http.HttpService;
import tech.pegasys.samples.sidechains.common.coordination.CrosschainCoordinationContractSetup;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Scanner;

/**
 * Main Class for sample code.
 *
 * Book a hotel room and a train seat atomically.
 */
public class HotelTrain {
    private static final Logger LOG = LogManager.getLogger(HotelTrain.class);

    // For this sample to work, two Hyperledger Besu Ethereum Clients which represent
    // the blockchains need to be deployed at the addresses shown below,
    // with the blockchain IDs indicated.
    private static final BigInteger BC1_SIDECHAIN_ID = BigInteger.valueOf(11);
    private static final String BC1_IP_PORT = "127.0.0.1:8110";
    private static final String BC1_URI = "http://" + BC1_IP_PORT + "/";
    private static final BigInteger BC2_SIDECHAIN_ID = BigInteger.valueOf(22);
    private static final String BC2_IP_PORT = "127.0.0.1:8220";
    private static final String BC2_URI = "http://" + BC2_IP_PORT + "/";
    private static final BigInteger BC3_SIDECHAIN_ID = BigInteger.valueOf(33);
    private static final String BC3_IP_PORT = "127.0.0.1:8330";
    private static final String BC3_URI = "http://" + BC3_IP_PORT + "/";

    // Have the polling interval equal to the block time.
    private static final int POLLING_INTERVAL = 2000;
    // Retry reqests to Ethereum Clients up to five times.
    private static final int RETRY = 5;

    // Time-out for Crosschain Transactions in terms of block numbers on SC0.
    private static final int CROSSCHAIN_TRANSACTION_TIMEOUT = 10;

    // Web services for each blockchain.
    private Besu web3jBc1;
    private Besu web3jBc2;
    private Besu web3jBc3;


    private EntityTrain train;
    private EntityHotel hotel;
    private EntityTravelAgency agency;

    static boolean automatedRun = false;

    public static void main(final String args[]) throws Exception {
        LOG.info("Hotel Train - started");
        new HotelTrain().run();
    }

    public static void automatedRun() throws Exception {
      // Delete all properties files as a starting point. This will ensure all contracts are redeployed.
      deleteAllPropertiesFile();

      // Run the samples in a way that does not require input from the keyboard.
      automatedRun = true;
      new HotelTrain().run();

      // Clean-up.
      deleteAllPropertiesFile();
    }

    private static void deleteAllPropertiesFile() throws IOException {
    }

    private HotelTrain() throws Exception {
        this.web3jBc1 = Besu.build(new HttpService(BC1_URI), POLLING_INTERVAL);
        this.web3jBc2 = Besu.build(new HttpService(BC2_URI), POLLING_INTERVAL);
        this.web3jBc3 = Besu.build(new HttpService(BC3_URI), POLLING_INTERVAL);

        // Note that the multi-chain node is assumed to be configured.
        // If this is not the case, please use the Multichain Manager sample with the options "config auto".
        CrosschainCoordinationContractSetup coordinationContractSetup = new CrosschainCoordinationContractSetup(this.web3jBc1);

        this.agency = new EntityTravelAgency(this.web3jBc1, BC1_SIDECHAIN_ID, RETRY, POLLING_INTERVAL,
            this.web3jBc3, BC3_SIDECHAIN_ID, // hotel
            this.web3jBc2, BC2_SIDECHAIN_ID, // train
            coordinationContractSetup.getCrosschainCoordinationWeb3J(),
            coordinationContractSetup.getCrosschainCoordinationContractBlockcainId(),
            coordinationContractSetup.getCrosschainCoordinationContractAddress(),
            CROSSCHAIN_TRANSACTION_TIMEOUT);
        this.train = new EntityTrain(this.web3jBc2, BC2_SIDECHAIN_ID, RETRY, POLLING_INTERVAL,
            coordinationContractSetup.getCrosschainCoordinationWeb3J(),
            coordinationContractSetup.getCrosschainCoordinationContractBlockcainId(),
            coordinationContractSetup.getCrosschainCoordinationContractAddress(),
            CROSSCHAIN_TRANSACTION_TIMEOUT);
        this.hotel = new EntityHotel(this.web3jBc3, BC3_SIDECHAIN_ID, RETRY, POLLING_INTERVAL,
            coordinationContractSetup.getCrosschainCoordinationWeb3J(),
            coordinationContractSetup.getCrosschainCoordinationContractBlockcainId(),
            coordinationContractSetup.getCrosschainCoordinationContractAddress(),
            CROSSCHAIN_TRANSACTION_TIMEOUT);

    }

    private void run() throws Exception {
        boolean runOnce = false;
        Scanner myInput = new Scanner( System.in );
        while (true) {
            System.out.println("What do you want to do next?");
            System.out.println("0  Run entire sample.");
            System.out.println("1  Deploy all contracts.");
            System.out.println("2  Book hotel room and train seat.");
            System.out.println("3  Buy hotel tokens");
            System.out.println("4  Buy train tokens");
            System.out.println("5  Show information.");
            System.out.println("6  Show detailed information.");
            System.out.println("9  Quit.");

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
                    deploy();
                    buyHotelTokens(20);
                    buyTrainTokens(100);
                    int theDate = 3;
                    int anotherDate = 20;
                    show(theDate);
                    book(theDate); // will be committed
                    book(theDate); // will be ignored - not enough hotel tokens
                    buyHotelTokens(200);
                    book(theDate); // will be committed
                    book(theDate); // will be committed
                    book(theDate); // will be ignored - not enough train seats available
                    book(theDate); // will be committed
                    book(anotherDate); // will be committed
                    show(theDate);
                    show(anotherDate);
                    break;
                case 1:
                    deploy();
                    break;
                case 2:
                    System.out.println("What is the booking date? (0 to 365):");
                    int date = myInput.nextInt();
                    book(date);
                    break;
                case 3:
                    System.out.println("How many tokens do you want to buy?:");
                    int hotelTokens = myInput.nextInt();
                    buyHotelTokens(hotelTokens);
                    break;
                case 4:
                    System.out.println("How many tokens do you want to buy?:");
                    int trainTokens = myInput.nextInt();
                    buyTrainTokens(trainTokens);
                    break;
                case 5:
                    System.out.println("What date do you wish to check bookings for? (0 to 365):");
                    date = myInput.nextInt();
                    show(date);
                    break;
                case 6:
                    System.out.println("Showing detailed information:");
                    detail();
                    break;

                case 9:
                    return;
                default:
                    LOG.error("Unknown option {}", option);
                    break;
            }

            if (runOnce) {
                return;
            }
        }
    }

    private void deploy() throws Exception {
        LOG.info("Deploying All Contracts");
        this.hotel.deploy();
        this.train.deploy();

        String hotelContractAddress = this.hotel.getRouterContractAddress();
        String hotelErc20ContractAddress = this.hotel.getErc20ContractAddress();
        String trainContractAddress = this.train.getRouterContractAddress();
        String trainErc20ContractAddress = this.train.getErc20ContractAddress();

        this.agency.deploy(this.web3jBc2, BC2_SIDECHAIN_ID, trainContractAddress, trainErc20ContractAddress,
            this.web3jBc3, BC3_SIDECHAIN_ID, hotelContractAddress, hotelErc20ContractAddress);
    }

    private void book(int date) throws Exception {
        LOG.info("Book a room for date: {}", date);
        // TODO specify a room rates.
        this.agency.book(date);
    }

    private void show(int date) throws Exception {
        String travelAgencyAccount = this.agency.getTravelAgencyAccount();

        LOG.info("Hotel ERC20 Balances");
        this.hotel.showErc20Balances(new String[]{travelAgencyAccount});
        LOG.info("Train ERC20 Balances");
        this.train.showErc20Balances(new String[]{travelAgencyAccount});

        LOG.info("Checking reservations for date {}", date);
        this.agency.showBookingInformation(date);
    }

    private void detail() throws Exception {
        String travelAgencyAccount = this.agency.getTravelAgencyAccount();
        LOG.info("Hotel ERC20 Balances");
        this.hotel.showErc20Detail(new String[]{travelAgencyAccount});
        LOG.info("Train ERC20 Balances");
        this.train.showErc20Detail(new String[]{travelAgencyAccount});
    }

    private void buyHotelTokens(final int number) throws Exception {
        String travelAgencyAccount = this.agency.getTravelAgencyAccount();
        this.hotel.buyTokens(travelAgencyAccount, number);
    }
    private void buyTrainTokens(final int number) throws Exception {
        String travelAgencyAccount = this.agency.getTravelAgencyAccount();
        this.train.buyTokens(travelAgencyAccount, number);
    }

}
