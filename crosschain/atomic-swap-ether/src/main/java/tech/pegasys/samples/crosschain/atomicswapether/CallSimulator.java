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

import java.math.BigInteger;

/**
 * Simulate the call flow through the Solidity contracts. Based on the current values,
 * determine the expected values. These expected values can be used to create the nested
 * crosschain transaction.
 */
class CallSimulator {
  private static final Logger LOG = LogManager.getLogger(CallSimulator.class);

  public boolean atomicSwapSenderError;
  public boolean atomicSwapReceiverError;

  public BigInteger atomicSwapSender_Exchange_exchangeRate;
  public BigInteger atomicSwapReceiver_Exchange_amount;

  public static BigInteger DECIMAL_POINT = BigInteger.TWO.pow(64);
  private static BigInteger MAX_VALUE = BigInteger.TWO.pow(127);


  public BigInteger receiverBalanceInWei;
  public BigInteger senderBalanceInWei;
  public BigInteger accepterBalanceInWei;

  CallSimulator(BigInteger exchangeRate) {
    this.atomicSwapSender_Exchange_exchangeRate = exchangeRate;
  }

  void setValues(final BigInteger receiverBalanceInWei, final BigInteger accepterBalanceInWei, final BigInteger senderBalanceInWei) {
    this.receiverBalanceInWei = receiverBalanceInWei;
    this.accepterBalanceInWei = accepterBalanceInWei;
    this.senderBalanceInWei = senderBalanceInWei;
  }

  // Simulate AtomicSwapSender's exchange function.
  void exchange(final BigInteger amountInWei) {
    this.senderBalanceInWei.add(amountInWei);

    // require(msg.value < MAX_VALUE);
    atomicSwapSenderError = amountInWei.compareTo(MAX_VALUE) == 1;
    if (atomicSwapSenderError) {
      return;
    }

    // uint256 swapAmountOtherChain = msg.value * exchangeRate / DECIMAL_POINT;
    BigInteger temp = amountInWei.multiply(this.atomicSwapSender_Exchange_exchangeRate);
    this.atomicSwapReceiver_Exchange_amount = temp.divide(DECIMAL_POINT);

    //crosschainTransaction(receiverSidechainId, address(receiverContract), abi.encodeWithSelector(receiverContract.exchange.selector, swapAmountOtherChain) );
    receiverExchange(this.atomicSwapReceiver_Exchange_amount);
  }

  private void receiverExchange(BigInteger _amount) {
    // The amount transferred could be the same or less than the amount of Ether
    // in the contract.
    //require(address(this).balance >= _amount);
    this.atomicSwapReceiverError = this.receiverBalanceInWei.compareTo(_amount) == -1;
    if (this.atomicSwapReceiverError) {
      return;
    }

    // msg.sender.transfer(_amount);
    this.receiverBalanceInWei = this.receiverBalanceInWei.subtract(_amount);
    this.accepterBalanceInWei = this.accepterBalanceInWei.add(_amount);
  }

}
