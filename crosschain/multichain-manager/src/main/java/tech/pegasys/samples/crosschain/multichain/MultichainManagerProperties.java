package tech.pegasys.samples.crosschain.multichain;

import org.web3j.crypto.Credentials;
import tech.pegasys.samples.sidechains.common.coordination.CrosschainCoordinationContractSetup;
import tech.pegasys.samples.sidechains.common.utils.BasePropertiesFile;
import tech.pegasys.samples.sidechains.common.utils.KeyPairGen;

import java.io.IOException;

public class MultichainManagerProperties {

  Credentials credentials;

  public MultichainManagerProperties() {
    loadStoreProperties();
  }


  public static void deleteAllPropertiesFile() throws IOException {
    (new ConfigProperties()).deletePropertiesFile();
  }

  private void loadStoreProperties() {
    ConfigProperties props = new ConfigProperties();
    if (props.propertiesFileExists()) {
      props.load();
    }
    else {
      // Generate a key and store it in the format required for Credentials.
      props.privateKey = new KeyPairGen().generateKeyPairGetPrivateKey();
      props.store();
    }
    this.credentials = Credentials.create(props.privateKey);
  }


  public static class ConfigProperties extends BasePropertiesFile {
    private static final String PROP_PRIV_KEY = "privateKey";
    String privateKey;

    public ConfigProperties() {
      super("multichain");
    }

    void load() {
      loadProperties();
      this.privateKey = this.properties.getProperty(PROP_PRIV_KEY);
    }

    void store() {
      this.properties.setProperty(PROP_PRIV_KEY, this.privateKey);
      storeProperties();
    }
  }
}
