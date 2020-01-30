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
import org.web3j.tx.CrosschainTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.TravelAgency;
import tech.pegasys.samples.sidechains.common.utils.BasePropertiesFile;
import tech.pegasys.samples.sidechains.common.utils.KeyPairGen;

import java.math.BigInteger;


/**
 * TODO
 */
public class EntityTravelAgency {
    private static final Logger LOG = LogManager.getLogger(EntityTravelAgency.class);

    private Credentials credentials;

    private CrosschainTransactionManager tmTravelAgency;
    private Besu web3jTravelAgency;
    private String agencyContractAddress;
    private BigInteger agencyBcId;


    TravelAgency agencyContract;


    // A gas provider which indicates no gas is charged for transactions.
    private ContractGasProvider freeGasProvider = new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);

    public EntityTravelAgency(final Besu web3j, BigInteger bcId, int retry, int pollingInterval,
        final Besu web3jCoordinationBlockchain,
        final BigInteger coordinationBlockchainId,
        final String coordinationContractAddress,
        final long crosschainTransactionTimeout) {

        loadStoreProperties();
        this.web3jTravelAgency = web3j;
        this.tmTravelAgency = new CrosschainTransactionManager(web3j, this.credentials, bcId, retry, pollingInterval,
            web3jCoordinationBlockchain, coordinationBlockchainId, coordinationContractAddress, crosschainTransactionTimeout);
        this.agencyBcId = bcId;
    }

    public void deploy(final BigInteger hotelBcId, final String hotelContractAddress,
                       final BigInteger trainBcId, final String trainContractAddress) throws Exception {
        LOG.info(" Deploying travel agency contract");
        RemoteCall<TravelAgency> remoteCall =
            TravelAgency.deployLockable(this.web3jTravelAgency, this.tmTravelAgency, this.freeGasProvider,
                hotelBcId, hotelContractAddress, trainBcId, trainContractAddress);
        this.agencyContract = remoteCall.send();
        this.agencyContractAddress = agencyContract.getContractAddress();
        LOG.info("  Travel Agency Contract deployed on blockchain {}, at address: {}",
            this.agencyBcId, this.agencyContractAddress);
        storeContractAddress();
    }

    public void load() throws Exception {
        LOG.info(" Loading travel agency contract");
        this.agencyContract =
            TravelAgency.load(this.agencyContractAddress, this.web3jTravelAgency, this.tmTravelAgency, this.freeGasProvider);
        LOG.info("  Travel Agency Contract loaded on blockchain {}, at address: {}",
            this.agencyBcId, this.agencyContractAddress);
    }

    private void loadStoreProperties() {
        AgencyProperties props = new AgencyProperties();
        if (props.propertiesFileExists()) {
            props.load();
            this.agencyContractAddress = props.agencyContractAddress;
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
        AgencyProperties props = new AgencyProperties();
        props.load();
        props.agencyContractAddress = this.agencyContractAddress;
        props.store();
    }



    static class AgencyProperties extends BasePropertiesFile {
        private static final String PROP_PRIV_KEY = "privateKey";
        private static final String PROP_AGENCY_CONTRACT_ADDRESS = "AgencyContractAddress";
        String privateKey;
        String agencyContractAddress;

        AgencyProperties() {
            super("ragency");
        }

        void load() {
            loadProperties();
            this.privateKey = this.properties.getProperty(PROP_PRIV_KEY);
            this.agencyContractAddress = this.properties.getProperty(PROP_AGENCY_CONTRACT_ADDRESS);
        }

        void store() {
            this.properties.setProperty(PROP_PRIV_KEY, this.privateKey);
            if (agencyContractAddress != null) {
                this.properties.setProperty(PROP_AGENCY_CONTRACT_ADDRESS, this.agencyContractAddress);
            }
            storeProperties();
        }
    }
}
