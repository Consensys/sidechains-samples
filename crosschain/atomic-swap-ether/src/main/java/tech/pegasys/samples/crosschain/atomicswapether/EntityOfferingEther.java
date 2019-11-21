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
import org.web3j.tx.CrosschainTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tech.pegasys.samples.crosschain.atomicswapether.soliditywrappers.AtomicSwapReceiver;
import tech.pegasys.samples.crosschain.atomicswapether.soliditywrappers.AtomicSwapRegistration;
import tech.pegasys.samples.crosschain.atomicswapether.soliditywrappers.AtomicSwapSender;
import tech.pegasys.samples.sidechains.common.utils.BasePropertiesFile;
import tech.pegasys.samples.sidechains.common.utils.KeyPairGen;

import java.math.BigInteger;

/**
 * Act as the entity which offers Ether on sidechain 2 in exchange for Ether on sidechain 1.
 */
public class EntityOfferingEther {
    private static final Logger LOG = LogManager.getLogger(EntityOfferingEther.class);

    private Credentials credentials;
    private CrosschainTransactionManager tmSc1;
    private CrosschainTransactionManager tmSc2;
    private Besu web3jSc1;
    private Besu web3jSc2;
    private BigInteger sc1Id;
    private BigInteger sc2Id;

    // A gas provider which indicates no gas is charged for transactions.
    private ContractGasProvider freeGasProvider =  new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);


    // Smart contract addresses and objected.of contracts.
    public String senderContractAddress = null;
    public String receiverContractAddress = null;
    private AtomicSwapSender senderContract;
    private AtomicSwapReceiver receiverContract;


    public EntityOfferingEther(final Besu web3jSc1, final BigInteger sc1Id, final Besu web3jSc2, final BigInteger sc2Id,
                               final int retry, final int pollingInterval,
                               final Besu web3jCoordinationBlockchain,
                               final BigInteger coordinationBlockchainId,
                               final String coordinationContractAddress,
                               final long crosschainTransactionTimeout) {
        loadStoreProperties();
        this.web3jSc1 = web3jSc1;
        this.web3jSc2 = web3jSc2;
        this.tmSc1 = new CrosschainTransactionManager(this.web3jSc1, this.credentials, sc1Id, retry, pollingInterval,
            web3jCoordinationBlockchain, coordinationBlockchainId, coordinationContractAddress, crosschainTransactionTimeout);
        this.tmSc2 = new CrosschainTransactionManager(this.web3jSc2, this.credentials, sc2Id, retry, pollingInterval,
            web3jCoordinationBlockchain, coordinationBlockchainId, coordinationContractAddress, crosschainTransactionTimeout);
        this.sc1Id = sc1Id;
        this.sc2Id = sc2Id;
    }




    private void loadContracts() {
        LOG.info("Loading contracts");
        LOG.info(" Sender Contract: {}", this.senderContractAddress);
        LOG.info(" Receiver Contract: {}", this.receiverContractAddress);

        this.senderContract = AtomicSwapSender.load(this.senderContractAddress, this.web3jSc1, this.tmSc1, this.freeGasProvider);
        this.receiverContract = AtomicSwapReceiver.load(this.receiverContractAddress, this.web3jSc2, this.tmSc2, this.freeGasProvider);
    }


    public void setUpAndDeployContracts(String registrationContractAddress, BigInteger exchangeRate, BigInteger sc2OfferValueInWei) throws Exception {
        LOG.info("Set-up and deploying contracts");
        RemoteCall<AtomicSwapReceiver> remoteCallReceiverContract =
            AtomicSwapReceiver.deployLockable(this.web3jSc2, this.tmSc2, this.freeGasProvider, sc2OfferValueInWei, this.sc1Id);
        this.receiverContract = remoteCallReceiverContract.send();
        this.receiverContractAddress = this.receiverContract.getContractAddress();
        LOG.info(" Receiver Contract deployed on sidechain 2 (id={}), at address: {}", this.sc2Id, this.receiverContractAddress);

        RemoteCall<AtomicSwapSender> remoteCallSenderContract =
            AtomicSwapSender.deployLockable(this.web3jSc1, this.tmSc1, this.freeGasProvider,
                this.sc2Id, this.receiverContractAddress, exchangeRate);
        this.senderContract = remoteCallSenderContract.send();
        this.senderContractAddress = this.senderContract.getContractAddress();
        LOG.info(" Sender Contract deployed on sidechain 1 (id={}), at address: {}", this.sc1Id, this.senderContractAddress);

        this.receiverContract.setSenderContract(this.senderContractAddress).send();
        LOG.info(" Sender contract linked to receiver contract");

        AtomicSwapRegistration registrationContract = AtomicSwapRegistration.load(registrationContractAddress, this.web3jSc1, this.tmSc1, this.freeGasProvider);
        registrationContract.register(this.senderContractAddress).send();
        LOG.info(" Sender contract registered in the registration contract");

        storeContractAddresses();
    }


    public void withdrawEverythingSc1() throws Exception {
        LOG.info("Withdrawing funds from sender contract on Sidechain 1");
        if (this.senderContract == null) {
            loadContracts();
        }
        BigInteger amountInWei = this.senderContract.getMyBalance().send();
        this.senderContract.withdraw(amountInWei).send();
        LOG.info(" Withdrawl completed");
    }

    public void withdrawSc2(BigInteger amountInWei) throws Exception {
        LOG.info("Withdrawing {} wei from receiver contract on Sidechain 2", amountInWei);
        if (this.receiverContract == null) {
            loadContracts();
        }
        this.receiverContract.withdraw(amountInWei).send();
        LOG.info(" Withdrawl completed");
    }

    public void depositSc2(BigInteger amountInWei) throws Exception {
        LOG.info("Deposit {} wei into receiver account", amountInWei);
        if (this.receiverContract == null) {
            loadContracts();
        }
        this.receiverContract.deposit(amountInWei).send();
        LOG.info(" Deposit completed");
    }

    public String accountAddress() {
        return this.credentials.getAddress();
    }

    public String myAccountBalanceSenderContract() throws Exception {
        if (this.senderContract == null) {
            loadContracts();
        }
        return this.senderContract.getMyBalance().send().toString();
    }
    public String myAccountBalanceReceiverContract() throws Exception {
        if (this.receiverContract == null) {
            loadContracts();
        }
        return this.receiverContract.getMyBalance().send().toString();
    }

    private void loadStoreProperties() {
        EntityOfferingProperties props = new EntityOfferingProperties();
        if (props.propertiesFileExists()) {
            props.load();
            this.senderContractAddress = props.senderContractAddress;
            this.receiverContractAddress = props.receiverContractAddress;
        }
        else {
            // Generate a key and store it in the format required for Credentials.
            props.privateKey = new KeyPairGen().generateKeyPairGetPrivateKey();
            props.store();
        }
        this.credentials = Credentials.create(props.privateKey);
    }

    private void storeContractAddresses() {
        EntityOfferingProperties props = new EntityOfferingProperties();
        props.load();
        props.senderContractAddress = this.senderContractAddress;
        props.receiverContractAddress = this.receiverContractAddress;
        props.store();
    }



    static class EntityOfferingProperties extends BasePropertiesFile {
        private static final String PROP_PRIV_KEY = "privateKey";
        private static final String PROP_SENDER_CONTRACT_ADDRESS = "SenderContractAddress";
        private static final String PROP_RECEIVER_CONTRACT_ADDRESS = "ReceiverContractAddress";
        String privateKey;
        String senderContractAddress;
        String receiverContractAddress;

        EntityOfferingProperties() {
            super("offering");
        }

        void load() {
            loadProperties();
            this.privateKey = this.properties.getProperty(PROP_PRIV_KEY);
            this.senderContractAddress = this.properties.getProperty(PROP_SENDER_CONTRACT_ADDRESS);
            this.receiverContractAddress = this.properties.getProperty(PROP_RECEIVER_CONTRACT_ADDRESS);
        }

        void store() {
            this.properties.setProperty(PROP_PRIV_KEY, this.privateKey);
            if (senderContractAddress != null) {
                this.properties.setProperty(PROP_SENDER_CONTRACT_ADDRESS, this.senderContractAddress);
                this.properties.setProperty(PROP_RECEIVER_CONTRACT_ADDRESS, this.receiverContractAddress);
            }
            storeProperties();
        }
    }
}
