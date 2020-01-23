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
package tech.pegasys.samples.sidechains.common.coordination;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.besu.response.crosschain.CoordinationContractInformation;
import org.web3j.protocol.besu.response.crosschain.ListCoordinationContractsResponse;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tech.pegasys.samples.sidechains.common.coordination.soliditywrappers.CrosschainCoordinationV1;
import tech.pegasys.samples.sidechains.common.coordination.soliditywrappers.VotingAlgMajorityWhoVoted;
import tech.pegasys.samples.sidechains.common.utils.BasePropertiesFile;
import tech.pegasys.samples.sidechains.common.utils.KeyPairGen;

import java.math.BigInteger;
import java.util.List;

/**
 * Act as the entity which is deploying and setting up the crosschain coordination contract.
 *
 * Note the way this code does not match how you should do things in a production environment.
 * All nodes in a multichain node will need to trust the same coordination contracts. The
 * coordination contract to use will depend on which blockchains are going to be used, and
 * which coordination contracts the other participants trust.
 */
public class CrosschainCoordinationContractSetup {
    // The polling interval should be the same at the block period of the coordination blockchain.
    private static final int POLLING_INTERVAL = 2000;


    private static final Logger LOG = LogManager.getLogger(CrosschainCoordinationContractSetup.class);

    private String crosschainCoordinationContractAddress;
    private BigInteger crosschainCoordinationContractBlockcainId;
    private Besu crosschainCoordinationBesu;


    public CrosschainCoordinationContractSetup(final Besu blockchainNodeWeb3j) throws Exception {
        ListCoordinationContractsResponse resp = blockchainNodeWeb3j.crossListCoordinationContracts().send();
        List<CoordinationContractInformation> info = resp.getInfo();
        if (info.isEmpty()) {
            LOG.error("No Crosschain Coordination Contract appears to have been configured");
            LOG.error("Please run the Multichain Manager sample with the options: config auto");
            System.exit(-1);
        }
        CoordinationContractInformation coordContractInfo = info.get(0);
        this.crosschainCoordinationContractAddress = coordContractInfo.coodinationContract;
        this.crosschainCoordinationContractBlockcainId = coordContractInfo.coordinationBlockchainId;
        String uri = "http://" + coordContractInfo.ipAddressAndPort + "/";
        this.crosschainCoordinationBesu = Besu.build(new HttpService(uri), POLLING_INTERVAL);
    }

    public String getCrosschainCoordinationContractAddress() {
        return this.crosschainCoordinationContractAddress;
    }

    public BigInteger getCrosschainCoordinationContractBlockcainId() {
        return this.crosschainCoordinationContractBlockcainId;
    }

    public Besu getCrosschainCoordinationWeb3J() {
        return this.crosschainCoordinationBesu;
    }
}
