package tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers.cc;

import java.io.IOException;
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
public class HotelRouter extends CrosschainContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516108da3803806108da8339818101604052604081101561003357600080fd5b50805160209091015160018054336001600160a01b031991821617909155600392909255600480549092166001600160a01b0390911617905561085f8061007b6000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80631358646e146100675780632e69d210146100925780635121b831146100ce57806389e37733146100d6578063d131aa7e14610146578063df25307814610175575b600080fd5b6100906004803603606081101561007d57600080fd5b5080359060208101359060400135610192565b005b6100b5600480360360408110156100a857600080fd5b50803590602001356104d2565b6040805192835260208301919091528051918290030190f35b6100b5610641565b610090600480360360208110156100ec57600080fd5b81019060208101813564010000000081111561010757600080fd5b82018360208201111561011957600080fd5b8035906020019184602083028401116401000000008311171561013b57600080fd5b509092509050610648565b6101636004803603602081101561015c57600080fd5b50356106bb565b60408051918252519081900360200190f35b6100906004803603602081101561018b57600080fd5b5035610772565b6002548310156101d35760405162461bcd60e51b81526004018080602001828103825260228152602001806108096022913960400191505060405180910390fd5b600354600254018311156102185760405162461bcd60e51b81526004018080602001828103825260308152602001806107d96030913960400191505060405180910390fd5b60005b60005481101561048a5761024f6000828154811061023557fe5b6000918252602090912001546001600160a01b0316610777565b61048257600080828154811061026157fe5b6000918252602091829020015460408051631d02419560e11b815290516001600160a01b0390921692633a04832a92600480840193829003018186803b1580156102aa57600080fd5b505afa1580156102be573d6000803e3d6000fd5b505050506040513d60208110156102d457600080fd5b5051905082811180159061036d5750600082815481106102f057fe5b6000918252602091829020015460408051633a178d9960e01b81526004810189905290516001600160a01b0390921692633a178d9992602480840193829003018186803b15801561034057600080fd5b505afa158015610354573d6000803e3d6000fd5b505050506040513d602081101561036a57600080fd5b50515b15610480576000828154811061037f57fe5b600091825260208220015460408051631147434360e31b8152600481018990526024810188905290516001600160a01b0390921692638a3a1a189260448084019382900301818387803b1580156103d557600080fd5b505af11580156103e9573d6000803e3d6000fd5b505060048054600154604080516323b872dd60e01b815232948101949094526001600160a01b0391821660248501526044840187905251911693506323b872dd925060648083019260209291908290030181600087803b15801561044c57600080fd5b505af1158015610460573d6000803e3d6000fd5b505050506040513d602081101561047657600080fd5b506104cd92505050565b505b60010161021b565b506040805162461bcd60e51b81526020600482015260126024820152714e6f20726f6f6d7320617661696c61626c6560701b604482015290519081900360640190fd5b505050565b600080805b60005481101561063157600080600083815481106104f157fe5b6000918252602090912001546040805163cac2afd760e01b8152600481018a905281516001600160a01b039093169263cac2afd792602480840193919291829003018186803b15801561054357600080fd5b505afa158015610557573d6000803e3d6000fd5b505050506040513d604081101561056d57600080fd5b5080516020909101519092509050858214801561059257506001600160a01b03811632145b1561062757829350600083815481106105a757fe5b6000918252602091829020015460408051631d02419560e11b815290516001600160a01b0390921692633a04832a92600480840193829003018186803b1580156105f057600080fd5b505afa158015610604573d6000803e3d6000fd5b505050506040513d602081101561061a57600080fd5b5051945061063a92505050565b50506001016104d7565b50600091508190505b9250929050565b6000908190565b6001546001600160a01b0316331461065f57600080fd5b60005b818110156104cd57600083838381811061067857fe5b835460018181018655600095865260209586902090910180546001600160a01b0319166001600160a01b0396909302949094013594909416179091555001610662565b6000805b60005481101561076c5760008082815481106106d757fe5b6000918252602090912001546040805163cac2afd760e01b81526004810187905281516001600160a01b039093169263cac2afd792602480840193919291829003018186803b15801561072957600080fd5b505afa15801561073d573d6000803e3d6000fd5b505050506040513d604081101561075357600080fd5b5051905080610763576001909201915b506001016106bf565b50919050565b600255565b600060206107836107ba565b6001600160a01b03841681526107976107ba565b602060798183868684600019fa6107ad57600080fd5b5050511515949350505050565b6040518060200160405280600190602082028038833950919291505056fe426f6f6b696e6720646174652063616e206e6f74206265206265796f6e6420746865206576656e7420686f72697a6f6e426f6f6b696e672064617465206d75737420626520696e2074686520667574757265a265627a7a72305820bdfc0158bf21af25b271f9ecc5058fd10f46e764dda4785b0ce8085da63d631d64736f6c634300050a0032";

    public static final String FUNC_BOOKROOM = "bookRoom";

    public static final String FUNC_GETROOMINFORMATION = "getRoomInformation";

    public static final String FUNC_GETROOMRATES = "getRoomRates";

    public static final String FUNC_ADDROOMS = "addRooms";

    public static final String FUNC_GETNUMBERROOMSAVAILABLE = "getNumberRoomsAvailable";

    public static final String FUNC_CHANGEDATE = "changeDate";

    @Deprecated
    protected HotelRouter(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    protected HotelRouter(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, contractGasProvider);
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

    public byte[] bookRoom_AsSignedCrosschainSubordinateTransaction(BigInteger _date, BigInteger _uniqueId, BigInteger _maxAmountToPay, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_BOOKROOM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_uniqueId), 
                new org.web3j.abi.datatypes.generated.Uint256(_maxAmountToPay)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> bookRoom_AsCrosschainOriginatingTransaction(BigInteger _date, BigInteger _uniqueId, BigInteger _maxAmountToPay, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_BOOKROOM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_uniqueId), 
                new org.web3j.abi.datatypes.generated.Uint256(_maxAmountToPay)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<Tuple2<BigInteger, BigInteger>> getRoomInformation(BigInteger _date, BigInteger _uniqueId) {
        final Function function = new Function(FUNC_GETROOMINFORMATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_uniqueId)), 
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

    public byte[] getRoomInformation_AsSignedCrosschainSubordinateView(BigInteger _date, BigInteger _uniqueId, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_GETROOMINFORMATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_uniqueId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, crosschainContext);
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

    public byte[] getRoomRates_AsSignedCrosschainSubordinateView(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_GETROOMRATES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, crosschainContext);
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

    public byte[] addRooms_AsSignedCrosschainSubordinateTransaction(List<String> _roomsToAdd, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_ADDROOMS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(_roomsToAdd, org.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> addRooms_AsCrosschainOriginatingTransaction(List<String> _roomsToAdd, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_ADDROOMS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(_roomsToAdd, org.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<BigInteger> getNumberRoomsAvailable(BigInteger _date) {
        final Function function = new Function(FUNC_GETNUMBERROOMSAVAILABLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] getNumberRoomsAvailable_AsSignedCrosschainSubordinateView(BigInteger _date, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_GETNUMBERROOMSAVAILABLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> changeDate(BigInteger _date) {
        final Function function = new Function(
                FUNC_CHANGEDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] changeDate_AsSignedCrosschainSubordinateTransaction(BigInteger _date, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_CHANGEDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> changeDate_AsCrosschainOriginatingTransaction(BigInteger _date, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_CHANGEDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    @Deprecated
    public static HotelRouter load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new HotelRouter(contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    public static HotelRouter load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        return new HotelRouter(contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public static RemoteCall<HotelRouter> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _eventHorizon, String _erc20Router) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployLockableContractRemoteCall(HotelRouter.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<HotelRouter> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _eventHorizon, String _erc20Router) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployLockableContractRemoteCall(HotelRouter.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, crosschainContext);
    }

    public static RemoteCall<HotelRouter> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _eventHorizon, String _erc20Router, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployLockableContractRemoteCall(HotelRouter.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<HotelRouter> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _eventHorizon, String _erc20Router, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployLockableContractRemoteCall(HotelRouter.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, crosschainContext);
    }
}
