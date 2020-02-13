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
public class HotelRoom extends CrosschainContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516103383803806103388339818101604052604081101561003357600080fd5b508051602090910151600080546001600160a01b0319166001600160a01b039093169290921782556001556102ca90819061006e90396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c80638c8c4f3f1161005b5780638c8c4f3f146100f2578063b054241314610118578063c7f4e6d314610135578063cac2afd7146101595761007d565b80633a04832a146100825780633a178d991461009c5780638a3a1a18146100cd575b600080fd5b61008a610197565b60408051918252519081900360200190f35b6100b9600480360360208110156100b257600080fd5b503561019d565b604080519115158252519081900360200190f35b6100f0600480360360408110156100e357600080fd5b50803590602001356101b0565b005b6100f06004803603602081101561010857600080fd5b50356001600160a01b0316610206565b6100f06004803603602081101561012e57600080fd5b503561023f565b61013d61025b565b604080516001600160a01b039092168252519081900360200190f35b6101766004803603602081101561016f57600080fd5b503561026a565b604080519283526001600160a01b0390911660208301528051918290030190f35b60015481565b6000908152600260205260409020541590565b6000546001600160a01b031633146101c757600080fd5b6101d08261019d565b6101d957600080fd5b60009182526002602090815260408084209290925560039052902080546001600160a01b03191632179055565b6000546001600160a01b0316331461021d57600080fd5b600080546001600160a01b0319166001600160a01b0392909216919091179055565b6000546001600160a01b0316331461025657600080fd5b600155565b6000546001600160a01b031681565b60009081526002602090815260408083205460039092529091205490916001600160a01b039091169056fea265627a7a7230582038a8e7ef5812fa4c6c7f0abfd2f989f06635476bc41e8401ba11ea0acae56ef864736f6c634300050a0032";

    public static final String FUNC_ROOMRATE = "roomRate";

    public static final String FUNC_ISAVAILABLE = "isAvailable";

    public static final String FUNC_BOOKROOM = "bookRoom";

    public static final String FUNC_CHANGEHOTELROUTERCONTRACT = "changeHotelRouterContract";

    public static final String FUNC_CHANGEROOMRATE = "changeRoomRate";

    public static final String FUNC_HOTELROUTERCONTRACT = "hotelRouterContract";

    public static final String FUNC_GETBOOKINGINFO = "getBookingInfo";

    @Deprecated
    protected HotelRoom(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    protected HotelRoom(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<BigInteger> roomRate() {
        final Function function = new Function(FUNC_ROOMRATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] roomRate_AsSignedCrosschainSubordinateView(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_ROOMRATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
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

    public RemoteFunctionCall<TransactionReceipt> bookRoom(BigInteger _date, BigInteger _uniqueId) {
        final Function function = new Function(
                FUNC_BOOKROOM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_uniqueId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] bookRoom_AsSignedCrosschainSubordinateTransaction(BigInteger _date, BigInteger _uniqueId, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_BOOKROOM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_uniqueId)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> bookRoom_AsCrosschainOriginatingTransaction(BigInteger _date, BigInteger _uniqueId, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_BOOKROOM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_uniqueId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> changeHotelRouterContract(String _hotelRouterContract) {
        final Function function = new Function(
                FUNC_CHANGEHOTELROUTERCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] changeHotelRouterContract_AsSignedCrosschainSubordinateTransaction(String _hotelRouterContract, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_CHANGEHOTELROUTERCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> changeHotelRouterContract_AsCrosschainOriginatingTransaction(String _hotelRouterContract, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_CHANGEHOTELROUTERCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> changeRoomRate(BigInteger _roomRate) {
        final Function function = new Function(
                FUNC_CHANGEROOMRATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_roomRate)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] changeRoomRate_AsSignedCrosschainSubordinateTransaction(BigInteger _roomRate, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_CHANGEROOMRATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_roomRate)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> changeRoomRate_AsCrosschainOriginatingTransaction(BigInteger _roomRate, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_CHANGEROOMRATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_roomRate)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<String> hotelRouterContract() {
        final Function function = new Function(FUNC_HOTELROUTERCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public byte[] hotelRouterContract_AsSignedCrosschainSubordinateView(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_HOTELROUTERCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return createSignedSubordinateView(function, crosschainContext);
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

    @Deprecated
    public static HotelRoom load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new HotelRoom(contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    public static HotelRoom load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        return new HotelRoom(contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public static RemoteCall<HotelRoom> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, String _hotelRouterContract, BigInteger _roomRate) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_roomRate)));
        return deployLockableContractRemoteCall(HotelRoom.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<HotelRoom> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _hotelRouterContract, BigInteger _roomRate) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_roomRate)));
        return deployLockableContractRemoteCall(HotelRoom.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, crosschainContext);
    }

    public static RemoteCall<HotelRoom> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, String _hotelRouterContract, BigInteger _roomRate, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_roomRate)));
        return deployLockableContractRemoteCall(HotelRoom.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<HotelRoom> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _hotelRouterContract, BigInteger _roomRate, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_roomRate)));
        return deployLockableContractRemoteCall(HotelRoom.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, crosschainContext);
    }
}
