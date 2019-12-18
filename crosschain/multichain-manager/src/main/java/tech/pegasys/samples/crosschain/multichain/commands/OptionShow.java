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
import org.web3j.protocol.besu.response.crosschain.BlockchainNodeInformation;
import org.web3j.protocol.besu.response.crosschain.CoordinationContractInformation;
import org.web3j.protocol.besu.response.crosschain.ListBlockchainNodesResponse;
import org.web3j.protocol.besu.response.crosschain.ListCoordinationContractsResponse;
import org.web3j.protocol.besu.response.crosschain.LongResponse;
import tech.pegasys.samples.crosschain.multichain.config.ConfigControl;
import tech.pegasys.samples.sidechains.common.BlockchainInfo;
import tech.pegasys.samples.sidechains.common.CrosschainCoordinationContractInfo;

import java.util.Collection;
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


  public OptionShow() throws Exception {
    super();
  }


  public String getName() {
    return COMMAND;
  }
  public String getDescription() { return "Display information"; }

  public void interactive(Scanner myInput) throws Exception {
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

  public void command(final String[] args, final int argOffset) throws Exception {
    printCommandLine(args, argOffset);
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

  void showAll() throws Exception {
    LOG.info("Coordination Contracts");
    Collection<CrosschainCoordinationContractInfo> configuredCoordContracts =
        ConfigControl.getInstance().coordContracts().values();
    if (configuredCoordContracts.isEmpty()) {
      LOG.info(" NONE");
    }
    for (CrosschainCoordinationContractInfo coordContractInfo: configuredCoordContracts) {
      if (coordContractInfo.isOnline()) {
        LOG.info(" Blockchain Id: 0x{}, {}, Contract Address: {}",
            coordContractInfo.blockchainId.toString(16),
            coordContractInfo.ipAddressAndPort,
            coordContractInfo.contractAddress);
      }
    }

    LOG.info("Blockchains");
    Collection<BlockchainInfo> configuredNodes = ConfigControl.getInstance().linkedNodes().values();
    if (configuredNodes.isEmpty()) {
      LOG.info(" NONE");
    }
    for (BlockchainInfo chain: configuredNodes) {
      if (chain.isOnline()) {
        LOG.info(" Blockchain Id: 0x{}, {}",
            chain.blockchainId.toString(16),
            chain.ipAddressAndPort);

        Besu webService = chain.getWebService();
        LongResponse activeKeyVersionResponse = webService.crossGetActiveKeyVersion().send();
        long activeKeyVersion = activeKeyVersionResponse.getValue();
        LOG.info("  Active Key Version: {}",
            ((activeKeyVersion == 0) ? "NONE" : activeKeyVersion));

        ListBlockchainNodesResponse nodes = webService.crossListLinkedNodes().send();
        List<BlockchainNodeInformation> bcNodeInfo = nodes.getNodes();
        LOG.info("  Blockchains this node is connected to:");
        if (bcNodeInfo.size() == 0) {
          LOG.info("   NONE");
        } else {
          for (BlockchainNodeInformation nodeInfo : bcNodeInfo) {
            LOG.info("   Blockchain id: 0x{}, IP and Port: {}",
                nodeInfo.blockchainId.toString(16),
                nodeInfo.ipAddressAndPort);
          }
        }

        ListCoordinationContractsResponse coordResp = webService.crossListCoordinationContracts().send();
        List<CoordinationContractInformation> coordInfo = coordResp.getInfo();
        LOG.info("  Coordination Contracts this node trusts:");
        if (coordInfo.size() == 0) {
          LOG.info("   NONE\n");
        } else {
          for (CoordinationContractInformation info: coordInfo) {
            LOG.info("   Blockchain id: 0x{}", info.coordinationBlockchainId.toString(16));
            LOG.info("   IP Address and Port: {}", info.ipAddressAndPort);
            LOG.info("   Contract Address: 0x{}", info.coodinationContract);
          }
        }
      }
    }
  }

}