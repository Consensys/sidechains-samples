/*
 * Copyright 2018 ConsenSys AG.
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
package tech.pegasys.samples.sidechains.common.utils;

import java.security.DrbgParameters;
import java.security.SecureRandom;

import static java.security.DrbgParameters.Capability.PR_AND_RESEED;
import static tech.pegasys.samples.sidechains.common.utils.PersonalisationString.getPersonalizationString;


public class PRNGSecureRandom extends SecureRandom {
  private static final int SECURITY_STRENGTH = 256;
  private final SecureRandom rand;
  private final QuickEntropy quickEntropy;

  public PRNGSecureRandom() throws Exception {
    this.quickEntropy = new QuickEntropy();
    this.rand = SecureRandom.getInstance("DRBG", DrbgParameters.instantiation(
        SECURITY_STRENGTH, PR_AND_RESEED, getPersonalizationString()));
  }

  @Override
  public String getAlgorithm() {
    return rand.getAlgorithm();
  }

  @Override
  /*
    JDK SecureRandom.setSeed method is synchronized on some JDKs, it varies between versions.
    But sync at method level isn't needed as we are delegating to SP800SecureRandom and it uses a sync block.
  */
  @SuppressWarnings("UnsynchronizedOverridesSynchronized")
  public void setSeed(final byte[] seed) {
    rand.setSeed(seed);
  }

  @Override
  /*
    JDK SecureRandom.setSeed method is synchronized on some JDKs, it varies between versions.
    But sync at method level isn't needed as we are delegating to SP800SecureRandom and it uses a sync block.
  */
  @SuppressWarnings("UnsynchronizedOverridesSynchronized")
  public void setSeed(final long seed) {
    // As setSeed is called by the super constructor this can be called before the sp800SecureRandom
    // field is initialised
    if (rand != null) {
      rand.setSeed(seed);
    }
  }

  @Override
  /*
    JDK SecureRandom.nextBytes method is synchronized on some JDKs, it varies between versions.
    But sync at method level isn't needed as we are delegating to SP800SecureRandom and it uses a sync block.
  */
  @SuppressWarnings("UnsynchronizedOverridesSynchronized")
  public void nextBytes(final byte[] bytes) {
    rand.setSeed(quickEntropy.getQuickEntropy());
    rand.nextBytes(bytes);
  }

  @Override
  public byte[] generateSeed(final int numBytes) {
    rand.setSeed(quickEntropy.getQuickEntropy());
    return rand.generateSeed(numBytes);
  }
}
