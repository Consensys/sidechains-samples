# Atomic Crosschain Transaction Sample Code
The repository contains sample code to demonstrate how to use Sidechains and 
Atomic Crosschain Transaction technology.

Atomic Crosschain Transactions are explained in our white paper 
[https://github.com/PegaSysEng/sidechains-samples/blob/master/AtomicCrosschainTransactions-WhitePaper.pdf]
and in this YouTube video [https://www.youtube.com/watch?v=MrrUHC-d6lc].

The samples in this repo should be used in conjunction with 
[https://github.com/PegaSysEng/sidechains-besu], a modified version of Hyperledger Besu
and [https://github.com/PegaSysEng/sidechains-web3j], a modified version of Web3J. To start:

``` 
--- install JDK8, JDK 11, node version 10 ---
--- then:                                 ---
mkdir crosschain
git clone https://github.com/PegaSysEng/sidechains-samples.git
git clone https://github.com/PegaSysEng/sidechains-web3j.git
git clone https://github.com/PegaSysEng/sidechains-besu.git
--- set the PATH to point to JDK 8        ---
--- then:                                  ---
cd sidechains-web3j
./gradlew installDist
--- set the PATH to point to JDK 11       ---
--- then:                                  ---
cd ../sidechains-besu
./gradlew installDist
cd ../sidechains-samples
scripts/create_chain.js 11 1 
scripts/create_chain.js 22 1
scripts/create_chain.js 33 1
```

To test the sample code:
```
./gradlew integrationTest
```

To run any of the samples:
1. Run three Hyperledger Besu nodes, one node per blockchain. The simplest way
to do this is run the command: ```scripts/run_multichain.sh```. Alternatively,
using three command windows execute the commands:
```scripts/run_node.sh 11```,
```scripts/run_node.sh 22``` and,
```scripts/run_node.sh 33```.
2. Before the first time the samples are run, configure the Multichain Node: 
   ```
   ./gradlew build
   cd crosschain/multichain/build/distributions
   tar xvf tar xvf crosschain-multichain.tar
   cd crosschain-multichain/bin
   sh crosschain-multichain config auto
   cd ../../../../../..
   ```
3. Execute any of the samples either in an IDE of by running the binary
in a similar way to how the Multichain Manager is run in the code box above.  

The samples:
- crosschain/atomic-swap-ether: Atomic Swap of Ether between blockchains.
- crosschian/hotel-train: Allows a hotel room and a train seat to be booked and paid for.
- crosschain/multichain-manager: Multichain Node management tool.
- crosschain/simple: Samples showing how to do specific operations.
- crosschain/three-chains-six-contracts: Crosschain transaction example containing a 
  complex call graph that includes subordinate transactions and subordinate views.

Other directories:
- common: Common code used across many samples. This includes the default simple set-up
  of the Crosschain Coordination Contract.
- crosschain/common-solidity: Crosschain Coordination Contract Truffle project and other
  pieces of Solidity code used across many samples related to crosschain.
- testall: Allows some key code paths of each of the samples to be run as a JUnit test.
- scripts: Ancillary scripts to help create a set of nodes in a blockchain and run them.


Limitations of the PoC: 
- The Sidechains-Besu repo and Sidechains-Web3J repo were forked from their respective upstream
 repositories in September 2019, and as such do not incorporate the latest enhancements and 
 defect fixes.
- This technology has been created as a PoC. The Atomic Crosschain Transaction technology has
 some level of test coverage, though not as much as would be expected with production software.
- Only one node per blockchain is currently supported. The DevP2P sub-protocol to facilitate 
distributed threshold key generation and distributed signing needs to be completed to allow 
for multiple nodes per blockchain.
- The signature verification of Subordinate View Result messages and Subordinate Transaction 
Ready messages has been disabled. This is a result of the current (Java) implementation of the 
ECC BLS pairing code taking a long time to execute, resulting in the test system timing out. 
The current implementation needs to be replaced with a faster implementation.
- The on-chain signature verification of Start, Commit and Ignore messages has 
been disabled. This is due to some inconsistencies that are yet to be resolved 
in the expected and actual to-be-signed data. Analysis has shown this is 
related to a data encoding issue. The on-chain signature verification code itself is working.

