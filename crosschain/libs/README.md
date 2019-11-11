# Crosschain Web3J

This directory contains jars from Web3J enhanced to allow Crosschain. When / if the
features are integrated into official Web3J, this directory should be removed and the 
gradle files should be updated to use the official web3j.

The script gather.sh copies files from ../../../sidechains-web3j directories. The 
assumption is that the modified Web3J has been built in that directory. The modified 
Web3J code can be obtained from here:

https://github.com/PegaSysEng/sidechains-web3j

Build with `./gradlew installDist` on Java 8. 


