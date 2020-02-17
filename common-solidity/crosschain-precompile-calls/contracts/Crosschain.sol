/*
 * Copyright 2018 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
pragma solidity ^0.5.0;

contract Crosschain {
    uint256 constant private LENGTH_OF_LENGTH_FIELD = 4;
    uint256 constant private LENGTH_OF_UINT32 = 4;
    uint256 constant private LENGTH_OF_UINT256 = 0x20;
    uint256 constant private LENGTH_OF_BYTES32 = 0x20;
    uint256 constant private LENGTH_OF_ADDRESS = 20;

    // Value to be passed ot the getInfo precompile.
    uint32 constant private GET_INFO_CROSSCHAIN_TRANSACTION_TYPE = 0;
    uint32 constant private GET_INFO_BLOCKCHAIN_ID = 1;
    uint32 constant private GET_INFO_COORDINAITON_BLOCKHCAIN_ID = 2;
    uint32 constant private GET_INFO_COORDINAITON_CONTRACT_ADDRESS = 3;
    uint32 constant private GET_INFO_ORIGINATING_BLOCKCHAIN_ID = 4;
    uint32 constant private GET_INFO_FROM_BLOCKCHAIN_ID = 5;
    uint32 constant private GET_INFO_FROM_CONTRACT_ADDRESS = 6;
    uint32 constant private GET_INFO_CROSSCHAIN_TRANSACTION_ID = 7;

    address constant private PRECOMPILE_SUBORDINATE_VIEW = address(11);
    address constant private PRECOMPILE_SUBORDINATE_TRANSACTION = address(10);
    address constant private PRECOMPILE_IS_LOCKED = address(121);


    /**
      * Functions allowing for generic calling of functions across chains.
      *
      * Combined with abi.encodeWithSelector, these functions allow to use normal Solidity function types and arbitrary arguments in a
      * convenient way.
      * For example, to call the function foo(arg1, arg2, ...) across chains, use one of the convenience functions with
      *     encodedFunctionCall = abi.encodeWithSelector(foo.selector, arg1, arg2, ... )
      */

    /**
      * Generic call for crosschain transactions.
      *
      * Since transactions return nothing, and the encodedFunctionCall accepts any arguments, this call generalizes all
      * transactions.
      */
    function crosschainTransaction(uint256 sidechainId, address addr, bytes memory encodedFunctionCall) internal {
        bytes memory dataBytes = abi.encode(sidechainId, addr, encodedFunctionCall);
        uint256 dataBytesRawLength = calculateRawLength(dataBytes);
        address a = PRECOMPILE_SUBORDINATE_TRANSACTION;

        assembly {
        // Read: https://medium.com/@rbkhmrcr/precompiles-solidity-e5d29bd428c4
        //call(gasLimit, to, value, inputOffset, inputSize, outputOffset, outputSize)
            if iszero(call(not(0), a, 0, dataBytes, dataBytesRawLength, 0, 0)) {
                revert(0, 0)
            }
        }
    }

    /**
      * Generic call for crosschain views with no parameters and returning an Uint256.
      */
    function crosschainViewUint256(uint256 sidechainId, address addr, bytes memory encodedFunctionCall) internal view returns (uint256) {
        bytes memory dataBytes = abi.encode(sidechainId, addr, encodedFunctionCall);
        uint256 dataBytesRawLength = calculateRawLength(dataBytes);

        uint256[1] memory result;
        uint256 resultLength = LENGTH_OF_UINT256;
        address a = PRECOMPILE_SUBORDINATE_VIEW;

        assembly {
            if iszero(staticcall(not(0), a, dataBytes, dataBytesRawLength, result, resultLength)) {
                revert(0, 0)
            }
        }

        return result[0];
    }

    /**
     * Indicates if the contract at the address is locked.
     */
    function crosschainIsLocked(address addr) internal view returns (bool) {
        bytes memory dataBytes = abi.encode(addr);
        uint256 dataBytesRawLength = calculateRawLength(dataBytes);

        uint256[1] memory result;
        uint256 resultLength = LENGTH_OF_UINT256;
        address a = PRECOMPILE_IS_LOCKED;

        assembly {
            if iszero(staticcall(not(0), a, dataBytes, dataBytesRawLength, result, resultLength)) {
                revert(0, 0)
            }
        }

        return result[0] != 0;
    }

    function calculateRawLength(bytes memory b) internal pure returns (uint256){
        // The "bytes" type has a 32 byte header containing the size in bytes of the actual data,
        // which is transparent to Solidity, so the bytes.length property doesn't report it.
        // But the assembly "call" instruction gets the underlying bytes of the "bytes" data type, so the length needs
        // to be corrected.
        // Also, as of Solidity 0.5.11 there is no sane way to convert a dynamic type to a static array.
        // Therefore we hackishly compensate the "bytes" length and deal with it inside the precompile.

        // FYI: at this point, b is an abi-encoded tuple of multiple types, the last of which is again a "bytes" array, which is dynamic.
        // This affects the tuple's internal representation; but since it was abi-encoded in Solidity, it's still transparent to us.
        // The problem we are fixing here only appears when using b in Assembly.

        return b.length + LENGTH_OF_LENGTH_FIELD;
    }

    /**
     * Determine the type of crosschain transaction being executed.
     *
     * The return value will be one of:
     *  ORIGINATING_TRANSACTION = 0
     *  SUBORDINATE_TRANSACTION = 1
     *  SUBORDINATE_VIEW = 2
     *  ORIGINATING_LOCKABLE_CONTRACT_DEPLOY = 3
     *  SUBORDINATE_LOCKABLE_CONTRACT_DEPLOY = 4
     *  SINGLECHAIN_LOCKABLE_CONTRACT_DEPLOY = 5
     *  UNLOCK_COMMIT_SIGNALLING_TRANSACTION = 6
     *  UNLOCK_IGNORE_SIGNALLING_TRANSACTION = 7
     *
     * @return the type of crosschain transaction.
     */
    function crosschainGetInfoTransactionType() internal view returns (uint32) {
        uint256 inputLength = LENGTH_OF_UINT32;
        uint32[1] memory input;
        input[0] = GET_INFO_CROSSCHAIN_TRANSACTION_TYPE;

        uint32[1] memory result;
        uint256 resultLength = LENGTH_OF_UINT32;

        assembly {
        // Read: https://medium.com/@rbkhmrcr/precompiles-solidity-e5d29bd428c4
        // and
        // https://www.reddit.com/r/ethdev/comments/7p8b86/it_is_possible_to_call_a_precompiled_contracts/
        //  staticcall(gasLimit, to, inputOffset, inputSize, outputOffset, outputSize)
        // GET_INFO_PRECOMPILE = 250. Inline assembler doesn't support constants.
            if iszero(staticcall(not(0), 250, input, inputLength, result, resultLength)) {
                revert(0, 0)
            }
        }

        return result[0];
    }



    /**
     * Get information about the transaction currently executing.
     *
     * @return Blockchain ID of this blockchain.
     */
    function crosschainGetInfoBlockchainId() internal view returns (uint256) {
        /* should probably be changed to use EIP-1344 once it is supported by sidechains-Besu */
        return getInfoBlockchainId(GET_INFO_BLOCKCHAIN_ID);
    }

    /**
     * Get information about the transaction currently executing.
     *
     * @return Blockchain ID of the Coordination Blockchain.
     *   0x00 is returned it the current transaction is a Single Blockchain Lockable
     *   Contract Deploy.
     */
    function crosschainGetInfoCoordinationBlockchainId() internal view returns (uint256) {
        return getInfoBlockchainId(GET_INFO_COORDINAITON_BLOCKHCAIN_ID);
    }

    /**
     * Get information about the transaction currently executing.
     *
     * @return Blockchain ID of the Originating Blockchain.
     *   0x00 is returned it the current transaction is an Originating Transaction or a
     *   Single Blockchain Lockable Contract Deploy.
     */
    function crosschainGetInfoOriginatingBlockchainId() internal view returns (uint256) {
        return getInfoBlockchainId(GET_INFO_ORIGINATING_BLOCKCHAIN_ID);
    }

    /**
     * Get information about the transaction currently executing.
     *
     * @return Blockchain ID of the blockchain from which this function was called.
     *   0x00 is returned it the current transaction is an Originating Transaction or a
     *   Single Blockchain Lockable Contract Deploy.
     */
    function crosschainGetInfoFromBlockchainId() internal view returns (uint256) {
        return getInfoBlockchainId(GET_INFO_FROM_BLOCKCHAIN_ID);
    }

    /**
     * Get information about the transaction currently executing.
     *
     * @return Crosschain Transaction Identifier.
     *   0x00 is returned it the current transaction is a Single Blockchain Lockable
     *   Contract Deploy.
     */
    function crosschainGetInfoCrosschainTransactionId() internal view returns (uint256) {
        return getInfoBlockchainId(GET_INFO_CROSSCHAIN_TRANSACTION_ID);
    }

    function getInfoBlockchainId(uint256 _requestedId) private view returns (uint256) {
        uint256 inputLength = LENGTH_OF_UINT256;
        uint256[1] memory input;
        input[0] = _requestedId;

        uint256[1] memory result;
        uint256 resultLength = LENGTH_OF_UINT256;

        assembly {
        // GET_INFO_PRECOMPILE = 120. Inline assembler doesn't support constants.
            if iszero(staticcall(not(0), 120, input, inputLength, result, resultLength)) {
                revert(0, 0)
            }
        }
        return result[0];
    }


    /**
     * Get information about the transaction currently executing.
     *
     * @return Crosschain Coordination Contract address.
     *   0x00 is returned it the current transaction is a Single Blockchain Lockable
     *   Contract Deploy.
     */
    function crosschainGetInfoCoordinationContractAddress() internal view returns (address) {
        return getInfoAddress(GET_INFO_COORDINAITON_CONTRACT_ADDRESS);
    }

    /**
     * Get information about the transaction currently executing.
     *
     * @return Address of contract from which this function was called.
     *   0x00 is returned it the current transaction is an Originating Transaction or a
     *   Single Blockchain Lockable Contract Deploy.
     */
    function crosschainGetInfoFromAddress() internal view returns (address) {
        return getInfoAddress(GET_INFO_FROM_CONTRACT_ADDRESS);
    }


    function getInfoAddress(uint256 _requestedAddress) private view returns (address) {
        uint256 inputLength = LENGTH_OF_UINT256;
        uint256[1] memory input;
        input[0] = _requestedAddress;

        // The return type is an address. However, we need to specify that we want a whole
        // Ethereum word copied or we will end up with 20 bytes of th eaddress being masked off.
        address[1] memory result;
        uint256 resultLength = LENGTH_OF_UINT256;

        assembly {
        // GET_INFO_PRECOMPILE = 120. Inline assembler doesn't support constants.
            if iszero(staticcall(not(0), 120, input, inputLength, result, resultLength)) {
                revert(0, 0)
            }
        }
        return result[0];
    }

}
