package tech.pegasys.samples.crosschain.multichain.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.besu.response.crosschain.BlockchainNodeInformation;
import org.web3j.protocol.besu.response.crosschain.CoordinationContractInformation;
import org.web3j.protocol.besu.response.crosschain.ListBlockchainNodesResponse;
import org.web3j.protocol.besu.response.crosschain.ListCoordinationContractsResponse;
import tech.pegasys.samples.crosschain.multichain.MultichainManager;
import tech.pegasys.samples.crosschain.multichain.config.ConfigControl;
import tech.pegasys.samples.sidechains.common.BlockchainInfo;
import tech.pegasys.samples.sidechains.common.CrosschainCoordinationContractInfo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OptionConfig extends AbstractOption {
  private static final Logger LOG = LogManager.getLogger(OptionConfig.class);

  public static final String COMMAND = "config";
  private static final String VALIDATE = "validate";
  private static final String DERIVE = "derive";
  public static final String AUTO = "auto";

  public OptionConfig() throws Exception {
    super();
  }


  public String getName() {
    return COMMAND;
  }
  public String getDescription() { return "Configuration set-up"; }

  public void interactive(Scanner myInput) throws Exception {
    boolean stayHere = true;
    while (stayHere) {
      printSubCommandIntro();
      printSubCommand(VALIDATE, "Validate the Multichain Node configuration");
      printSubCommand(DERIVE, "Derive the Multichain Node configuration from a node");
      printSubCommand(AUTO, "Automatically configure the Multichain Node configuration for use with the sample code");
      printSubCommand(QUIT, "Exit the " + getName() + " command menu");
      String subCommand = myInput.next();

      if (subCommand.equalsIgnoreCase(VALIDATE)) {
        command(new String[]{
            VALIDATE,
        }, 0);
      }
      else if (subCommand.equalsIgnoreCase(DERIVE)) {
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
      else if (subCommand.equalsIgnoreCase(AUTO)) {
        command(new String[]{
            AUTO,
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
    else if (subCommand.equalsIgnoreCase(AUTO)) {
      autoConfigForSamples(args, argOffset+1);
    } else {
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


  public void autoConfigForSamples(String args[], final int argOffset) throws Exception {
    if (args.length != argOffset + 0) {
      help();
      return;
    }

    // Start by removing all coordination contracts and linked nodes.
    // Note that there will still be keys that have been generated.
    ConfigControl config = ConfigControl.getInstance();
    Map<String, CrosschainCoordinationContractInfo> coordContracts = config.coordContracts();
    class CoordInfo {
      BigInteger bcId;
      String addr;
      public CoordInfo(final BigInteger bcId, final String addr) {
        this.addr = addr;
        this.bcId = bcId;
      }
    }
    List<CoordInfo> coords = new ArrayList<>();
    for (CrosschainCoordinationContractInfo info: coordContracts.values()) {
      coords.add(new CoordInfo(info.blockchainId, info.contractAddress));
    }
    for (CoordInfo info: coords) {
      MultichainManager.main(new String[]{"coord", "remove", info.bcId.toString(16), info.addr});
    }

    Map<BigInteger, BlockchainInfo> linkedNodes = config.linkedNodes();
    List<BigInteger> bcIds = new ArrayList<>();
    for (BlockchainInfo info: linkedNodes.values()) {
      bcIds.add(info.blockchainId);
    }
    for (BigInteger bcId: bcIds) {
      MultichainManager.main(new String[]{"linked", "remove", bcId.toString(16)});
    }

    String bc1Id = "b";
    String bc2Id = "16";
    String bc3Id = "21";

    String bc1IpPort = "127.0.0.1:8110";
    String bc2IpPort = "127.0.0.1:8220";
    String bc3IpPort = "127.0.0.1:8330";

    // TODO: Check to see if the config is already what is expected!

    MultichainManager.main(new String[]{"linked", "add", bc1Id, bc1IpPort});
    MultichainManager.main(new String[]{"linked", "add", bc2Id, bc2IpPort});
    MultichainManager.main(new String[]{"linked", "add", bc3Id, bc3IpPort});

    MultichainManager.main(new String[]{"coord", "deploy", bc3Id, bc3IpPort});

    MultichainManager.main(new String[]{"key", "generate", bc1Id, "1", "1"});
    MultichainManager.main(new String[]{"key", "activate", bc1Id, "1"});

    MultichainManager.main(new String[]{"key", "generate", bc2Id, "1", "1"});
    MultichainManager.main(new String[]{"key", "activate", bc2Id, "1"});

    MultichainManager.main(new String[]{"key", "generate", bc3Id, "1", "1"});
    MultichainManager.main(new String[]{"key", "activate", bc3Id, "1"});

  }




}
