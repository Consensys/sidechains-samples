#!/usr/bin/env bash
rm -rf build

HERE=crosschain/atomic-swap-ether
BUILDDIR=$HERE/build
CONTRACTSDIR=$HERE/contracts
PACKAGE=tech.pegasys.samples.crosschain.atomicswapether.soliditywrappers

cd ../..


# compiling one file also compiles its dependendencies. We use overwrite to avoid the related warnings.
solc $CONTRACTSDIR/AtomicSwapReceiver.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
solc $CONTRACTSDIR/AtomicSwapSender.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
solc $CONTRACTSDIR/AtomicSwapRegistration.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
ls -al $BUILDDIR

WEB3J=../sidechains-web3j/besucodegen/build/install/besucodegen/bin/besucodegen

$WEB3J solidity generate -cc -a=$BUILDDIR/AtomicSwapReceiver.abi -b=$BUILDDIR/AtomicSwapReceiver.bin -o=$BUILDDIR -p=$PACKAGE
$WEB3J solidity generate -cc -a=$BUILDDIR/AtomicSwapSender.abi -b=$BUILDDIR/AtomicSwapSender.bin -o=$BUILDDIR -p=$PACKAGE
$WEB3J solidity generate -a=$BUILDDIR/AtomicSwapRegistration.abi -b=$BUILDDIR/AtomicSwapRegistration.bin -o=$BUILDDIR -p=$PACKAGE

cd $HERE

ls -al build/tech/pegasys/samples/crosschain/atomicswapether/soliditywrappers
