# Crosschain example contracts

These contracts are to be used with the crosschain-enabled web3j and Besu code found elsewhere.

Sidechain1 calls TODO add description.

Crosschain.sol is a set of helper functions that abstract away most of the complication for generation of crosschain calls.

To generate Java wrapper code:

bash ./generatewrapper.sh

The output is in the build directory. NOTE: Check the solidity compiler output to ensure the compile did not fail!