package tech.pegasys.samples.crosschain.threechainsfivecontracts.soliditywrappers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.CrosschainContract;
import org.web3j.tx.CrosschainTransactionManager;
import org.web3j.tx.TransactionManager;
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
public class Sc3Contract6 extends CrosschainContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516102aa3803806102aa8339818101604052604081101561003357600080fd5b5080516020909101516000918255600180546001600160a01b0319166001600160a01b0390921691909117905561023a90819061007090396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80633c6bb436146100465780633d4197f0146100605780639507d39a1461007f575b600080fd5b61004e61009c565b60408051918252519081900360200190f35b61007d6004803603602081101561007657600080fd5b50356100a2565b005b61004e6004803603602081101561009557600080fd5b50356100a7565b60025481565b600255565b60008054600154600254604080516024808201939093528151808203909301835260440190526020810180516001600160e01b0316634a83e9cd60e11b17905283926101009290916001600160a01b0390911690610109565b90920192915050565b6000606084848460405160200180848152602001836001600160a01b03166001600160a01b0316815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561016e578181015183820152602001610156565b50505050905090810190601f16801561019b5780820380516001836020036101000a031916815260200191505b5094505050505060405160208183030381529060405290506000815160200190506101c46101e7565b6020808284866000600b600019f16101db57600080fd5b50519695505050505050565b6040518060200160405280600190602082028038833950919291505056fea265627a7a7230582002b83de87b2d80f4db3565bbf17a67d1908a0d73f4ff219644bc27c13ca4380164736f6c634300050a0032";

    public static final String FUNC_VAL = "val";

    public static final String FUNC_SETVAL = "setVal";

    public static final String FUNC_GET = "get";

    @Deprecated
    protected Sc3Contract6(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    protected Sc3Contract6(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<BigInteger> val() {
        final Function function = new Function(FUNC_VAL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] val_AsSignedCrosschainSubordinateView(final byte[][] nestedSubordinateViews) throws IOException {
        final Function function = new Function(FUNC_VAL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, nestedSubordinateViews);
    }

    public RemoteFunctionCall<TransactionReceipt> setVal(BigInteger _val) {
        final Function function = new Function(
                FUNC_SETVAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_val)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] setVal_AsSignedCrosschainSubordinateTransaction(BigInteger _val, final byte[][] nestedSubordinateTransactionsAndViews) throws IOException {
        final Function function = new Function(
                FUNC_SETVAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_val)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, nestedSubordinateTransactionsAndViews);
    }

    public RemoteFunctionCall<TransactionReceipt> setVal_AsCrosschainTransaction(BigInteger _val, final byte[][] nestedSubordinateTransactionsAndViews) {
        final Function function = new Function(
                FUNC_SETVAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_val)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, nestedSubordinateTransactionsAndViews);
    }

    public RemoteFunctionCall<TransactionReceipt> get(BigInteger _val) {
        final Function function = new Function(
                FUNC_GET, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_val)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] get_AsSignedCrosschainSubordinateTransaction(BigInteger _val, final byte[][] nestedSubordinateTransactionsAndViews) throws IOException {
        final Function function = new Function(
                FUNC_GET, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_val)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, nestedSubordinateTransactionsAndViews);
    }

    public RemoteFunctionCall<TransactionReceipt> get_AsCrosschainTransaction(BigInteger _val, final byte[][] nestedSubordinateTransactionsAndViews) {
        final Function function = new Function(
                FUNC_GET, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_val)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, nestedSubordinateTransactionsAndViews);
    }

    @Deprecated
    public static Sc3Contract6 load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Sc3Contract6(contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    public static Sc3Contract6 load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        return new Sc3Contract6(contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public static RemoteCall<Sc3Contract6> deploy(Besu besu, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _Sc2SidechainId, String _contract4) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_Sc2SidechainId), 
                new org.web3j.abi.datatypes.Address(160, _contract4)));
        return deployRemoteCall(Sc3Contract6.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Sc3Contract6> deploy(Besu besu, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _Sc2SidechainId, String _contract4) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_Sc2SidechainId), 
                new org.web3j.abi.datatypes.Address(160, _contract4)));
        return deployRemoteCall(Sc3Contract6.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Sc3Contract6> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _Sc2SidechainId, String _contract4) {
        byte[][] nestedSubordinateTransactionsAndViews = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_Sc2SidechainId), 
                new org.web3j.abi.datatypes.Address(160, _contract4)));
        return deployLockableContractRemoteCall(Sc3Contract6.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, nestedSubordinateTransactionsAndViews);
    }

    @Deprecated
    public static RemoteCall<Sc3Contract6> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _Sc2SidechainId, String _contract4) {
        byte[][] nestedSubordinateTransactionsAndViews = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_Sc2SidechainId), 
                new org.web3j.abi.datatypes.Address(160, _contract4)));
        return deployLockableContractRemoteCall(Sc3Contract6.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, nestedSubordinateTransactionsAndViews);
    }

    public static RemoteCall<Sc3Contract6> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _Sc2SidechainId, String _contract4, final byte[][] nestedSubordinateTransactionsAndViews) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_Sc2SidechainId), 
                new org.web3j.abi.datatypes.Address(160, _contract4)));
        return deployLockableContractRemoteCall(Sc3Contract6.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, nestedSubordinateTransactionsAndViews);
    }

    @Deprecated
    public static RemoteCall<Sc3Contract6> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _Sc2SidechainId, String _contract4, final byte[][] nestedSubordinateTransactionsAndViews) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_Sc2SidechainId), 
                new org.web3j.abi.datatypes.Address(160, _contract4)));
        return deployLockableContractRemoteCall(Sc3Contract6.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, nestedSubordinateTransactionsAndViews);
    }
}
