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
  private static final String ACTIVATE = "activate";


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
      printSubCommand(ACTIVATE, "Activate key: upload to coordination contract and activate");
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
      } else if (subCommand.equalsIgnoreCase(ACTIVATE)) {
        LOG.info("Add a blockchain key to a coordination contract - assuming no existing keys");
        System.out.println(" Blockchain id (in hex, no leading 0x) of blockchain:");
        String blockchainId = myInput.next();
        System.out.println(" Key verison (in decimal):");
        String keyVersion = myInput.next();
        command(new String[]{
            ACTIVATE,
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
    } else if (subCommand.equalsIgnoreCase(ACTIVATE)) {
      activate(args, argOffset + 1);
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


  void activate(final String[] args, final int argOffset) throws Exception {
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
    long keyVersion = Long.parseLong(keyVersionStr);

    Map<String, CrosschainCoordinationContractInfo> coordContracts = ConfigControl.getInstance().coordContracts();
    if (coordContracts.size() == 0) {
      LOG.error("No crosschain coordination contracts configured.");
      return;
    }

    Besu webService = bcInfo.getWebService();
    CrossBlockchainPublicKeyResponse resp = webService.crossGetBlockchainPublicKey(keyVersion).send();
    String encodedPublicKey = resp.getEncodedKey();
    LOG.info(" Fetched key: version {}, encoded value: {}",
        keyVersion,
        encodedPublicKey);
    byte[] encodedPubKeyBytes = stringToBytes(encodedPublicKey);

    // TODO check to see if the blockchain has already been added.
    for (CrosschainCoordinationContractInfo coordContract: coordContracts.values()) {
      LOG.info("  Add key for blockchain {} to Coordination Contract {}, {}",
          bcIdBigInt,
          coordContract.blockchainId,
          coordContract.contractAddress);

      // TODO: Need to define gas provider to use for this coordination contract.
      ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);
      Besu coordWebService = coordContract.getWebService();
      TransactionManager coordTm = coordContract.getTransactionManager(this.credentials);
      CrosschainCoordinationV1 coordinationContract =
          CrosschainCoordinationV1.load(coordContract.contractAddress, coordWebService, coordTm, freeGasProvider);

      boolean exists = coordinationContract.getBlockchainExists(bcIdBigInt).send();
      LOG.info(" Blockchain {} has been added to coordination contract: {}", bcIdBigInt, exists);

      LOG.info(" Propose vote to add key");
      TransactionReceipt receipt = coordinationContract.proposeVote(
          bcIdBigInt,
          OptionCoordination.VOTE_CHANGE_PUBLIC_KEY,
          BigInteger.ZERO,
          BigInteger.valueOf(keyVersion),
          encodedPubKeyBytes).send();
      LOG.info(" TX Receipt: {}", receipt);

      // Sleep for voting period
      LOG.info("Waiting for end of voting period");
      Thread.sleep(OptionCoordination.SHORT_VOTING_WAIT_TIME);

      LOG.info(" Action vote to add key");
      receipt = coordinationContract.actionVotes(bcIdBigInt, BigInteger.ZERO).send();
      LOG.info(" TX Receipt: {}", receipt);

      LOG.info("Waiting for block to be mined");
      Thread.sleep(OptionCoordination.BLOCK_PERIOD);

      boolean keyExists = coordinationContract.publicKeyExists(bcIdBigInt, BigInteger.valueOf(keyVersion)).send();
      if (keyExists) {
        LOG.info("Key successfully added to coordination contract");
      }
      else {
        LOG.error("FAILED to add key to contract");
        return;
      }
    }

    // Activate the key.
    webService.crossActivateKey(keyVersion).send();

    LongResponse activeKeyVersion = webService.crossGetActiveKeyVersion().send();
    LOG.info("Key version now active: {}", activeKeyVersion.getValue());
  }


  public static byte[] stringToBytes(String str1) {
    System.out.println(str1);
    String str = str1.substring(2);
    byte[] out = new byte[(str.length()) / 2];
    for (int i = 0; i < out.length; i++) {
      String s = str.substring(i * 2, i * 2 + 2);
      byte b = (byte) (Integer.decode("0x" + s) & 0xff);
      out[i] = b;
    }
    return out;
  }
}