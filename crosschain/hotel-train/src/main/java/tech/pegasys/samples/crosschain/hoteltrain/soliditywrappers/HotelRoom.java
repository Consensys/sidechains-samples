package tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.6.0-SNAPSHOT.
 */
@SuppressWarnings("rawtypes")
public class HotelRoom extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060405161033e38038061033e8339818101604052604081101561003357600080fd5b508051602090910151600080546001600160a01b0319166001600160a01b039093169290921782556001556102d090819061006e90396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c80633a178d991161005b5780633a178d99146100fe5780638c8c4f3f1461011b578063b054241314610141578063c7f4e6d31461015e5761007d565b80631358646e146100825780632a63ca3e146100ad5780633a04832a146100e4575b600080fd5b6100ab6004803603606081101561009857600080fd5b5080359060208101359060400135610182565b005b6100d0600480360360408110156100c357600080fd5b50803590602001356101ff565b604080519115158252519081900360200190f35b6100ec61021e565b60408051918252519081900360200190f35b6100d06004803603602081101561011457600080fd5b5035610224565b6100ab6004803603602081101561013157600080fd5b50356001600160a01b0316610237565b6100ab6004803603602081101561015757600080fd5b5035610270565b61016661028c565b604080516001600160a01b039092168252519081900360200190f35b6000546001600160a01b0316331461019957600080fd5b6101a283610224565b6101ab57600080fd5b8060015411156101ba57600080fd5b50604080513260601b60208083019190915260348083019490945282518083039094018452605490910182528251928101929092206000938452600290925290912055565b60008160015411158015610217575061021783610224565b9392505050565b60015481565b6000908152600260205260409020541590565b6000546001600160a01b0316331461024e57600080fd5b600080546001600160a01b0319166001600160a01b0392909216919091179055565b6000546001600160a01b0316331461028757600080fd5b600155565b6000546001600160a01b03168156fea265627a7a7230582050f9873478b3cd3ca0cb3b54d108cbaab9d0ab19c087b383673a89f18c59f2de64736f6c634300050a0032";

    public static final String FUNC_BOOKROOM = "bookRoom";

    public static final String FUNC_ISAVAILABLEFORLESSTHAN = "isAvailableForLessThan";

    public static final String FUNC_ROOMRATE = "roomRate";

    public static final String FUNC_ISAVAILABLE = "isAvailable";

    public static final String FUNC_CHANGEHOTELROUTERCONTRACT = "changeHotelRouterContract";

    public static final String FUNC_CHANGEROOMRATE = "changeRoomRate";

    public static final String FUNC_HOTELROUTERCONTRACT = "hotelRouterContract";

    @Deprecated
    protected HotelRoom(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected HotelRoom(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected HotelRoom(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected HotelRoom(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> bookRoom(BigInteger _date, BigInteger _uniqueId, BigInteger _maxAmountToPay) {
        final Function function = new Function(
                FUNC_BOOKROOM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_uniqueId), 
                new org.web3j.abi.datatypes.generated.Uint256(_maxAmountToPay)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> isAvailableForLessThan(BigInteger _date, BigInteger _maxAmountToPay) {
        final Function function = new Function(FUNC_ISAVAILABLEFORLESSTHAN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_maxAmountToPay)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> roomRate() {
        final Function function = new Function(FUNC_ROOMRATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> isAvailable(BigInteger _date) {
        final Function function = new Function(FUNC_ISAVAILABLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> changeHotelRouterContract(String _hotelRouterContract) {
        final Function function = new Function(
                FUNC_CHANGEHOTELROUTERCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> changeRoomRate(BigInteger _roomRate) {
        final Function function = new Function(
                FUNC_CHANGEROOMRATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_roomRate)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> hotelRouterContract() {
        final Function function = new Function(FUNC_HOTELROUTERCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static HotelRoom load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new HotelRoom(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static HotelRoom load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new HotelRoom(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static HotelRoom load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new HotelRoom(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static HotelRoom load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new HotelRoom(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<HotelRoom> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _hotelRouterContract, BigInteger _roomRate) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_roomRate)));
        return deployRemoteCall(HotelRoom.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<HotelRoom> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _hotelRouterContract, BigInteger _roomRate) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_roomRate)));
        return deployRemoteCall(HotelRoom.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<HotelRoom> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _hotelRouterContract, BigInteger _roomRate) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_roomRate)));
        return deployRemoteCall(HotelRoom.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<HotelRoom> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _hotelRouterContract, BigInteger _roomRate) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _hotelRouterContract), 
                new org.web3j.abi.datatypes.generated.Uint256(_roomRate)));
        return deployRemoteCall(HotelRoom.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
