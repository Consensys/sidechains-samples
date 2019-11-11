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
public class AtomicSwapSender extends CrosschainContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516104c83803806104c88339818101604052606081101561003357600080fd5b508051602080830151604093840151600080546001600160a01b031916339081178255815260019093529390912034905590916f80000000000000000000000000000000811061008257600080fd5b8061008c57600080fd5b600392909255600480546001600160a01b0319166001600160a01b0392909216919091179055600255610404806100c46000396000f3fe60806040526004361061007b5760003560e01c8063535565591161004e578063535565591461011957806353c6cc00146101435780638da5cb5b14610158578063d0e30db01461016d5761007b565b806303cd346f146100805780632e1a7d4d146100b15780633ba0b9a9146100dd5780634c73890914610104575b600080fd5b34801561008c57600080fd5b50610095610175565b604080516001600160a01b039092168252519081900360200190f35b3480156100bd57600080fd5b506100db600480360360208110156100d457600080fd5b5035610184565b005b3480156100e957600080fd5b506100f26101e3565b60408051918252519081900360200190f35b34801561011057600080fd5b506100f26101e9565b34801561012557600080fd5b506100db6004803603602081101561013c57600080fd5b50356101fc565b34801561014f57600080fd5b506100f26102be565b34801561016457600080fd5b506100956102c4565b6100db6102d3565b6004546001600160a01b031681565b336000908152600160205260409020548111156101a057600080fd5b33600081815260016020526040808220805485900390555183156108fc0291849190818181858888f193505050501580156101df573d6000803e3d6000fd5b5050565b60025481565b3360009081526001602052604090205490565b6001607f1b811061020c57600080fd5b3360009081526001602052604090205481111561022857600080fd5b336000908152600160205260408082208054849003905581546001600160a01b03168252812080548301905560025468010000000000000000908302600354600454604080519490930460248086018290528451808703909101815260449095019093526020840180516001600160e01b0316635355655960e01b1790529193506101df9290916001600160a01b031690610301565b60035481565b6000546001600160a01b031681565b6000546001600160a01b031633146102ea57600080fd5b336000908152600160205260409020805434019055565b606083838360405160200180848152602001836001600160a01b03166001600160a01b0316815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561036457818101518382015260200161034c565b50505050905090810190601f1680156103915780820380516001836020036101000a031916815260200191505b50945050505050604051602081830303815290604052905060006004825101905060008082846000600a600019f16103c857600080fd5b505050505056fea265627a7a72305820f61dea38627c87d7b4b3ff0447224894357091e53d3c9aad34588fc2b140d9af64736f6c634300050a0032";

    public static final String FUNC_RECEIVERCONTRACT = "receiverContract";

    public static final String FUNC_WITHDRAW = "withdraw";

    public static final String FUNC_EXCHANGERATE = "exchangeRate";

    public static final String FUNC_GETMYBALANCE = "getMyBalance";

    public static final String FUNC_EXCHANGE = "exchange";

    public static final String FUNC_RECEIVERSIDECHAINID = "receiverSidechainId";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_DEPOSIT = "deposit";

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

    public byte[] receiverContract_AsSignedCrosschainSubordinateView(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_RECEIVERCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return createSignedSubordinateView(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> withdraw(BigInteger _amount) {
        final Function function = new Function(
                FUNC_WITHDRAW, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] withdraw_AsSignedCrosschainSubordinateTransaction(BigInteger _amount, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_WITHDRAW, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> withdraw_AsCrosschainTransaction(BigInteger _amount, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_WITHDRAW, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<BigInteger> exchangeRate() {
        final Function function = new Function(FUNC_EXCHANGERATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] exchangeRate_AsSignedCrosschainSubordinateView(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_EXCHANGERATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, crosschainContext);
    }

    public RemoteFunctionCall<BigInteger> getMyBalance() {
        final Function function = new Function(FUNC_GETMYBALANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] getMyBalance_AsSignedCrosschainSubordinateView(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_GETMYBALANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> exchange(BigInteger _amount) {
        final Function function = new Function(
                FUNC_EXCHANGE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] exchange_AsSignedCrosschainSubordinateTransaction(BigInteger _amount, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_EXCHANGE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> exchange_AsCrosschainTransaction(BigInteger _amount, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_EXCHANGE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<BigInteger> receiverSidechainId() {
        final Function function = new Function(FUNC_RECEIVERSIDECHAINID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] receiverSidechainId_AsSignedCrosschainSubordinateView(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_RECEIVERSIDECHAINID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, crosschainContext);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public byte[] owner_AsSignedCrosschainSubordinateView(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return createSignedSubordinateView(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> deposit(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_DEPOSIT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    @Deprecated
    public static AtomicSwapSender load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AtomicSwapSender(contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    public static AtomicSwapSender load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        return new AtomicSwapSender(contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public static RemoteCall<AtomicSwapSender> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _receiverSidechainId, String _receiverContract, BigInteger _exchangeRate) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_receiverSidechainId), 
                new org.web3j.abi.datatypes.Address(160, _receiverContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_exchangeRate)));
        return deployLockableContractRemoteCall(AtomicSwapSender.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<AtomicSwapSender> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _receiverSidechainId, String _receiverContract, BigInteger _exchangeRate) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_receiverSidechainId), 
                new org.web3j.abi.datatypes.Address(160, _receiverContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_exchangeRate)));
        return deployLockableContractRemoteCall(AtomicSwapSender.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, crosschainContext);
    }

    public static RemoteCall<AtomicSwapSender> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _receiverSidechainId, String _receiverContract, BigInteger _exchangeRate, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_receiverSidechainId), 
                new org.web3j.abi.datatypes.Address(160, _receiverContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_exchangeRate)));
        return deployLockableContractRemoteCall(AtomicSwapSender.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<AtomicSwapSender> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _receiverSidechainId, String _receiverContract, BigInteger _exchangeRate, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_receiverSidechainId), 
                new org.web3j.abi.datatypes.Address(160, _receiverContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_exchangeRate)));
        return deployLockableContractRemoteCall(AtomicSwapSender.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, crosschainContext);
    }
}
