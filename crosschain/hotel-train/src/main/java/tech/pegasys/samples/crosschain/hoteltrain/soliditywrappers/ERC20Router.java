package tech.pegasys.samples.crosschain.hoteltrain.soliditywrappers;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
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
public class ERC20Router extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50600380546001600160a01b03191633179055610f6a806100326000396000f3fe608060405234801561001057600080fd5b50600436106100cf5760003560e01c80638da5cb5b1161008c578063a457c2d711610066578063a457c2d71461024b578063a9059cbb14610277578063dd62ed3e146102a3578063eff63433146102d1576100cf565b80638da5cb5b146101de5780639953904d14610202578063a0712d681461022e576100cf565b8063095ea7b3146100d457806318160ddd1461011457806323b872dd1461012e57806339509351146101645780634c6d0f2b1461019057806370a08231146101b8575b600080fd5b610100600480360360408110156100ea57600080fd5b506001600160a01b038135169060200135610374565b604080519115158252519081900360200190f35b61011c61038a565b60408051918252519081900360200190f35b6101006004803603606081101561014457600080fd5b506001600160a01b03813581169160208101359091169060400135610390565b6101006004803603604081101561017a57600080fd5b506001600160a01b038135169060200135610418565b6101b6600480360360208110156101a657600080fd5b50356001600160a01b0316610454565b005b61011c600480360360208110156101ce57600080fd5b50356001600160a01b03166105eb565b6101e66106f4565b604080516001600160a01b039092168252519081900360200190f35b6101e66004803603604081101561021857600080fd5b506001600160a01b038135169060200135610703565b6101b66004803603602081101561024457600080fd5b5035610743565b6101006004803603604081101561026157600080fd5b506001600160a01b03813516906020013561088b565b6101006004803603604081101561028d57600080fd5b506001600160a01b0381351690602001356108fb565b61011c600480360360408110156102b957600080fd5b506001600160a01b0381358116916020013516610908565b6101b6600480360360208110156102e757600080fd5b81019060208101813564010000000081111561030257600080fd5b82018360208201111561031457600080fd5b8035906020019184602083028401116401000000008311171561033657600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600092019190915250929550610933945050505050565b6000610381338484610a44565b50600192915050565b60025490565b600061039d848484610b30565b6001600160a01b0384166000908152600160209081526040808320338452909152902054828110156104005760405162461bcd60e51b8152600401808060200182810382526028815260200180610ea06028913960400191505060405180910390fd5b61040d8533858403610a44565b506001949350505050565b6001600160a01b03821660009081526001602090815260408083203380855292528220549061044a9085858401610a44565b5060019392505050565b6001600160a01b038116600090815260208181526040918290208054835181840281018401909452808452606093928301828280156104bc57602002820191906000526020600020905b81546001600160a01b0316815260019091019060200180831161049e575b505050505090508051600014156104d257600080fd5b600060015b825181101561056d5760008382815181106104ee57fe5b60200260200101519050806001600160a01b031663853828b66040518163ffffffff1660e01b8152600401602060405180830381600087803b15801561053357600080fd5b505af1158015610547573d6000803e3d6000fd5b505050506040513d602081101561055d57600080fd5b50519290920191506001016104d7565b5060008260008151811061057d57fe5b60200260200101519050806001600160a01b0316631003e2d2836040518263ffffffff1660e01b815260040180828152602001915050600060405180830381600087803b1580156105cd57600080fd5b505af11580156105e1573d6000803e3d6000fd5b5050505050505050565b6001600160a01b038116600090815260208181526040808320805482518185028101850190935280835260609383018282801561065157602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610633575b5050505050905080516000141561066c5760009150506106ef565b60008160008151811061067b57fe5b60200260200101519050806001600160a01b031663b69ef8a86040518163ffffffff1660e01b815260040160206040518083038186803b1580156106be57600080fd5b505afa1580156106d2573d6000803e3d6000fd5b505050506040513d60208110156106e857600080fd5b5051925050505b919050565b6003546001600160a01b031681565b6001600160a01b038216600090815260208190526040812080548390811061072757fe5b6000918252602090912001546001600160a01b03169392505050565b6003546001600160a01b0316331461075a57600080fd5b600280548201905533600090815260208181526040918290208054835181840281018401909452808452606093928301828280156107c157602002820191906000526020600020905b81546001600160a01b031681526001909101906020018083116107a3575b505050505090508051600014156107d757600080fd5b6000816000815181106107e657fe5b60200260200101519050806001600160a01b0316631003e2d2846040518263ffffffff1660e01b815260040180828152602001915050600060405180830381600087803b15801561083657600080fd5b505af115801561084a573d6000803e3d6000fd5b5050604080518681529051339350600092507fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9181900360200190a3505050565b3360009081526001602090815260408083206001600160a01b0386168452909152812054828110156108ee5760405162461bcd60e51b8152600401808060200182810382526025815260200180610f116025913960400191505060405180910390fd5b61044a3385858403610a44565b6000610381338484610b30565b6001600160a01b03918216600090815260016020908152604080832093909416825291909152205490565b60005b8151811015610a4057600082828151811061094d57fe5b60200260200101519050336001600160a01b0316816001600160a01b0316638da5cb5b6040518163ffffffff1660e01b815260040160206040518083038186803b15801561099a57600080fd5b505afa1580156109ae573d6000803e3d6000fd5b505050506040513d60208110156109c457600080fd5b50516001600160a01b0316146109d957600080fd5b33600090815260208190526040902083518490849081106109f657fe5b60209081029190910181015182546001808201855560009485529290932090920180546001600160a01b0319166001600160a01b0390931692909217909155919091019050610936565b5050565b6001600160a01b038316610a895760405162461bcd60e51b8152600401808060200182810382526024815260200180610eed6024913960400191505060405180910390fd5b6001600160a01b038216610ace5760405162461bcd60e51b8152600401808060200182810382526022815260200180610e5c6022913960400191505060405180910390fd5b6001600160a01b03808416600081815260016020908152604080832094871680845294825291829020859055815185815291517f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259281900390910190a3505050565b6001600160a01b038316610b755760405162461bcd60e51b8152600401808060200182810382526025815260200180610ec86025913960400191505060405180910390fd5b6001600160a01b038216610bba5760405162461bcd60e51b8152600401808060200182810382526023815260200180610e396023913960400191505060405180910390fd5b6001600160a01b03831660009081526020818152604091829020805483518184028101840190945280845260609392830182828015610c2257602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610c04575b50505050509050805160001415610c6a5760405162461bcd60e51b8152600401808060200182810382526022815260200180610e7e6022913960400191505060405180910390fd5b600081600081518110610c7957fe5b602002602001015190506060600080866001600160a01b03166001600160a01b03168152602001908152602001600020805480602002602001604051908101604052809291908181526020018280548015610cfd57602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610cdf575b50505050509050805160001415610d1357600080fd5b60008060009050828181518110610d2657fe5b60200260200101519150836001600160a01b03166327ee58a6876040518263ffffffff1660e01b815260040180828152602001915050600060405180830381600087803b158015610d7657600080fd5b505af1158015610d8a573d6000803e3d6000fd5b50505050816001600160a01b0316631003e2d2876040518263ffffffff1660e01b815260040180828152602001915050600060405180830381600087803b158015610dd457600080fd5b505af1158015610de8573d6000803e3d6000fd5b50506040805189815290516001600160a01b03808c1694508c1692507fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9181900360200190a3505050505050505056fe45524332303a207472616e7366657220746f20746865207a65726f206164647265737345524332303a20617070726f766520746f20746865207a65726f206164647265737345524332303a206e6f2073656e646572206c6f636b61626c6520636f6e747261637445524332303a207472616e7366657220616d6f756e74206578636565647320616c6c6f77616e636545524332303a207472616e736665722066726f6d20746865207a65726f206164647265737345524332303a20617070726f76652066726f6d20746865207a65726f206164647265737345524332303a2064656372656173656420616c6c6f77616e63652062656c6f77207a65726fa265627a7a72305820f88f4641da37a38bb65bbdbda0c2bbe469d5e9c78440b1df45c9da8c35c9c66c64736f6c634300050a0032";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_INCREASEALLOWANCE = "increaseAllowance";

    public static final String FUNC_CONDENSE = "condense";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_GETLOCKABLEACCOUNTADDRESS = "getLockableAccountAddress";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_DECREASEALLOWANCE = "decreaseAllowance";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_CREATEACCOUNT = "createAccount";

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected ERC20Router(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ERC20Router(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ERC20Router(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ERC20Router(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String _spender, BigInteger _amount) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _spender), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String _sender, String _recipient, BigInteger _amount) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _sender), 
                new org.web3j.abi.datatypes.Address(160, _recipient), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> increaseAllowance(String _spender, BigInteger _addedValue) {
        final Function function = new Function(
                FUNC_INCREASEALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _spender), 
                new org.web3j.abi.datatypes.generated.Uint256(_addedValue)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> condense(String _account) {
        final Function function = new Function(
                FUNC_CONDENSE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String account) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getLockableAccountAddress(String _account, BigInteger _instance) {
        final Function function = new Function(FUNC_GETLOCKABLEACCOUNTADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.generated.Uint256(_instance)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> mint(BigInteger _amount) {
        final Function function = new Function(
                FUNC_MINT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> decreaseAllowance(String _spender, BigInteger _subtractedValue) {
        final Function function = new Function(
                FUNC_DECREASEALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _spender), 
                new org.web3j.abi.datatypes.generated.Uint256(_subtractedValue)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transfer(String _recipient, BigInteger _amount) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _recipient), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> allowance(String _owner, String _spender) {
        final Function function = new Function(FUNC_ALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _owner), 
                new org.web3j.abi.datatypes.Address(160, _spender)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> createAccount(List<String> _lockableAccountContracts) {
        final Function function = new Function(
                FUNC_CREATEACCOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(_lockableAccountContracts, org.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    @Deprecated
    public static ERC20Router load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ERC20Router(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ERC20Router load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ERC20Router(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ERC20Router load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ERC20Router(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ERC20Router load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC20Router(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ERC20Router> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC20Router.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ERC20Router> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ERC20Router.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ERC20Router> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ERC20Router.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ERC20Router> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ERC20Router.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger value;
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String spender;

        public BigInteger value;
    }
}
