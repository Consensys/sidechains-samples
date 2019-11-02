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
import tech.pegasys.samples.crosschain.atomicswapether.soliditywrappers.CrosschainCoordinationV1;
import tech.pegasys.samples.crosschain.atomicswapether.soliditywrappers.VotingAlgMajorityWhoVoted;
import tech.pegasys.samples.crosschain.atomicswapether.utils.AbstractPropertiesFile;
import tech.pegasys.samples.crosschain.atomicswapether.utils.KeyPairGen;

import java.math.BigInteger;

/**
 * Act as the entity which owns the registration contract.
 */
public class CrosschainCoordinationContractSetup {
    private static final Logger LOG = LogManager.getLogger(CrosschainCoordinationContractSetup.class);

    // The voting period used for adding members to the consortia who can add sidechains. Also used for
    // voting on public keys.
    private static final BigInteger VOTING_PERIOD = BigInteger.valueOf(5);

    private Credentials credentials;
    private TransactionManager tmSc0;
    private Besu web3jSc0;
    private BigInteger sc0Id;

    // A gas provider which indicates no gas is charged for transactions.
    private ContractGasProvider freeGasProvider = new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);

    private String crosschainCoordinationContractAddress;


    public CrosschainCoordinationContractSetup(final Besu web3jSc0, BigInteger sc0Id, int retry, int pollingInterval) {
        loadStoreProperties();
        this.web3jSc0 = web3jSc0;
        this.tmSc0 = new RawTransactionManager(this.web3jSc0, this.credentials, sc0Id.longValue(), retry, pollingInterval);
        this.sc0Id = sc0Id;
    }

    public void deployCrosschainCoordinationContract() throws Exception {
        LOG.info(" Deploying Crosschain Coordination contract and Voting contract used with Crosschain Coordination");
        RemoteCall<VotingAlgMajorityWhoVoted> remoteCallVotingContract =
            VotingAlgMajorityWhoVoted.deploy(this.web3jSc0, this.tmSc0, this.freeGasProvider);
        VotingAlgMajorityWhoVoted votingContract = remoteCallVotingContract.send();
        String votingContractAddress = votingContract.getContractAddress();
        LOG.info("  Voting Contract deployed on sidechain 0 (id={}), at address: {}",
            this.sc0Id, votingContractAddress);

        RemoteCall<CrosschainCoordinationV1> remoteCallCoordinationContract =
            CrosschainCoordinationV1.deploy(this.web3jSc0, this.tmSc0, this.freeGasProvider, votingContractAddress, VOTING_PERIOD);
        CrosschainCoordinationV1 coordinationContract = remoteCallCoordinationContract.send();
        this.crosschainCoordinationContractAddress = coordinationContract.getContractAddress();
        LOG.info("  Crosschain Coordination Contract deployed on sidechain 0 (id={}), at address: {}",
            this.sc0Id, this.crosschainCoordinationContractAddress);
        storeContractAddress();
    }


    public void registerSidechainWithCrosschainCoordinationContract() {
        // TODO Need to register the IP addres, chain id, and crosschain coordination contract address with the node, and get the public key.
        // TODO In the crosschain coordination contract, need to create the entry for the sidechain, and register the public key.
    }



    public String getCrosschainCoordinationContractAddress() {
        return this.crosschainCoordinationContractAddress;
    }

    private void loadStoreProperties() {
        CrosschainCoordinationContractSetupProperties props = new CrosschainCoordinationContractSetupProperties();
        if (props.propertiesFileExists()) {
            props.load();
            this.crosschainCoordinationContractAddress = props.crosschainCoordinationContractAddress;
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
        CrosschainCoordinationContractSetupProperties props = new CrosschainCoordinationContractSetupProperties();
        props.load();
        props.crosschainCoordinationContractAddress = this.crosschainCoordinationContractAddress;
        props.store();
    }



    static class CrosschainCoordinationContractSetupProperties extends AbstractPropertiesFile {
        private static final String PROP_PRIV_KEY = "privateKey";
        private static final String PROP_CROSSCHAIN_CONTRACT_ADDRESS = "CrosschainCoordinationContractAddress";
        String privateKey;
        String crosschainCoordinationContractAddress;

        CrosschainCoordinationContractSetupProperties() {
            super("crosschaincoordination");
        }

        void load() {
            loadProperties();
            this.privateKey = this.properties.getProperty(PROP_PRIV_KEY);
            this.crosschainCoordinationContractAddress = this.properties.getProperty(PROP_CROSSCHAIN_CONTRACT_ADDRESS);
        }

        void store() {
            this.properties.setProperty(PROP_PRIV_KEY, this.privateKey);
            if (crosschainCoordinationContractAddress != null) {
                this.properties.setProperty(PROP_CROSSCHAIN_CONTRACT_ADDRESS, this.crosschainCoordinationContractAddress);
            }
            storeProperties();
        }
    }
}
