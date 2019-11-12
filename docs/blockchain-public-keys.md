# Blockchain Public Keys

[Introduction](#introduction)

[BLS Threshold Signatures](#bls-threshold-signatures)

[Initial Public Key Upload Process](#initial-public-key-upload-process)

[Changing the Blockchain Public Key](changing-the-blockchain-public-key)

# Introduction
The Atomic Crosschain Transaction technology uses BLS threshold signatures to allow values to be verified as having come from one blockchain on another blockchain. 
Each validator node holds a share of the BLS private key. The corresponding public key is stored in a Crosschain Coordination Contract. 
This document describes the process of publishing the public key for a blockchain. The public key is known as the Blockchain Public Key.

# BLS Threshold Signatures
Distributed BLS threshold key generation and signing is described in detail in this video: [https://www.youtube.com/watch?v=XZTvBYG9pn4&t=597s](https://www.youtube.com/watch?v=XZTvBYG9pn4&t=597s).

At a high level, the protocol for key generation involves each validator node sending messages to each validator node, with the nodes combining the messages using a formula to create a set of BLS Private Key Shares. A subset of the shares, known as the threshold, need to be used to sign any message. The resulting signature does not indicate which nodes signed or what the threshold number of nodes that needed to sign is. 

A node that needs some information signed is called a Coordinating Node. The Coordinating Node sends data to be signed to all other nodes. Nodes that agree with the data, sign the data with their BLS Private Key Share, creating a Signature Share. The nodes send the Signature Share to the Coordinating Node. The Coordinating Node combines the Signature Shares from threshold number of validators to create the combined signature. Any entity with the Blockchain Public Key can verify the signature. Importantly, the Private Key Shares never need to be co-located to generate the signature.

# Initial Public Key Upload Process
The process for creating a Blockchain Public Key so Atomic Crosschain Transactions can take place involves first generating the key and then deploying it in the Crosschain Coordination Contract. The steps are:

1. Check to see if there is an existing key. This key may have been generated at the request of another entity. If there is an existing key, check to see if it has the required threshold. The JSON RPC API for fetching the Blockchain Public Key is `crosschainBlockchainPublicKey`. The API returns an RLP list containing the public key, the key version number and the threshold.


2. If no key is available, or if the key does not have the correct threshold, then the status of any in-progress key generation should be checked. 
The status can be determined using the JSON RPC API: `crosschainGetBlockchainPublicKeyGenerationStatus`. The status indicates that a key generation is in progress or not, the key version number of the key being generated and the threshold being generated for.

3. To request a key be generated call the JSON RPC API: `crosschainGenerateBlockchainKey(threshold)`. The key generation process will take in the order of hundreds of milliseconds or seconds, depending on the network latency between nodes. This request will fail if a key generation is already in progress.

4. Check key generation status using the process described above.

5. Fetch the public key using the `crosschainBlockchainPublicKey` API.

6. Create the blockchain in the Crosschain Coordination Contract using the `addSidechain` function. The participant must be a member of the Crosschain Coordination Contract to be able to call this function. See the Crosschain Coordination Contract documentation for information about how to become a member of a Crosschain Coordination Contract. The `addSidechain` function takes the blockchain ID number of the blockchain, the public key, the public key version number, the address of a voting contract, the voting period in terms of blocks of the Coordination Blockchain, and the Cut-Over time in blocks in which both a new and old public key are valid.

7. Call the JSON RPC API `crosschainCheckBlockchainPublicKey`. This will result in a signalling transaction being submitted to the blockchain. When each validator node processes the signalling transaction, they will set a flag to check the Crosschain Coordination Contract to work out which key to use.

# Changing the Blockchain Public Key
It may be desirable to change the threshold used with the Blockchain Public Key, for example if validator nodes are added to or removed from the blockchain. New public keys must be voted on by blockchain participants. To change the Blockchain Public Key do the following:

1. Follow steps 1 to 5 in [Initial Public Key Upload Process](#initial-public-key-upload-process).

2. Use the Crosschain Coordination Contract function `proposeVote` to propose the new public key. This takes the parameter of the public key and the public key version.

3. Depending on the voting algorithm, all of the participants, some of the participants, or just the proposer may need to vote. As part of the action of proposing the vote, the proposer automatically votes in agreement of the proposal. Participants can vote using the Crosschain Coordination Contract’s `vote` function.

4. Once the voting period has expired, any participant can finalise the vote by calling `actionVotes` function. The new public key is immediately active. The existing public key can continue to be used for Cut-Over time in blocks.

5. Follow step 7 in [Initial Public Key Upload Process](#initial-public-key-upload-process).


