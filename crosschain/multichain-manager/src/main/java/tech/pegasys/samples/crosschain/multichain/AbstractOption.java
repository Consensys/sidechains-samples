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

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;


/**
 * All options must inherit from this class.
 *
 */
public abstract class AbstractOption implements MultichainManagerOptions {
  private static final Logger LOG = LogManager.getLogger(AbstractOption.class);

  protected static String QUIT = "q";

  // Information about a node on each of the coordination blockchains.
  // TODO this is not a good model for coordination contracts: there can be more than one per blockchain.
  // TODO: If there are multiple coordination contracts on a blockchain, how do you know when you can remove a coordination blockchain from the map?
  Map<BigInteger, BlockchainInfo> coordinationBlockchains;

  // Information about a node on each of the blockchains that make up this Multichain Node.
  Map<BigInteger, BlockchainInfo> multichainBlockchains;

  protected Credentials credentials = new MultichainManagerProperties().credentials;

  public void setMultichainInfo(Map<BigInteger, BlockchainInfo> blockchains, Map<BigInteger, BlockchainInfo> coordinationBlockchains) {
    this.multichainBlockchains = blockchains;
    this.coordinationBlockchains = coordinationBlockchains;
  }


  public void help() {
    LOG.info(getName() + " command not yet documented.");
  }

  protected void printSubCommandIntro() {
    System.out.println("Enter option: ");
  }
  public static void printSubCommand(final String name, final String description) {
    LOG.info(String.format(" %6s  %s", name, description));
  }
  protected void printUnknownSubCommandMessage(String enteredCommand) {
    LOG.info("Unknown option: " + enteredCommand);
  }

  protected void printCommandLine(String[] args, int offset) {
    StringBuilder builder = new StringBuilder();
    builder.append("Executing: ");
    if (offset == 0) {
      builder.append(getName());
      builder.append(" ");
    }
    for (int i=0; i<args.length; i++) {
      builder.append(args[i]);
      builder.append(" ");
    }
    LOG.info(builder.toString());
  }

  protected boolean checkNetworkConnection(BlockchainInfo chain) {
    Socket socket = new Socket();
    try {
      InetAddress ip = InetAddress.getByName(chain.getIp());
      SocketAddress addr = new InetSocketAddress(ip, chain.getPort());
      socket.connect(addr);
    } catch (Exception ex) {
      LOG.error(
          "Error connecting with blockchainId {} ({}:{}): {}",
          chain.blockchainId.toString(16),
          chain.getIp(),
          chain.getPort(),
          ex.toString());
      return false;
    }
    return true;
  }

}