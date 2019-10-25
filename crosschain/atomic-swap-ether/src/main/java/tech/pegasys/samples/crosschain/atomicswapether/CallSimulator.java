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

import java.math.BigInteger;

/**
 * Simulate the call flow through the Solidity contracts. Based on the current values,
 * determine the expected values. These expected values can be used to create the nested
 * crosschain transaction.
 */
class CallSimulator {
  public BigInteger receiverBalanceInWei;
  public BigInteger senderBalanceInWei;
  public BigInteger accepterBalanceInWei;

  public boolean atomicSwapSenderError;
  public boolean atomicSwapReceiverError;

  public BigInteger atomicSwapSender_Exchange_exchangeRate;
  public BigInteger atomicSwapReceiver_Exchange_amount;

  public static BigInteger DECIMAL_POINT = BigInteger.TWO.pow(64);
  private static BigInteger MAX_VALUE = BigInteger.TWO.pow(127);


  /**
   * Simulate the sender contract constructor.
   *
   * @param exchangeRate Exchange rate used for converting between Ether on blockchain 2 and blockchain 1.
   */
  CallSimulator(BigInteger exchangeRate) {
    this.atomicSwapSender_Exchange_exchangeRate = exchangeRate;
  }

  /**
   * Set the values that the contracts will have available to them.
   *
   * @param receiverBalanceInWei Receiver contract's balance.
   * @param accepterBalanceInWei Entity accepting offer's balance.
   * @param senderBalanceInWei Sender contract's balance.
   */
  void setValues(final BigInteger receiverBalanceInWei, final BigInteger accepterBalanceInWei, final BigInteger senderBalanceInWei) {
    this.receiverBalanceInWei = receiverBalanceInWei;
    this.accepterBalanceInWei = accepterBalanceInWei;
    this.senderBalanceInWei = senderBalanceInWei;

    this.atomicSwapSenderError = false;
    this.atomicSwapReceiverError = false;
  }

  // Simulate AtomicSwapSender's exchange function.
  void exchange(final BigInteger amountInWei) {
    // require(msg.value < MAX_VALUE);
    atomicSwapSenderError = amountInWei.compareTo(MAX_VALUE) == 1;
    if (atomicSwapSenderError) {
      return;
    }

    // uint256 swapAmountOtherChain = msg.value * exchangeRate / DECIMAL_POINT;
    BigInteger swapAmountOtherChain = amountInWei.multiply(this.atomicSwapSender_Exchange_exchangeRate);
    swapAmountOtherChain = swapAmountOtherChain.divide(DECIMAL_POINT);

    //crosschainTransaction(receiverSidechainId, address(receiverContract), abi.encodeWithSelector(receiverContract.exchange.selector, swapAmountOtherChain) );
    receiverExchange(swapAmountOtherChain);
    if (this.atomicSwapReceiverError) {
      return;
    }

    this.atomicSwapReceiver_Exchange_amount = swapAmountOtherChain;

    this.senderBalanceInWei.add(amountInWei);
  }

  // Simulate AtomicSwapReceiver's exchange function.
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
