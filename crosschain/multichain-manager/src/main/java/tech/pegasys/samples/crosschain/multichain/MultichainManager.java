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
import org.web3j.protocol.besu.response.crosschain.CoordinationContractInformation;
import org.web3j.protocol.besu.response.crosschain.ListCoordinationContractsResponse;
import org.web3j.protocol.http.HttpService;
import tech.pegasys.samples.crosschain.multichain.commands.AbstractOption;
import tech.pegasys.samples.crosschain.multichain.commands.MultichainManagerOptions;
import tech.pegasys.samples.crosschain.multichain.commands.OptionConfig;
import tech.pegasys.samples.crosschain.multichain.commands.OptionCoordination;
import tech.pegasys.samples.crosschain.multichain.commands.OptionKey;
import tech.pegasys.samples.crosschain.multichain.commands.OptionLinkedNodes;
import tech.pegasys.samples.crosschain.multichain.commands.OptionShow;
import tech.pegasys.samples.crosschain.multichain.config.ConfigControl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Main class for Multichain Node Manager sample.
 *
 */
public class MultichainManager {
  private static final Logger LOG = LogManager.getLogger(MultichainManager.class);

  Map<String, MultichainManagerOptions> commands;

  public static void main(String[] args) throws Exception {
    LOG.info("Multichain Node Manager - started");
    new MultichainManager().run(args);
  }

  MultichainManager() throws Exception {
    this.commands = new TreeMap<>();
    OptionShow show = new OptionShow();
    this.commands.put(show.getName(), show);
    OptionKey key = new OptionKey();
    this.commands.put(key.getName(), key);
    OptionCoordination coordination = new OptionCoordination();
    this.commands.put(coordination.getName(), coordination);
    OptionConfig config = new OptionConfig();
    this.commands.put(config.getName(), config);
    OptionLinkedNodes linked = new OptionLinkedNodes();
    this.commands.put(linked.getName(), linked);
  }


  public static void automatedRun() throws Exception {
    // Delete all properties files as a starting point. This will ensure all contracts are redeployed.
    ConfigControl.wipeConfig();;
    // Run the samples in a way that does not require input from the keyboard.
    new MultichainManager().run(new String[]{OptionShow.COMMAND, OptionShow.ALL});
    // Clean-up.
    ConfigControl.wipeConfig();;
  }

  // Run the "config auto" set-up if there is no coordination contract set-up of the node
  // as 127.0.0.1:8110.
  public static void automatedSetup() throws Exception {
//    final String SC1_IP_PORT = "127.0.0.1:8110";
//    final String SC1_URI = "http://" + SC1_IP_PORT + "/";
//    final int POLLING_INTERVAL = 2000;
//    Besu blockchainNodeWeb3j = Besu.build(new HttpService(SC1_URI), POLLING_INTERVAL);
//    ListCoordinationContractsResponse resp = blockchainNodeWeb3j.crossListCoordinationContracts().send();
//    List<CoordinationContractInformation> info = resp.getInfo();
//    if (info.isEmpty()) {
      new MultichainManager().run(new String[]{OptionConfig.COMMAND, OptionConfig.AUTO});
//  }
    }




  private void run(final String[] args) throws Exception {
    if (args.length == 0) {
      interactive();
    } else {
      String cmd = args[0].toLowerCase();
      MultichainManagerOptions commandToExecute = this.commands.get(cmd);
      commandToExecute.command(args, 1);
    }
  }

  void interactive() throws Exception {
    LOG.info("Interactive mode");
    Scanner myInput = new Scanner( System.in );

    while (true) {
      LOG.info("Please type a command: ");
      for (String possibleCommand : this.commands.keySet()) {
        MultichainManagerOptions commandToExecute = this.commands.get(possibleCommand);
        String description = commandToExecute.getDescription();
        AbstractOption.printSubCommand(possibleCommand, description);
      }
      AbstractOption.printSubCommand(AbstractOption.QUIT, "exit");

      String cmd = myInput.next();
      if (cmd.equalsIgnoreCase(AbstractOption.QUIT)) {
        return;
      }
      MultichainManagerOptions commandToExecute = this.commands.get(cmd);
      if (commandToExecute == null) {
        LOG.error("Unknown command: " + cmd);
        continue;
      }
      commandToExecute.interactive(myInput);
    }
  }

}