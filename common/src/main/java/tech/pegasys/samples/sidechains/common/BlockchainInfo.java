package tech.pegasys.samples.sidechains.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.CrosschainTransactionManager;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class BlockchainInfo {
  private static final Logger LOG = LogManager.getLogger(BlockchainInfo.class);

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

  public String getIp() {
    return this.ipAddressAndPort.substring(0, this.ipAddressAndPort.indexOf(':'));
  }
  public int getPort() {
    String port = this.ipAddressAndPort.substring(this.ipAddressAndPort.indexOf(':')+1);
    return Integer.valueOf(port);
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

  public boolean isOnline() {
    Socket socket = new Socket();
    try {
      InetAddress ip = InetAddress.getByName(getIp());
      SocketAddress addr = new InetSocketAddress(ip, getPort());
      socket.connect(addr);
    } catch (Exception ex) {
      LOG.error(
          "Error connecting with blockchainId 0x{} ({}:{}): {}",
          this.blockchainId.toString(16),
          getIp(),
          getPort(),
          ex.toString());
      return false;
    }
    return true;
  }

}
