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
public class TrainRouter extends CrosschainContract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516108da3803806108da8339818101604052604081101561003357600080fd5b50805160209091015160018054336001600160a01b031991821617909155600392909255600480549092166001600160a01b0390911617905561085f8061007b6000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80632465d6fd1461006757806375958733146100925780637b27ea1d146100b3578063834cad1314610123578063df25307814610152578063e9cbe8031461016f575b600080fd5b6100906004803603606081101561007d57600080fd5b5080359060208101359060400135610192565b005b61009a6104d2565b6040805192835260208301919091528051918290030190f35b610090600480360360208110156100c957600080fd5b8101906020810181356401000000008111156100e457600080fd5b8201836020820111156100f657600080fd5b8035906020019184602083028401116401000000008311171561011857600080fd5b5090925090506104d9565b6101406004803603602081101561013957600080fd5b503561054c565b60408051918252519081900360200190f35b6100906004803603602081101561016857600080fd5b5035610603565b61009a6004803603604081101561018557600080fd5b5080359060200135610608565b6002548310156101d35760405162461bcd60e51b81526004018080602001828103825260228152602001806108096022913960400191505060405180910390fd5b600354600254018311156102185760405162461bcd60e51b81526004018080602001828103825260308152602001806107d96030913960400191505060405180910390fd5b60005b60005481101561048a5761024f6000828154811061023557fe5b6000918252602090912001546001600160a01b0316610777565b61048257600080828154811061026157fe5b6000918252602091829020015460408051630ebb449760e01b815290516001600160a01b0390921692630ebb449792600480840193829003018186803b1580156102aa57600080fd5b505afa1580156102be573d6000803e3d6000fd5b505050506040513d60208110156102d457600080fd5b5051905082811180159061036d5750600082815481106102f057fe5b6000918252602091829020015460408051633a178d9960e01b81526004810189905290516001600160a01b0390921692633a178d9992602480840193829003018186803b15801561034057600080fd5b505afa158015610354573d6000803e3d6000fd5b505050506040513d602081101561036a57600080fd5b50515b15610480576000828154811061037f57fe5b60009182526020822001546040805163080f94e960e01b8152600481018990526024810188905290516001600160a01b039092169263080f94e99260448084019382900301818387803b1580156103d557600080fd5b505af11580156103e9573d6000803e3d6000fd5b505060048054600154604080516323b872dd60e01b815232948101949094526001600160a01b0391821660248501526044840187905251911693506323b872dd925060648083019260209291908290030181600087803b15801561044c57600080fd5b505af1158015610460573d6000803e3d6000fd5b505050506040513d602081101561047657600080fd5b506104cd92505050565b505b60010161021b565b506040805162461bcd60e51b81526020600482015260126024820152714e6f20736561747320617661696c61626c6560701b604482015290519081900360640190fd5b505050565b6000908190565b6001546001600160a01b031633146104f057600080fd5b60005b818110156104cd57600083838381811061050957fe5b835460018181018655600095865260209586902090910180546001600160a01b0319166001600160a01b03969093029490940135949094161790915550016104f3565b6000805b6000548110156105fd57600080828154811061056857fe5b6000918252602090912001546040805163cac2afd760e01b81526004810187905281516001600160a01b039093169263cac2afd792602480840193919291829003018186803b1580156105ba57600080fd5b505afa1580156105ce573d6000803e3d6000fd5b505050506040513d60408110156105e457600080fd5b50519050806105f4576001909201915b50600101610550565b50919050565b600255565b600080805b600054811015610767576000806000838154811061062757fe5b6000918252602090912001546040805163cac2afd760e01b8152600481018a905281516001600160a01b039093169263cac2afd792602480840193919291829003018186803b15801561067957600080fd5b505afa15801561068d573d6000803e3d6000fd5b505050506040513d60408110156106a357600080fd5b508051602090910151909250905085821480156106c857506001600160a01b03811632145b1561075d57829350600083815481106106dd57fe5b6000918252602091829020015460408051630ebb449760e01b815290516001600160a01b0390921692630ebb449792600480840193829003018186803b15801561072657600080fd5b505afa15801561073a573d6000803e3d6000fd5b505050506040513d602081101561075057600080fd5b5051945061077092505050565b505060010161060d565b50600091508190505b9250929050565b600060206107836107ba565b6001600160a01b03841681526107976107ba565b602060798183868684600019fa6107ad57600080fd5b5050511515949350505050565b6040518060200160405280600190602082028038833950919291505056fe426f6f6b696e6720646174652063616e206e6f74206265206265796f6e6420746865206576656e7420686f72697a6f6e426f6f6b696e672064617465206d75737420626520696e2074686520667574757265a265627a7a723058202b049147c77599b1ea89c1c50182d7e8636851363af4363c53ab28964a22dafe64736f6c634300050a0032";

    public static final String FUNC_BOOKSEAT = "bookSeat";

    public static final String FUNC_GETSEATRATES = "getSeatRates";

    public static final String FUNC_ADDSEATS = "addSeats";

    public static final String FUNC_GETNUMBERSEATSAVAILABLE = "getNumberSeatsAvailable";

    public static final String FUNC_CHANGEDATE = "changeDate";

    public static final String FUNC_GETSEATINFORMATION = "getSeatInformation";

    @Deprecated
    protected TrainRouter(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    protected TrainRouter(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> bookSeat(BigInteger _date, BigInteger _uniqueId, BigInteger _maxAmountToPay) {
        final Function function = new Function(
                FUNC_BOOKSEAT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_uniqueId), 
                new org.web3j.abi.datatypes.generated.Uint256(_maxAmountToPay)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] bookSeat_AsSignedCrosschainSubordinateTransaction(BigInteger _date, BigInteger _uniqueId, BigInteger _maxAmountToPay, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_BOOKSEAT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_uniqueId), 
                new org.web3j.abi.datatypes.generated.Uint256(_maxAmountToPay)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> bookSeat_AsCrosschainOriginatingTransaction(BigInteger _date, BigInteger _uniqueId, BigInteger _maxAmountToPay, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_BOOKSEAT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_uniqueId), 
                new org.web3j.abi.datatypes.generated.Uint256(_maxAmountToPay)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<Tuple2<BigInteger, BigInteger>> getSeatRates() {
        final Function function = new Function(FUNC_GETSEATRATES, 
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

    public byte[] getSeatRates_AsSignedCrosschainSubordinateView(final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_GETSEATRATES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> addSeats(List<String> _seatsToAdd) {
        final Function function = new Function(
                FUNC_ADDSEATS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(_seatsToAdd, org.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public byte[] addSeats_AsSignedCrosschainSubordinateTransaction(List<String> _seatsToAdd, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(
                FUNC_ADDSEATS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(_seatsToAdd, org.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedSubordinateTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<TransactionReceipt> addSeats_AsCrosschainOriginatingTransaction(List<String> _seatsToAdd, final CrosschainContext crosschainContext) {
        final Function function = new Function(
                FUNC_ADDSEATS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(_seatsToAdd, org.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallCrosschainTransaction(function, crosschainContext);
    }

    public RemoteFunctionCall<BigInteger> getNumberSeatsAvailable(BigInteger _date) {
        final Function function = new Function(FUNC_GETNUMBERSEATSAVAILABLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public byte[] getNumberSeatsAvailable_AsSignedCrosschainSubordinateView(BigInteger _date, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_GETNUMBERSEATSAVAILABLE, 
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

    public RemoteFunctionCall<Tuple2<BigInteger, BigInteger>> getSeatInformation(BigInteger _date, BigInteger _uniqueId) {
        final Function function = new Function(FUNC_GETSEATINFORMATION, 
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

    public byte[] getSeatInformation_AsSignedCrosschainSubordinateView(BigInteger _date, BigInteger _uniqueId, final CrosschainContext crosschainContext) throws IOException {
        final Function function = new Function(FUNC_GETSEATINFORMATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date), 
                new org.web3j.abi.datatypes.generated.Uint256(_uniqueId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return createSignedSubordinateView(function, crosschainContext);
    }

    @Deprecated
    public static TrainRouter load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TrainRouter(contractAddress, besu, crosschainTransactionManager, gasPrice, gasLimit);
    }

    public static TrainRouter load(String contractAddress, Besu besu, CrosschainTransactionManager crosschainTransactionManager, ContractGasProvider contractGasProvider) {
        return new TrainRouter(contractAddress, besu, crosschainTransactionManager, contractGasProvider);
    }

    public static RemoteCall<TrainRouter> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _eventHorizon, String _erc20Router) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployLockableContractRemoteCall(TrainRouter.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<TrainRouter> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _eventHorizon, String _erc20Router) {
        CrosschainContext crosschainContext = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployLockableContractRemoteCall(TrainRouter.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, crosschainContext);
    }

    public static RemoteCall<TrainRouter> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _eventHorizon, String _erc20Router, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployLockableContractRemoteCall(TrainRouter.class, besu, transactionManager, contractGasProvider, BINARY, encodedConstructor, crosschainContext);
    }

    @Deprecated
    public static RemoteCall<TrainRouter> deployLockable(Besu besu, CrosschainTransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _eventHorizon, String _erc20Router, final CrosschainContext crosschainContext) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployLockableContractRemoteCall(TrainRouter.class, besu, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, crosschainContext);
    }
}
