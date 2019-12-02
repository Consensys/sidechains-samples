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

import com.fasterxml.jackson.databind.node.BigIntegerNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.besu.response.crosschain.ListCoordinationContractsResponse;
import org.web3j.protocol.besu.response.crosschain.ListNodesResponse;
import org.web3j.protocol.besu.response.crosschain.LongResponse;
import tech.pegasys.samples.sidechains.common.BlockchainInfo;
import tech.pegasys.samples.sidechains.common.DefaultBlockchainInfo;
import tech.pegasys.samples.sidechains.common.coordination.CrosschainCoordinationContractSetup;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Main class for Multichain Node Manager sample.
 *
 */
public class MultichainManager {
  private static final Logger LOG = LogManager.getLogger(MultichainManager.class);
  private static boolean automatedRun = false;


  // Information about a node on each of the coordination blockchains.
  Map<BigInteger, BlockchainInfo> coordinationBlockchains;

  // Information about a node on each of the blockchains that make up this Multichain Node.
  Map<BigInteger, BlockchainInfo> blockchains;



  public static void main(String[] args) throws Exception {
    LOG.info("Multichain Node Manager - started");
    new MultichainManager().run();
  }

  public static void automatedRun() throws Exception {
    // Delete all properties files as a starting point. This will ensure all contracts are redeployed.
    deleteAllPropertiesFile();

    // Run the samples in a way that does not require input from the keyboard.
    automatedRun = true;
    new MultichainManager().run();

    // Clean-up.
    deleteAllPropertiesFile();
  }

  private static void deleteAllPropertiesFile() throws IOException {
  }



  private void run() throws Exception {
    this.coordinationBlockchains = DefaultBlockchainInfo.getDefaultCoordinationBlockchains();
    this.blockchains = DefaultBlockchainInfo.getDefaultBlockchains();


    System.out.println(getMultichainNodeInformation());



  }

  String getMultichainNodeInformation() throws IOException {
    StringBuilder stb = new StringBuilder();
    stb.append("Coordination Blockchains\n");
    for (BlockchainInfo coordChains: this.coordinationBlockchains.values()) {
      stb.append(" Blockchain Id: 0x");
      stb.append(coordChains.blockchainId.toString(16));
      stb.append(", ");
      stb.append(coordChains.ipAddressAndPort);
      stb.append("\n");
    }

    stb.append("Blockchains\n");
    for (BlockchainInfo chains: this.blockchains.values()) {
      Besu webService = chains.getWebService();

      stb.append(" Blockchain Id: 0x");
      stb.append(chains.blockchainId.toString(16));
      stb.append(", ");
      stb.append(chains.ipAddressAndPort);
      stb.append("\n");

      LongResponse activeKeyVersionResponse = webService.crossGetActiveKeyVersion().send();
      long activeKeyVersion = activeKeyVersionResponse.getValue();
      stb.append("  Active Key Version: ");
      if (activeKeyVersion == 0) {
        stb.append("NONE");
      }
      else {
        stb.append(activeKeyVersion);
      }
      stb.append("\n");

      ListNodesResponse nodes = webService.crossListMultichainNodes().send();
      List<BigInteger> nodeBlockchainIds = nodes.getNodes();
      stb.append("  Blockchains this node is connected to: \n");
      if (nodeBlockchainIds.size() == 0) {
        stb.append("   NONE\n");
      }
      else {
        for (BigInteger bcId : nodeBlockchainIds) {
          stb.append("   Blockchain id: 0x");
          stb.append(bcId.toString(16));
          stb.append("\n");
        }
      }

      


//      ListCoordinationContractsResponse trustedCrossContracts = webService.crossListCoordinationContracts().send();
//      List<BigInteger> nodes = trustedCrossContracts.getNodes();







    }



    return stb.toString();
  }

}