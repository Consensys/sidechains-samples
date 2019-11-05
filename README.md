# Sidechains and Atomic Crosschain Transaction Sample Code
The repository contains sample code to demonstrate how to use Sidechains and 
Atomic Crosschain Transaction technology.

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



