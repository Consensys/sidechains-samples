package tech.pegasys.samples.crosschain.simple.soliditywrappers;

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
    private static final String BINARY = "608060405234801561001057600080fd5b506040516103843803806103848339818101604052604081101561003357600080fd5b5080516020909101516000918255600280546001600160a01b0319166001600160a01b0390921691909117905561031490819061007090396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c806347066e29146100465780634ede842414610050578063a35f68bc14610058575b600080fd5b61004e610072565b005b61004e6100b7565b6100606100ff565b60408051918252519081900360200190f35b6000546002546040805160048152602481019091526020810180516001600160e01b0316635c70086f60e11b1790526100b592916001600160a01b031690610105565b565b6000546002546040805160048152602481019091526020810180516001600160e01b0316636889597960e01b1790526100fa92916001600160a01b0316906101d8565b600155565b60015481565b606083838360405160200180848152602001836001600160a01b03166001600160a01b0316815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610168578181015183820152602001610150565b50505050905090810190601f1680156101955780820380516001836020036101000a031916815260200191505b50945050505050604051602081830303815290604052905060006101b8826102ba565b9050600a60008083858285600019f16101d057600080fd5b505050505050565b6000606084848460405160200180848152602001836001600160a01b03166001600160a01b0316815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561023d578181015183820152602001610225565b50505050905090810190601f16801561026a5780820380516001836020036101000a031916815260200191505b509450505050506040516020818303038152906040529050600061028d826102ba565b90506102976102c1565b6020600b8183858784600019fa6102ad57600080fd5b5050519695505050505050565b5160040190565b6040518060200160405280600190602082028038833950919291505056fea265627a7a72305820a664f23dfeb66c534d209a76b3a921a92a2af5b660d3ee301ed7fd176b23707964736f6c634300050a0032";

    public static final String FUNC_CROSSCHAIN_SETTER = "crosschain_setter";

    public static final String FUNC_CROSSCHAIN_GETUINT256_TRANSACTION = "crosschain_getUint256_transaction";

    public static final String FUNC_LOCALVALUE = "localValue";

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

    public RemoteFunctionCall<TransactionReceipt> crosschain_setter_AsCrosschainOriginatingTransaction(final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_CROSSCHAIN_SETTER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> crosschain_getUint256_transaction() {
        final Function function = new Function(
                FUNC_CROSSCHAIN_GETUINT256_TRANSACTION, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] crosschain_getUint256_transaction_AsSignedCrosschainSubordinateTransaction(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_CROSSCHAIN_GETUINT256_TRANSACTION, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> crosschain_getUint256_transaction_AsCrosschainOriginatingTransaction(final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_CROSSCHAIN_GETUINT256_TRANSACTION, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<BigInteger> localValue() {
        final Function function = new Function(FUNC_LOCALVALUE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] localValue_AsSignedCrosschainSubordinateView(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_LOCALVALUE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, crosschainContext);
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
