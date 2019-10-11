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
public class Sc2Contract2 extends CrosschainContract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060c18061001f6000396000f3fe6080604052348015600f57600080fd5b5060043610603c5760003560e01c80633c6bb4361460415780633d4197f01460595780636d4ce63c146075575b600080fd5b6047607b565b60408051918252519081900360200190f35b607360048036036020811015606d57600080fd5b50356081565b005b60476086565b60005481565b600055565b6000549056fea265627a7a7230582096a231165bf2a8c041fae5b71d46bd94823d4a236480e95425c309303f02de1e64736f6c634300050a0032";

    public static final String FUNC_VAL = "val";

    public static final String FUNC_SETVAL = "setVal";

    public static final String FUNC_GET = "get";

    @Deprecated
    protected Sc2Contract2(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    protected Sc2Contract2(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
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

    public RemoteFunctionCall<BigInteger> get() {
        final Function function = new Function(FUNC_GET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] get_AsSignedCrosschainSubordinateView(final byte[][] nestedSubordinateViews) throws IOException {
        final Function function = new Function(FUNC_GET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, nestedSubordinateViews);
    }

    @Deprecated
    public static Sc2Contract2 load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Sc2Contract2(contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    public static Sc2Contract2 load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        return new Sc2Contract2(contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public static RemoteCall<Sc2Contract2> deploy(Besu besu, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Sc2Contract2.class, besu, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Sc2Contract2> deploy(Besu besu, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Sc2Contract2.class, besu, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Sc2Contract2> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        byte[][] nestedSubordinateTransactionsAndViews = null;
        return deployLockableContractRemoteCall(Sc2Contract2.class, besu, transactionManager, contractGasProvider, BINARY, "", nestedSubordinateTransactionsAndViews);
    }

    @Deprecated
    public static RemoteCall<Sc2Contract2> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        byte[][] nestedSubordinateTransactionsAndViews = null;
        return deployLockableContractRemoteCall(Sc2Contract2.class, besu, transactionManager, gasPrice, gasLimit, BINARY, "", nestedSubordinateTransactionsAndViews);
    }

    public static RemoteCall<Sc2Contract2> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, final byte[][] nestedSubordinateTransactionsAndViews) {
        return deployLockableContractRemoteCall(Sc2Contract2.class, besu, transactionManager, contractGasProvider, BINARY, "", nestedSubordinateTransactionsAndViews);
    }

    @Deprecated
    public static RemoteCall<Sc2Contract2> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, final byte[][] nestedSubordinateTransactionsAndViews) {
        return deployLockableContractRemoteCall(Sc2Contract2.class, besu, transactionManager, gasPrice, gasLimit, BINARY, "", nestedSubordinateTransactionsAndViews);
    }
}
