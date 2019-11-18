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
 * Simulate the call flow through the Solidity contracts. Based on the current values (given as arguments),
 * determine the expected values. These expected values can be used to create the nested
 * crosschain transaction.
 */
class CallSimulator {

  Boolean val2;

  CallSimulator(Boolean v2) {
    this.val2 = v2;
  }

  // Simulate Sc1Contract1's doStuff function.
  void c1Crosschain_setter() {
    Boolean sc2Val = true;
  }

  // Simulate Sc2Contract2's get function.
  private Boolean c2Get() {
    return this.val2;
  }

}
