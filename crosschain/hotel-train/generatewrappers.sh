#!/usr/bin/env bash
rm -rf build

HERE=crosschain/hotel-train
BUILDDIR=$HERE/build
CONTRACTSDIR=$HERE/contracts
PACKAGE=tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers

cd ../..


# compiling one file also compiles its dependendencies. We use overwrite to avoid the related warnings.
solc $CONTRACTSDIR/hotel/HotelRoom.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
solc $CONTRACTSDIR/hotel/HotelRouter.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
solc $CONTRACTSDIR/erc20/token/ERC20/ERC20.sol --allow-paths . --bin --abi --optimize -o $BUILDDIR --overwrite
ls -al $BUILDDIR

WEB3J=../sidechains-web3j/besucodegen/build/install/besucodegen/bin/besucodegen

$WEB3J solidity generate -a=$BUILDDIR/HotelRoom.abi -b=$BUILDDIR/HotelRoom.bin -o=$BUILDDIR -p=$PACKAGE
$WEB3J solidity generate -a=$BUILDDIR/HotelRouter.abi -b=$BUILDDIR/HotelRouter.bin -o=$BUILDDIR -p=$PACKAGE
$WEB3J solidity generate -a=$BUILDDIR/ERC20.abi -b=$BUILDDIR/ERC20.bin -o=$BUILDDIR -p=$PACKAGE

cd $HERE

ls -al build/tech/pegasys/samples/crosschain/hoteltrain/soliditywrappers
