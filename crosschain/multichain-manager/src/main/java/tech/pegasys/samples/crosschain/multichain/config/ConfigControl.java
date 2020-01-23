package tech.pegasys.samples.crosschain.multichain.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.besu.response.crosschain.BlockchainNodeInformation;
import org.web3j.protocol.besu.response.crosschain.CoordinationContractInformation;
import org.web3j.protocol.besu.response.crosschain.ListBlockchainNodesResponse;
import org.web3j.protocol.besu.response.crosschain.ListCoordinationContractsResponse;
import tech.pegasys.samples.sidechains.common.BlockchainInfo;
import tech.pegasys.samples.sidechains.common.CrosschainCoordinationContractInfo;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ConfigControl {
  private static final Logger LOG = LogManager.getLogger(ConfigControl.class);

  // Information about a node on each of the blockchains that make up this Multichain Node.
  private Map<BigInteger, BlockchainInfo> linkedNodes;

  private Map<String, CrosschainCoordinationContractInfo> coordinationContracts;

  private boolean validConfig;

  private Credentials credentials;

  private static ConfigControl instance;

  public static ConfigControl getInstance() throws Exception {
    synchronized (ConfigControl.class) {
      if (instance == null) {
        instance = new ConfigControl();
      }
      return instance;
    }
  }

  public static void wipeConfig() throws Exception {
    MultichainManagerProperties.deleteAllPropertiesFile();
    instance = new ConfigControl();
  }

  private ConfigControl() throws Exception {
    MultichainManagerProperties props = new MultichainManagerProperties();
    props.loadStoreProperties();
    this.linkedNodes = props.bcInfos;
    this.credentials = props.credentials;
    this.validConfig = validateConfig();
    this.coordinationContracts = new TreeMap<>();
  }


  public Map<BigInteger, BlockchainInfo> linkedNodes() {
    return this.linkedNodes;
  }
  public Map<String, CrosschainCoordinationContractInfo> coordContracts() {
    return this.coordinationContracts;
  }
  public Credentials credentials() {
    return this.credentials;
  }
  public boolean isValidConfig() {
    return this.validConfig;
  }

  public void addCoordContract(final BigInteger bcId, final String ipAndPort, final String address) {
    final String mapKey = calcCoordMapKey(bcId, address);
    this.coordinationContracts.put(mapKey, new CrosschainCoordinationContractInfo(bcId, ipAndPort, address));
    MultichainManagerProperties props = new MultichainManagerProperties();
    props.store(this.credentials.getEcKeyPair().getPrivateKey().toString(16), this.linkedNodes, this.coordinationContracts);
  }

  public void removeCoordContract(final BigInteger bcId, final String address) {
    final String mapKey = calcCoordMapKey(bcId, address);
    this.coordinationContracts.remove(mapKey);
    MultichainManagerProperties props = new MultichainManagerProperties();
    props.store(this.credentials.getEcKeyPair().getPrivateKey().toString(16), this.linkedNodes, this.coordinationContracts);
  }
  public void removeAllCoordContracts() {
    this.coordinationContracts = new TreeMap<>();
    MultichainManagerProperties props = new MultichainManagerProperties();
    props.store(this.credentials.getEcKeyPair().getPrivateKey().toString(16), this.linkedNodes, this.coordinationContracts);
  }

  public void addLinkedNode(final BigInteger bcId, final String ipAndPort) {
    this.linkedNodes.put(bcId, new BlockchainInfo(bcId, ipAndPort));
    MultichainManagerProperties props = new MultichainManagerProperties();
    props.store(this.credentials.getEcKeyPair().getPrivateKey().toString(16), this.linkedNodes, this.coordinationContracts);
  }

  public void removeLinkedNode(final BigInteger bcId) {
    this.linkedNodes.remove(bcId);
    MultichainManagerProperties props = new MultichainManagerProperties();
    props.store(this.credentials.getEcKeyPair().getPrivateKey().toString(16), this.linkedNodes, this.coordinationContracts);
  }

  public void removeAllLinkedNodes() {
    this.linkedNodes = new TreeMap<>();
    MultichainManagerProperties props = new MultichainManagerProperties();
    props.store(this.credentials.getEcKeyPair().getPrivateKey().toString(16), this.linkedNodes, this.coordinationContracts);

  }

    /**
     * Check that all nodes described in the configuration:
     * - Are contactable.
     * - Are interlinked.
     * - All trust the same Crosschain Coordination Contracts.
     * - Aren't linked to nodes that aren't in the configuration.
     */
  public boolean validateConfig() throws Exception {
    boolean validConfig = true;


    Set<BigInteger> configBlockchainIds = this.linkedNodes.keySet();
    // For each of the nodes in the configuration.
    for (BlockchainInfo configNodeInfo: this.linkedNodes.values()) {
      if (!configNodeInfo.isOnline()) {
        validConfig = false;
        continue;
      }

      Besu webService = configNodeInfo.getWebService();

      ListBlockchainNodesResponse nodes = webService.crossListLinkedNodes().send();
      List<BlockchainNodeInformation> nodeBlockchainIds = nodes.getNodes();
      // For each of the blockchains that the nodes in the configuration are on.
      for (BigInteger bcId : configBlockchainIds) {
        // Only do checks about configuration that a node is connected to: Don't check that
        // the node is connected to itself.
        if (!configNodeInfo.blockchainId.equals(bcId)) {
          boolean found = false;
          // For each of the nodes that this node is connected to.
          for (BlockchainNodeInformation bcNodeInfo : nodeBlockchainIds) {
            // If the blockchain of the node that the node is connected to is the same as
            // the blockchain in the configuration, check to ensure the configuration matches.
            if (bcNodeInfo.blockchainId.equals(bcId)) {
              found = true;

              // Check that the IP address and port configuration is the same between the configuration and
              // the nodes.
              if (!bcNodeInfo.ipAddressAndPort.equals(this.linkedNodes.get(bcId).ipAddressAndPort)) {
                validConfig = false;
                LOG.error("Node at {} on blockchain 0x{} is linked to a node on blockchain 0x{}, but with the incorrect IP and Port. Is {}, Should be {}",
                    configNodeInfo.ipAddressAndPort,
                    configNodeInfo.blockchainId.toString(16),
                    bcId.toString(16),
                    bcNodeInfo.ipAddressAndPort,
                    this.linkedNodes.get(bcId).ipAddressAndPort);
              }
              break;
            }
          }
          if (!found) {
            validConfig = false;
            LOG.error("Node at {} on blockchain 0x{} is not linked to a node on blockchain 0x{}",
                configNodeInfo.ipAddressAndPort,
                configNodeInfo.blockchainId.toString(16),
                bcId.toString(16));
          }
        }
      }

      // Check to see if there are nodes linked to on the node that aren't in the configuration.
      // This check can only be done simply by checking length if the node
      // doesn't contain links that aren't in the configuration.
      int diff = nodeBlockchainIds.size() +1 - this.linkedNodes.size();
      if (validConfig && diff != 0) {
        validConfig = false;
        LOG.error("Node at {} on blockchain 0x{} is linked to  {} additional node(s)",
            configNodeInfo.ipAddressAndPort,
            configNodeInfo.blockchainId.toString(16),
            diff);
      }

      // If there isn't any coordination contracts set-up, then just use them. Otherwise,
      // compare what is returned by the node to what we have.
      if (this.coordinationContracts == null) {
        this.coordinationContracts = new TreeMap<>();
        ListCoordinationContractsResponse response = webService.crossListCoordinationContracts().send();
        List<CoordinationContractInformation> infos = response.getInfo();
        for (CoordinationContractInformation info: infos) {
          String mapKey = calcCoordMapKey(info.coordinationBlockchainId, info.coodinationContract);
          this.coordinationContracts.put(mapKey,
              new CrosschainCoordinationContractInfo(info.coordinationBlockchainId, info.ipAddressAndPort, info.coodinationContract));
        }
      }
      else {
        // TODO do the comparison.
      }


    }
    return validConfig;
  }

  public static String calcCoordMapKey(final BigInteger bcId, final String address) {
    return bcId.toString(16) + address;
  }


}
