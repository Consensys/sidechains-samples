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
    private static final String BINARY = "608060405260405161041c38038061041c8339818101604052602081101561002657600080fd5b5051600080546001600160a01b031916331790556002556103d08061004c6000396000f3fe60806040526004361061007b5760003560e01c8063700eac1f1161004e578063700eac1f1461012e5780638da5cb5b14610143578063d0e30db014610158578063fc564fc6146101605761007b565b80632e1a7d4d146100805780633c6bb436146100ac57806353556559146100d357806367f235ea146100fd575b600080fd5b34801561008c57600080fd5b506100aa600480360360208110156100a357600080fd5b5035610193565b005b3480156100b857600080fd5b506100c16101db565b60408051918252519081900360200190f35b3480156100df57600080fd5b506100aa600480360360208110156100f657600080fd5b50356101e1565b34801561010957600080fd5b50610112610297565b604080516001600160a01b039092168252519081900360200190f35b34801561013a57600080fd5b506100c16102a6565b34801561014f57600080fd5b506101126102ac565b6100aa6102bb565b34801561016c57600080fd5b506100aa6004803603602081101561018357600080fd5b50356001600160a01b03166102d4565b6000546001600160a01b031633146101aa57600080fd5b604051339082156108fc029083906000818181858888f193505050501580156101d7573d6000803e3d6000fd5b5050565b60035481565b6001546001600160a01b03166101f657600080fd5b60006102006102f6565b9050600061020c610307565b90506000610218610313565b9050826002541461022857600080fd5b6001546001600160a01b0383811691161461024257600080fd5b806002541461025057600080fd5b6003839055303184111561026357600080fd5b604051339085156108fc029086906000818181858888f19350505050158015610290573d6000803e3d6000fd5b5050505050565b6001546001600160a01b031681565b60025481565b6000546001600160a01b031681565b6000546001600160a01b031633146102d257600080fd5b565b600180546001600160a01b0319166001600160a01b0392909216919091179055565b6000610302600561031b565b905090565b60006103026006610351565b600061030260045b6000602061032761037d565b83815261033261037d565b6020808285856078600019fa61034757600080fd5b5051949350505050565b6000602061035d61037d565b83815261036861037d565b6014808285856078600019fa61034757600080fd5b6040518060200160405280600190602082028038833950919291505056fea265627a7a72305820304d43847af9f1b81c6b26c794b19815c0310fd497e5294a6e0841ded062631564736f6c634300050a0032";

    public static final String FUNC_WITHDRAW = "withdraw";

    public static final String FUNC_VAL = "val";

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

    public RemoteFunctionCall<BigInteger> val() {
        final Function function = new Function(FUNC_VAL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] val_AsSignedCrosschainSubordinateView(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_VAL, 
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
