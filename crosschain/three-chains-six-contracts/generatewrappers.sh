#!/usr/bin/env bash
rm -rf build
HERE=crosschain/three-chains-six-contracts
BUILDDIR=$HERE/build
CONTRACTSDIR=$HERE/contracts
PACKAGE=tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers

cd ../..

# compiling one file also compiles its dependendencies. We use overwrite to avoid the related warnings.
solc $CONTRACTSDIR/Sc1Contract1.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
solc $CONTRACTSDIR/Sc2Contract2.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
solc $CONTRACTSDIR/Sc2Contract3.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
solc $CONTRACTSDIR/Sc2Contract4.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
solc $CONTRACTSDIR/Sc3Contract5.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
solc $CONTRACTSDIR/Sc3Contract6.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite

ls -al $BUILDDIR

WEB3J=../sidechains-web3j/besucodegen/build/install/besucodegen/bin/besucodegen


$WEB3J solidity generate -cc -a=$BUILDDIR/Sc1Contract1.abi -b=$BUILDDIR/Sc1Contract1.bin -o=$BUILDDIR -p=$PACKAGE
$WEB3J solidity generate -cc -a=$BUILDDIR/Sc2Contract2.abi -b=$BUILDDIR/Sc2Contract2.bin -o=$BUILDDIR -p=$PACKAGE
$WEB3J solidity generate -cc -a=$BUILDDIR/Sc2Contract3.abi -b=$BUILDDIR/Sc2Contract3.bin -o=$BUILDDIR -p=$PACKAGE
$WEB3J solidity generate -cc -a=$BUILDDIR/Sc2Contract4.abi -b=$BUILDDIR/Sc2Contract4.bin -o=$BUILDDIR -p=$PACKAGE
$WEB3J solidity generate -cc -a=$BUILDDIR/Sc3Contract5.abi -b=$BUILDDIR/Sc3Contract5.bin -o=$BUILDDIR -p=$PACKAGE
$WEB3J solidity generate -cc -a=$BUILDDIR/Sc3Contract6.abi -b=$BUILDDIR/Sc3Contract6.bin -o=$BUILDDIR -p=$PACKAGE

cd $HERE

ls -al build/tech/pegasys/samples/crosschain/threechainssixcontracts/soliditywrappers
