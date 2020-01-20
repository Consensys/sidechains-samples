#!/usr/bin/env bash

# abort if anything fails
set -e
set -o pipefail

rm -rf build
HERE=crosschain/simple
BUILDDIR=$HERE/build
CONTRACTSDIR=$HERE/contracts
PACKAGE=tech.pegasys.samples.crosschain.simple.soliditywrappers

cd ../.. # we change dirs so the allow-paths parameter can reach common-solidity/ ; it has restrictions/bugs on relative dirs.

# compiling one file also compiles its dependendencies. We use overwrite to avoid the related warnings.
solc $CONTRACTSDIR/Sc1Contract1.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
solc $CONTRACTSDIR/Sc2Contract2.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite

#ls -al $BUILDDIR

WEB3J=../sidechains-web3j/besucodegen/build/install/besucodegen/bin/besucodegen

$WEB3J solidity generate -cc -a=$BUILDDIR/Sc1Contract1.abi -b=$BUILDDIR/Sc1Contract1.bin -o=$BUILDDIR -p=$PACKAGE
$WEB3J solidity generate -cc -a=$BUILDDIR/Sc2Contract2.abi -b=$BUILDDIR/Sc2Contract2.bin -o=$BUILDDIR -p=$PACKAGE

cd $HERE

#ls -al build/tech/pegasys/samples/crosschain/simple/soliditywrappers
