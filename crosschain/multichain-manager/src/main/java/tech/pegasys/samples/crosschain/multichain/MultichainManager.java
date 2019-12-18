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
import org.web3j.crypto.Credentials;
import tech.pegasys.samples.sidechains.common.BlockchainInfo;
import tech.pegasys.samples.sidechains.common.DefaultBlockchainInfo;

import java.math.BigInteger;
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

  Map<BigInteger, BlockchainInfo> coordinationBlockchains;
  Map<BigInteger, BlockchainInfo> blockchains;





  public static void main(String[] args) throws Exception {
    LOG.info("Multichain Node Manager - started");
    new MultichainManager().run(args);
  }

  MultichainManager() {
    this.commands = new TreeMap<>();
    OptionShow show = new OptionShow();
    this.commands.put(show.getName(), show);
    OptionKey key = new OptionKey();
    this.commands.put(key.getName(), key);
    OptionCoordination coordination = new OptionCoordination();
    this.commands.put(coordination.getName(), coordination);
  }


  public static void automatedRun() throws Exception {
    // Delete all properties files as a starting point. This will ensure all contracts are redeployed.
    MultichainManagerProperties.deleteAllPropertiesFile();
    // Run the samples in a way that does not require input from the keyboard.
    new MultichainManager().run(new String[]{OptionShow.COMMAND, OptionShow.ALL});
    // Clean-up.
    MultichainManagerProperties.deleteAllPropertiesFile();
  }





  private void run(final String[] args) throws Exception {

    // Information about a node on each of the coordination blockchains.
    this.coordinationBlockchains
        = DefaultBlockchainInfo.getDefaultCoordinationBlockchains();

    // Information about a node on each of the blockchains that make up this Multichain Node.
    this.blockchains
        = DefaultBlockchainInfo.getDefaultBlockchains();

    if (args.length == 0) {
      interactive();
    } else {
      String cmd = args[0];
      for (String possibleCommand : this.commands.keySet()) {
        if (possibleCommand.equalsIgnoreCase(cmd)) {
          MultichainManagerOptions commandToExecute = this.commands.get(possibleCommand);
          commandToExecute.setMultichainInfo(blockchains, coordinationBlockchains);
          commandToExecute.command(args, 1);
        }
      }
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
      commandToExecute.setMultichainInfo(blockchains, coordinationBlockchains);
      commandToExecute.interactive(myInput);
    }
  }

}