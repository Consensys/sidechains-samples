package tech.pegasys.samples.crosschain.simple.soliditywrappers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.CrosschainContext;
import org.web3j.tx.CrosschainContract;
import org.web3j.tx.CrosschainTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the org.web3j.codegen.CrosschainSolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/PegaSysEng/sidechains-web3j/tree/master/besucodegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.6.0-SNAPSHOT.
 */
@SuppressWarnings("rawtypes")
public class Sc1Contract1 extends CrosschainContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516101f23803806101f28339818101604052604081101561003357600080fd5b5080516020909101516000918255600180546001600160a01b0319166001600160a01b0390921691909117905561018290819061007090396000f3fe608060405234801561001057600080fd5b506004361061002b5760003560e01c806347066e2914610030575b600080fd5b61003861003a565b005b6000546001546040805160048152602481019091526020810180516001600160e01b0316635c70086f60e11b17905261007d92916001600160a01b03169061007f565b565b606083838360405160200180848152602001836001600160a01b03166001600160a01b0316815260200180602001828103825283818151815260200191508051906020019080838360005b838110156100e25781810151838201526020016100ca565b50505050905090810190601f16801561010f5780820380516001836020036101000a031916815260200191505b50945050505050604051602081830303815290604052905060006004825101905060008082846000600a600019f161014657600080fd5b505050505056fea265627a7a72305820814cad908528a4cbefb6c9d341b1f89d0f8c3bc28f0f4af58f35cf99a5ce04b964736f6c634300050a0032";

    public static final String FUNC_CROSSCHAIN_SETTER = "crosschain_setter";

    @Deprecated
    protected Sc1Contract1(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    protected Sc1Contract1(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> crosschain_setter() {
        final Function function = new Function(
                FUNC_CROSSCHAIN_SETTER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] crosschain_setter_AsSignedCrosschainSubordinateTransaction(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_CROSSCHAIN_SETTER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> crosschain_setter_AsCrosschainTransaction(final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_CROSSCHAIN_SETTER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    @Deprecated
    public static Sc1Contract1 load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Sc1Contract1(contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    public static Sc1Contract1 load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        return new Sc1Contract1(contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public static RemoteCall<Sc1Contract1> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _Sc2SidechainId, String _contract2) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_Sc2SidechainId), 
                new org.web3j.abi.datatypes.Address(160, _contract2)));
        return deployLockableContractRemoteCall(Sc1Contract1.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<Sc1Contract1> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _Sc2SidechainId, String _contract2) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_Sc2SidechainId), 
                new org.web3j.abi.datatypes.Address(160, _contract2)));
        return deployLockableContractRemoteCall(Sc1Contract1.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, crosschainContext);
    }

    public static RemoteCall<Sc1Contract1> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _Sc2SidechainId, String _contract2, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_Sc2SidechainId), 
                new org.web3j.abi.datatypes.Address(160, _contract2)));
        return deployLockableContractRemoteCall(Sc1Contract1.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<Sc1Contract1> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _Sc2SidechainId, String _contract2, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_Sc2SidechainId), 
                new org.web3j.abi.datatypes.Address(160, _contract2)));
        return deployLockableContractRemoteCall(Sc1Contract1.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, crosschainContext);
    }
}
