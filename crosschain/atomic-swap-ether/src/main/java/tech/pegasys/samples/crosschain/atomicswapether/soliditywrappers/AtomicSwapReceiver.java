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
    private static final String BINARY = "608060405260405161048b38038061048b8339818101604052602081101561002657600080fd5b5051600080546001600160a01b031916339081178255815260016020526040902034905560035561042f8061005c6000396000f3fe6080604052600436106100865760003560e01c8063700eac1f11610059578063700eac1f146101395780638da5cb5b1461014e578063d0e30db014610163578063f8b2cb4f1461016b578063fc564fc61461019e57610086565b80632e1a7d4d1461008b5780634c738909146100b757806353556559146100de57806367f235ea14610108575b600080fd5b34801561009757600080fd5b506100b5600480360360208110156100ae57600080fd5b50356101d1565b005b3480156100c357600080fd5b506100cc610230565b60408051918252519081900360200190f35b3480156100ea57600080fd5b506100b56004803603602081101561010157600080fd5b5035610243565b34801561011457600080fd5b5061011d610309565b604080516001600160a01b039092168252519081900360200190f35b34801561014557600080fd5b506100cc610318565b34801561015a57600080fd5b5061011d61031e565b6100b561032d565b34801561017757600080fd5b506100cc6004803603602081101561018e57600080fd5b50356001600160a01b0316610344565b3480156101aa57600080fd5b506100b5600480360360208110156101c157600080fd5b50356001600160a01b031661035f565b336000908152600160205260409020548111156101ed57600080fd5b33600081815260016020526040808220805485900390555183156108fc0291849190818181858888f1935050505015801561022c573d6000803e3d6000fd5b5050565b3360009081526001602052604090205490565b6002546001600160a01b031661025857600080fd5b6000610262610381565b9050600061026e610392565b9050600061027a61039e565b9050826003541461028a57600080fd5b6002546001600160a01b038381169116146102a457600080fd5b80600354146102b257600080fd5b600080546001600160a01b03168152600160205260409020548411156102d757600080fd5b5050600080546001600160a01b0316815260016020526040808220805485900390553382529020805490920190915550565b6002546001600160a01b031681565b60035481565b6000546001600160a01b031681565b336000908152600160205260409020805434019055565b6001600160a01b031660009081526001602052604090205490565b600280546001600160a01b0319166001600160a01b0392909216919091179055565b600061038d60056103a6565b905090565b600061038d60066103a6565b600061038d60045b600060206103b26103dc565b8381526103bd6103dc565b6020808285856078600019fa6103d257600080fd5b5051949350505050565b6040518060200160405280600190602082028038833950919291505056fea265627a7a723058204f3bbb49f2e3f605dffdccc8956708056d9fe484c859e0f7151df3f0753cec6d64736f6c634300050a0032";

    public static final String FUNC_WITHDRAW = "withdraw";

    public static final String FUNC_GETMYBALANCE = "getMyBalance";

    public static final String FUNC_EXCHANGE = "exchange";

    public static final String FUNC_SENDERCONTRACT = "senderContract";

    public static final String FUNC_SENDERSIDECHAINID = "senderSidechainId";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_DEPOSIT = "deposit";

    public static final String FUNC_GETBALANCE = "getBalance";

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

    public RemoteFunctionCall<TransactionReceipt> withdraw_AsCrosschainOriginatingTransaction(BigInteger _amount, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_WITHDRAW, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
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

    public RemoteFunctionCall<TransactionReceipt> exchange_AsCrosschainOriginatingTransaction(BigInteger _amount, final CrosschainContext crosschainContext) {
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

    public RemoteFunctionCall<BigInteger> getBalance(String account) {
        final Function function = new Function(FUNC_GETBALANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] getBalance_AsSignedCrosschainSubordinateView(String account, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_GETBALANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, crosschainContext);
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

    public RemoteFunctionCall<TransactionReceipt> setSenderContract_AsCrosschainOriginatingTransaction(String _senderContract, final CrosschainContext crosschainContext) {
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
}
