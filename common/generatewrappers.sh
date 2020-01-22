#!/usr/bin/env bash

# abort if anything fails
set -e
set -o pipefail

rm -rf build
solc ../common-solidity/coordination-contract/contracts/CrosschainCoordinationV1.sol --bin --abi --optimize --overwrite -o build
solc ../common-solidity/coordination-contract/contracts/VotingAlgMajorityWhoVoted.sol --bin --abi --optimize --overwrite -o build
#ls -al build

# WEB3J=web3j
WEB3J=../../sidechains-web3j/besucodegen/build/install/besucodegen/bin/besucodegen
$WEB3J solidity generate -a=build/CrosschainCoordinationV1.abi -b=build/CrosschainCoordinationV1.bin -o=build -p=tech.pegasys.samples.sidechains.common.coordination.soliditywrappers
$WEB3J solidity generate -a=build/VotingAlgMajorityWhoVoted.abi -b=build/VotingAlgMajorityWhoVoted.bin -o=build -p=tech.pegasys.samples.sidechains.common.coordination.soliditywrappers

#ls -al build/tech/pegasys/samples/sidechains/common/coordination/soliditywrappers
