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
import tech.pegasys.samples.sidechains.common.coordination.CrosschainCoordinationContractSetup;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Main class for Multichain Node Manager sample.
 *
 */
public class MultichainManager {
  private static final Logger LOG = LogManager.getLogger(MultichainManager.class);
  private static boolean automatedRun = false;

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

  }

}