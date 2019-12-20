package tech.pegasys.samples.crosschain.multichain.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.besu.response.crosschain.BlockchainNodeInformation;
import org.web3j.protocol.besu.response.crosschain.CoordinationContractInformation;
import org.web3j.protocol.besu.response.crosschain.ListBlockchainNodesResponse;
import org.web3j.protocol.besu.response.crosschain.ListCoordinationContractsResponse;
import tech.pegasys.samples.crosschain.multichain.config.ConfigControl;
import tech.pegasys.samples.sidechains.common.BlockchainInfo;

import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;

public class OptionConfig extends AbstractOption {
  private static final Logger LOG = LogManager.getLogger(OptionConfig.class);

  private static final String COMMAND = "config";
  private static final String VALIDATE = "validate";
  private static final String DERIVE = "derive";

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
      printSubCommand(DERIVE, "Derive the Multichain Node configuration from a node");
      printSubCommand(QUIT, "Exit the " + getName() + " command menu");
      String subCommand = myInput.next();

      if (subCommand.equalsIgnoreCase(VALIDATE)) {
        command(new String[]{
            VALIDATE,
        }, 0);
      }
      if (subCommand.equalsIgnoreCase(DERIVE)) {
        LOG.info("Defive the Multichain Node configuration from a node");
        System.out.println(" Blockchain id (in hex, no leading 0x) of blockchain:");
        String blockchainId = myInput.next();
        System.out.println(" IP address and RPC port of blockchain node (for example 127.0.0.1:8110):");
        String ipAndPort = myInput.next();

        command(new String[]{
            DERIVE,
            blockchainId,
            ipAndPort,
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
    else if (subCommand.equalsIgnoreCase(DERIVE)) {
      deriveConfig(args, argOffset+1);
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

  public void deriveConfig(String args[], final int argOffset) throws Exception {
    if (args.length != argOffset + 2) {
      help();
      return;
    }

    // TODO The blockchain ID can be fetched from the node, once the ethChainId method is implemented in Web3J.
    String blockchainIdStr = args[argOffset];
    String ipAndPort = args[argOffset+1];

    BigInteger bcIdBigInt = new BigInteger(blockchainIdStr, 16);
    BlockchainInfo info = new BlockchainInfo(bcIdBigInt, ipAndPort);
    if (!info.isOnline()) {
      LOG.error(" Unable to derive configuration from node as it is offline: {}",
          ipAndPort);
      return;
    }

    LOG.info(" Loading configuration from node: blockchain 0x{} at {}",
        bcIdBigInt.toString(16),
        ipAndPort);
    ConfigControl config = ConfigControl.getInstance();
    config.removeAllCoordContracts();
    config.removeAllLinkedNodes();

    config.addLinkedNode(bcIdBigInt, ipAndPort);
    Besu webService = info.getWebService();
    ListBlockchainNodesResponse nodes = webService.crossListLinkedNodes().send();
    List<BlockchainNodeInformation> nodeBlockchainIds = nodes.getNodes();
    for (BlockchainNodeInformation bcNodeinfo: nodeBlockchainIds) {
      config.addLinkedNode(bcNodeinfo.blockchainId, bcNodeinfo.ipAddressAndPort);
    }

    ListCoordinationContractsResponse response = webService.crossListCoordinationContracts().send();
    List<CoordinationContractInformation> infos = response.getInfo();
    for (CoordinationContractInformation cCInfo: infos) {
      config.addCoordContract(cCInfo.coordinationBlockchainId, cCInfo.ipAddressAndPort, cCInfo.coodinationContract);
    }

    LOG.info("Configuration loaded");

    boolean valid = ConfigControl.getInstance().validateConfig();
    LOG.info("Configuration valid: {}", valid);
  }

}
