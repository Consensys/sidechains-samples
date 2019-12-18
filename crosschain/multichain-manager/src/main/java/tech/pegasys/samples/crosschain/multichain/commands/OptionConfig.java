package tech.pegasys.samples.crosschain.multichain.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.pegasys.samples.crosschain.multichain.config.ConfigControl;

import java.util.Scanner;

public class OptionConfig extends AbstractOption {
  private static final Logger LOG = LogManager.getLogger(OptionConfig.class);

  private static final String COMMAND = "config";
  private static final String VALIDATE = "validate";

  public OptionConfig() throws Exception {
    super();
  }


  public String getName() {
    return COMMAND;
  }
  public String getDescription() { return "Blockchain threshold keys"; }

  public void interactive(Scanner myInput) throws Exception {
    boolean stayHere = true;
    while (stayHere) {
      printSubCommandIntro();
      printSubCommand(VALIDATE, "Validate the Multichain Node configuration");
      printSubCommand(QUIT, "Exit the " + getName() + " command menu");
      String subCommand = myInput.next();

      if (subCommand.equalsIgnoreCase(VALIDATE)) {
        command(new String[]{
            VALIDATE,
        }, 0);
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
    String subCommand = args[argOffset];
    if (subCommand.equalsIgnoreCase(VALIDATE)) {
      validateConfig(args, argOffset+1);
    }
    else {
      printUnknownSubCommandMessage(subCommand);
    }
  }


  public void validateConfig(String args[], final int argOffset) throws Exception {
    if (args.length != argOffset) {
      help();
      return;
    }
    boolean valid = ConfigControl.getInstance().validateConfig();
    LOG.info("Configuration valid: {}", valid);
  }
}
