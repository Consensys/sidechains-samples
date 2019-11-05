#!/usr/bin/env bash
rm -rf build
solc ../crosschain/common-solidity/coordination-contract/contracts/CrosschainCoordinationV1.sol --bin --abi --optimize -o build
solc ../crosschain/common-solidity/coordination-contract/contracts/VotingAlgMajorityWhoVoted.sol --bin --abi --optimize -o build
ls -al build

# WEB3J=web3j
WEB3J=../../sidechains-web3j/besucodegen/build/distributions/besucodegen-4.6.0-SNAPSHOT/bin/besucodegen


$WEB3J solidity generate -a=build/CrosschainCoordinationV1.abi -b=build/CrosschainCoordinationV1.bin -o=build -p=tech.pegasys.samples.sidechains.common.coordination.soliditywrappers
$WEB3J solidity generate -a=build/VotingAlgMajorityWhoVoted.abi -b=build/VotingAlgMajorityWhoVoted.bin -o=build -p=tech.pegasys.samples.sidechains.common.coordination.soliditywrappers

ls -al build/tech/pegasys/samples/sidechains/common/coordination/soliditywrappers
