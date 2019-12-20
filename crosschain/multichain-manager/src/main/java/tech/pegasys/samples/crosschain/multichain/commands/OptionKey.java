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
import org.web3j.protocol.besu.crypto.crosschain.BlsThresholdCryptoSystem;
import org.web3j.protocol.besu.response.crosschain.CrossBlockchainPublicKeyResponse;
import org.web3j.protocol.besu.response.crosschain.LongResponse;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.NetPeerCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tech.pegasys.samples.crosschain.multichain.config.ConfigControl;
import tech.pegasys.samples.sidechains.common.BlockchainInfo;
import tech.pegasys.samples.sidechains.common.CrosschainCoordinationContractInfo;
import tech.pegasys.samples.sidechains.common.coordination.soliditywrappers.CrosschainCoordinationV1;
import tech.pegasys.samples.sidechains.common.coordination.soliditywrappers.VotingAlgMajorityWhoVoted;

import java.math.BigInteger;
import java.util.Map;
import java.util.Scanner;

/**
 * Option "key"
 *
 */
public class OptionKey extends AbstractOption {
  private static final Logger LOG = LogManager.getLogger(OptionKey.class);

  private static final String COMMAND = "key";
  private static final String GENERATE = "generate";
  private static final String FIRST = "first";


  public OptionKey() throws Exception {
    super();
  }


  public String getName() {
    return COMMAND;
  }

  public String getDescription() {
    return "Blockchain threshold keys";
  }

  public void interactive(Scanner myInput) throws Exception {
    boolean stayHere = true;
    while (stayHere) {
      printSubCommandIntro();
      printSubCommand(GENERATE, "Generate a new threshold key");
      printSubCommand(FIRST, "Get the latest key, add this blockchain to the coord contract, and add key");
      printSubCommand(QUIT, "Exit the " + getName() + " command menu");
      String subCommand = myInput.next();

      if (subCommand.equalsIgnoreCase(GENERATE)) {
        LOG.info("Generate a key, upload to coordination contracts, activate key");
        System.out.println(" Blockchain id (in hex) of blockchain to generate new Blockchain Threshold Key for:");
        String blockchainId = myInput.next();
        BigInteger bcIdBigInt = new BigInteger(blockchainId, 16);
        BlockchainInfo bcInfo = ConfigControl.getInstance().linkedNodes().get(bcIdBigInt);
        if (bcInfo == null) {
          LOG.info(" Blockchain {} not part of multichain node", bcIdBigInt);
          LOG.info(" Please try again");
          continue;
        }
        if (!bcInfo.isOnline()) {
          LOG.error("Unable to generate key as node is offline: blockchain 0x{} at {}",
              bcIdBigInt.toString(16),
              bcInfo.ipAddressAndPort);
          continue;
        }

        Besu webService = bcInfo.getWebService();
        NetPeerCount netPeerCount = webService.netPeerCount().send();
        BigInteger numberOfPeers = netPeerCount.getQuantity();
        int numPeers = numberOfPeers.intValue();
        int numNodesOnBlockchain = numPeers + 1;
        System.out.println(" Threshold to use for Blockchain Threshold Public Key (in decimal):");
        int threshold = myInput.nextInt();
        if (threshold > numNodesOnBlockchain) {
          LOG.info(" Threshold {} must be the same as or less than the number of nodes in the blockchain {}",
              threshold, numNodesOnBlockchain);
          LOG.info(" Please try again");
          continue;
        }

        System.out.println(" Crypto system (algorithm) to use:");
        BlsThresholdCryptoSystem[] cryptoSystems = BlsThresholdCryptoSystem.values();
        for (BlsThresholdCryptoSystem system : cryptoSystems) {
          System.out.println("  For " + system.name() + " choose " + system.value);
        }
        int algorithm = myInput.nextInt();
        try {
          BlsThresholdCryptoSystem.create(algorithm);
        } catch (RuntimeException ex) {
          LOG.info(" Unknown crypto system / algorithm");
          LOG.info(" Please try again");
          continue;
        }

        command(new String[]{
            GENERATE,
            blockchainId,
            Integer.valueOf(threshold).toString(),
            Integer.valueOf(algorithm).toString()
        }, 0);
      } else if (subCommand.equalsIgnoreCase(FIRST)) {
        LOG.info("Add a blockchain key to a coordination contract - assuming no existing keys");
        System.out.println(" Blockchain id (in hex, no leading 0x) of blockchain:");
        String blockchainId = myInput.next();
        System.out.println(" Key verison (in decimal):");
        String keyVersion = myInput.next();
        command(new String[]{
            FIRST,
            blockchainId,
            keyVersion,
        }, 0);

      } else if (subCommand.equalsIgnoreCase(QUIT)) {
        stayHere = false;
      } else {
        printUnknownSubCommandMessage(subCommand);
      }
    }

  }

  public void command(final String[] args, final int argOffset) throws Exception {
    printCommandLine(args, argOffset);
    if (args.length < argOffset + 1) {
      help();
      return;
    }
    String subCommand = args[argOffset];
    if (subCommand.equalsIgnoreCase(GENERATE)) {
      generate(args, argOffset + 1);
    } else if (subCommand.equalsIgnoreCase(FIRST)) {
      first(args, argOffset + 1);
    } else {
      printUnknownSubCommandMessage(subCommand);
    }
  }

  void generate(final String[] args, final int argOffset) throws Exception {
    if (args.length < argOffset + 3) {
      help();
      return;
    }
    String blockchainIdStr = args[argOffset];
    String thresholdStr = args[argOffset + 1];
    String algorithmStr = args[argOffset + 2];

    BigInteger bcIdBigInt = new BigInteger(blockchainIdStr, 16);
    BlockchainInfo bcInfo = ConfigControl.getInstance().linkedNodes().get(bcIdBigInt);
    if (bcInfo == null) {
      LOG.error(" Blockchain {} not part of multichain node", bcIdBigInt);
      return;
    }
    if (!bcInfo.isOnline()) {
      LOG.error("Unable to generate key as node is offline: blockchain 0x{} at {}",
          bcIdBigInt.toString(16),
          bcInfo.ipAddressAndPort);
      return;
    }

    Besu webService = bcInfo.getWebService();
    NetPeerCount netPeerCount = webService.netPeerCount().send();
    BigInteger numberOfPeers = netPeerCount.getQuantity();
    int numPeers = numberOfPeers.intValue();
    int numNodesOnBlockchain = numPeers + 1;
    int threshold = Integer.parseInt(thresholdStr);
    if (threshold > numNodesOnBlockchain) {
      LOG.error(" Threshold {} must be the same as or less than the number of nodes in the blockchain {}",
          threshold, numNodesOnBlockchain);
      return;
    }

    int algorithm = Integer.parseInt(algorithmStr);
    BlsThresholdCryptoSystem cryptoSystem;
    try {
      cryptoSystem = BlsThresholdCryptoSystem.create(algorithm);
    } catch (RuntimeException ex) {
      LOG.error(" Unknown crypto system / algorithm");
      return;
    }

    LOG.info("Generating key for: BlockchainId: {}, Threshold: {}, CryptoSystem: {}", blockchainIdStr, threshold, cryptoSystem);
    LongResponse keyVersionResponse = webService.crossStartThresholdKeyGeneration(threshold, cryptoSystem).send();
    long keyVersion = keyVersionResponse.getValue();
    LOG.info("  Generating key version: {}", keyVersion);


  }


  void first(final String[] args, final int argOffset) throws Exception {
    if (args.length < argOffset + 2) {
      help();
      return;
    }
    String blockchainIdStr = args[argOffset];
    String keyVersionStr = args[argOffset+1];

    BigInteger bcIdBigInt = new BigInteger(blockchainIdStr, 16);
    BlockchainInfo bcInfo = ConfigControl.getInstance().linkedNodes().get(bcIdBigInt);
    if (bcInfo == null) {
      LOG.error(" Blockchain {} not part of multichain node", bcIdBigInt);
      return;
    }
    if (!bcInfo.isOnline()) {
      LOG.error("Unable to execute upload first key process as node is offline: blockchain 0x{} at {}",
          bcIdBigInt.toString(16),
          bcInfo.ipAddressAndPort);
      return;
    }
    long keyVersion = Long.valueOf(keyVersionStr);

    Besu webService = bcInfo.getWebService();
    CrossBlockchainPublicKeyResponse resp = webService.crossGetBlockchainPublicKey(keyVersion).send();
    String encodedPublicKey = resp.getEncodedKey();
    LOG.info(" Fetched key: version {}, encoded value: {}",
        keyVersion,
        encodedPublicKey);


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


    // TODO check to see if the blockchain has already been added.
    Map<String, CrosschainCoordinationContractInfo> coordContracts = ConfigControl.getInstance().coordContracts();
    for (CrosschainCoordinationContractInfo coordContract: coordContracts.values()) {
      CrosschainCoordinationV1 coordinationContract =
          CrosschainCoordinationV1.load(coordContract.contractAddress, webService, tm, freeGasProvider);

      // TODO check this translation
      BigInteger val = new BigInteger(encodedPublicKey.substring(2), 16);
      byte[] encodedPubKeyBytes = val.toByteArray();

      TransactionReceipt txReceipt =
          coordinationContract.addBlockchain(bcIdBigInt, votingContractAddress, VOTING_PERIOD, BigInteger.valueOf(keyVersion), encodedPubKeyBytes).send();
      LOG.info("Tx Receipt: {}", txReceipt);

      boolean keyExists = coordinationContract.publicKeyExists(bcIdBigInt, BigInteger.valueOf(keyVersion)).send();
      LOG.info("Key exists in coordination contract: {}", keyExists);
    }




  }
}