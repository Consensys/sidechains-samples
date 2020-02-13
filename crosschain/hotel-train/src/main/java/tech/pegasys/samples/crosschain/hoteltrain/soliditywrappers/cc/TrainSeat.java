package tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.cc;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
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
public class TrainSeat extends CrosschainContract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060405161032a38038061032a8339818101604052604081101561003357600080fd5b508051602090910151600080546001600160a01b0319166001600160a01b039093169290921782556001556102bc90819061006e90396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c80633a178d991161005b5780633a178d99146100e557806356b65dc614610116578063cac2afd71461013c578063fa7d2b451461017a5761007d565b8063080f94e9146100825780630ebb4497146100a75780632058e15f146100c1575b600080fd5b6100a56004803603604081101561009857600080fd5b5080359060200135610197565b005b6100af6101df565b60408051918252519081900360200190f35b6100c96101e5565b604080516001600160a01b039092168252519081900360200190f35b610102600480360360208110156100fb57600080fd5b50356101f4565b604080519115158252519081900360200190f35b6100a56004803603602081101561012c57600080fd5b50356001600160a01b0316610207565b6101596004803603602081101561015257600080fd5b5035610240565b604080519283526001600160a01b0390911660208301528051918290030190f35b6100a56004803603602081101561019057600080fd5b503561026b565b6000546001600160a01b031633146101ae57600080fd5b6000918252600260209081526040808420600d90930190925560039052902080546001600160a01b03191632179055565b60015481565b6000546001600160a01b031681565b6000908152600260205260409020541590565b6000546001600160a01b0316331461021e57600080fd5b600080546001600160a01b0319166001600160a01b0392909216919091179055565b60009081526002602090815260408083205460039092529091205490916001600160a01b0390911690565b6000546001600160a01b0316331461028257600080fd5b60015556fea265627a7a7230582099fc271bf76b157565a2e350de7a6a97a266467c6ed87c9d0babd54ac0faadd864736f6c634300050a0032";

    public static final String FUNC_BOOKSEAT = "bookSeat";

    public static final String FUNC_SEATRATE = "seatRate";

    public static final String FUNC_TRAINROUTERCONTRACT = "trainRouterContract";

    public static final String FUNC_ISAVAILABLE = "isAvailable";

    public static final String FUNC_CHANGETRAINROUTERCONTRACT = "changeTrainRouterContract";

    public static final String FUNC_GETBOOKINGINFO = "getBookingInfo";

    public static final String FUNC_CHANGESEATRATE = "changeSeatRate";

    @Deprecated
    protected TrainSeat(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    protected TrainSeat(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> bookSeat(BigInteger _date, BigInteger _uniqueId) {
        final Function function = new Function(
                FUNC_BOOKSEAT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_uniqueId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] bookSeat_AsSignedCrosschainSubordinateTransaction(BigInteger _date, BigInteger _uniqueId, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_BOOKSEAT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_uniqueId)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> bookSeat_AsCrosschainOriginatingTransaction(BigInteger _date, BigInteger _uniqueId, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_BOOKSEAT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_uniqueId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<BigInteger> seatRate() {
        final Function function = new Function(FUNC_SEATRATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] seatRate_AsSignedCrosschainSubordinateView(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_SEATRATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, crosschainContext);
    }

    public RemoteFunctionCall<String> trainRouterContract() {
        final Function function = new Function(FUNC_TRAINROUTERCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public byte[] trainRouterContract_AsSignedCrosschainSubordinateView(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_TRAINROUTERCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return createSignedSubordinateView(function, crosschainContext);
    }

    public RemoteFunctionCall<Boolean> isAvailable(BigInteger _date) {
        final Function function = new Function(FUNC_ISAVAILABLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public byte[] isAvailable_AsSignedCrosschainSubordinateView(BigInteger _date, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_ISAVAILABLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return createSignedSubordinateView(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> changeTrainRouterContract(String _trainRouterContract) {
        final Function function = new Function(
                FUNC_CHANGETRAINROUTERCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _trainRouterContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] changeTrainRouterContract_AsSignedCrosschainSubordinateTransaction(String _trainRouterContract, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_CHANGETRAINROUTERCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _trainRouterContract)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> changeTrainRouterContract_AsCrosschainOriginatingTransaction(String _trainRouterContract, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_CHANGETRAINROUTERCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _trainRouterContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<Tuple2<BigInteger, String>> getBookingInfo(BigInteger _date) {
        final Function function = new Function(FUNC_GETBOOKINGINFO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
        return new RemoteFunctionCall<Tuple2<BigInteger, String>>(function,
                new Callable<Tuple2<BigInteger, String>>() {
                    @Override
                    public Tuple2<BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, String>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue());
                    }
                });
    }

    public byte[] getBookingInfo_AsSignedCrosschainSubordinateView(BigInteger _date, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_GETBOOKINGINFO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> changeSeatRate(BigInteger _seatRate) {
        final Function function = new Function(
                FUNC_CHANGESEATRATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_seatRate)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] changeSeatRate_AsSignedCrosschainSubordinateTransaction(BigInteger _seatRate, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_CHANGESEATRATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_seatRate)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> changeSeatRate_AsCrosschainOriginatingTransaction(BigInteger _seatRate, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_CHANGESEATRATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_seatRate)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    @Deprecated
    public static TrainSeat load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TrainSeat(contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    public static TrainSeat load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        return new TrainSeat(contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public static RemoteCall<TrainSeat> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, String _hotelRouterContract, BigInteger _roomRate) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_roomRate)));
        return deployLockableContractRemoteCall(TrainSeat.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<TrainSeat> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _hotelRouterContract, BigInteger _roomRate) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_roomRate)));
        return deployLockableContractRemoteCall(TrainSeat.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, crosschainContext);
    }

    public static RemoteCall<TrainSeat> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, String _hotelRouterContract, BigInteger _roomRate, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_roomRate)));
        return deployLockableContractRemoteCall(TrainSeat.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<TrainSeat> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _hotelRouterContract, BigInteger _roomRate, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_roomRate)));
        return deployLockableContractRemoteCall(TrainSeat.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, crosschainContext);
    }
}
