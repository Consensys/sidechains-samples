package tech.pegasys.samples.crosschain.atomicswapether.soliditywrappers;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
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
public class AtomicSwapRegistration extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b506102f3806100206000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c80634420e4861461005157806350ee9b101461007957806354564886146100a857806393b518bb146100e7575b600080fd5b6100776004803603602081101561006757600080fd5b50356001600160a01b031661010a565b005b6100966004803603602081101561008f57600080fd5b5035610239565b60408051918252519081900360200190f35b6100cb600480360360408110156100be57600080fd5b508035906020013561024e565b604080516001600160a01b039092168252519081900360200190f35b610096600480360360408110156100fd57600080fd5b5080359060200135610287565b60008190506000816001600160a01b03166353c6cc006040518163ffffffff1660e01b815260040160206040518083038186803b15801561014a57600080fd5b505afa15801561015e573d6000803e3d6000fd5b505050506040513d602081101561017457600080fd5b505160408051633ba0b9a960e01b815290519192506001600160a01b03841691633ba0b9a991600480820192602092909190829003018186803b1580156101ba57600080fd5b505afa1580156101ce573d6000803e3d6000fd5b505050506040513d60208110156101e457600080fd5b50516000918252602082815260408084206001600160a01b0390961680855286835290842092909255828152600194850180549586018155835290912090920180546001600160a01b03191690921790915550565b60009081526020819052604090206001015490565b600082815260208190526040812060010180548390811061026b57fe5b6000918252602090912001546001600160a01b03169392505050565b600080610294848461024e565b6000858152602081815260408083206001600160a01b03909416835292905220549150509291505056fea265627a7a7230582064d493fe8874b537cfa7093a021523e6a4a2a1c97a4f342a89ddec3c5757a76a64736f6c634300050a0032";

    public static final String FUNC_REGISTER = "register";

    public static final String FUNC_GETOFFERADDRESSESSIZE = "getOfferAddressesSize";

    public static final String FUNC_GETOFFERSENDERCONTRACT = "getOfferSenderContract";

    public static final String FUNC_GETOFFEREXCHANGERATE = "getOfferExchangeRate";

    @Deprecated
    protected AtomicSwapRegistration(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected AtomicSwapRegistration(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected AtomicSwapRegistration(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected AtomicSwapRegistration(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> register(String _senderContract) {
        final Function function = new Function(
                FUNC_REGISTER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _senderContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getOfferAddressesSize(BigInteger _sidechainId) {
        final Function function = new Function(FUNC_GETOFFERADDRESSESSIZE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> getOfferSenderContract(BigInteger _sidechainId, BigInteger offset) {
        final Function function = new Function(FUNC_GETOFFERSENDERCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId), 
                new org.web3j.abi.datatypes.generated.Uint256(offset)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> getOfferExchangeRate(BigInteger _sidechainId, BigInteger offset) {
        final Function function = new Function(FUNC_GETOFFEREXCHANGERATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId), 
                new org.web3j.abi.datatypes.generated.Uint256(offset)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static AtomicSwapRegistration load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new AtomicSwapRegistration(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static AtomicSwapRegistration load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AtomicSwapRegistration(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static AtomicSwapRegistration load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new AtomicSwapRegistration(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static AtomicSwapRegistration load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new AtomicSwapRegistration(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<AtomicSwapRegistration> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AtomicSwapRegistration.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<AtomicSwapRegistration> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AtomicSwapRegistration.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<AtomicSwapRegistration> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AtomicSwapRegistration.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<AtomicSwapRegistration> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AtomicSwapRegistration.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
