# Simple samples

Note that the terms _sidechain_ and _blockchain_ are used in this sample. For the purposes
of this sample, they should be considered the same thing. 

## Introduction
This sample demonstrates:
 * ...

## Running the Sample Code

This sample is to be used with three the crosschain-enabled Hyperledger Besu instances
each operating as a node on three separate sidechains. See [../../README>md] 
for details of how to do this.  


## Details



# Modifying the Sampe Code
## Solidity Code
To update the Solidity code:
* Change the contracts in ./contracts
* Have sidechains-web3j (https://github.com/drinkcoffee/sidechains-web3j) at the same directory 
level as this repo. 
* Build the sidechains-web3j using `./gradlew build` with Java8
* Extract the crosschain code generator executable using `cd sidechains-web3j/besucodegen/build/distributions`. 
Then extract the tar file containing the executable `tar xvf NAME_OF_TAR.tar`
* From this directory, regenerate the Solidity wrapper code using `sh generatewrappers.sh` 
* Copy the generated Java files from `build/tech/pegasys/samples/crosschain/atomicswapether/soliditywrappers`
to `src/main/java/tech/pegasys/samples/crosschain/atomicswapether/soliditywrappers` using
`sh copygeneratedwrappers.sh`.
* Rebuild the sample code.
