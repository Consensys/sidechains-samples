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
package tech.pegasys.samples.sidechains;

import org.junit.Test;
import tech.pegasys.samples.crosschain.atomicswapether.AtomicSwapEther;
import tech.pegasys.samples.crosschain.threechainssixcontracts.ThreeChainsSixContracts;

/**
 * The tests in this class require three sidechain nodes to be set-up:
 *
 * Sidechain ID   Port Number
 *
 * 11             8110
 * 22             8220
 * 33             8330
 */

public class CrosschainTests {

	@Test
	public void atomicSwapEther() throws Exception {
		AtomicSwapEther.automatedRun();
	}

	@Test
	public void threeChainsSixContracts() throws Exception {
		ThreeChainsSixContracts.automatedRun();
	}

}
