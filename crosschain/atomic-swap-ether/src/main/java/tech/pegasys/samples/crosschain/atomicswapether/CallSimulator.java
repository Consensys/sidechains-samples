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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Simulate the call flow through the Solidity contracts. Based on the current values,
 * determine the expected values. These expected values can be used to create the nested
 * crosschain transaction.
 */
class CallSimulator {
  public boolean atomicSwapSenderError1;

  public BigInteger atomicSwapSender_Exchange_exchangeRate;
  public BigInteger atomicSwapReceiver_Exchange_amount;

  private BigInteger DECIMAL_POINT = BigInteger.TWO.pow(128);

  CallSimulator() {
    this.atomicSwapSender_Exchange_exchangeRate = AtomicSwapEther.getAdjustedExchangeRate(AtomicSwapEther.EXCHANGE_RATE);
  }

  // Simulate AtomicSwapSender's exchange function.
  void exchange(final BigInteger amountInWei, final BigInteger receiverBalanceInWei) {
    atomicSwapSenderError1 = receiverBalanceInWei.compareTo(DECIMAL_POINT) == 1;
    if (atomicSwapSenderError1) {
      return;
    }

    BigInteger amountGivenReceiver = receiverBalanceInWei.divide(this.atomicSwapSender_Exchange_exchangeRate);
    BigInteger amountToTransferInWei = amountInWei;
    if (amountGivenReceiver.compareTo(amountInWei) == -1) {
      amountToTransferInWei = amountGivenReceiver;
    }

    this.atomicSwapReceiver_Exchange_amount = this.atomicSwapSender_Exchange_exchangeRate.multiply(amountToTransferInWei);
  }
}
