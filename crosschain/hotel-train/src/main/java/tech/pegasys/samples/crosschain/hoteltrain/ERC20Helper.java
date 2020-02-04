package tech.pegasys.samples.crosschain.hoteltrain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.CrosschainTransactionManager;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.ERC20Router;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.cc.ERC20LockableAccount;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ERC20Helper {
  private static final Logger LOG = LogManager.getLogger(ERC20Helper.class);

  private ERC20Router erc20;

  public ERC20Helper(final ERC20Router erc20Router) {
    this.erc20 = erc20Router;
  }

  public void dumpRouterInformation() throws Exception {
    LOG.info(" ERC20 Router Information");
    LOG.info("  Contract address: {}",this.erc20.getContractAddress());
    String owner = this.erc20.owner().send();
    LOG.info("  ERC20 Router Contract Owner: {}", owner);
    BigInteger totalSupply = this.erc20.totalSupply().send();
    LOG.info("  ERC20 Router Contract: Total Supply: {}", totalSupply);
  }

  public void dumpAccountInformation(String acc) throws Exception {
    LOG.info(" ERC20 Account Information for account: {}", acc);

    BigInteger balance = this.erc20.balanceOf(acc).send();
    LOG.info("  Current balance of account: {}", balance);

    BigInteger accSize = this.erc20.accSize(acc).send();
    LOG.info("  Size (number of storage slots): {}", accSize);

    int accSizeInt = accSize.intValue();
    for (int i=0; i<accSizeInt; i++) {
      BigInteger bal = this.erc20.accGetBalance(acc, BigInteger.valueOf(i)).send();
      LOG.info("   Balance of slot {}: {}", i, bal);

      String addressLockableContract = this.erc20.getLockableAccountAddress(acc, BigInteger.valueOf(i)).send();
      LOG.info("   Address of lockable account for slot {}: {}", i, addressLockableContract);

      String addressRouterContract = this.erc20.accGetRouter(acc, BigInteger.valueOf(i)).send();
      LOG.info("   Address of router contract in lockable contract. slot {}: {}", i, addressRouterContract);

      String lockableContractOwner = this.erc20.accGetOwner(acc, BigInteger.valueOf(i)).send();
      LOG.info("   Owner of lockable contract. slot {}: {}", i, lockableContractOwner);
    }
  }

  // Create an account in the ERC 20 contract, in the name of the address derived from the credentials
  // associated with the transaction manager.
  public void createAccount(Besu web3j, CrosschainTransactionManager tm, ContractGasProvider gasProvider, int numSlots) throws Exception {
    String routerContractAddress = this.erc20.getContractAddress();
    LOG.info("  Creating account slots for router contract: {}", routerContractAddress);

    List<String> addresses = new ArrayList<>();
    for (int i = 0; i < numSlots; i++) {
      ERC20LockableAccount acc2 =
          ERC20LockableAccount.deployLockable(web3j, tm, gasProvider, routerContractAddress).send();
      LOG.info("   Deployed lockable contract for account slot {}: {}", i, acc2.getContractAddress());
      addresses.add(acc2.getContractAddress());
    }
    LOG.info("  Linking ERC20 lockable account contracts to ERC20 Router contract");
    this.erc20.createAccount(addresses).send();
  }
}
