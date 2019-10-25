package tech.pegasys.samples.crosschain.atomicswapether.soliditywrappers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
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
public class AtomicSwapSender extends CrosschainContract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060405161039e38038061039e8339818101604052606081101561003357600080fd5b50805160208201516040909201519091906f80000000000000000000000000000000811061006057600080fd5b8061006a57600080fd5b600080546001600160a01b03199081163317909155600293909355600380546001600160a01b039390931692909316919091179091556001556102ec806100b26000396000f3fe6080604052600436106100555760003560e01c806303cd346f1461005a5780633ba0b9a91461008b5780633ccfd60b146100b257806353c6cc00146100c95780638da5cb5b146100de578063d2f7265a146100f3575b600080fd5b34801561006657600080fd5b5061006f6100fb565b604080516001600160a01b039092168252519081900360200190f35b34801561009757600080fd5b506100a061010a565b60408051918252519081900360200190f35b3480156100be57600080fd5b506100c7610110565b005b3480156100d557600080fd5b506100a0610157565b3480156100ea57600080fd5b5061006f61015d565b6100c761016c565b6003546001600160a01b031681565b60015481565b6000546001600160a01b0316331461012757600080fd5b6040513390303180156108fc02916000818181858888f19350505050158015610154573d6000803e3d6000fd5b50565b60025481565b6000546001600160a01b031681565b6001607f1b341061017c57600080fd5b60006801000000000000000060015434028161019457fe5b600254600354604080519490930460248086018290528451808703909101815260449095019093526020840180516001600160e01b0316635355655960e01b1790529193506101549290916001600160a01b031690606083838360405160200180848152602001836001600160a01b03166001600160a01b0316815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561024c578181015183820152602001610234565b50505050905090810190601f1680156102795780820380516001836020036101000a031916815260200191505b50945050505050604051602081830303815290604052905060008151602001905060008082846000600a600019f16102b057600080fd5b505050505056fea265627a7a72305820b1f473a38f02b73fad892330e808b686a0ef92221f9b17a3d6a7b4f667299d4864736f6c634300050a0032";

    public static final String FUNC_RECEIVERCONTRACT = "receiverContract";

    public static final String FUNC_EXCHANGERATE = "exchangeRate";

    public static final String FUNC_WITHDRAW = "withdraw";

    public static final String FUNC_RECEIVERSIDECHAINID = "receiverSidechainId";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_EXCHANGE = "exchange";

    @Deprecated
    protected AtomicSwapSender(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    protected AtomicSwapSender(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<String> receiverContract() {
        final Function function = new Function(FUNC_RECEIVERCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public byte[] receiverContract_AsSignedCrosschainSubordinateView(final byte[][] nestedSubordinateViews) throws IOException {
        final Function function = new Function(FUNC_RECEIVERCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return createSignedSubordinateView(function, nestedSubordinateViews);
    }

    public RemoteFunctionCall<BigInteger> exchangeRate() {
        final Function function = new Function(FUNC_EXCHANGERATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] exchangeRate_AsSignedCrosschainSubordinateView(final byte[][] nestedSubordinateViews) throws IOException {
        final Function function = new Function(FUNC_EXCHANGERATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, nestedSubordinateViews);
    }

    public RemoteFunctionCall<TransactionReceipt> withdraw() {
        final Function function = new Function(
                FUNC_WITHDRAW, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] withdraw_AsSignedCrosschainSubordinateTransaction(final byte[][] nestedSubordinateTransactionsAndViews) throws IOException {
        final Function function = new Function(
                FUNC_WITHDRAW, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, nestedSubordinateTransactionsAndViews);
    }

    public RemoteFunctionCall<TransactionReceipt> withdraw_AsCrosschainTransaction(final byte[][] nestedSubordinateTransactionsAndViews) {
        final Function function = new Function(
                FUNC_WITHDRAW, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, nestedSubordinateTransactionsAndViews);
    }

    public RemoteFunctionCall<BigInteger> receiverSidechainId() {
        final Function function = new Function(FUNC_RECEIVERSIDECHAINID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] receiverSidechainId_AsSignedCrosschainSubordinateView(final byte[][] nestedSubordinateViews) throws IOException {
        final Function function = new Function(FUNC_RECEIVERSIDECHAINID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, nestedSubordinateViews);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public byte[] owner_AsSignedCrosschainSubordinateView(final byte[][] nestedSubordinateViews) throws IOException {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return createSignedSubordinateView(function, nestedSubordinateViews);
    }

    public RemoteFunctionCall<TransactionReceipt> exchange(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_EXCHANGE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public byte[] exchange_AsSignedCrosschainSubordinateTransaction(final byte[][] nestedSubordinateTransactionsAndViews, BigInteger weiValue) throws IOException {
        final Function function = new Function(
                FUNC_EXCHANGE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, nestedSubordinateTransactionsAndViews, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> exchange_AsCrosschainTransaction(final byte[][] nestedSubordinateTransactionsAndViews, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_EXCHANGE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, nestedSubordinateTransactionsAndViews, weiValue);
    }

    @Deprecated
    public static AtomicSwapSender load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AtomicSwapSender(contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    public static AtomicSwapSender load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        return new AtomicSwapSender(contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public static RemoteCall<AtomicSwapSender> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _receiverSidechainId, String _receiverContract, BigInteger _exchangeRate) {
        byte[][] nestedSubordinateTransactionsAndViews = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_receiverSidechainId), 
                new org.web3j.abi.datatypes.Address(160, _receiverContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_exchangeRate)));
        return deployLockableContractRemoteCall(AtomicSwapSender.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, nestedSubordinateTransactionsAndViews);
    }

    @Deprecated
    public static RemoteCall<AtomicSwapSender> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _receiverSidechainId, String _receiverContract, BigInteger _exchangeRate) {
        byte[][] nestedSubordinateTransactionsAndViews = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_receiverSidechainId), 
                new org.web3j.abi.datatypes.Address(160, _receiverContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_exchangeRate)));
        return deployLockableContractRemoteCall(AtomicSwapSender.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, nestedSubordinateTransactionsAndViews);
    }

    public static RemoteCall<AtomicSwapSender> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _receiverSidechainId, String _receiverContract, BigInteger _exchangeRate, final byte[][] nestedSubordinateTransactionsAndViews) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_receiverSidechainId), 
                new org.web3j.abi.datatypes.Address(160, _receiverContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_exchangeRate)));
        return deployLockableContractRemoteCall(AtomicSwapSender.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, nestedSubordinateTransactionsAndViews);
    }

    @Deprecated
    public static RemoteCall<AtomicSwapSender> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _receiverSidechainId, String _receiverContract, BigInteger _exchangeRate, final byte[][] nestedSubordinateTransactionsAndViews) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_receiverSidechainId), 
                new org.web3j.abi.datatypes.Address(160, _receiverContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_exchangeRate)));
        return deployLockableContractRemoteCall(AtomicSwapSender.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, nestedSubordinateTransactionsAndViews);
    }
}
