package tech.pegasys.samples.crosschain.threechainsfivecontracts.soliditywrappers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
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
public class Sc3Contract5 extends CrosschainContract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060e08061001f6000396000f3fe6080604052348015600f57600080fd5b5060043610603c5760003560e01c80633c6bb4361460415780633d4197f01460595780638dfa4363146075575b600080fd5b60476095565b60408051918252519081900360200190f35b607360048036036020811015606d57600080fd5b5035609b565b005b604760048036036040811015608957600080fd5b508035906020013560a0565b60005481565b600055565b60005491909101019056fea265627a7a723058208d0db220858e93bc8eec02f277a6f2ea8b65f949a8f366bb2912877d0968938364736f6c634300050a0032";

    public static final String FUNC_VAL = "val";

    public static final String FUNC_SETVAL = "setVal";

    public static final String FUNC_CALCULATE = "calculate";

    @Deprecated
    protected Sc3Contract5(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    protected Sc3Contract5(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
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

    public RemoteFunctionCall<BigInteger> calculate(BigInteger _val1, BigInteger _val2) {
        final Function function = new Function(FUNC_CALCULATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_val1), 
                new org.web3j.abi.datatypes.generated.Uint256(_val2)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] calculate_AsSignedCrosschainSubordinateView(BigInteger _val1, BigInteger _val2, final byte[][] nestedSubordinateViews) throws IOException {
        final Function function = new Function(FUNC_CALCULATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_val1), 
                new org.web3j.abi.datatypes.generated.Uint256(_val2)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, nestedSubordinateViews);
    }

    @Deprecated
    public static Sc3Contract5 load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Sc3Contract5(contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    public static Sc3Contract5 load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        return new Sc3Contract5(contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public static RemoteCall<Sc3Contract5> deploy(Besu besu, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Sc3Contract5.class, besu, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Sc3Contract5> deploy(Besu besu, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Sc3Contract5.class, besu, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Sc3Contract5> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        byte[][] nestedSubordinateTransactionsAndViews = null;
        return deployLockableContractRemoteCall(Sc3Contract5.class, besu, transactionManager, contractGasProvider, BINARY, "", nestedSubordinateTransactionsAndViews);
    }

    @Deprecated
    public static RemoteCall<Sc3Contract5> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        byte[][] nestedSubordinateTransactionsAndViews = null;
        return deployLockableContractRemoteCall(Sc3Contract5.class, besu, transactionManager, gasPrice, gasLimit, BINARY, "", nestedSubordinateTransactionsAndViews);
    }

    public static RemoteCall<Sc3Contract5> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, final byte[][] nestedSubordinateTransactionsAndViews) {
        return deployLockableContractRemoteCall(Sc3Contract5.class, besu, transactionManager, contractGasProvider, BINARY, "", nestedSubordinateTransactionsAndViews);
    }

    @Deprecated
    public static RemoteCall<Sc3Contract5> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, final byte[][] nestedSubordinateTransactionsAndViews) {
        return deployLockableContractRemoteCall(Sc3Contract5.class, besu, transactionManager, gasPrice, gasLimit, BINARY, "", nestedSubordinateTransactionsAndViews);
    }
}
