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
package tech.pegasys.samples.crosschain.multichain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.besu.crypto.crosschain.BlsThresholdCryptoSystem;
import org.web3j.protocol.besu.response.crosschain.LongResponse;
import org.web3j.protocol.core.methods.response.NetPeerCount;
import tech.pegasys.samples.sidechains.common.BlockchainInfo;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Scanner;

/**
 * Option "key"
 *
 */
public class OptionCoordination extends AbstractOption {
  private static final Logger LOG = LogManager.getLogger(OptionCoordination.class);

  private static final String COMMAND = "coordination";
  private static final String ADD = "add";
  private static final String REMOVE = "remove";
  private static final String DEPLOY = "deploy";

  public String getName() {
    return COMMAND;
  }
  public String getDescription() { return "Coordination contracts"; }

  public void interactive(Scanner myInput) throws IOException {
    boolean stayHere = true;
    while (stayHere) {
      printSubCommandIntro();
      printSubCommand(DEPLOY, "Deploy a new coordination contract & add as trusted to all blockchains");
      printSubCommand(ADD, "Add a deployed coordination contract & add as trusted to all blockchains");
      printSubCommand(REMOVE, "Remove a coordination contract");
      printSubCommand(QUIT, "Exit the " + getName() + " command menu");
      String subCommand = myInput.next();

      if (subCommand.equalsIgnoreCase(DEPLOY)) {
        LOG.info("Deploy a Crososchain Coordination Contract");
        System.out.println(" Blockchain id (in hex) of coordination blockchain:");
        String blockchainId = myInput.next();
        BigInteger bcIdBigInt = new BigInteger(blockchainId, 16);




//        command(new String[]{
//            DEPLOY,
//            blockchainId,
//            Integer.valueOf(threshold).toString(),
//            Integer.valueOf(algorithm).toString()
//        }, 0);
      }
      else if (subCommand.equalsIgnoreCase(ADD)) {
        LOG.info(" Not implemented yet");
        continue;
      }
      else if (subCommand.equalsIgnoreCase(REMOVE)) {
        LOG.info(" Not implemented yet");
        continue;
      }
      else if (subCommand.equalsIgnoreCase(QUIT)) {
        stayHere = false;
      }
      else {
        printUnknownSubCommandMessage(subCommand);
      }
    }

  }

  public void command(final String[] args, final int argOffset) throws IOException {
    if (args.length < argOffset+1) {
      help();
      return;
    }
    String subCommand = args[argOffset];
    if (subCommand.equalsIgnoreCase(DEPLOY)) {
      generate(args, argOffset+1);
    }
    else {
      printUnknownSubCommandMessage(subCommand);
    }
  }

  void generate(String args[], final int argOffset) throws IOException {
    LOG.info("Executing: {} {}", getName(), DEPLOY);
    if (args.length < argOffset+3) {
      help();
      return;
    }
    String blockchainIdStr = args[argOffset];
    String thresholdStr = args[argOffset+1];
    String algorithmStr = args[argOffset+2];

    BigInteger bcIdBigInt = new BigInteger(blockchainIdStr, 16);
    BlockchainInfo bcInfo = this.blockchains.get(bcIdBigInt);
    if (bcInfo == null) {
      LOG.error(" Blockchain {} not part of multichain node", bcIdBigInt);
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

    BlsThresholdCryptoSystem[] cryptoSystems = BlsThresholdCryptoSystem.values();
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

}