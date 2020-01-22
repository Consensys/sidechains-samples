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
package tech.pegasys.samples.crosschain.simple.views;

import java.math.BigInteger;

/**
 * Simulate the call flow through the Solidity contracts and its results.
 *
 * For clarity, the interface to the simulator should be equivalent to the interface to the system of contracts:
 *  - deployment of the contracts = construction of CallSimulator, with each contract being an inner class
 *  - calls to contract functions = calls to methods in the inner classes
 *
 * The simulator is intended to help the programmer plan the state of the system of contracts for the purpose of
 * preparing the needed Subordinate Transactions/Views. This might necessitate the simulator to give extra visibility
 * into solidity-private state of the system of contracts.
 */
class CallSimulator {

    Contract1 c1;
    Contract2 c2;

    CallSimulator(){
        c1 = new Contract1();
        c2 = new Contract2();
    }

    class Contract1 {

        BigInteger localValue;

        void crosschain_getUint256_transaction() {
            localValue = c2.getUint256();
        }

    }

    class Contract2 {

        BigInteger val2;

        public BigInteger getUint256() {
            return val2;
        }

        public void setUint256(BigInteger value) {
            val2 = value;
        }

    }

}
