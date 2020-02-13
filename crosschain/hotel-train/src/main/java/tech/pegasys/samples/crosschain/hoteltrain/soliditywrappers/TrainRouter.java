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
public class TrainRouter extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060405161080c38038061080c8339818101604052604081101561003357600080fd5b50805160209091015160018054336001600160a01b031991821617909155600392909255600480549092166001600160a01b039091161790556107918061007b6000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80632465d6fd1461006757806375958733146100925780637b27ea1d146100b3578063834cad1314610123578063df25307814610152578063e9cbe8031461016f575b600080fd5b6100906004803603606081101561007d57600080fd5b5080359060208101359060400135610192565b005b61009a610460565b6040805192835260208301919091528051918290030190f35b610090600480360360208110156100c957600080fd5b8101906020810181356401000000008111156100e457600080fd5b8201836020820111156100f657600080fd5b8035906020019184602083028401116401000000008311171561011857600080fd5b509092509050610467565b6101406004803603602081101561013957600080fd5b50356104df565b60408051918252519081900360200190f35b6100906004803603602081101561016857600080fd5b5035610596565b61009a6004803603604081101561018557600080fd5b508035906020013561059b565b6002548310156101d35760405162461bcd60e51b815260040180806020018281038252602281526020018061073b6022913960400191505060405180910390fd5b600354600254018311156102185760405162461bcd60e51b815260040180806020018281038252603081526020018061070b6030913960400191505060405180910390fd5b60005b60005481101561045a57600080828154811061023357fe5b6000918252602091829020015460408051630ebb449760e01b815290516001600160a01b0390921692630ebb449792600480840193829003018186803b15801561027c57600080fd5b505afa158015610290573d6000803e3d6000fd5b505050506040513d60208110156102a657600080fd5b5051905082811180159061033f5750600082815481106102c257fe5b6000918252602091829020015460408051633a178d9960e01b81526004810189905290516001600160a01b0390921692633a178d9992602480840193829003018186803b15801561031257600080fd5b505afa158015610326573d6000803e3d6000fd5b505050506040513d602081101561033c57600080fd5b50515b15610451576000828154811061035157fe5b60009182526020822001546040805163080f94e960e01b8152600481018990526024810188905290516001600160a01b039092169263080f94e99260448084019382900301818387803b1580156103a757600080fd5b505af11580156103bb573d6000803e3d6000fd5b505060048054600154604080516323b872dd60e01b815232948101949094526001600160a01b0391821660248501526044840187905251911693506323b872dd925060648083019260209291908290030181600087803b15801561041e57600080fd5b505af1158015610432573d6000803e3d6000fd5b505050506040513d602081101561044857600080fd5b5061045a915050565b5060010161021b565b50505050565b6000908190565b6001546001600160a01b0316331461047e57600080fd5b60005b818110156104da57600083838381811061049757fe5b835460018181018655600095865260209586902090910180546001600160a01b0319166001600160a01b0396909302949094013594909416179091555001610481565b505050565b6000805b6000548110156105905760008082815481106104fb57fe5b6000918252602090912001546040805163cac2afd760e01b81526004810187905281516001600160a01b039093169263cac2afd792602480840193919291829003018186803b15801561054d57600080fd5b505afa158015610561573d6000803e3d6000fd5b505050506040513d604081101561057757600080fd5b5051905080610587576001909201915b506001016104e3565b50919050565b600255565b600080805b6000548110156106fa57600080600083815481106105ba57fe5b6000918252602090912001546040805163cac2afd760e01b8152600481018a905281516001600160a01b039093169263cac2afd792602480840193919291829003018186803b15801561060c57600080fd5b505afa158015610620573d6000803e3d6000fd5b505050506040513d604081101561063657600080fd5b5080516020909101519092509050858214801561065b57506001600160a01b03811632145b156106f0578293506000838154811061067057fe5b6000918252602091829020015460408051630ebb449760e01b815290516001600160a01b0390921692630ebb449792600480840193829003018186803b1580156106b957600080fd5b505afa1580156106cd573d6000803e3d6000fd5b505050506040513d60208110156106e357600080fd5b5051945061070392505050565b50506001016105a0565b50600091508190505b925092905056fe426f6f6b696e6720646174652063616e206e6f74206265206265796f6e6420746865206576656e7420686f72697a6f6e426f6f6b696e672064617465206d75737420626520696e2074686520667574757265a265627a7a723058207fb7277941596ba03f8321df161f4e14156dee6141677c657ce7e0634b0d109a64736f6c634300050a0032";

    public static final String FUNC_BOOKSEAT = "bookSeat";

    public static final String FUNC_GETSEATRATES = "getSeatRates";

    public static final String FUNC_ADDSEATS = "addSeats";

    public static final String FUNC_GETNUMBERSEATSAVAILABLE = "getNumberSeatsAvailable";

    public static final String FUNC_CHANGEDATE = "changeDate";

    public static final String FUNC_GETSEATINFORMATION = "getSeatInformation";

    @Deprecated
    protected TrainRouter(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TrainRouter(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected TrainRouter(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected TrainRouter(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
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

    public RemoteFunctionCall<TransactionReceipt> addSeats(List<String> _seatsToAdd) {
        final Function function = new Function(
                FUNC_ADDSEATS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(_seatsToAdd, org.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getNumberSeatsAvailable(BigInteger _date) {
        final Function function = new Function(FUNC_GETNUMBERSEATSAVAILABLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_date)), 
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

    @Deprecated
    public static TrainRouter load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TrainRouter(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TrainRouter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TrainRouter(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TrainRouter load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new TrainRouter(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TrainRouter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TrainRouter(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TrainRouter> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger _eventHorizon, String _erc20Router) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployRemoteCall(TrainRouter.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<TrainRouter> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _eventHorizon, String _erc20Router) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployRemoteCall(TrainRouter.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<TrainRouter> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _eventHorizon, String _erc20Router) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployRemoteCall(TrainRouter.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<TrainRouter> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _eventHorizon, String _erc20Router) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventHorizon), 
                new org.web3j.abi.datatypes.Address(160, _erc20Router)));
        return deployRemoteCall(TrainRouter.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
