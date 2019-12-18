package tech.pegasys.samples.sidechains.common;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

public class DefaultBlockchainInfo {
  private static final BigInteger BC1_BLOCKCHAIN_ID = BigInteger.valueOf(11);
  private static final String BC1_IP_PORT = "127.0.0.1:8110";
  private static final BigInteger BC2_BLOCKCHAIN_ID = BigInteger.valueOf(22);
  private static final String BC2_IP_PORT = "127.0.0.1:8220";
  private static final BigInteger BC3_BLOCKCHAIN_ID = BigInteger.valueOf(33);
  private static final String BC3_IP_PORT = "127.0.0.1:8330";


  public static Map<BigInteger, BlockchainInfo> getDefaultCoordinationBlockchains() {
    Map<BigInteger, BlockchainInfo> coodinationblockchains = new TreeMap<>();
    // BC1 is being re-used as a coordination blockchain. Typically, this would be a
    // separate blockchain. Reusing the BC1 means that not as many blockchains will need
    // to be spun up to run the sample code.
    coodinationblockchains.put(BC1_BLOCKCHAIN_ID, new BlockchainInfo(BC1_BLOCKCHAIN_ID, BC1_IP_PORT));
    return coodinationblockchains;
  }

  public static Map<BigInteger, BlockchainInfo> getDefaultBlockchains() {
    Map<BigInteger, BlockchainInfo> blockchains = new TreeMap<>();
    blockchains.put(BC1_BLOCKCHAIN_ID, new BlockchainInfo(BC1_BLOCKCHAIN_ID, BC1_IP_PORT));
    blockchains.put(BC2_BLOCKCHAIN_ID, new BlockchainInfo(BC2_BLOCKCHAIN_ID, BC2_IP_PORT));
    blockchains.put(BC3_BLOCKCHAIN_ID, new BlockchainInfo(BC3_BLOCKCHAIN_ID, BC3_IP_PORT));
    return blockchains;
  }

}
