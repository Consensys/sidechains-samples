# Sidechains and Atomic Crosschain Transaction Sample Code
The repository contains sample code to demonstrate how to use Atomic Crosschain Transaction technology.
The Atomic Crosschain Transaction white paper is available here: TODO.

The samples in this repo must be used in conjunction with 
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


