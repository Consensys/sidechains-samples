package tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
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
public class HotelRouter extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516105f53803806105f58339818101604052604081101561003357600080fd5b50805160209091015160018054336001600160a01b031991821617909155600392909255600480549092166001600160a01b0390911617905561057a8061007b6000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80631358646e146100675780632e69d210146100925780635121b831146100ce57806389e37733146100d6578063d131aa7e14610146578063df25307814610175575b600080fd5b6100906004803603606081101561007d57600080fd5b5080359060208101359060400135610192565b005b6100b5600480360360408110156100a857600080fd5b5080359060200135610460565b6040805192835260208301919091528051918290030190f35b6100b5610469565b610090600480360360208110156100ec57600080fd5b81019060208101813564010000000081111561010757600080fd5b82018360208201111561011957600080fd5b8035906020019184602083028401116401000000008311171561013b57600080fd5b509092509050610470565b6101636004803603602081101561015c57600080fd5b50356104e8565b60408051918252519081900360200190f35b6100906004803603602081101561018b57600080fd5b50356104ee565b6002548310156101d35760405162461bcd60e51b81526004018080602001828103825260228152602001806105246022913960400191505060405180910390fd5b600354600254018311156102185760405162461bcd60e51b81526004018080602001828103825260308152602001806104f46030913960400191505060405180910390fd5b60005b60005481101561045a57600080828154811061023357fe5b6000918252602091829020015460408051631d02419560e11b815290516001600160a01b0390921692633a04832a92600480840193829003018186803b15801561027c57600080fd5b505afa158015610290573d6000803e3d6000fd5b505050506040513d60208110156102a657600080fd5b5051905082811180159061033f5750600082815481106102c257fe5b6000918252602091829020015460408051633a178d9960e01b81526004810189905290516001600160a01b0390921692633a178d9992602480840193829003018186803b15801561031257600080fd5b505afa158015610326573d6000803e3d6000fd5b505050506040513d602081101561033c57600080fd5b50515b15610451576000828154811061035157fe5b600091825260208220015460408051631147434360e31b8152600481018990526024810188905290516001600160a01b0390921692638a3a1a189260448084019382900301818387803b1580156103a757600080fd5b505af11580156103bb573d6000803e3d6000fd5b505060048054600154604080516323b872dd60e01b815232948101949094526001600160a01b0391821660248501526044840187905251911693506323b872dd925060648083019260209291908290030181600087803b15801561041e57600080fd5b505af1158015610432573d6000803e3d6000fd5b505050506040513d602081101561044857600080fd5b5061045a915050565b5060010161021b565b50505050565b50600091829150565b6000908190565b6001546001600160a01b0316331461048757600080fd5b60005b818110156104e35760008383838181106104a057fe5b835460018181018655600095865260209586902090910180546001600160a01b0319166001600160a01b039690930294909401359490941617909155500161048a565b505050565b50600090565b60025556fe426f6f6b696e6720646174652063616e206e6f74206265206265796f6e6420746865206576656e7420686f72697a6f6e426f6f6b696e672064617465206d75737420626520696e2074686520667574757265a265627a7a723058206d2c04b8c895bc6bbee78c03d3063c21f8fdede5eee3b4279a81dc627e9c4bd664736f6c634300050a0032";

    public static final String FUNC_BOOKROOM = "bookRoom";

    public static final String FUNC_GETROOMINFORMATION = "getRoomInformation";

    public static final String FUNC_GETROOMRATES = "getRoomRates";

    public static final String FUNC_ADDROOMS = "addRooms";

    public static final String FUNC_GETNUMBERROOMSAVAILABLE = "getNumberRoomsAvailable";

    public static final String FUNC_CHANGEDATE = "changeDate";

    @Deprecated
    protected HotelRouter(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected HotelRouter(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected HotelRouter(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected HotelRouter(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
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

    public RemoteFunctionCall<Tuple2<BigInteger, BigInteger>> getRoomInformation(BigInteger param0, BigInteger param1) {
        final Function function = new Function(FUNC_GETROOMINFORMATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<BigInteger, BigInteger>>(function,
                new Callable<Tuple2<BigInteger, BigInteger>>() {
                    @Override
                    public Tuple2<BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Tuple2<BigInteger, BigInteger>> getRoomRates() {
        final Function function = new Function(FUNC_GETROOMRATES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<BigInteger, BigInteger>>(function,
                new Callable<Tuple2<BigInteger, BigInteger>>() {
                    @Override
                    public Tuple2<BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> addRooms(List<String> _roomsToAdd) {
        final Function function = new Function(
                FUNC_ADDROOMS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(_roomsToAdd, org.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getNumberRoomsAvailable(BigInteger param0) {
        final Function function = new Function(FUNC_GETNUMBERROOMSAVAILABLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> changeDate(BigInteger _date) {
        final Function function = new Function(
                FUNC_CHANGEDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static HotelRouter load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new HotelRouter(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static HotelRouter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new HotelRouter(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static HotelRouter load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new HotelRouter(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static HotelRouter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new HotelRouter(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<HotelRouter> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger _eventHorizon, String _erc20Router) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployRemoteCall(HotelRouter.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<HotelRouter> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _eventHorizon, String _erc20Router) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployRemoteCall(HotelRouter.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<HotelRouter> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _eventHorizon, String _erc20Router) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployRemoteCall(HotelRouter.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<HotelRouter> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _eventHorizon, String _erc20Router) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployRemoteCall(HotelRouter.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
