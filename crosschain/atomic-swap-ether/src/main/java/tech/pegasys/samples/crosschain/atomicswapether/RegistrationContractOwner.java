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
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tech.pegasys.samples.crosschain.atomicswapether.soliditywrappers.AtomicSwapRegistration;
import tech.pegasys.samples.sidechains.common.utils.BasePropertiesFile;
import tech.pegasys.samples.sidechains.common.utils.KeyPairGen;

import java.math.BigInteger;

/**
 * Act as the entity which owns the registration contract.
 */
public class RegistrationContractOwner {
    private static final Logger LOG = LogManager.getLogger(RegistrationContractOwner.class);

    private Credentials credentials;
    private TransactionManager tmSc1;
    private Besu web3jSc1;
    private BigInteger sc1Id;

    // A gas provider which indicates no gas is charged for transactions.
    private ContractGasProvider freeGasProvider = new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);

    private String registrationContractAddress;


    public RegistrationContractOwner(final Besu web3jSc1, BigInteger sc1Id, int retry, int pollingInterval) {
        loadStoreProperties();
        this.web3jSc1 = web3jSc1;
        this.tmSc1 = new RawTransactionManager(this.web3jSc1, this.credentials, sc1Id.longValue(), retry, pollingInterval);
        this.sc1Id = sc1Id;
    }

    public void deployRegistrationContract() throws Exception {
        LOG.info(" Deploying registration contract");
        RemoteCall<AtomicSwapRegistration> remoteCallRegistrationContract =
            AtomicSwapRegistration.deploy(this.web3jSc1, this.tmSc1, this.freeGasProvider);
        AtomicSwapRegistration registrationContract = remoteCallRegistrationContract.send();
        this.registrationContractAddress = registrationContract.getContractAddress();
        LOG.info("  Registration Contract deployed on sidechain 1 (id={}), at address: {}",
            this.sc1Id, this.registrationContractAddress);
        storeContractAddress();
    }

    public String getRegistrationContractAddress() {
        return this.registrationContractAddress;
    }

    private void loadStoreProperties() {
        RegistrationProperties props = new RegistrationProperties();
        if (props.propertiesFileExists()) {
            props.load();
            this.registrationContractAddress = props.registrationContractAddress;
        }
        else {
            // Generate a key and store it in the format required for Credentials.
            props.privateKey = new KeyPairGen().generateKeyPairGetPrivateKey();
            System.out.println("Priv2: " + props.privateKey);
            props.store();
        }
        this.credentials = Credentials.create(props.privateKey);
    }

    private void storeContractAddress() {
        RegistrationProperties props = new RegistrationProperties();
        props.load();
        props.registrationContractAddress = this.registrationContractAddress;
        props.store();
    }



    static class RegistrationProperties extends BasePropertiesFile {
        private static final String PROP_PRIV_KEY = "privateKey";
        private static final String PROP_REGISTRATION_CONTRACT_ADDRESS = "RegistrationContractAddress";
        String privateKey;
        String registrationContractAddress;

        RegistrationProperties() {
            super("registration");
        }

        void load() {
            loadProperties();
            this.privateKey = this.properties.getProperty(PROP_PRIV_KEY);
            this.registrationContractAddress = this.properties.getProperty(PROP_REGISTRATION_CONTRACT_ADDRESS);
        }

        void store() {
            this.properties.setProperty(PROP_PRIV_KEY, this.privateKey);
            if (registrationContractAddress != null) {
                this.properties.setProperty(PROP_REGISTRATION_CONTRACT_ADDRESS, this.registrationContractAddress);
            }
            storeProperties();
        }
    }
}
