# Atomic Crosschain Transaction Sample Code
The repository contains sample code to demonstrate how to use Sidechains and 
Atomic Crosschain Transaction technology.

Atomic Crosschain Transactions are explained in our white paper [https://github.com/PegaSysEng/sidechains-samples/blob/master/AtomicCrosschainTransactions-WhitePaper.pdf]
and in this YouTube video [https://www.youtube.com/watch?v=MrrUHC-d6lc].

The samples in this repo should be used in conjunction with 
[https://github.com/PegaSysEng/sidechains-besu], a modified version of Hyperledger Besu
and [https://github.com/PegaSysEng/sidechains-web3j], a modified version of Web3J.

The samples:
- crosschain/atomic-swap-ether: Atomic Swap of Ether between blockchains.
- crosschain/three-chains-six-contracts: Crosschain transaction example containing a 
  complex call graph that includes subordinate transactions and subordinate views.

Other directories:
- common: Common code used across many samples. This includes the default simple set-up
  of the Crosschain Coordination Contract.
- crosschain/common-solidity: Crosschain Coordination Contract Truffle project and other
  pieces of Solidity code used across many samples related to crosschain.
- testall: Allows some key code paths of each of the samples to be run as a JUnit test.
- scripts: Ancillary scripts to help create a set of nodes in a blockchain and run them


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

