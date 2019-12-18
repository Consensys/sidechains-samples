#!/usr/bin/env bash
rm *.jar

WEB3JDIR=../../../sidechains-web3j
VER=4.6.0-SNAPSHOT
cp $WEB3JDIR/abi/build/libs/abi-$VER.jar web3j-abi.jar
cp $WEB3JDIR/besu/build/libs/besu-$VER.jar web3j-besu.jar
cp $WEB3JDIR/core/build/libs/core-$VER.jar web3j-core.jar
cp $WEB3JDIR/crypto/build/libs/crypto-$VER.jar web3j-crypto.jar
cp $WEB3JDIR/eea/build/libs/eea-$VER.jar web3j-eea.jar
cp $WEB3JDIR/rlp/build/libs/rlp-$VER.jar web3j-rlp.jar
cp $WEB3JDIR/tuples/build/libs/tuples-$VER.jar web3j-tuples.jar
cp $WEB3JDIR/utils/build/libs/utils-$VER.jar web3j-utils.jar

ls -l *.jar
