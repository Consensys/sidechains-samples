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
package tech.pegasys.samples.crosschain.simple.transaction;

/**
 * Simulate the call flow through the Solidity contracts and its results.
 *
 * The simulator is intended to give insight into the state of the system of contracts for the
 * purpose of * preparing the needed Subordinate Transactions/Views. This might necessitate the
 * simulator to give extra visibility * into otherwise private state of the system of contracts.
 *
 * For clarity, the interface to the simulator should be equivalent to the interface to the
 * system of contracts:
 *      - deployment of the contracts = construction of the simulator
 *      - calls to contract functions = calls to methods in the simulator
 */

/* This is an extremely simple sample, so the CallSimulator is not useful. It is just provided for completeness. */

class CallSimulator {

  Boolean c2val;

  CallSimulator(){
  }

  void c1Crosschain_setter() {
      c2val = true;
  }

  Boolean c2Get() {
      return c2val;
  }

  void c2clear() {
      c2val = false;
  }

}
