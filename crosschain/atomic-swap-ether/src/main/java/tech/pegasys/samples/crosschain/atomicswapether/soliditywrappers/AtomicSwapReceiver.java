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
public class AtomicSwapReceiver extends CrosschainContract {
    private static final String BINARY = "60806040526040516103c53803806103c58339818101604052602081101561002657600080fd5b5051600080546001600160a01b031916331790556002556103798061004c6000396000f3fe6080604052600436106100705760003560e01c8063700eac1f1161004e578063700eac1f146100fc5780638da5cb5b14610123578063d0e30db014610138578063fc564fc61461014057610070565b80632e1a7d4d1461007557806353556559146100a157806367f235ea146100cb575b600080fd5b34801561008157600080fd5b5061009f6004803603602081101561009857600080fd5b5035610173565b005b3480156100ad57600080fd5b5061009f600480360360208110156100c457600080fd5b50356101bb565b3480156100d757600080fd5b506100e061026c565b604080516001600160a01b039092168252519081900360200190f35b34801561010857600080fd5b5061011161027b565b60408051918252519081900360200190f35b34801561012f57600080fd5b506100e0610281565b61009f610290565b34801561014c57600080fd5b5061009f6004803603602081101561016357600080fd5b50356001600160a01b03166102a9565b6000546001600160a01b0316331461018a57600080fd5b604051339082156108fc029083906000818181858888f193505050501580156101b7573d6000803e3d6000fd5b5050565b6001546001600160a01b03166101d057600080fd5b60006101da6102cb565b905060006101e66102dc565b905060006101f26102e8565b9050826002541461020257600080fd5b6001546001600160a01b0383811691161461021c57600080fd5b806002541461022a57600080fd5b303184111561023857600080fd5b604051339085156108fc029086906000818181858888f19350505050158015610265573d6000803e3d6000fd5b5050505050565b6001546001600160a01b031681565b60025481565b6000546001600160a01b031681565b6000546001600160a01b031633146102a757600080fd5b565b600180546001600160a01b0319166001600160a01b0392909216919091179055565b60006102d760056102f0565b905090565b60006102d760066102f0565b60006102d760045b600060206102fc610326565b838152610307610326565b6020808285856078600019fa61031c57600080fd5b5051949350505050565b6040518060200160405280600190602082028038833950919291505056fea265627a7a723058201bacb2e62b85be3ccf11c886d5d8e06ef9635b050849c847378a2329729e952564736f6c634300050a0032";

    public static final String FUNC_WITHDRAW = "withdraw";

    public static final String FUNC_EXCHANGE = "exchange";

    public static final String FUNC_SENDERCONTRACT = "senderContract";

    public static final String FUNC_SENDERSIDECHAINID = "senderSidechainId";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_DEPOSIT = "deposit";

    public static final String FUNC_SETSENDERCONTRACT = "setSenderContract";

    @Deprecated
    protected AtomicSwapReceiver(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    protected AtomicSwapReceiver(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, contractGasProvider);
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

    public RemoteFunctionCall<String> senderContract() {
        final Function function = new Function(FUNC_SENDERCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public byte[] senderContract_AsSignedCrosschainSubordinateView(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_SENDERCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return createSignedSubordinateView(function, crosschainContext);
    }

    public RemoteFunctionCall<BigInteger> senderSidechainId() {
        final Function function = new Function(FUNC_SENDERSIDECHAINID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] senderSidechainId_AsSignedCrosschainSubordinateView(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_SENDERSIDECHAINID, 
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

    public byte[] deposit_AsSignedCrosschainSubordinateTransaction(final CrosschainContext crosschainContext, BigInteger weiValue) throws IOException {
        final Function function = new Function(
                FUNC_DEPOSIT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> deposit_AsCrosschainTransaction(final CrosschainContext crosschainContext, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_DEPOSIT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> setSenderContract(String _senderContract) {
        final Function function = new Function(
                FUNC_SETSENDERCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _senderContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] setSenderContract_AsSignedCrosschainSubordinateTransaction(String _senderContract, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_SETSENDERCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _senderContract)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> setSenderContract_AsCrosschainTransaction(String _senderContract, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_SETSENDERCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _senderContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    @Deprecated
    public static AtomicSwapReceiver load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AtomicSwapReceiver(contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    public static AtomicSwapReceiver load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        return new AtomicSwapReceiver(contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public static RemoteCall<AtomicSwapReceiver> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger initialWeiValue, BigInteger _senderSidechainId) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_senderSidechainId)));
        return deployLockableContractRemoteCall(AtomicSwapReceiver.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, initialWeiValue, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<AtomicSwapReceiver> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, BigInteger _senderSidechainId) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_senderSidechainId)));
        return deployLockableContractRemoteCall(AtomicSwapReceiver.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue, crosschainContext);
    }

    public static RemoteCall<AtomicSwapReceiver> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger initialWeiValue, BigInteger _senderSidechainId, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_senderSidechainId)));
        return deployLockableContractRemoteCall(AtomicSwapReceiver.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, initialWeiValue, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<AtomicSwapReceiver> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, BigInteger _senderSidechainId, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_senderSidechainId)));
        return deployLockableContractRemoteCall(AtomicSwapReceiver.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue, crosschainContext);
    }
}
