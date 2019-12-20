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
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tech.pegasys.samples.crosschain.multichain.config.ConfigControl;
import tech.pegasys.samples.sidechains.common.BlockchainInfo;
import tech.pegasys.samples.sidechains.common.coordination.soliditywrappers.CrosschainCoordinationV1;
import tech.pegasys.samples.sidechains.common.coordination.soliditywrappers.VotingAlgMajorityWhoVoted;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * Option "key"
 *
 */
public class OptionCoordination extends AbstractOption {
  private static final Logger LOG = LogManager.getLogger(OptionCoordination.class);

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

    // TODO specify voting period
    BigInteger VOTING_PERIOD = BigInteger.valueOf(10);

    RemoteCall<CrosschainCoordinationV1> remoteCallCoordinationContract =
        CrosschainCoordinationV1.deploy(webService, tm, freeGasProvider, votingContractAddress, VOTING_PERIOD);
    CrosschainCoordinationV1 coordinationContract = remoteCallCoordinationContract.send();
    String crosschainCoordinationContractAddress = coordinationContract.getContractAddress();
    LOG.info("  Crosschain Coordination Contract deployed on blockchain (id={}), at address: {}",
        bcIdBigInt, crosschainCoordinationContractAddress);
    ConfigControl.getInstance().addCoordContract(bcIdBigInt, ipAndPort, crosschainCoordinationContractAddress);

    //TODO Add to the nodes.
    for (BlockchainInfo bc: ConfigControl.getInstance().linkedNodes().values()) {
      Besu besu = bc.getWebService();
      besu.crossAddCoordinationContract(bcIdBigInt, crosschainCoordinationContractAddress, ipAndPort).send();
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