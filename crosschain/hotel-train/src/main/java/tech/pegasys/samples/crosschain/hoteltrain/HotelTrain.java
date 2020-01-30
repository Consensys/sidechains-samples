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
            System.out.println("3  Show booking information.");

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
                    book();
                    break;
                case 1:
                    deploy();
                    break;
                case 2:
                    book();
                    break;
                case 3:
                    show();
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

    private void deploy() throws Exception {
        LOG.info("Deploying All Contracts");
        this.train.deploy();
        this.hotel.deploy();

        String trainContractAddress = this.train.getRouterContractAddress();
        String hotelContractAddress = this.hotel.getRouterContractAddress();

        this.agency.deploy(BC2_SIDECHAIN_ID, trainContractAddress, BC3_SIDECHAIN_ID, hotelContractAddress);
    }

    private void book() {
        LOG.info("Not implemented yet");
    }

    private void show() {
        LOG.info("Not implemented yet");
    }

}
