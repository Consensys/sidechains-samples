/*
 * Copyright 2019 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package tech.pegasys.samples.crosschain.hoteltrain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.CrosschainTransactionManager;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.ERC20Router;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.HotelRouter;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.cc.HotelRoom;
import tech.pegasys.samples.sidechains.common.utils.BasePropertiesFile;
import tech.pegasys.samples.sidechains.common.utils.KeyPairGen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class EntityBase {
    private static final Logger LOG = LogManager.getLogger(EntityBase.class);

    // Total number of tokens issued for booking.
    public static final BigInteger TOKEN_SUPPLY = BigInteger.valueOf(1000);

    public static final int NUM_RECEIVING_ACCOUNTS = 3;

    private Credentials credentials;
    protected TransactionManager tm;
    protected CrosschainTransactionManager xtm;
    protected Besu web3j;
    protected BigInteger bcId;

    protected ERC20Router erc20;
    protected String routerContractAddress;

    String entity;

    // A gas provider which indicates no gas is charged for transactions.
    protected ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);


    public EntityBase(final String entity, final Besu web3j, final BigInteger bcId, final int retry, final int pollingInterval,
                      final Besu web3jCoordinationBlockchain,
                      final BigInteger coordinationBlockchainId,
                      final String coordinationContractAddress,
                      final long crosschainTransactionTimeout) {
        this.entity = entity;
        loadStoreProperties(entity);
        this.web3j = web3j;
        this.tm = new RawTransactionManager(this.web3j, this.credentials, bcId.longValue(), retry, pollingInterval);
        this.xtm = new CrosschainTransactionManager(this.web3j, this.credentials, bcId, retry, pollingInterval,
            web3jCoordinationBlockchain, coordinationBlockchainId, coordinationContractAddress, crosschainTransactionTimeout);
        this.bcId = bcId;
    }

    protected void deployErc20() throws Exception {
        LOG.info(" Deploy ERC20 router contract");
        this.erc20 = ERC20Router.deploy(this.web3j, this.tm, this.freeGasProvider).send();

        LOG.info(" Deploy ERC20 lockable account contracts for owner");
        ERC20Helper hotelErc20Helper = new ERC20Helper(this.erc20);
        hotelErc20Helper.createAccount(this.web3j, this.xtm, this.freeGasProvider, NUM_RECEIVING_ACCOUNTS);

        LOG.info(" Setting total supply as {} tokens", TOKEN_SUPPLY);
        TransactionReceipt receipt = this.erc20.mint(TOKEN_SUPPLY).send();
    }

    public String getRouterContractAddress() {
        return this.routerContractAddress;
    }
    public String getErc20ContractAddress() {
        return this.erc20.getContractAddress();
    }

    public void buyTokens(final String account, final int number) throws Exception {
        LOG.info("Buy some tokens to be used for bookings: Account: {}, Number: {}", account, number);
        String myAccount = this.credentials.getAddress();

        ERC20Helper helper = new ERC20Helper(this.erc20);
        helper.dumpRouterInformation();
        LOG.info(" Transfer from account / my account");
        helper.dumpAccountInformation(myAccount);
        LOG.info(" Transfer destination account");
        helper.dumpAccountInformation(account);

        this.erc20.transfer(account, BigInteger.valueOf(number)).send();
        BigInteger balance1 = this.erc20.balanceOf(account).send();
        LOG.info(" New balance of account: {}", balance1);
    }

    public void showErc20Balances(String[] accounts) throws Exception {
        this.erc20.condense(this.credentials.getAddress()).send();
        BigInteger myBal = this.erc20.balanceOf(this.credentials.getAddress()).send();
        LOG.info(" Owner account {} balance: {}", this.credentials.getAddress(), myBal);

        for (String acc: accounts) {
            BigInteger bal = this.erc20.balanceOf(acc).send();
            LOG.info(" Account {} balance: {}", acc, bal);
        }
    }



    protected void loadStoreProperties(String name) {
        EntityProperties props = new EntityProperties(name);
        if (props.propertiesFileExists()) {
            props.load();
            this.routerContractAddress = props.routerContractAddress;
        }
        else {
            // Generate a key and store it in the format required for Credentials.
            props.privateKey = new KeyPairGen().generateKeyPairGetPrivateKey();
            props.store();
        }
        this.credentials = Credentials.create(props.privateKey);
    }

    protected void storeContractAddresses(String name) {
        EntityProperties props = new EntityProperties(name);
        props.load();
        props.routerContractAddress = this.routerContractAddress;
        props.store();
    }



    static class EntityProperties extends BasePropertiesFile {
        private static final String PROP_PRIV_KEY = "privateKey";
        private static final String PROP_CONTRACT_ADDRESS = "RouterContractAddress";
        String privateKey;
        String routerContractAddress;

        EntityProperties(String name) {
            super(name);
        }

        void load() {
            loadProperties();
            this.privateKey = this.properties.getProperty(PROP_PRIV_KEY);
            this.routerContractAddress = this.properties.getProperty(PROP_CONTRACT_ADDRESS);
        }

        void store() {
            this.properties.setProperty(PROP_PRIV_KEY, this.privateKey);
            if (this.routerContractAddress != null) {
                this.properties.setProperty(PROP_CONTRACT_ADDRESS, this.routerContractAddress);
            }
            storeProperties();
        }
    }
}
