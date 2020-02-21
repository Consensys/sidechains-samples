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
    private static final String BINARY = "608060405234801561001057600080fd5b50600380546001600160a01b03191633179055611520806100326000396000f3fe608060405234801561001057600080fd5b50600436106101165760003560e01c80639953904d116100a2578063abcff49b11610071578063abcff49b14610396578063bd581215146103bc578063dd62ed3e146103e8578063df27b0e814610416578063eff634331461044257610116565b80639953904d146102f5578063a0712d6814610321578063a457c2d71461033e578063a9059cbb1461036a57610116565b80634c6d0f2b116100e95780634c6d0f2b146101d757806370a08231146101ff57806385d2b2b814610225578063892f44ec146102a55780638da5cb5b146102ed57610116565b8063095ea7b31461011b57806318160ddd1461015b57806323b872dd1461017557806339509351146101ab575b600080fd5b6101476004803603604081101561013157600080fd5b506001600160a01b0381351690602001356104e5565b604080519115158252519081900360200190f35b6101636104fb565b60408051918252519081900360200190f35b6101476004803603606081101561018b57600080fd5b506001600160a01b03813581169160208101359091169060400135610501565b610147600480360360408110156101c157600080fd5b506001600160a01b038135169060200135610518565b6101fd600480360360208110156101ed57600080fd5b50356001600160a01b031661054a565b005b6101636004803603602081101561021557600080fd5b50356001600160a01b031661071f565b6101fd6004803603604081101561023b57600080fd5b6001600160a01b03823516919081019060408101602082013564010000000081111561026657600080fd5b82018360208201111561027857600080fd5b8035906020019184602083028401116401000000008311171561029a57600080fd5b509092509050610828565b6102d1600480360360408110156102bb57600080fd5b506001600160a01b038135169060200135610955565b604080516001600160a01b039092168252519081900360200190f35b6102d1610a48565b6102d16004803603604081101561030b57600080fd5b506001600160a01b038135169060200135610a57565b6101fd6004803603602081101561033757600080fd5b5035610a97565b6101476004803603604081101561035457600080fd5b506001600160a01b038135169060200135610bdf565b6101476004803603604081101561038057600080fd5b506001600160a01b038135169060200135610c4f565b610163600480360360208110156103ac57600080fd5b50356001600160a01b0316610c5c565b610163600480360360408110156103d257600080fd5b506001600160a01b038135169060200135610cd0565b610163600480360360408110156103fe57600080fd5b506001600160a01b0381358116916020013516610d8e565b6102d16004803603604081101561042c57600080fd5b506001600160a01b038135169060200135610db9565b6101fd6004803603602081101561045857600080fd5b81019060208101813564010000000081111561047357600080fd5b82018360208201111561048557600080fd5b803590602001918460208302840111640100000000831117156104a757600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600092019190915250929550610e77945050505050565b60006104f2338484610f88565b50600192915050565b60025490565b600061050e848484611074565b5060019392505050565b6001600160a01b03821660009081526001602090815260408083203380855292528220549061050e9085858401610f88565b6001600160a01b038116600090815260208181526040918290208054835181840281018401909452808452606093928301828280156105b257602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610594575b505050505090508051600014156105c857600080fd5b6105e5816000815181106105d857fe5b60200260200101516113b5565b156105ef57600080fd5b6000816000815181106105fe57fe5b60209081029190910101519050600060015b83518110156106ba576106288482815181106105d857fe5b6106b257600084828151811061063a57fe5b60200260200101519050806001600160a01b031663853828b66040518163ffffffff1660e01b8152600401602060405180830381600087803b15801561067f57600080fd5b505af1158015610693573d6000803e3d6000fd5b505050506040513d60208110156106a957600080fd5b50519290920191505b600101610610565b50816001600160a01b0316631003e2d2826040518263ffffffff1660e01b815260040180828152602001915050600060405180830381600087803b15801561070157600080fd5b505af1158015610715573d6000803e3d6000fd5b5050505050505050565b6001600160a01b038116600090815260208181526040808320805482518185028101850190935280835260609383018282801561078557602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610767575b505050505090508051600014156107a0576000915050610823565b6000816000815181106107af57fe5b60200260200101519050806001600160a01b031663b69ef8a86040518163ffffffff1660e01b815260040160206040518083038186803b1580156107f257600080fd5b505afa158015610806573d6000803e3d6000fd5b505050506040513d602081101561081c57600080fd5b5051925050505b919050565b6003546001600160a01b0316331461083f57600080fd5b60005b8181101561094f57600083838381811061085857fe5b905060200201356001600160a01b03169050846001600160a01b0316816001600160a01b0316638da5cb5b6040518163ffffffff1660e01b815260040160206040518083038186803b1580156108ad57600080fd5b505afa1580156108c1573d6000803e3d6000fd5b505050506040513d60208110156108d757600080fd5b50516001600160a01b0316146108ec57600080fd5b33600090815260208190526040902084848481811061090757fe5b835460018181018655600095865260209586902090910180546001600160a01b0319166001600160a01b03969093029490940135949094161790915550919091019050610842565b50505050565b6001600160a01b03821660009081526020818152604080832080548251818502810185019093528083526060938301828280156109bb57602002820191906000526020600020905b81546001600160a01b0316815260019091019060200180831161099d575b5050505050905060008184815181106109d057fe5b60200260200101519050806001600160a01b031663a5a1f2e56040518163ffffffff1660e01b815260040160206040518083038186803b158015610a1357600080fd5b505afa158015610a27573d6000803e3d6000fd5b505050506040513d6020811015610a3d57600080fd5b505195945050505050565b6003546001600160a01b031681565b6001600160a01b0382166000908152602081905260408120805483908110610a7b57fe5b6000918252602090912001546001600160a01b03169392505050565b6003546001600160a01b03163314610aae57600080fd5b60028054820190553360009081526020818152604091829020805483518184028101840190945280845260609392830182828015610b1557602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610af7575b50505050509050805160001415610b2b57600080fd5b600081600081518110610b3a57fe5b60200260200101519050806001600160a01b0316631003e2d2846040518263ffffffff1660e01b815260040180828152602001915050600060405180830381600087803b158015610b8a57600080fd5b505af1158015610b9e573d6000803e3d6000fd5b5050604080518681529051339350600092507fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9181900360200190a3505050565b3360009081526001602090815260408083206001600160a01b038616845290915281205482811015610c425760405162461bcd60e51b81526004018080602001828103825260258152602001806114c76025913960400191505060405180910390fd5b61050e3385858403610f88565b60006104f2338484611074565b6001600160a01b0381166000908152602081815260408083208054825181850281018501909352808352606093830182828015610cc257602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610ca4575b505092519695505050505050565b6001600160a01b0382166000908152602081815260408083208054825181850281018501909352808352606093830182828015610d3657602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610d18575b505050505090506000818481518110610d4b57fe5b60200260200101519050806001600160a01b031663b69ef8a86040518163ffffffff1660e01b815260040160206040518083038186803b158015610a1357600080fd5b6001600160a01b03918216600090815260016020908152604080832093909416825291909152205490565b6001600160a01b0382166000908152602081815260408083208054825181850281018501909352808352606093830182828015610e1f57602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610e01575b505050505090506000818481518110610e3457fe5b60200260200101519050806001600160a01b0316638da5cb5b6040518163ffffffff1660e01b815260040160206040518083038186803b158015610a1357600080fd5b60005b8151811015610f84576000828281518110610e9157fe5b60200260200101519050336001600160a01b0316816001600160a01b0316638da5cb5b6040518163ffffffff1660e01b815260040160206040518083038186803b158015610ede57600080fd5b505afa158015610ef2573d6000803e3d6000fd5b505050506040513d6020811015610f0857600080fd5b50516001600160a01b031614610f1d57600080fd5b3360009081526020819052604090208351849084908110610f3a57fe5b60209081029190910181015182546001808201855560009485529290932090920180546001600160a01b0319166001600160a01b0390931692909217909155919091019050610e7a565b5050565b6001600160a01b038316610fcd5760405162461bcd60e51b81526004018080602001828103825260248152602001806114a36024913960400191505060405180910390fd5b6001600160a01b0382166110125760405162461bcd60e51b815260040180806020018281038252602281526020018061143a6022913960400191505060405180910390fd5b6001600160a01b03808416600081815260016020908152604080832094871680845294825291829020859055815185815291517f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259281900390910190a3505050565b6001600160a01b0383166110b95760405162461bcd60e51b815260040180806020018281038252602581526020018061147e6025913960400191505060405180910390fd5b6001600160a01b0382166110fe5760405162461bcd60e51b81526004018080602001828103825260238152602001806114176023913960400191505060405180910390fd5b6001600160a01b0383166000908152602081815260409182902080548351818402810184019094528084526060939283018282801561116657602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311611148575b505050505090508051600014156111ae5760405162461bcd60e51b815260040180806020018281038252602281526020018061145c6022913960400191505060405180910390fd5b6000816000815181106111bd57fe5b602002602001015190506060600080866001600160a01b03166001600160a01b0316815260200190815260200160002080548060200260200160405190810160405280929190818152602001828054801561124157602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311611223575b5050505050905080516000141561125757600080fd5b6000805b825181101561129a576112738382815181106105d857fe5b6112925782818151811061128357fe5b6020026020010151915061129a565b60010161125b565b506001600160a01b0381166112ae57600080fd5b826001600160a01b03166327ee58a6866040518263ffffffff1660e01b815260040180828152602001915050600060405180830381600087803b1580156112f457600080fd5b505af1158015611308573d6000803e3d6000fd5b50505050806001600160a01b0316631003e2d2866040518263ffffffff1660e01b815260040180828152602001915050600060405180830381600087803b15801561135257600080fd5b505af1158015611366573d6000803e3d6000fd5b50506040805188815290516001600160a01b03808b1694508b1692507fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9181900360200190a350505050505050565b600060206113c16113f8565b6001600160a01b03841681526113d56113f8565b602060798183868684600019fa6113eb57600080fd5b5050511515949350505050565b6040518060200160405280600190602082028038833950919291505056fe45524332303a207472616e7366657220746f20746865207a65726f206164647265737345524332303a20617070726f766520746f20746865207a65726f206164647265737345524332303a206e6f2073656e646572206c6f636b61626c6520636f6e747261637445524332303a207472616e736665722066726f6d20746865207a65726f206164647265737345524332303a20617070726f76652066726f6d20746865207a65726f206164647265737345524332303a2064656372656173656420616c6c6f77616e63652062656c6f77207a65726fa265627a7a72305820f22edefa5f8b04829c4d49602796ecb003f554a6094a958ddd514c4ab5a60e7164736f6c634300050a0032";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_INCREASEALLOWANCE = "increaseAllowance";

    public static final String FUNC_CONDENSE = "condense";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_CREATEACCOUNTFOR = "createAccountFor";

    public static final String FUNC_ACCGETROUTER = "accGetRouter";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_GETLOCKABLEACCOUNTADDRESS = "getLockableAccountAddress";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_DECREASEALLOWANCE = "decreaseAllowance";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_ACCSIZE = "accSize";

    public static final String FUNC_ACCGETBALANCE = "accGetBalance";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_ACCGETOWNER = "accGetOwner";

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

    public RemoteFunctionCall<TransactionReceipt> createAccountFor(String account, List<String> _lockableAccountContracts) {
        final Function function = new Function(
                FUNC_CREATEACCOUNTFOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(_lockableAccountContracts, org.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> accGetRouter(String _acc, BigInteger _index) {
        final Function function = new Function(FUNC_ACCGETROUTER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _acc), 
                new org.web3j.abi.datatypes.generated.Uint256(_index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public RemoteFunctionCall<BigInteger> accSize(String _acc) {
        final Function function = new Function(FUNC_ACCSIZE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _acc)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> accGetBalance(String _acc, BigInteger _index) {
        final Function function = new Function(FUNC_ACCGETBALANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _acc), 
                new org.web3j.abi.datatypes.generated.Uint256(_index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> allowance(String _owner, String _spender) {
        final Function function = new Function(FUNC_ALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _owner), 
                new org.web3j.abi.datatypes.Address(160, _spender)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> accGetOwner(String _acc, BigInteger _index) {
        final Function function = new Function(FUNC_ACCGETOWNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _acc), 
                new org.web3j.abi.datatypes.generated.Uint256(_index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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
