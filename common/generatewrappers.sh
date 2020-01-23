#!/usr/bin/env bash

# abort if anything fails
set -e
set -o pipefail

rm -rf build

HERE=common
BUILDDIR=$HERE/build
CONTRACTSDIR=common-solidity/coordination-contract/contracts
PACKAGE=tech.pegasys.samples.sidechains.common.coordination.soliditywrappers

cd ..

solc $CONTRACTSDIR/CrosschainCoordinationV1.sol --bin --abi --optimize --overwrite -o $BUILDDIR --allow-paths $PWD/common-solidity/crosschain-precompile-calls/contracts,$PWD/common-solidity/signature-verification/contracts
solc $CONTRACTSDIR/VotingAlgMajorityWhoVoted.sol --bin --abi --optimize --overwrite -o $BUILDDIR
# ls -al $BUILDDIR

# WEB3J=web3j
WEB3J=../sidechains-web3j/besucodegen/build/install/besucodegen/bin/besucodegen


$WEB3J solidity generate -a=$BUILDDIR/CrosschainCoordinationV1.abi -b=$BUILDDIR/CrosschainCoordinationV1.bin -o=$BUILDDIR -p=$PACKAGE
$WEB3J solidity generate -a=$BUILDDIR/VotingAlgMajorityWhoVoted.abi -b=$BUILDDIR/VotingAlgMajorityWhoVoted.bin -o=$BUILDDIR -p=$PACKAGE

cd $HERE
# ls -al build/tech/pegasys/samples/sidechains/common/coordination/soliditywrappers
