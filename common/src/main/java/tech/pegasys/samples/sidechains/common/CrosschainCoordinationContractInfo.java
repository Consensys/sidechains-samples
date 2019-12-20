package tech.pegasys.samples.sidechains.common;

import java.math.BigInteger;

public class CrosschainCoordinationContractInfo extends BlockchainInfo {
  // Time-out for Crosschain Transactions in terms of block numbers.
  private static final int DEFAULT_CROSSCHAIN_TRANSACTION_TIMEOUT = 10;

  public String contractAddress;

  public CrosschainCoordinationContractInfo(BigInteger blockchainId, String ipAddressAndPort,
                                            String crosschainCoordinationContractAddress) {
    super(blockchainId, ipAddressAndPort);
    this.contractAddress = crosschainCoordinationContractAddress;
  }

  public int getCrosschainTransactionTimeout() {
    return DEFAULT_CROSSCHAIN_TRANSACTION_TIMEOUT;

  }
}
