package tech.pegasys.samples.crosschain.multichain.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.besu.response.crosschain.CoordinationContractInformation;
import tech.pegasys.samples.crosschain.multichain.config.ConfigControl;
import tech.pegasys.samples.sidechains.common.BlockchainInfo;
import tech.pegasys.samples.sidechains.common.CrosschainCoordinationContractInfo;

import java.math.BigInteger;
import java.util.Scanner;

public class OptionLinkedNodes extends AbstractOption {
  private static final Logger LOG = LogManager.getLogger(OptionLinkedNodes.class);

  private static final String COMMAND = "linked";
  private static final String ADD = "add";
  private static final String REMOVE = "remove";

  public OptionLinkedNodes() throws Exception {
    super();
  }

  public String getName() {
    return COMMAND;
  }
  public String getDescription() { return "Linked nodes that form the Multichain Node"; }

  public void interactive(Scanner myInput) throws Exception {
    boolean stayHere = true;
    while (stayHere) {
      printSubCommandIntro();
      printSubCommand(ADD, "Add a node to the Multichain Node configuration");
      printSubCommand(REMOVE, "Remove a node from the Multichain Node configuration");
      printSubCommand(QUIT, "Exit the " + getName() + " command menu");
      String subCommand = myInput.next();

      if (subCommand.equalsIgnoreCase(ADD)) {
        LOG.info("Add a node to the Multichain Node configuration");
        System.out.println(" Blockchain id (in hex, no leading 0x) of blockchain:");
        String blockchainId = myInput.next();

        System.out.println(" IP address and RPC port of blockchain node (for example 127.0.0.1:8110):");
        String ipAndPort = myInput.next();

        command(new String[]{
            ADD,
            blockchainId,
            ipAndPort,
        }, 0);
      }
      else if (subCommand.equalsIgnoreCase(REMOVE)) {
        LOG.info("Remove a node from the Multichain Node configuration");
        System.out.println(" Blockchain id (in hex) of blockchain:");
        String blockchainId = myInput.next();

        command(new String[]{
            REMOVE,
            blockchainId,
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
    if (subCommand.equalsIgnoreCase(ADD)) {
      add(args, argOffset+1);
    }
    else if (subCommand.equalsIgnoreCase(REMOVE)) {
      remove(args, argOffset+1);
    }
    else {
      printUnknownSubCommandMessage(subCommand);
    }
  }


  void add(String args[], final int argOffset) throws Exception {
    if (args.length != argOffset+2) {
      help();
      return;
    }
    String blockchainIdStr = args[argOffset];
    String ipAndPort = args[argOffset+1];

    BigInteger bcIdBigInt = new BigInteger(blockchainIdStr, 16);
    LOG.info(" Adding node at {} on blockchain 0x{} to multichain node.",
        ipAndPort,
        bcIdBigInt.toString(16));
    ConfigControl.getInstance().addLinkedNode(bcIdBigInt, ipAndPort);
    BlockchainInfo info = new BlockchainInfo(bcIdBigInt, ipAndPort);
    if (!info.isOnline()) {
      LOG.error(" Unable to add node as it is offline: blockchain 0x{} at {}",
          bcIdBigInt.toString(16),
          ipAndPort);
      return;
    }

    LOG.info(" Linking each existing node to node on blockchain 0x{}.",
        bcIdBigInt.toString(16));
    for (BlockchainInfo bc: ConfigControl.getInstance().linkedNodes().values()) {
      if (bc.blockchainId.equals(bcIdBigInt)) {
        // Don't link the new node to itself.
        continue;
      }
      Besu besu = bc.getWebService();
      besu.crossAddLinkedNode(bcIdBigInt, ipAndPort).send();
    }
    LOG.info(" Linking node on blockchain 0x{} to each existing node.",
        bcIdBigInt.toString(16));
    BlockchainInfo newBcInfo = new BlockchainInfo(bcIdBigInt, ipAndPort);
    Besu besu = newBcInfo.getWebService();
    for (BlockchainInfo bc: ConfigControl.getInstance().linkedNodes().values()) {
      if (bc.blockchainId.equals(bcIdBigInt)) {
        // Don't link the new node to itself.
        continue;
      }
      besu.crossAddLinkedNode(bc.blockchainId, bc.ipAddressAndPort).send();
    }
    LOG.info(" Adding trusted coordination contracts to node on blockchain 0x{}.",
        bcIdBigInt.toString(16));
    for (CrosschainCoordinationContractInfo coordInfo: ConfigControl.getInstance().coordContracts().values()) {
      besu.crossAddCoordinationContract(coordInfo.blockchainId, coordInfo.ipAddressAndPort, coordInfo.contractAddress);
    }

  }


  void remove(String args[], final int argOffset) throws Exception {
    if (args.length != argOffset+1) {
      help();
      return;
    }
    String blockchainIdStr = args[argOffset];

    BigInteger bcIdBigInt = new BigInteger(blockchainIdStr, 16);
    LOG.info(" Remove node on blockchain 0x{} from multichain node.",
        bcIdBigInt.toString(16));
    ConfigControl.getInstance().removeLinkedNode(bcIdBigInt);

    LOG.info(" Remove link from remaining nodes to node on blockchain 0x{}.",
        bcIdBigInt.toString(16));
    for (BlockchainInfo bc: ConfigControl.getInstance().linkedNodes().values()) {
      Besu besu = bc.getWebService();
      besu.crossRemoveLinkedNode(bcIdBigInt).send();
    }
  }
}
