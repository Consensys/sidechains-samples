## General organization

Assumptions:
 - The repos sidechains-besu, sidechains-samples and sidechains-web3j all reside together under a same directory.
 - Node configuration and data files go into ~/crosschain_data.

## 1. Build Besu 

Inside of the sidechains-besu directory, build Besu with 

```
./gradlew installDist
```

This will skip tests (they take very long) and unpack the distribution file (which will leave the binary at  `sidechains-besu/build/install/besu/bin/besu`).

### Prepare to run the helper scripts

They are written in JavaScript for coherence with Truffle and such.  
In macOS, it's currently recommended to use Node v10, not the latest v12. (Truffle suffers the same issue)
```bash
brew install node@10
``` 

Then install the libraries needed by the scripts: inside of the sidechains-samples directory, run:
```bash
npm install scripts
```

## 2. Configuration of blockchain nodes

### Quick start: 
To create 2 blockchains with chainIds 11 and 22 and with one node in each:
```bash
scripts/create_chain.js 11 1 
scripts/create_chain.js 22 1
```
 ----------------
#### Explanation:
 
You will need to run 2 crosschain-enabled blockchains, each of them with at least 1 node.

To ease the related configuration, the script `scripts/create_chain.js <chainId> <numNodes>` creates a set of configuration files (`genesis.json` and `config.toml`) for the specified number of nodes `numNodes`(currently) in one blockchain. You can specify a `chainId` <99. The data for each node will be stored in the directory ~/crosschain_data/chain***N***/node***M***.  For details on the process, see point 4.

You can run the script for as many blockchains as you need. Note that it will delete any previously existing configuration for the given `chainId`, so if you need to re-create a chain, just re-run the script.

Each node directory will contain the node's key and data directory.

Each node will listen for RPC at port `8000 + chainId*10 + nodeN`. For example, node 0 of chain 22 will listen at port 8220. The script will remind you of this when it finishes.



## 3. Run each node with the prepared config files

### Quick start: 

```bash
scripts/run_multichain.sh
```
--------------------
### Explanation:
The script `scripts/run_multichain.sh` runs multiple nodes in a single terminal window using tmux. 
Each node is run through another script, `scripts/run_node.js <chainId>`, which invokes Besu with the appropriate arguments to use each data directory. 


------------  

## FYI: creating a Genesis File

As stated, this is all done for you by the script `scripts/create_chain.js`, but as a FYI here is the manual process to create an IBFT2 crosschain-enabled genesis file with the account address of the validator nodes (as explained in the Besu docs).

The `genesis_template.json` file can be used as a template, but needs to be customized with the address of your node/s. To do so:
1. Ensure that the nodes intended to be validators have their keys somewhere where they will not be deleted randomly - else they will be regenerated and you'll have to reconfigure the genesis file. 
2. Obtain each node's account address and put them all into a valid JSON file
3. Make Besu RLP-encode the extraData structure, built containing the addresses in the JSON file
3. Copy the extradata text into the "extradata" field of the genesis file

## 4. Creating Threshold Keys

Run CrosschainPocGenerateKeys.java file to generate keys

