package tech.pegasys.samples.crosschain.threechainssixcontracts.soliditywrappers;

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
    private static final String BINARY = "608060405234801561001057600080fd5b5060405161046b38038061046b833981810160405260a081101561003357600080fd5b5080516020820151604083015160608401516080909401516000938455600192909255600280546001600160a01b03199081166001600160a01b039384161790915560038054821695831695909517909455600480549094169116179091556103c99081906100a290396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c806323bad5cd146100465780633c6bb436146100655780633d4197f01461007f575b600080fd5b6100636004803603602081101561005c57600080fd5b503561009c565b005b61006d6101af565b60408051918252519081900360200190f35b6100636004803603602081101561009557600080fd5b50356101b5565b600080546002546040805160048152602481019091526020810180516001600160e01b0316631b53398f60e21b1790526100e092916001600160a01b0316906101ba565b60058190559050808211156101ab57600154600454604080516024810186905260448082018690528251808303909101815260649091019091526020810180516001600160e01b0316638dfa436360e01b17905260009261014e9290916001600160a01b03909116906101ba565b6000546003546040805160248082018690528251808303909101815260449091019091526020810180516001600160e01b03166001624d3b8760e01b03191790529293506101a7926001600160a01b039091169061029c565b6005555b5050565b60055481565b600555565b6000606084848460405160200180848152602001836001600160a01b03166001600160a01b0316815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561021f578181015183820152602001610207565b50505050905090810190601f16801561024c5780820380516001836020036101000a031916815260200191505b509450505050506040516020818303038152906040529050600061026f8261036f565b9050610279610376565b6020600b8183858784600019fa61028f57600080fd5b5050519695505050505050565b606083838360405160200180848152602001836001600160a01b03166001600160a01b0316815260200180602001828103825283818151815260200191508051906020019080838360005b838110156102ff5781810151838201526020016102e7565b50505050905090810190601f16801561032c5780820380516001836020036101000a031916815260200191505b509450505050506040516020818303038152906040529050600061034f8261036f565b9050600a60008083858285600019f161036757600080fd5b505050505050565b5160040190565b6040518060200160405280600190602082028038833950919291505056fea265627a7a72305820cc1d93b65f37c09c9a6496cffad6ebd18a37718c1dba08e846df08eff4e9f5f264736f6c634300050a0032";

    public static final String FUNC_DOSTUFF = "doStuff";

    public static final String FUNC_VAL = "val";

    public static final String FUNC_SETVAL = "setVal";

    @Deprecated
    protected Sc1Contract1(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    protected Sc1Contract1(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> doStuff(BigInteger _val) {
        final Function function = new Function(
                FUNC_DOSTUFF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_val)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] doStuff_AsSignedCrosschainSubordinateTransaction(BigInteger _val, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_DOSTUFF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_val)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> doStuff_AsCrosschainOriginatingTransaction(BigInteger _val, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_DOSTUFF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_val)), 
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

    public RemoteFunctionCall<TransactionReceipt> setVal(BigInteger _val) {
        final Function function = new Function(
                FUNC_SETVAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_val)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] setVal_AsSignedCrosschainSubordinateTransaction(BigInteger _val, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_SETVAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_val)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> setVal_AsCrosschainOriginatingTransaction(BigInteger _val, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_SETVAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_val)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    @Deprecated
    public static Sc1Contract1 load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Sc1Contract1(contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    public static Sc1Contract1 load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        return new Sc1Contract1(contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public static RemoteCall<Sc1Contract1> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _Sc2SidechainId, BigInteger _Sc3SidechainId, String _contract2, String _contract3, String _contract5) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_Sc2SidechainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_Sc3SidechainId), 
                new org.web3j.abi.datatypes.Address(160, _contract2), 
                new org.web3j.abi.datatypes.Address(160, _contract3), 
                new org.web3j.abi.datatypes.Address(160, _contract5)));
        return deployLockableContractRemoteCall(Sc1Contract1.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<Sc1Contract1> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _Sc2SidechainId, BigInteger _Sc3SidechainId, String _contract2, String _contract3, String _contract5) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_Sc2SidechainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_Sc3SidechainId), 
                new org.web3j.abi.datatypes.Address(160, _contract2), 
                new org.web3j.abi.datatypes.Address(160, _contract3), 
                new org.web3j.abi.datatypes.Address(160, _contract5)));
        return deployLockableContractRemoteCall(Sc1Contract1.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, crosschainContext);
    }

    public static RemoteCall<Sc1Contract1> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _Sc2SidechainId, BigInteger _Sc3SidechainId, String _contract2, String _contract3, String _contract5, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_Sc2SidechainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_Sc3SidechainId), 
                new org.web3j.abi.datatypes.Address(160, _contract2), 
                new org.web3j.abi.datatypes.Address(160, _contract3), 
                new org.web3j.abi.datatypes.Address(160, _contract5)));
        return deployLockableContractRemoteCall(Sc1Contract1.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<Sc1Contract1> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _Sc2SidechainId, BigInteger _Sc3SidechainId, String _contract2, String _contract3, String _contract5, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_Sc2SidechainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_Sc3SidechainId), 
                new org.web3j.abi.datatypes.Address(160, _contract2), 
                new org.web3j.abi.datatypes.Address(160, _contract3), 
                new org.web3j.abi.datatypes.Address(160, _contract5)));
        return deployLockableContractRemoteCall(Sc1Contract1.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, crosschainContext);
    }
}
