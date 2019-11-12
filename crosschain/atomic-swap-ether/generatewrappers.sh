#!/usr/bin/env bash
rm -rf build
solc contracts/AtomicSwapReceiver.sol --bin --abi --optimize -o build
solc contracts/AtomicSwapSender.sol --bin --abi --optimize -o build
solc contracts/AtomicSwapRegistration.sol --bin --abi --optimize -o build
ls -al build

# WEB3J=web3j
# WEB3J=../../../sidechains-web3j/besucodegen/build/distributions/besucodegen-4.6.0-SNAPSHOT/bin/besucodegen
WEB3J=../../../sidechains-web3j/besucodegen/build/install/besucodegen/bin/besucodegen


$WEB3J solidity generate -cc -a=build/AtomicSwapReceiver.abi -b=build/AtomicSwapReceiver.bin -o=build -p=tech.pegasys.samples.crosschain.atomicswapether.soliditywrappers
$WEB3J solidity generate -cc -a=build/AtomicSwapSender.abi -b=build/AtomicSwapSender.bin -o=build -p=tech.pegasys.samples.crosschain.atomicswapether.soliditywrappers
$WEB3J solidity generate -a=build/AtomicSwapRegistration.abi -b=build/AtomicSwapRegistration.bin -o=build -p=tech.pegasys.samples.crosschain.atomicswapether.soliditywrappers

ls -al build/tech/pegasys/samples/crosschain/atomicswapether/soliditywrappers
