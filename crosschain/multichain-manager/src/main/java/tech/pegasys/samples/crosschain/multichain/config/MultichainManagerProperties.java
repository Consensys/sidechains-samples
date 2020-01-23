package tech.pegasys.samples.crosschain.multichain.config;

import org.web3j.crypto.Credentials;
import tech.pegasys.samples.sidechains.common.BlockchainInfo;
import tech.pegasys.samples.sidechains.common.CrosschainCoordinationContractInfo;
import tech.pegasys.samples.sidechains.common.utils.BasePropertiesFile;
import tech.pegasys.samples.sidechains.common.utils.KeyPairGen;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


/**
 * Stores the private key to use and the list of nodes which make up
 * the Multichain Node.
 */
class MultichainManagerProperties {

  Credentials credentials;
  Map<BigInteger, BlockchainInfo> bcInfos;

  public MultichainManagerProperties() {
//    loadStoreProperties();
  }


  public static void deleteAllPropertiesFile() throws IOException {
    (new ConfigProperties()).deletePropertiesFile();
  }

  public void loadStoreProperties() {
    ConfigProperties props = new ConfigProperties();
    if (props.propertiesFileExists()) {
      props.load();
    }
    else {
      // TODO need to allow a user-defined key.
      // Generate a key and store it in the format required for Credentials.
      props.privateKey = new KeyPairGen().generateKeyPairGetPrivateKey();
      props.store();
    }
    this.credentials = Credentials.create(props.privateKey);
    this.bcInfos = (props.bcInfos == null) ? new TreeMap<>() : props.bcInfos;
  }

  public void store(String privateKey, Map<BigInteger, BlockchainInfo> bcInfos, Map<String, CrosschainCoordinationContractInfo> coordContracts) {
    ConfigProperties props = new ConfigProperties();
    props.privateKey = privateKey;
    props.bcInfos = bcInfos;
    props.coordContracts = coordContracts;
    props.store();
  }




  public static class ConfigProperties extends BasePropertiesFile {
    private static final String PROP_PRIV_KEY = "privateKey";
    String privateKey;

    Map<BigInteger, BlockchainInfo> bcInfos;
    Map<String, CrosschainCoordinationContractInfo> coordContracts;

    private static final String PROP_NUM_BLOCKCHAINS = "numBlockchains";
    private static final String PROP_BCID = "bcId";
    private static final String PROP_BCNODEIPPORT = "bcNodeIpPort";
    private static final String PROP_NUM_COORD = "numCoord";
    private static final String PROP_COORD_BCID = "coordBcId";
    private static final String PROP_COORD_IPPORT = "coordIpPort";
    private static final String PROP_COORD_ADDR = "coordAddr";


    public ConfigProperties() {
      super("multichain");
    }

    void load() {
      loadProperties();
      this.privateKey = this.properties.getProperty(PROP_PRIV_KEY);

      this.bcInfos = new TreeMap<>();
      String numBlockchainsStr = this.properties.getProperty(PROP_NUM_BLOCKCHAINS);
      if (numBlockchainsStr == null) {
        return;
      }
      int numBlockchains = Integer.valueOf(numBlockchainsStr);

      for (int i = 0; i < numBlockchains; i++) {
        String bcIdStr = this.properties.getProperty(PROP_BCID + i);
        BigInteger bcId = new BigInteger(bcIdStr, 16);
        String bcNodeIpPort = this.properties.getProperty(PROP_BCNODEIPPORT + i);
        this.bcInfos.put(bcId, new BlockchainInfo(bcId, bcNodeIpPort));
      }

      this.coordContracts = new TreeMap<>();
      String numCoordStr = this.properties.getProperty(PROP_NUM_COORD);
      if (numCoordStr == null) {
        return;
      }
      int numCoords = Integer.valueOf(numCoordStr);

      for (int i = 0; i < numCoords; i++) {
        String bcIdStr = this.properties.getProperty(PROP_COORD_BCID + i);
        BigInteger bcId = new BigInteger(bcIdStr, 16);
        String bcNodeIpPort = this.properties.getProperty(PROP_COORD_IPPORT + i);
        String contract = this.properties.getProperty(PROP_COORD_ADDR + i);
        this.coordContracts.put(ConfigControl.calcCoordMapKey(bcId, contract), new CrosschainCoordinationContractInfo(bcId, bcNodeIpPort, contract));
      }

    }

    void store() {
      this.properties.setProperty(PROP_PRIV_KEY, this.privateKey);

      int numBlockchains = 0;
      if (this.bcInfos != null) {
        numBlockchains = this.bcInfos.size();
        this.properties.setProperty(PROP_NUM_BLOCKCHAINS, Integer.toString(numBlockchains));
        Collection<BlockchainInfo> infos = this.bcInfos.values();
        Iterator<BlockchainInfo> iter = infos.iterator();
        for (int i = 0; i < numBlockchains; i++) {
          BlockchainInfo info = iter.next();
          this.properties.setProperty(PROP_BCID + i, info.blockchainId.toString(16));
          this.properties.setProperty(PROP_BCNODEIPPORT + i, info.ipAddressAndPort);
        }
      }

      int numCoords = 0;
      if (this.coordContracts != null) {
        numCoords = this.coordContracts.size();
        this.properties.setProperty(PROP_NUM_COORD, Integer.toString(numCoords));
        Collection<CrosschainCoordinationContractInfo> coordCons = this.coordContracts.values();
        Iterator<CrosschainCoordinationContractInfo> iter = coordCons.iterator();
        for (int i = 0; i < numCoords; i++) {
          CrosschainCoordinationContractInfo info = iter.next();
          this.properties.setProperty(PROP_COORD_BCID + i, info.blockchainId.toString(16));
          this.properties.setProperty(PROP_COORD_IPPORT + i, info.ipAddressAndPort);
          this.properties.setProperty(PROP_COORD_ADDR + i, info.contractAddress);
        }
      }

      storeProperties();
    }
  }
}
