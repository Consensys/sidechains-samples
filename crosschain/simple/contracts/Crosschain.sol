/* SPDX-License-Identifier: Apache-2.0 */
pragma solidity ^0.5.0;

contract Crosschain {
    uint256 constant private LENGTH_OF_LENGTH_FIELD = 4;
    uint256 constant private LENGTH_OF_UINT256 = 0x20;

    /** Generic calling of functions across chains.
      * Combined with abi.encodeWithSelector, allows to use Solidity function types and arbitrary arguments.
      * @param encodedFunctionCall  = abi.encodeWithSelector(function.selector, param1, param2, ... )
      */
    function crosschainTransaction(uint256 sidechainId, address addr, bytes memory encodedFunctionCall) internal {

        bytes memory dataBytes = abi.encode(sidechainId, addr, encodedFunctionCall);
        // The "bytes" type has a 32 byte header containing the size in bytes of the actual data,
        // which is transparent to Solidity, so the bytes.length property doesn't report it.
        // But the assembly "call" instruction gets the underlying bytes of the "bytes" data type, so the length needs
        // to be corrected.
        // Also, as of Solidity 0.5.11 there is no sane way to convert a dynamic type to a static array.
        // Therefore we hackishly compensate the "bytes" length and deal with it inside the precompile.
        uint256 dataBytesRawLength = dataBytes.length + LENGTH_OF_LENGTH_FIELD;

        // Note: the tuple being encoded contains a dynamic type itself, which changes its internal representation;
        // but since it is abi.encoded in Solidity, it's transparent.
        // The problem only appears when using the wrapping "bytes" in Assembly.

        assembly {
        // Read: https://medium.com/@rbkhmrcr/precompiles-solidity-e5d29bd428c4
        //call(gasLimit, to, value, inputOffset, inputSize, outputOffset, outputSize)
        // SUBORDINATE_TRANSACTION_PRECOMPILE = 10. Inline assembler doesn't support constants.
            if iszero(call(not(0), 10, 0, dataBytes, dataBytesRawLength, 0, 0)) {
                revert(0, 0)
            }
        }
    }



    function crosschainViewUint256(uint256 sidechainId, address addr, bytes memory encodedFunctionCall) internal view returns (uint256) {

        bytes memory dataBytes = abi.encode(sidechainId, addr, encodedFunctionCall);
        // The "bytes" type has a 32 byte header containing the size in bytes of the actual data,
        // which is transparent to Solidity, so the bytes.length property doesn't report it.
        // But the assembly "call" instruction gets the underlying bytes of the "bytes" data type, so the length needs
        // to be corrected.
        // Also, as of Solidity 0.5.11 there is no sane way to convert a dynamic type to a static array.
        // Therefore we hackishly compensate the "bytes" length and deal with it inside the precompile.
        uint256 dataBytesRawLength = dataBytes.length + LENGTH_OF_LENGTH_FIELD;

        // Note: the tuple being encoded contains a dynamic type itself, which changes its internal representation;
        // but since it is abi.encoded in Solidity, it's transparent.
        // The problem only appears when using the wrapping "bytes" in Assembly.

        uint256[1] memory result;
        uint256 resultLength = LENGTH_OF_UINT256;

        assembly {
            // Read: https://medium.com/@rbkhmrcr/precompiles-solidity-e5d29bd428c4
            // and
            // https://www.reddit.com/r/ethdev/comments/7p8b86/it_is_possible_to_call_a_precompiled_contracts/
            //  staticcall(gasLimit, to, inputOffset, inputSize, outputOffset, outputSize)
            // SUBORDINATE_VIEW_PRECOMPILE = 11. Inline assembler doesn't support constants.
            if iszero(staticcall(not(0), 11, dataBytes, dataBytesRawLength, result, resultLength)) {
                revert(0, 0)
            }
        }

        return result[0];
    }

}
