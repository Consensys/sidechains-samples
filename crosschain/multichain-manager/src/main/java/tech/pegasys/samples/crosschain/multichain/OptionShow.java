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
import org.web3j.protocol.besu.response.crosschain.ListNodesResponse;
import org.web3j.protocol.besu.response.crosschain.LongResponse;
import tech.pegasys.samples.sidechains.common.BlockchainInfo;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;

/**
 * Option "show"
 *
 */
public class OptionShow extends AbstractOption {
  private static final Logger LOG = LogManager.getLogger(OptionShow.class);

  public static final String COMMAND = "show";
  public static final String ALL = "all";




  public String getName() {
    return COMMAND;
  }
  public String getDescription() { return "Display information"; }

  public void interactive(Scanner myInput) throws IOException {
    boolean stayHere = true;
    while (stayHere) {
      printSubCommandIntro();
      printSubCommand(ALL, "Print out all available information");
      printSubCommand(QUIT, "Exit the " + getName() + " command menu");
      String subCommand = myInput.next();
      if (subCommand.equalsIgnoreCase(ALL)) {
        command(new String[]{ALL}, 0);
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
    if (subCommand.equalsIgnoreCase(ALL)) {
      showAll();
    }
    else {
      printUnknownSubCommandMessage(subCommand);
    }
  }

  void showAll() throws IOException {
    LOG.info("Executing: {} {}", getName(), ALL);

    LOG.info("Coordination Blockchains");
    for (BlockchainInfo chain1: this.coordinationBlockchains.values()) {
      if (checkNetworkConnection(chain1)) {
        LOG.info(" Blockchain Id: 0x{}, {}",
            chain1.blockchainId.toString(16),
            chain1.ipAddressAndPort);
      }
    }

    LOG.info("Blockchains");
    for (BlockchainInfo chain: this.blockchains.values()) {
      if (checkNetworkConnection(chain)) {
        LOG.info(" Blockchain Id: 0x{}, {}",
            chain.blockchainId.toString(16),
            chain.ipAddressAndPort);

        Besu webService = chain.getWebService();
        LongResponse activeKeyVersionResponse = webService.crossGetActiveKeyVersion().send();
        long activeKeyVersion = activeKeyVersionResponse.getValue();
        LOG.info("  Active Key Version: {}",
            ((activeKeyVersion == 0) ? "NONE" : activeKeyVersion));

        ListNodesResponse nodes = webService.crossListMultichainNodes().send();
        List<BigInteger> nodeBlockchainIds = nodes.getNodes();
        LOG.info("  Blockchains this node is connected to:");
        if (nodeBlockchainIds.size() == 0) {
          LOG.info("   NONE\n");
        } else {
          for (BigInteger bcId : nodeBlockchainIds) {
            LOG.info("   Blockchain id: 0x{}", bcId.toString(16));
          }
        }
      }
    }

//      ListCoordinationContractsResponse trustedCrossContracts = webService.crossListCoordinationContracts().send();
//      List<BigInteger> nodes = trustedCrossContracts.getNodes();
  }

}