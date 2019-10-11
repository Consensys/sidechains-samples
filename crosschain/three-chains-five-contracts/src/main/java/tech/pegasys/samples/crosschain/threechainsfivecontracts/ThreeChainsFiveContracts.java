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
package tech.pegasys.samples.crosschain.threechainsfivecontracts;

import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.CrosschainTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import tech.pegasys.samples.crosschain.threechainsfivecontracts.soliditywrappers.Sc1Contract1;
import tech.pegasys.samples.crosschain.threechainsfivecontracts.soliditywrappers.Sc2Contract2;
import tech.pegasys.samples.crosschain.threechainsfivecontracts.soliditywrappers.Sc2Contract3;
import tech.pegasys.samples.crosschain.threechainsfivecontracts.soliditywrappers.Sc2Contract4;
import tech.pegasys.samples.crosschain.threechainsfivecontracts.soliditywrappers.Sc3Contract5;
import tech.pegasys.samples.crosschain.threechainsfivecontracts.soliditywrappers.Sc3Contract6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.Scanner;

/**
 * The main class.
 */
public class ThreeChainsFiveContracts {
    private static final BigInteger SC1_SIDECHAIN_ID = BigInteger.valueOf(11);
    private static final String SC1_URI = "http://127.0.0.1:8110/";
    private static final BigInteger SC2_SIDECHAIN_ID = BigInteger.valueOf(22);
    private static final String SC2_URI = "http://127.0.0.1:8220/";
    private static final BigInteger SC3_SIDECHAIN_ID = BigInteger.valueOf(33);
    private static final String SC3_URI = "http://127.0.0.1:8330/";

    // Have the polling interval equal to the block time.
    private static final int POLLING_INTERVAL = 2000;
    // Retry reqests to Ethereum Clients up to five times.
    private static final int RETRY = 5;




    private boolean deploy = false;



//    private Operation operation;
    private String contract1Address = null;
    private String contract2Address = null;
    private String contract3Address = null;
    private String contract4Address = null;
    private String contract5Address = null;
    private String contract6Address = null;
    private Credentials credentials;

    private Besu web3jSc1;
    private Besu web3jSc2;
    private Besu web3jSc3;

    private ContractGasProvider freeGasProvider;

    private CrosschainTransactionManager tmSc1;
    private CrosschainTransactionManager tmSc2;
    private CrosschainTransactionManager tmSc3;

    private Sc1Contract1 contract1;
    private Sc2Contract2 contract2;
    private Sc2Contract3 contract3;
    private Sc2Contract4 contract4;
    private Sc3Contract5 contract5;
    private Sc3Contract6 contract6;


    public static void main(final String args[]) throws Exception {
        new ThreeChainsFiveContracts().run(args);
    }

    private ThreeChainsFiveContracts() {

        this.web3jSc1 = Besu.build(new HttpService(SC1_URI));
        this.web3jSc2 = Besu.build(new HttpService(SC2_URI));
        this.web3jSc3 = Besu.build(new HttpService(SC3_URI));

        // Hyperledger Besu is configured as an IBFT2, free gas network. We need a free gas provider.
        this.freeGasProvider = new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT);
    }


    private void run(final String[] args) throws Exception {
        processArgs(args);

        this.tmSc1 = new CrosschainTransactionManager(this.web3jSc1, this.credentials, SC1_SIDECHAIN_ID.longValue(), RETRY, POLLING_INTERVAL);
        this.tmSc2 = new CrosschainTransactionManager(this.web3jSc2, this.credentials, SC2_SIDECHAIN_ID.longValue(), RETRY, POLLING_INTERVAL);
        this.tmSc3 = new CrosschainTransactionManager(this.web3jSc3, this.credentials, SC3_SIDECHAIN_ID.longValue(), RETRY, POLLING_INTERVAL);

        if (this.deploy) {
            deployContracts();
        }
        else {
            System.out.println("Using contracts deployed at: ");
            System.out.println(" Contract 1: " + this.contract1Address);
            System.out.println(" Contract 2: " + this.contract2Address);
            System.out.println(" Contract 3: " + this.contract3Address);
            System.out.println(" Contract 4: " + this.contract4Address);
            System.out.println(" Contract 5: " + this.contract5Address);
            System.out.println(" Contract 6: " + this.contract6Address);

            this.contract1 = Sc1Contract1.load(this.contract1Address, this.web3jSc1, this.tmSc1, this.freeGasProvider);
            this.contract2 = Sc2Contract2.load(this.contract2Address, this.web3jSc2, this.tmSc2, this.freeGasProvider);
            this.contract3 = Sc2Contract3.load(this.contract3Address, this.web3jSc2, this.tmSc2, this.freeGasProvider);
            this.contract4 = Sc2Contract4.load(this.contract4Address, this.web3jSc2, this.tmSc2, this.freeGasProvider);
            this.contract5 = Sc3Contract5.load(this.contract5Address, this.web3jSc3, this.tmSc3, this.freeGasProvider);
            this.contract6 = Sc3Contract6.load(this.contract6Address, this.web3jSc3, this.tmSc3, this.freeGasProvider);
        }

        runDemo();
    }


    private void usageAndExit() {
        System.err.println("Usage: tech.pegasys.poc.xweb3j.poc privateKeyFileName [<deploy>] [<addr_contract1>] [<addr_contract2>]");
        System.out.println();
        System.out.println("Example usage:");
        System.out.println(" tech.pegasys.samples.crosschain.threechainsfivecontract.ThreeChainsFiveContracts privateKeyFileName deploy");
        System.out.println(" tech.pegasys.samples.crosschain.threechainsfivecontract.ThreeChainsFiveContracts privateKeyFileName 0xda....  followed by six contract addresses");

        System.exit(1);
    }


    private void processArgs(String[] args) throws Exception {
        if (args.length < 2) {
            usageAndExit();
        }

        if (args[1].equalsIgnoreCase("deploy")) {
            if (args.length != 2) {
                usageAndExit();
            }
            this.deploy = true;
        } else {
            if (args.length != 7) {
                usageAndExit();
            } else {
                this.contract1Address = args[1];
                this.contract2Address = args[2];
                this.contract3Address = args[3];
                this.contract4Address = args[4];
                this.contract5Address = args[5];
                this.contract6Address = args[6];
            }

        }

        System.err.println(" Private Key file name: " + args[0]);

        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        String privateKey = br.readLine();
        br.close();

        this.credentials = Credentials.create(privateKey);
        System.out.println("Using credentials which correspond to account: " + this.credentials.getAddress());
    }


    private void deployContracts() throws Exception {
        System.out.println("Deploying contracts");
        RemoteCall<Sc2Contract2> remoteCallContract2 =
            Sc2Contract2.deployLockable(this.web3jSc2, this.tmSc2, this.freeGasProvider);
        this.contract2 = remoteCallContract2.send();
        this.contract2Address = this.contract2.getContractAddress();
        System.out.println(" Contract 2 deployed on sidechain 2 (id=" + SC2_SIDECHAIN_ID + "), at address: " + contract2Address);

        RemoteCall<Sc2Contract4> remoteCallContract4 =
            Sc2Contract4.deployLockable(this.web3jSc2, this.tmSc2, this.freeGasProvider);
        this.contract4 = remoteCallContract4.send();
        this.contract4Address = this.contract4.getContractAddress();
        System.out.println(" Contract 4 deployed on sidechain 2 (id=" + SC2_SIDECHAIN_ID + "), at address: " + contract4Address);

        RemoteCall<Sc3Contract5> remoteCallContract5 =
            Sc3Contract5.deployLockable(this.web3jSc3, this.tmSc3, this.freeGasProvider);
        this.contract5 = remoteCallContract5.send();
        this.contract5Address = this.contract5.getContractAddress();
        System.out.println(" Contract 5 deployed on sidechain 3 (id=" + SC3_SIDECHAIN_ID + "), at address: " + contract5Address);

        RemoteCall<Sc3Contract6> remoteCallContract6 =
            Sc3Contract6.deployLockable(this.web3jSc3, this.tmSc3, this.freeGasProvider, SC2_SIDECHAIN_ID, contract4Address);
        this.contract6 = remoteCallContract6.send();
        this.contract6Address = contract6.getContractAddress();
        System.out.println(" Contract 6 deployed on sidechain 3 (id=" + SC2_SIDECHAIN_ID + "), at address: " + contract6Address);

        RemoteCall<Sc2Contract3> remoteCallContract3 =
            Sc2Contract3.deployLockable(this.web3jSc2, this.tmSc2, this.freeGasProvider, SC3_SIDECHAIN_ID, contract6Address);
        this.contract3 = remoteCallContract3.send();
        this.contract3Address = this.contract3.getContractAddress();
        System.out.println(" Contract 3 deployed on sidechain 2 (id=" + SC2_SIDECHAIN_ID + "), at address: " + contract3Address);

        RemoteCall<Sc1Contract1> remoteCallContract1 =
            Sc1Contract1.deployLockable(this.web3jSc1, this.tmSc1, this.freeGasProvider, SC2_SIDECHAIN_ID, SC3_SIDECHAIN_ID, contract2Address, contract3Address, contract5Address);
        this.contract1 = remoteCallContract1.send();
        this.contract1Address = this.contract1.getContractAddress();
        System.out.println(" Contract 1 deployed on sidechain 1 (id=" + SC1_SIDECHAIN_ID + "), at address: " + contract1Address);

        System.out.println(" addresses: " + contract1Address
                + " " + contract2Address
                + " " + contract3Address
                + " " + contract4Address
                + " " + contract5Address
                + " " + contract6Address);
        System.out.println();
    }




    private void runDemo() throws Exception {
        System.out.println("Running Demo");

        System.out.println(" Set state in each contract to known values that aren't zero.");
        System.out.println("  Single-chain transaction: Contract1.set(1)");
        TransactionReceipt transactionReceipt = this.contract1.setVal(BigInteger.valueOf(1)).send();
        assertTrue(transactionReceipt.isStatusOK());
        System.out.println("  Single-chain transaction: Contract2.set(2)");
        transactionReceipt = this.contract2.setVal(BigInteger.valueOf(2)).send();
        assertTrue(transactionReceipt.isStatusOK());
        System.out.println("  Single-chain transaction: Contract3.set(3)");
        transactionReceipt = this.contract3.setVal(BigInteger.valueOf(3)).send();
        assertTrue(transactionReceipt.isStatusOK());
        System.out.println("  Single-chain transaction: Contract4.set(4)");
        transactionReceipt = this.contract4.setVal(BigInteger.valueOf(4)).send();
        assertTrue(transactionReceipt.isStatusOK());
        System.out.println("  Single-chain transaction: Contract5.set(5)");
        transactionReceipt = this.contract5.setVal(BigInteger.valueOf(5)).send();
        assertTrue(transactionReceipt.isStatusOK());
        System.out.println("  Single-chain transaction: Contract6.set(6)");
        transactionReceipt = this.contract6.setVal(BigInteger.valueOf(6)).send();
        assertTrue(transactionReceipt.isStatusOK());

        checkExpectedValues(1,2,3,4,5,6);

        Scanner myInput = new Scanner( System.in );

        while (true) {
            System.out.print( " Enter long value to call Contract1.doStuff with: " );
            long val = myInput.nextLong();
            if (val < 0) {
                System.out.println("  No negative numbers please!");
                continue;
            }

            BigInteger c1Val = this.contract1.val().send();
            BigInteger c2Val = this.contract2.val().send();
            BigInteger c3Val = this.contract3.val().send();
            BigInteger c4Val = this.contract4.val().send();
            BigInteger c5Val = this.contract5.val().send();
            BigInteger c6Val = this.contract6.val().send();

            System.out.println("  Executing call simulator to determine parameter values and expected results");
            CallSimulator sim = new CallSimulator(c1Val.longValue(), c2Val.longValue(), c3Val.longValue(), c4Val.longValue(), c5Val.longValue(), c6Val.longValue());
            sim.c1DoStuff(val);

            System.out.println("  Constructing Nested Crosschain Transaction");
            // Call to contract 2
            byte[] subordinateViewC2 = this.contract2.get_AsSignedCrosschainSubordinateView(null);

            // Call to contract 4
            byte[] subordinateViewC4 = this.contract4.get_AsSignedCrosschainSubordinateView(BigInteger.valueOf(sim.c4Get_val), null);

            // Call to contract 5
            byte[] subordinateViewC5 = this.contract5.calculate_AsSignedCrosschainSubordinateView(
                BigInteger.valueOf(sim.c5Calculate_val1), BigInteger.valueOf(sim.c5Calculate_val2), null);

            // Call to contract 6
            byte[][] subordinateTransactionsAndViewsForC6 = new byte[][] {subordinateViewC4};
            byte[] subordinateViewC6 = this.contract6.get_AsSignedCrosschainSubordinateView(
                BigInteger.valueOf(sim.c6Get_val), subordinateTransactionsAndViewsForC6);

            // Call to contract 3
            byte[][] subordinateTransactionsAndViewsForC3 = new byte[][] {subordinateViewC6};
            byte[] subordinateTransC3 = this.contract3.process_AsSignedCrosschainSubordinateTransaction(BigInteger.valueOf(sim.c3Process_val), subordinateTransactionsAndViewsForC3);

            // Call to contract 1
            Uint256[] params = new Uint256[]{};
            params = new Uint256[]{new Uint256(BigInteger.valueOf(val))};
            byte[][] subordinateTransactionsAndViewsForC1;
            if (sim.c1IsIfTaken) {
                subordinateTransactionsAndViewsForC1 = new byte[][]{subordinateViewC2, subordinateViewC5, subordinateTransC3};
            }
            else {
                subordinateTransactionsAndViewsForC1 = new byte[][] {subordinateViewC2};
            }
            System.out.println("  Executing Crosschain Transaction");
            transactionReceipt = this.contract1.doStuff_AsCrosschainTransaction(BigInteger.valueOf(val), subordinateTransactionsAndViewsForC1).send();
            System.out.println("  Tx Receipt: " + transactionReceipt.toString());
            assertTrue(transactionReceipt.isStatusOK());

            // TODO should check to see if contracts unlocked before fetching values.
            Thread.sleep(5000);

            checkExpectedValues(sim.val1, sim.val2, sim.val3, sim.val4, sim.val5, sim.val6);
        }
    }


    private void checkExpectedValues(long v1, long v2, long v3, long v4, long v5, long v6) throws Exception {
        System.out.println(" Check values have been set");
        BigInteger result = this.contract1.val().send();
        System.out.println("  Contract1.val = " + result.intValue()  + ", expecting " + v1);
        assertTrue(result.equals(BigInteger.valueOf(v1)));
        result = this.contract2.val().send();
        System.out.println("  Contract2.val = " + result.intValue()  + ", expecting " + v2);
        assertTrue(result.equals(BigInteger.valueOf(v2)));
        result = this.contract3.val().send();
        System.out.println("  Contract3.val = " + result.intValue()  + ", expecting " + v3);
        assertTrue(result.equals(BigInteger.valueOf(v3)));
        result = this.contract4.val().send();
        System.out.println("  Contract4.val = " + result.intValue()  + ", expecting " + v4);
        assertTrue(result.equals(BigInteger.valueOf(v4)));
        result = this.contract5.val().send();
        System.out.println("  Contract5.val = " + result.intValue()  + ", expecting " + v5);
        assertTrue(result.equals(BigInteger.valueOf(v5)));
        result = this.contract6.val().send();
        System.out.println("  Contract6.val = " + result.intValue()  + ", expecting " + v6);
        assertTrue(result.equals(BigInteger.valueOf(v6)));
    }




    static void assertTrue(boolean val) {
        if (!val) {
            throw new Error("Unexpected Result");
        }
    }


    class CallSimulator {
        long val1;
        long val2;
        long val3;
        long val4;
        long val5;
        long val6;

        boolean c1IsIfTaken = false;
        long c5Calculate_val1;
        long c5Calculate_val2;
        long c3Process_val;
        long c6Get_val;
        long c4Get_val;

        CallSimulator(long v1, long v2, long v3, long v4, long v5, long v6) {
            this.val1 = v1;
            this.val2 = v2;
            this.val3 = v3;
            this.val4 = v4;
            this.val5 = v5;
            this.val6 = v6;
        }

        void c1DoStuff(long _val) {
            long sc2Val = c2Get();
            this.val1 = sc2Val;

            this.c1IsIfTaken = false;
            if (_val > sc2Val) {
                this.c1IsIfTaken = true;
                long calc = c5Calculate(_val, sc2Val);
                c3Process(calc);
                this.val1 = calc;
            }


        }

        long c2Get() {
            return this.val2;
        }

        long c5Calculate(long _val1, long _val2) {
            // TODO this assumes this function is only called once with one paramter set.
            this.c5Calculate_val1 = _val1;
            this.c5Calculate_val2 = _val2;
            return this.val5 + _val1 + _val2;
        }

        void c3Process(long _val) {
            this.c3Process_val = _val;
            long sc3Val = c6Get(this.val3);
            this.val3 = _val + sc3Val;
        }

        long c6Get(long _val) {
            this.c6Get_val = _val;
            long sc2Val = c4Get(this.val6);
            return _val + sc2Val;
        }

        long c4Get(long _val) {
            this.c4Get_val = _val;
            return this.val4 + _val;
        }


    }

}
