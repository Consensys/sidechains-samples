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
import org.web3j.crypto.Credentials;
import tech.pegasys.samples.crosschain.multichain.config.ConfigControl;


/**
 * All options must inherit from this class.
 *
 */
public abstract class AbstractOption implements MultichainManagerOptions {
  private static final Logger LOG = LogManager.getLogger(AbstractOption.class);

  public static String QUIT = "q";

  protected Credentials credentials;


  protected AbstractOption() throws Exception {
    this.credentials = ConfigControl.getInstance().credentials();
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


}