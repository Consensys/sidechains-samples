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
    private static final String BINARY = "608060405260405161044a38038061044a8339818101604052602081101561002657600080fd5b5051600080546001600160a01b03191633908117825581526001602052604090203490556003556103ee8061005c6000396000f3fe60806040526004361061007b5760003560e01c8063700eac1f1161004e578063700eac1f1461012e5780638da5cb5b14610143578063d0e30db014610158578063fc564fc6146101605761007b565b80632e1a7d4d146100805780634c738909146100ac57806353556559146100d357806367f235ea146100fd575b600080fd5b34801561008c57600080fd5b506100aa600480360360208110156100a357600080fd5b5035610193565b005b3480156100b857600080fd5b506100c16101f2565b60408051918252519081900360200190f35b3480156100df57600080fd5b506100aa600480360360208110156100f657600080fd5b5035610205565b34801561010957600080fd5b506101126102cc565b604080516001600160a01b039092168252519081900360200190f35b34801561013a57600080fd5b506100c16102db565b34801561014f57600080fd5b506101126102e1565b6100aa6102f0565b34801561016c57600080fd5b506100aa6004803603602081101561018357600080fd5b50356001600160a01b031661031e565b336000908152600160205260409020548111156101af57600080fd5b33600081815260016020526040808220805485900390555183156108fc0291849190818181858888f193505050501580156101ee573d6000803e3d6000fd5b5050565b3360009081526001602052604090205490565b6002546001600160a01b031661021a57600080fd5b6000610224610340565b90506000610230610351565b9050600061023c61035d565b9050826003541461024c57600080fd5b6002546001600160a01b0383811691161461026657600080fd5b806003541461027457600080fd5b600080546001600160a01b031681526001602052604090205484111561029957600080fd5b5050600080546001600160a01b031681526001602052604080822080548590039055338252902080549290920390915550565b6002546001600160a01b031681565b60035481565b6000546001600160a01b031681565b6000546001600160a01b0316331461030757600080fd5b336000908152600160205260409020805434019055565b600280546001600160a01b0319166001600160a01b0392909216919091179055565b600061034c6005610365565b905090565b600061034c6006610365565b600061034c60045b6000602061037161039b565b83815261037c61039b565b6020808285856078600019fa61039157600080fd5b5051949350505050565b6040518060200160405280600190602082028038833950919291505056fea265627a7a72305820ee2b4b95643d935668fb49468d1b6dae888dedd6958219661569b92994521dde64736f6c634300050a0032";

    public static final String FUNC_WITHDRAW = "withdraw";

    public static final String FUNC_GETMYBALANCE = "getMyBalance";

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
}
