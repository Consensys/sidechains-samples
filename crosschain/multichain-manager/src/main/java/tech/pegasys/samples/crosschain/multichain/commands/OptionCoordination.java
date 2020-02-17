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
package tech.pegasys.samples.crosschain.multichain.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tech.pegasys.samples.crosschain.multichain.config.ConfigControl;
import tech.pegasys.samples.sidechains.common.BlockchainInfo;
import tech.pegasys.samples.sidechains.common.coordination.soliditywrappers.CrosschainCoordinationV1;
import tech.pegasys.samples.sidechains.common.coordination.soliditywrappers.VotingAlgMajorityWhoVoted;

import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;

/**
 * Option "key"
 *
 */
public class OptionCoordination extends AbstractOption {
  private static final Logger LOG = LogManager.getLogger(OptionCoordination.class);

  // Coordination Contract types of votes.
  public static final BigInteger VOTE_CHANGE_PUBLIC_KEY = BigInteger.valueOf(5);

  // Voting period used with the Coordination Contract.
  // This is in terms of blocks.
  public static final BigInteger VOTING_PERIOD = BigInteger.valueOf(10);
  // Note: This one block voting period is OK for the example when there is only one party voting, but
  // wouldn't work if there were multiple parties that need to recognise that a vote is occuring and then
  // cast a vote.
  public static final long SHORT_VOTING_PERIOD_LONG = 1;
  public static final BigInteger SHORT_VOTING_PERIOD = BigInteger.valueOf(SHORT_VOTING_PERIOD_LONG);
  // Amount of time to wait for votes to occur and to be able to be actioned.
  // If the block period isn't 2000ms, this will need to change.
  public static final long BLOCK_PERIOD = 2000;
  public static final long SHORT_VOTING_WAIT_TIME = SHORT_VOTING_PERIOD_LONG * BLOCK_PERIOD;

  private static final String COMMAND = "coord";
  private static final String ADD = "add";
  private static final String REMOVE = "remove";
  private static final String DEPLOY = "deploy";

  public OptionCoordination() throws Exception {
    super();
  }


  public String getName() {
    return COMMAND;
  }
  public String getDescription() { return "Coordination contracts"; }

  public void interactive(Scanner myInput) throws Exception {
    boolean stayHere = true;
    while (stayHere) {
      printSubCommandIntro();
      printSubCommand(DEPLOY, "Deploy a new coordination contract & add as trusted to all blockchains");
      printSubCommand(ADD, "Add a deployed coordination contract & add as trusted to all blockchains");
      printSubCommand(REMOVE, "Remove a coordination contract from the list of trusted coordination contracts for each node");
      printSubCommand(QUIT, "Exit the " + getName() + " command menu");
      String subCommand = myInput.next();

      if (subCommand.equalsIgnoreCase(DEPLOY)) {
        LOG.info("Deploy a Crososchain Coordination Contract");
        System.out.println(" Blockchain id (in hex) of coordination blockchain:");
        String blockchainId = myInput.next();

        System.out.println(" IP address and RPC port of blockchain node (for example 127.0.0.1:8110):");
        String ipAndPort = myInput.next();

        command(new String[]{
            DEPLOY,
            blockchainId,
            ipAndPort,
        }, 0);
      }
      else if (subCommand.equalsIgnoreCase(ADD)) {
        LOG.info("Add a Crososchain Coordination Contract");
        System.out.println(" Blockchain id (in hex, no leading 0x) of coordination blockchain:");
        String blockchainId = myInput.next();

        System.out.println(" IP address and RPC port of blockchain node (for example 127.0.0.1:8110):");
        String ipAndPort = myInput.next();

        System.out.println(" Coordination Contract Address (in hex, no leading 0x):");
        String address = myInput.next();
        // TODO validate

        command(new String[]{
            ADD,
            blockchainId,
            ipAndPort,
            address
        }, 0);
      }
      else if (subCommand.equalsIgnoreCase(REMOVE)) {
        LOG.info("Remove a Crososchain Coordination Contract");
        System.out.println(" Blockchain id (in hex) of coordination blockchain:");
        String blockchainId = myInput.next();
        BigInteger bcIdBigInt = new BigInteger(blockchainId, 16);

        System.out.println(" Coordination Contract Address:");
        String address = myInput.next();
        // TODO validate

        command(new String[]{
            REMOVE,
            blockchainId,
            address
        }, 0);
      }
      else if (subCommand.equalsIgnoreCase(QUIT)) {
        stayHere = false;
      }
      else {
        printUnknownSubCommandMessage(subCommand);
      }
    }

  }



  public void command(final String[] args, final int argOffset) throws Exception {
    printCommandLine(args, argOffset);
    if (args.length < argOffset+1) {
      help();
      return;
    }
    String subCommand = args[argOffset];
    if (subCommand.equalsIgnoreCase(DEPLOY)) {
      deploy(args, argOffset+1);
    }
    else if (subCommand.equalsIgnoreCase(ADD)) {
      add(args, argOffset+1);
    }
    else if (subCommand.equalsIgnoreCase(REMOVE)) {
      remove(args, argOffset+1);
    }
    else {
      printUnknownSubCommandMessage(subCommand);
    }
  }

  void deploy(String args[], final int argOffset) throws Exception {
    if (args.length != argOffset+2) {
      help();
      return;
    }
    String blockchainIdStr = args[argOffset];
    String ipAndPort = args[argOffset+1];

    BigInteger bcIdBigInt = new BigInteger(blockchainIdStr, 16);
    BlockchainInfo bcInfo = new BlockchainInfo(bcIdBigInt, ipAndPort);
    if (!bcInfo.isOnline()) {
      LOG.error(" Unable to deploy coordination contract as unable to connect to node at {} for blockchain 0x{}",
          ipAndPort,
          bcIdBigInt.toString(16));
      return;
    }
    Besu webService = bcInfo.getWebService();
    TransactionManager tm = bcInfo.getTransactionManager(this.credentials);


    // TODO: Need to define gas provider to use
    ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);

    // TODO: Need to ask what voting algorithm should be used.
    RemoteCall<VotingAlgMajorityWhoVoted> remoteCallVotingContract =
        VotingAlgMajorityWhoVoted.deploy(webService, tm, freeGasProvider);
    VotingAlgMajorityWhoVoted votingContract = remoteCallVotingContract.send();
    String votingContractAddress = votingContract.getContractAddress();
    LOG.info("  Voting Contract deployed on blockchain (id={}), at address: {}",
        bcIdBigInt.toString(16), votingContractAddress);
    LOG.info("  Voting contract appears to be validly deployed: {}", votingContract.isValid());

    RemoteCall<CrosschainCoordinationV1> remoteCallCoordinationContract =
        CrosschainCoordinationV1.deploy(webService, tm, freeGasProvider, votingContractAddress, SHORT_VOTING_PERIOD);
    CrosschainCoordinationV1 coordinationContract = remoteCallCoordinationContract.send();
    String crosschainCoordinationContractAddress = coordinationContract.getContractAddress();
    LOG.info("  Crosschain Coordination Contract deployed on blockchain (id={}), at address: {}",
        bcIdBigInt, crosschainCoordinationContractAddress);
    ConfigControl.getInstance().addCoordContract(bcIdBigInt, ipAndPort, crosschainCoordinationContractAddress);
    LOG.info("  Coord appears to be validly deployed: {}", coordinationContract.isValid());

    // Add coordination contract to the nodes.
    for (BlockchainInfo bc: ConfigControl.getInstance().linkedNodes().values()) {
      Besu besu = bc.getWebService();
      besu.crossAddCoordinationContract(bcIdBigInt, crosschainCoordinationContractAddress, ipAndPort).send();
    }

    // Add each blockchain to coordination contract.
    LOG.info(" Add each blockchain to the coordination contract");
    for (BlockchainInfo bc: ConfigControl.getInstance().linkedNodes().values()) {
      LOG.info("  adding blockchain {} with voting contract: {}", bc.blockchainId, votingContract.getContractAddress());
      TransactionReceipt receipt = coordinationContract.addBlockchain(bc.blockchainId, votingContract.getContractAddress(), SHORT_VOTING_PERIOD).send();
      LOG.info("   tx receipt: {}", receipt.toString());
      boolean registered = coordinationContract.getBlockchainExists(bc.blockchainId).send();
      LOG.info("  Blockchain {} now registered with coordination contract: {}", bc.blockchainId, registered);
    }
  }


  void add(String args[], final int argOffset) throws Exception {
    if (args.length != argOffset+3) {
      help();
      return;
    }
    String blockchainIdStr = args[argOffset];
    String ipAndPort = args[argOffset+1];
    String address = args[argOffset+2];

    BigInteger bcIdBigInt = new BigInteger(blockchainIdStr, 16);
    BlockchainInfo coordBcInfo = new BlockchainInfo(bcIdBigInt, ipAndPort);
    if (!coordBcInfo.isOnline()) {
      LOG.error("Unable to add coordination contract as node is offline: blockchain 0x{} at {}",
          coordBcInfo.blockchainId.toString(16),
          coordBcInfo.ipAddressAndPort);
      return;
    }
    ConfigControl.getInstance().addCoordContract(bcIdBigInt, ipAndPort, address);

    LOG.info(" Configuring Coordination Contract as trusted on each node.");
    for (BlockchainInfo bc: ConfigControl.getInstance().linkedNodes().values()) {
      if (!bc.isOnline()) {
        LOG.error("Unable to add coordination contract to node as node is offline: blockchain 0x{} at {}",
            bc.blockchainId.toString(16),
            bc.ipAddressAndPort);
        continue;
      }
      Besu besu = bc.getWebService();
      besu.crossAddCoordinationContract(bcIdBigInt, address, ipAndPort).send();
    }
  }


  // Remove a coordination contract as trusted from nodes. Do this even if it isn't listed
  // in the local configuration.
  void remove(String args[], final int argOffset) throws Exception {
    if (args.length != argOffset+2) {
      help();
      return;
    }
    String blockchainIdStr = args[argOffset];
    String address = args[argOffset+1];

    BigInteger bcIdBigInt = new BigInteger(blockchainIdStr, 16);
    ConfigControl.getInstance().removeCoordContract(bcIdBigInt, address);

    LOG.info(" Instructing all blockchain nodes to no longer trust: {}, {}", bcIdBigInt.toString(16), address);
    for (BlockchainInfo bc: ConfigControl.getInstance().linkedNodes().values()) {
      if (!bc.isOnline()) {
        LOG.error("Unable to remove coordination contract from node as node is offline: blockchain 0x{} at {}",
            bc.blockchainId.toString(16),
            bc.ipAddressAndPort);
        continue;
      }
      Besu besu = bc.getWebService();
      besu.crossRemoveCoordinationContract(bcIdBigInt, address).send();
    }
  }
}