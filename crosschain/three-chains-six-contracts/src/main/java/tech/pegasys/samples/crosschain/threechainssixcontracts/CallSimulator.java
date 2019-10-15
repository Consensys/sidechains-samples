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
package tech.pegasys.samples.crosschain.threechainssixcontracts;

/**
 * Simulate the call flow through the Solidity contracts. Based on the current values,
 * determine the expected values. These expected values can be used to create the nested
 * crosschain transaction.
 */
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

  // Simulate Sc1Contract1's doStuff function.
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

  // Simulate Sc2Contract2's get function.
  private long c2Get() {
    return this.val2;
  }

  // Simulate Sc3Contract5's calculate function.
  private long c5Calculate(long _val1, long _val2) {
    this.c5Calculate_val1 = _val1;
    this.c5Calculate_val2 = _val2;
    return this.val5 + _val1 + _val2;
  }

  // Simulate Sc2Contract3's process function.
  private void c3Process(long _val) {
    this.c3Process_val = _val;
    long sc3Val = c6Get(this.val3);
    this.val3 = _val + sc3Val;
  }

  // Simulate Sc3Contract6's get function.
  private long c6Get(long _val) {
    this.c6Get_val = _val;
    long sc2Val = c4Get(this.val6);
    return _val + sc2Val;
  }

  // Simulate Sc2Contract4's get function.
  private long c4Get(long _val) {
    this.c4Get_val = _val;
    return this.val4 + _val;
  }
}
