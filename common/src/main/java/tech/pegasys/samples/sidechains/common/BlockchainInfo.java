package tech.pegasys.samples.sidechains.common;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.CrosschainTransactionManager;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;

public class BlockchainInfo {
  // Have the polling interval equal to the block time.
  private static final int DEFAULT_POLLING_INTERVAL = 2000;
  // Retry reqests to Ethereum Clients up to five times.
  private static final int DEFAULT_RETRY = 5;

  public BigInteger blockchainId;
  public String ipAddressAndPort;

  public BlockchainInfo(BigInteger blockchainId, String ipAddressAndPort) {
    this.blockchainId = blockchainId;
    this.ipAddressAndPort = ipAddressAndPort;
  }

  public String getURI() {
    return "http://" + this.ipAddressAndPort + "/";
  }

  public Besu getWebService() {
    return getWebService(DEFAULT_POLLING_INTERVAL);
  }
  public Besu getWebService(int pollingInterval) {
    return Besu.build(new HttpService(getURI()), pollingInterval);
  }


  public CrosschainTransactionManager getCrosschainTransactionManager(
      Credentials credentials, CrosschainCoordinationContractInfo coordContract) {
    return new CrosschainTransactionManager(getWebService(), credentials, this.blockchainId, DEFAULT_RETRY,
        DEFAULT_POLLING_INTERVAL, coordContract.getWebService(), coordContract.blockchainId,
        coordContract.contractAddress, coordContract.getCrosschainTransactionTimeout());
  }


  public TransactionManager getTransactionManager(Credentials credentials) {
    return new RawTransactionManager(getWebService(), credentials, this.blockchainId.longValue(), DEFAULT_RETRY,
        DEFAULT_POLLING_INTERVAL);
  }
}
