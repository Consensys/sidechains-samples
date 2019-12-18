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
package tech.pegasys.samples.crosschain.atomicswapether;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.besu.Besu;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import tech.pegasys.samples.sidechains.common.utils.BasePropertiesFile;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Faucet account which issues Ether to other accounts. This account should be allocated
 * Ether in the genesis block.
 */
public class Faucet {
    private static final Logger LOG = LogManager.getLogger(Faucet.class);


    // Externally Owned Account key pairs and associated transaction managers.
    // Faucet is set-up in the genesis file to have lots of Ether.
    private Credentials credentials;
    private TransactionManager tmSc1;
    private TransactionManager tmSc2;


    // Web services for each blockchain / sidechain.
    private Besu web3jSc1;
    private Besu web3jSc2;



    public Faucet(final Besu web3jSc1, final BigInteger sc1Id, final Besu web3jSc2, final BigInteger sc2Id,
                  final int retry, final int pollingInterval) {
        loadStoreProperties();

        this.web3jSc1 = web3jSc1;
        this.web3jSc2 = web3jSc2;

        this.tmSc1 = new RawTransactionManager(this.web3jSc1, this.credentials, sc1Id.longValue(), retry, pollingInterval);
        this.tmSc2 = new RawTransactionManager(this.web3jSc2, this.credentials, sc2Id.longValue(), retry, pollingInterval);
    }

    public void sendEtherSc1(final String toAddress, final BigInteger amountInWei) throws Exception {
        new Transfer(this.web3jSc1, this.tmSc1).sendFunds(toAddress, new BigDecimal(amountInWei), Convert.Unit.WEI).send();
    }

    public void sendEtherSc2(final String toAddress, final BigInteger amountInWei) throws Exception {
        new Transfer(this.web3jSc2, this.tmSc2).sendFunds(toAddress, new BigDecimal(amountInWei), Convert.Unit.WEI).send();
    }

    public String getFaucetAddress() {
        return this.credentials.getAddress();
    }


    private void loadStoreProperties() {
        FaucetProperties props = new FaucetProperties();
        if (props.propertiesFileExists()) {
            props.load();
        }
        else {
            props.storeDefault();
        }
        this.credentials = Credentials.create(props.faucetPrivateKey);
    }


    static class FaucetProperties extends BasePropertiesFile {
        private static final String FAUCET_PRIVATE_KEY = "6960b51fb7c56858d752c2faf781de045c6418d0b4b60d55ab853bcc194d3770";

        private static final String PROP_FAUCET_PRIV_KEY = "FaucetPrivateKey";
        String faucetPrivateKey;

        FaucetProperties() {
            super("faucet");
        }

        void load() {
            loadProperties();
            this.faucetPrivateKey = this.properties.getProperty(PROP_FAUCET_PRIV_KEY);
        }

        void storeDefault() {
            this.faucetPrivateKey = FAUCET_PRIVATE_KEY;
            store();
        }

        void store() {
            this.properties.setProperty(PROP_FAUCET_PRIV_KEY, this.faucetPrivateKey);
            storeProperties();
        }
    }
}
