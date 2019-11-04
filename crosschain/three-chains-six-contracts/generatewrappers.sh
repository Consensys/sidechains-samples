#!/usr/bin/env bash
rm -rf build
solc contracts/Sc1Contract1.sol --bin --abi --optimize -o build
solc contracts/Sc2Contract2.sol --bin --abi --optimize -o build
solc contracts/Sc2Contract3.sol --bin --abi --optimize -o build
solc contracts/Sc2Contract4.sol --bin --abi --optimize -o build
solc contracts/Sc3Contract5.sol --bin --abi --optimize -o build
solc contracts/Sc3Contract6.sol --bin --abi --optimize -o build
solc ../common-solidity/coordination-contract/contracts/CrosschainCoordinationV1.sol --bin --abi --optimize -o build
solc ../common-solidity/coordination-contract/contracts/VotingAlgMajorityWhoVoted.sol --bin --abi --optimize -o build

ls -al build

# WEB3J=web3j
WEB3J=../../../sidechains-web3j/besucodegen/build/distributions/besucodegen-4.6.0-SNAPSHOT/bin/besucodegen


$WEB3J solidity generate -cc -a=build/Sc1Contract1.abi -b=build/Sc1Contract1.bin -o=build -p=tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers
$WEB3J solidity generate -cc -a=build/Sc2Contract2.abi -b=build/Sc2Contract2.bin -o=build -p=tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers
$WEB3J solidity generate -cc -a=build/Sc2Contract3.abi -b=build/Sc2Contract3.bin -o=build -p=tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers
$WEB3J solidity generate -cc -a=build/Sc2Contract4.abi -b=build/Sc2Contract4.bin -o=build -p=tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers
$WEB3J solidity generate -cc -a=build/Sc3Contract5.abi -b=build/Sc3Contract5.bin -o=build -p=tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers
$WEB3J solidity generate -cc -a=build/Sc3Contract6.abi -b=build/Sc3Contract6.bin -o=build -p=tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers
$WEB3J solidity generate -a=build/CrosschainCoordinationV1.abi -b=build/CrosschainCoordinationV1.bin -o=build -p=tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers
$WEB3J solidity generate -a=build/VotingAlgMajorityWhoVoted.abi -b=build/VotingAlgMajorityWhoVoted.bin -o=build -p=tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers

ls -al build/tech/pegasys/samples/crosschain/threechainssixcontracts/soliditywrappers
