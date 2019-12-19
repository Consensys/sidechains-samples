package tech.pegasys.samples.sidechains.common.coordination.soliditywrappers;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint16;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.abi.datatypes.generated.Uint64;
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
public class CrosschainCoordinationV1 extends Contract {
    private static final String BINARY = "60806040523480156200001157600080fd5b5060405162001cf038038062001cf0833981810160405260408110156200003757600080fd5b5080516020918201516040805193840190526000808452919290916200006f908463ffffffff8516846001600160e01b036200007816565b50505062000276565b6000848152602081905260409020547401000000000000000000000000000000000000000090046001600160401b031615620000b357600080fd5b6000826001600160401b031611620000ca57600080fd5b6040805185815290517f1edd0fcf19330896f6a214cd6a5129243c6b865b9b9725ffcc74dcbd9d02850d9181900360200190a16000848152602081815260408083208054600160a01b600160e01b031916740100000000000000000000000000000000000000006001600160401b0388811691909102919091176001600160a01b03199081166001600160a01b038a161783556002830180546001818101835591885286882001805433931683179055908652600383018552928520805460ff19168417905588855293835281810180546001600160401b03198116908616909301909416919091179092558251620001ca9260070191840190620001d1565b5050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200021457805160ff191683800117855562000244565b8280016001018555821562000244579182015b828111156200024457825182559160200191906001019062000227565b506200025292915062000256565b5090565b6200027391905b808211156200025257600081556001016200025d565b90565b611a6a80620002866000396000f3fe608060405234801561001057600080fd5b50600436106101375760003560e01c80634dbdfb1b116100b85780637d75219c1161007c5780637d75219c1461058457806398bd99911461058c578063b1d50a48146105a9578063cd941f6e14610623578063dcca364c14610662578063e4a4c5661461068557610137565b80634dbdfb1b1461045157806366ac16f514610486578063708f5a6b146104b257806375d2c1ce146104d55780637ac528621461056757610137565b806332ac34f5116100ff57806332ac34f5146102bd57806332f40261146103505780633352b8f8146103ca5780633e9b1adc1461040657806342cbb15c1461043757610137565b80630d8e6e2c1461013c57806310fb72a61461015b578063117368bc1461019457806317ff78be146102205780631e18d51b1461029a575b600080fd5b6101446106ae565b6040805161ffff9092168252519081900360200190f35b6101786004803603602081101561017157600080fd5b50356106b4565b604080516001600160401b039092168252519081900360200190f35b61021e600480360360a08110156101aa57600080fd5b81359161ffff6020820135169160408201359160608101359181019060a081016080820135600160201b8111156101e057600080fd5b8201836020820111156101f257600080fd5b803590602001918460018302840111600160201b8311171561021357600080fd5b5090925090506106d6565b005b61021e6004803603606081101561023657600080fd5b813591602081013591810190606081016040820135600160201b81111561025c57600080fd5b82018360208201111561026e57600080fd5b803590602001918460018302840111600160201b8311171561028f57600080fd5b509092509050610984565b61021e600480360360408110156102b057600080fd5b5080359060200135610a1f565b61021e600480360360808110156102d357600080fd5b8135916001600160a01b03602082013516916001600160401b036040830135169190810190608081016060820135600160201b81111561031257600080fd5b82018360208201111561032457600080fd5b803590602001918460018302840111600160201b8311171561034557600080fd5b509092509050610f61565b61021e6004803603608081101561036657600080fd5b813591602081013591810190606081016040820135600160201b81111561038c57600080fd5b82018360208201111561039e57600080fd5b803590602001918460018302840111600160201b831117156103bf57600080fd5b919350915035610fef565b6103ed600480360360408110156103e057600080fd5b508035906020013561106e565b6040805163ffffffff9092168252519081900360200190f35b6104236004803603602081101561041c57600080fd5b503561113b565b604080519115158252519081900360200190f35b61043f61115f565b60408051918252519081900360200190f35b61021e6004803603608081101561046757600080fd5b5080359061ffff60208201351690604081013590606001351515611163565b6104236004803603604081101561049c57600080fd5b50803590602001356001600160a01b0316611250565b61043f600480360360408110156104c857600080fd5b508035906020013561127d565b6104f2600480360360208110156104eb57600080fd5b50356112bb565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561052c578181015183820152602001610514565b50505050905090810190601f1680156105595780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61043f6004803603602081101561057d57600080fd5b5035611361565b61043f611376565b61043f600480360360208110156105a257600080fd5b503561137b565b61021e600480360360608110156105bf57600080fd5b813591602081013591810190606081016040820135600160201b8111156105e557600080fd5b8201836020820111156105f757600080fd5b803590602001918460018302840111600160201b8311171561061857600080fd5b509092509050611390565b6106466004803603604081101561063957600080fd5b5080359060200135611423565b604080516001600160a01b039092168252519081900360200190f35b61043f6004803603604081101561067857600080fd5b508035906020013561145c565b61021e6004803603606081101561069b57600080fd5b508035906020810135906040013561148c565b60015b90565b600090815260208190526040902054600160a01b90046001600160401b031690565b600086815260208181526040808320338452600301909152902054869060ff166106ff57600080fd5b60008661ffff16600581111561071157fe5b9050600080898152602081815260408083208a845260060190915290205460ff16600581111561073d57fe5b1461074757600080fd5b600181600581111561075557fe5b14156107835760008881526020818152604080832089845260050190915290205460ff161561078357600080fd5b600281600581111561079157fe5b14156107f95760008881526020818152604080832089845260050190915290205460ff1615156001146107c357600080fd5b856000808a815260200190815260200160002060040186815481106107e457fe5b9060005260206000200154146107f957600080fd5b600381600581111561080757fe5b141561083e576000888152602081815260408083206001600160a01b038a16845260030190915290205460ff161561083e57600080fd5b600481600581111561084c57fe5b14156108ea576000888152602081815260408083206001600160a01b038a168452600301909152902054869060ff16151560011461088957600080fd5b6001600160a01b03811633141561089f57600080fd5b806001600160a01b03166000808b815260200190815260200160002060020187815481106108c957fe5b6000918252602090912001546001600160a01b0316146108e857600080fd5b505b6000888152602081815260408083208984526006019091529020805482919060ff1916600183600581111561091b57fe5b021790555060008881526020818152604080832080548a85526006909101909252909120600160a01b9091046001600160401b0316430160018201556002810186905561096c906003018585611873565b5061097a888888600161161c565b5050505050505050565b60408051602080820187905281830186905282518083038401815260609092019092528051910120600160008281526001602052604090205460ff1660038111156109cb57fe5b146109d557600080fd5b600081815260016020819052604090912001544311156109f457600080fd5b6000818152600160208190526040909120805460039260ff1990911690835b02179055505050505050565b600082815260208181526040808320338452600301909152902054829060ff16610a4857600080fd5b60008381526020818152604080832085845260060190915281205460ff1690816005811115610a7357fe5b1415610a7e57600080fd5b6000848152602081815260408083208684526006019091529020600101544311610aa757600080fd5b600084815260208181526040808320805460018201548886526006909201845282852060050154835163a81ce84760e01b81526001600160401b0393841660048201528382166024820152600160401b909104909216604483015291516001600160a01b039092169392849263a81ce847926064808201939291829003018186803b158015610b3557600080fd5b505afa158015610b49573d6000803e3d6000fd5b505050506040513d6020811015610b5f57600080fd5b505190507faeb5b7640625260f6f8914a13e4cd86b256b00761645b6466e94ccb8f02d0fd486846005811115610b9157fe5b6040805192835261ffff90911660208301528181018890528315156060830152519081900360800190a18015610e3f57600086815260208181526040808320888452600601909152902060020154856003856005811115610bee57fe5b1415610c7a57600088815260208181526040808320600281018054600180820183559186528486200180546001600160a01b0319166001600160a01b0388169081179091558552600382018452918420805460ff1916831790558b845292909152908101805467ffffffffffffffff1981166001600160401b0391821690930116919091179055610e3c565b6001856005811115610c8857fe5b1415610cd25760008881526020818152604080832060048101805460018181018355918652848620018c90558b85526005909101909252909120805460ff19169091179055610e3c565b6004856005811115610ce057fe5b1415610d78576000888152602081905260409020600201805483908110610d0357fe5b600091825260208083209190910180546001600160a01b031916905589825281815260408083206001600160a01b0385168452600381018352908320805460ff191690558a8352919052600101805467ffffffffffffffff1981166001600160401b0391821660001901909116179055610e3c565b6002856005811115610d8657fe5b1415610ddc576000888152602081905260409020600401805483908110610da957fe5b6000918252602080832090910182905589825281815260408083208a84526005019091529020805460ff19169055610e3c565b6005856005811115610dea57fe5b1415610e3c576000888152602081815260408083208a84526006810183529083208b84529290915260039091018054610e3a926007019190600260001961010060018416150201909116046118f1565b505b50505b60005b600087815260208190526040902060020154811015610eff576000878152602081905260408120600201805483908110610e7857fe5b6000918252602090912001546001600160a01b031614610ef7576000878152602081815260408083208984526006810183529083208a84529183905260020180546004909201929184908110610eca57fe5b60009182526020808320909101546001600160a01b031683528201929092526040019020805460ff191690555b600101610e42565b506000868152602081815260408083208884526006019091528120805460ff19168155600181018290556002810182905590610f3e6003830182611966565b5060050180546fffffffffffffffffffffffffffffffff19169055505050505050565b3360009081527fad3228b676f7d3cd4284a5443f17f1962b36e491b30a40b2405849e597ba5fb8602052604081205460ff16610f9c57600080fd5b606083838080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929350610fe69250899150889050878461173e565b50505050505050565b604080516020808201889052818301879052825180830384018152606090920190925280519101206000808281526001602052604090205460ff16600381111561103557fe5b1461103f57600080fd5b43821161104b57600080fd5b6000908152600160208190526040909120805460ff191682178155015550505050565b60408051602080820185905281830184905282518083038401815260609092019092528051910120600090600260008281526001602052604090205460ff1660038111156110b857fe5b14156110c95760025b915050611135565b60008181526001602052604081205460ff1660038111156110e657fe5b14156110f35760006110c1565b600081815260016020819052604090912001544311156111145760036110c1565b60008181526001602052604090205460ff16600381111561113157fe5b9150505b92915050565b600090815260208190526040902054600160a01b90046001600160401b0316151590565b4390565b600084815260208181526040808320338452600301909152902054849060ff1661118c57600080fd5b60008461ffff16600581111561119e57fe5b90508060058111156111ac57fe5b60008781526020818152604080832088845260060190915290205460ff1660058111156111d557fe5b146111df57600080fd5b600086815260208181526040808320878452600601825280832033845260040190915290205460ff161561121257600080fd5b60008681526020818152604080832087845260060190915290206001015443111561123c57600080fd5b6112488686868661161c565b505050505050565b6000828152602081815260408083206001600160a01b038516845260030190915290205460ff1692915050565b604080516020808201949094528082019290925280518083038201815260609092018152815191830191909120600090815260019283905220015490565b600081815260208181526040918290206007018054835160026001831615610100026000190190921691909104601f810184900484028201840190945283815260609384939192918301828280156113545780601f1061132957610100808354040283529160200191611354565b820191906000526020600020905b81548152906001019060200180831161133757829003601f168201915b5093979650505050505050565b60009081526020819052604090206004015490565b600081565b60009081526020819052604090206002015490565b60408051602080820187905281830186905282518083038401815260609092019092528051910120600160008281526001602052604090205460ff1660038111156113d757fe5b146113e157600080fd5b6000818152600160208190526040909120015443111561140057600080fd5b6000818152600160208190526040909120805460029260ff199091169083610a13565b600082815260208190526040812060020180548390811061144057fe5b6000918252602090912001546001600160a01b03169392505050565b600082815260208190526040812060040180548390811061147957fe5b9060005260206000200154905092915050565b60008381526020819052604081206004018054849081106114a957fe5b60009182526020918290200154604080513360601b81850152603480820187905282518083039091018152605490910190915280519201919091209091508082146114f357600080fd5b60008581526020818152604080832033845260030190915290205460ff166115c9576040805186815233602082015281517f960e98814ae9a46102dc8e9663fd6016b08497e5cf8c33f8255f83ca06f5a9ef929181900390910190a160008581526020818152604080832060028101805460018082018355918652848620018054336001600160a01b031990911681179091558552600382018452918420805460ff19168317905588845292909152908101805467ffffffffffffffff1981166001600160401b03918216909301169190911790555b60008581526020819052604090206004018054859081106115e657fe5b600091825260208083209091018290559581528086526040808220938252600590930190955250909220805460ff191690555050565b6040805185815233602082015261ffff85168183015260608101849052821515608082015290517fef19f8b95a20f38d0a8a745702c10e0ef86b204ddcef51d7fba0b0da2d8aebf29181900360a00190a160008481526020818152604080832085845260060182528083203384526004019091529020805460ff1916600117905580156116e657600084815260208181526040808320858452600601909152902060050180546001600160401b038082166001011667ffffffffffffffff19909116179055611738565b6000848152602081815260408083208584526006019091529020600501805460016001600160401b03600160401b80840482169290920116026fffffffffffffffff0000000000000000199091161790555b50505050565b600084815260208190526040902054600160a01b90046001600160401b03161561176757600080fd5b6000826001600160401b03161161177d57600080fd5b6040805185815290517f1edd0fcf19330896f6a214cd6a5129243c6b865b9b9725ffcc74dcbd9d02850d9181900360200190a1600084815260208181526040808320805467ffffffffffffffff60a01b1916600160a01b6001600160401b0388811691909102919091176001600160a01b03199081166001600160a01b038a161783556002830180546001818101835591885286882001805433931683179055908652600383018552928520805460ff191684179055888552938352818101805467ffffffffffffffff19811690861690930190941691909117909255825161186c92600701918401906119ad565b5050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106118b45782800160ff198235161785556118e1565b828001600101855582156118e1579182015b828111156118e15782358255916020019190600101906118c6565b506118ed929150611a1b565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061192a57805485556118e1565b828001600101855582156118e157600052602060002091601f016020900482015b828111156118e157825482559160010191906001019061194b565b50805460018160011615610100020316600290046000825580601f1061198c57506119aa565b601f0160209004906000526020600020908101906119aa9190611a1b565b50565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106119ee57805160ff19168380011785556118e1565b828001600101855582156118e1579182015b828111156118e1578251825591602001919060010190611a00565b6106b191905b808211156118ed5760008155600101611a2156fea265627a7a72305820068e6dedc1402a9429b7f902dbcb91b21638ffefb45d4e0f745c27294a12831c64736f6c634300050a0032";

    public static final String FUNC_GETVERSION = "getVersion";

    public static final String FUNC_GETVOTINGPERIOD = "getVotingPeriod";

    public static final String FUNC_PROPOSEVOTE = "proposeVote";

    public static final String FUNC_IGNORE = "ignore";

    public static final String FUNC_ACTIONVOTES = "actionVotes";

    public static final String FUNC_ADDSIDECHAIN = "addSidechain";

    public static final String FUNC_START = "start";

    public static final String FUNC_GETCROSSCHAINTRANSACTIONSTATUS = "getCrosschainTransactionStatus";

    public static final String FUNC_GETSIDECHAINEXISTS = "getSidechainExists";

    public static final String FUNC_GETBLOCKNUMBER = "getBlockNumber";

    public static final String FUNC_VOTE = "vote";

    public static final String FUNC_ISUNMASKEDSIDECHAINPARTICIPANT = "isUnmaskedSidechainParticipant";

    public static final String FUNC_GETCROSSCHAINTRANSACTIONTIMEOUT = "getCrosschainTransactionTimeout";

    public static final String FUNC_GETPUBLICKEY = "getPublicKey";

    public static final String FUNC_GETMASKEDSIDECHAINPARTICIPANTSSIZE = "getMaskedSidechainParticipantsSize";

    public static final String FUNC_MANAGEMENT_PSEUDO_SIDECHAIN_ID = "MANAGEMENT_PSEUDO_SIDECHAIN_ID";

    public static final String FUNC_GETUNMASKEDSIDECHAINPARTICIPANTSSIZE = "getUnmaskedSidechainParticipantsSize";

    public static final String FUNC_COMMIT = "commit";

    public static final String FUNC_GETUNMASKEDSIDECHAINPARTICIPANT = "getUnmaskedSidechainParticipant";

    public static final String FUNC_GETMASKEDSIDECHAINPARTICIPANT = "getMaskedSidechainParticipant";

    public static final String FUNC_UNMASK = "unmask";

    public static final Event ADDEDSIDECHAIN_EVENT = new Event("AddedSidechain", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event ADDINGSIDECHAINMASKEDPARTICIPANT_EVENT = new Event("AddingSidechainMaskedParticipant", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}));
    ;

    public static final Event ADDINGSIDECHAINUNMASKEDPARTICIPANT_EVENT = new Event("AddingSidechainUnmaskedParticipant", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event PARTICIPANTVOTED_EVENT = new Event("ParticipantVoted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint16>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
    ;

    public static final Event VOTERESULT_EVENT = new Event("VoteResult", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint16>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
    ;

    public static final Event DUMP1_EVENT = new Event("Dump1", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event DUMP2_EVENT = new Event("Dump2", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected CrosschainCoordinationV1(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CrosschainCoordinationV1(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected CrosschainCoordinationV1(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected CrosschainCoordinationV1(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<BigInteger> getVersion() {
        final Function function = new Function(FUNC_GETVERSION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint16>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getVotingPeriod(BigInteger _sidechainId) {
        final Function function = new Function(FUNC_GETVOTINGPERIOD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> proposeVote(BigInteger _sidechainId, BigInteger _action, BigInteger _voteTarget, BigInteger _additionalInfo1, byte[] _additionalInfo2) {
        final Function function = new Function(
                FUNC_PROPOSEVOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId), 
                new org.web3j.abi.datatypes.generated.Uint16(_action), 
                new org.web3j.abi.datatypes.generated.Uint256(_voteTarget), 
                new org.web3j.abi.datatypes.generated.Uint256(_additionalInfo1), 
                new org.web3j.abi.datatypes.DynamicBytes(_additionalInfo2)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> ignore(BigInteger _originatingSidechainId, BigInteger _crosschainTransactionId, byte[] param2) {
        final Function function = new Function(
                FUNC_IGNORE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingSidechainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId), 
                new org.web3j.abi.datatypes.DynamicBytes(param2)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> actionVotes(BigInteger _sidechainId, BigInteger _voteTarget) {
        final Function function = new Function(
                FUNC_ACTIONVOTES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_voteTarget)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addSidechain(BigInteger _sidechainId, String _votingAlgorithmContract, BigInteger _votingPeriod, byte[] _publicKey) {
        final Function function = new Function(
                FUNC_ADDSIDECHAIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId), 
                new org.web3j.abi.datatypes.Address(160, _votingAlgorithmContract), 
                new org.web3j.abi.datatypes.generated.Uint64(_votingPeriod), 
                new org.web3j.abi.datatypes.DynamicBytes(_publicKey)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> start(BigInteger _originatingSidechainId, BigInteger _crosschainTransactionId, byte[] param2, BigInteger _transactionTimeoutBlock) {
        final Function function = new Function(
                FUNC_START, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingSidechainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId), 
                new org.web3j.abi.datatypes.DynamicBytes(param2), 
                new org.web3j.abi.datatypes.generated.Uint256(_transactionTimeoutBlock)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getCrosschainTransactionStatus(BigInteger _originatingSidechainId, BigInteger _crosschainTransactionId) {
        final Function function = new Function(FUNC_GETCROSSCHAINTRANSACTIONSTATUS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingSidechainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> getSidechainExists(BigInteger _sidechainId) {
        final Function function = new Function(FUNC_GETSIDECHAINEXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> getBlockNumber() {
        final Function function = new Function(FUNC_GETBLOCKNUMBER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> vote(BigInteger _sidechainId, BigInteger _action, BigInteger _voteTarget, Boolean _voteFor) {
        final Function function = new Function(
                FUNC_VOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId), 
                new org.web3j.abi.datatypes.generated.Uint16(_action), 
                new org.web3j.abi.datatypes.generated.Uint256(_voteTarget), 
                new org.web3j.abi.datatypes.Bool(_voteFor)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> isUnmaskedSidechainParticipant(BigInteger _sidechainId, String _participant) {
        final Function function = new Function(FUNC_ISUNMASKEDSIDECHAINPARTICIPANT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId), 
                new org.web3j.abi.datatypes.Address(160, _participant)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> getCrosschainTransactionTimeout(BigInteger _originatingSidechainId, BigInteger _crosschainTransactionId) {
        final Function function = new Function(FUNC_GETCROSSCHAINTRANSACTIONTIMEOUT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingSidechainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<byte[]> getPublicKey(BigInteger _sidechainId) {
        final Function function = new Function(FUNC_GETPUBLICKEY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<BigInteger> getMaskedSidechainParticipantsSize(BigInteger _sidechainId) {
        final Function function = new Function(FUNC_GETMASKEDSIDECHAINPARTICIPANTSSIZE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> MANAGEMENT_PSEUDO_SIDECHAIN_ID() {
        final Function function = new Function(FUNC_MANAGEMENT_PSEUDO_SIDECHAIN_ID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getUnmaskedSidechainParticipantsSize(BigInteger _sidechainId) {
        final Function function = new Function(FUNC_GETUNMASKEDSIDECHAINPARTICIPANTSSIZE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> commit(BigInteger _originatingSidechainId, BigInteger _crosschainTransactionId, byte[] param2) {
        final Function function = new Function(
                FUNC_COMMIT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingSidechainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId), 
                new org.web3j.abi.datatypes.DynamicBytes(param2)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getUnmaskedSidechainParticipant(BigInteger _sidechainId, BigInteger _index) {
        final Function function = new Function(FUNC_GETUNMASKEDSIDECHAINPARTICIPANT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> getMaskedSidechainParticipant(BigInteger _sidechainId, BigInteger _index) {
        final Function function = new Function(FUNC_GETMASKEDSIDECHAINPARTICIPANT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> unmask(BigInteger _sidechainId, BigInteger _index, BigInteger _salt) {
        final Function function = new Function(
                FUNC_UNMASK, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_index), 
                new org.web3j.abi.datatypes.generated.Uint256(_salt)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<AddedSidechainEventResponse> getAddedSidechainEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDEDSIDECHAIN_EVENT, transactionReceipt);
        ArrayList<AddedSidechainEventResponse> responses = new ArrayList<AddedSidechainEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddedSidechainEventResponse typedResponse = new AddedSidechainEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._sidechainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AddedSidechainEventResponse> addedSidechainEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AddedSidechainEventResponse>() {
            @Override
            public AddedSidechainEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDEDSIDECHAIN_EVENT, log);
                AddedSidechainEventResponse typedResponse = new AddedSidechainEventResponse();
                typedResponse.log = log;
                typedResponse._sidechainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddedSidechainEventResponse> addedSidechainEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDEDSIDECHAIN_EVENT));
        return addedSidechainEventFlowable(filter);
    }

    public List<AddingSidechainMaskedParticipantEventResponse> getAddingSidechainMaskedParticipantEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDINGSIDECHAINMASKEDPARTICIPANT_EVENT, transactionReceipt);
        ArrayList<AddingSidechainMaskedParticipantEventResponse> responses = new ArrayList<AddingSidechainMaskedParticipantEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddingSidechainMaskedParticipantEventResponse typedResponse = new AddingSidechainMaskedParticipantEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._sidechainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._participant = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AddingSidechainMaskedParticipantEventResponse> addingSidechainMaskedParticipantEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AddingSidechainMaskedParticipantEventResponse>() {
            @Override
            public AddingSidechainMaskedParticipantEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDINGSIDECHAINMASKEDPARTICIPANT_EVENT, log);
                AddingSidechainMaskedParticipantEventResponse typedResponse = new AddingSidechainMaskedParticipantEventResponse();
                typedResponse.log = log;
                typedResponse._sidechainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._participant = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddingSidechainMaskedParticipantEventResponse> addingSidechainMaskedParticipantEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDINGSIDECHAINMASKEDPARTICIPANT_EVENT));
        return addingSidechainMaskedParticipantEventFlowable(filter);
    }

    public List<AddingSidechainUnmaskedParticipantEventResponse> getAddingSidechainUnmaskedParticipantEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDINGSIDECHAINUNMASKEDPARTICIPANT_EVENT, transactionReceipt);
        ArrayList<AddingSidechainUnmaskedParticipantEventResponse> responses = new ArrayList<AddingSidechainUnmaskedParticipantEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddingSidechainUnmaskedParticipantEventResponse typedResponse = new AddingSidechainUnmaskedParticipantEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._sidechainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._participant = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AddingSidechainUnmaskedParticipantEventResponse> addingSidechainUnmaskedParticipantEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AddingSidechainUnmaskedParticipantEventResponse>() {
            @Override
            public AddingSidechainUnmaskedParticipantEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDINGSIDECHAINUNMASKEDPARTICIPANT_EVENT, log);
                AddingSidechainUnmaskedParticipantEventResponse typedResponse = new AddingSidechainUnmaskedParticipantEventResponse();
                typedResponse.log = log;
                typedResponse._sidechainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._participant = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddingSidechainUnmaskedParticipantEventResponse> addingSidechainUnmaskedParticipantEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDINGSIDECHAINUNMASKEDPARTICIPANT_EVENT));
        return addingSidechainUnmaskedParticipantEventFlowable(filter);
    }

    public List<ParticipantVotedEventResponse> getParticipantVotedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(PARTICIPANTVOTED_EVENT, transactionReceipt);
        ArrayList<ParticipantVotedEventResponse> responses = new ArrayList<ParticipantVotedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ParticipantVotedEventResponse typedResponse = new ParticipantVotedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._sidechainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._participant = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._action = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._voteTarget = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._votedFor = (Boolean) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ParticipantVotedEventResponse> participantVotedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ParticipantVotedEventResponse>() {
            @Override
            public ParticipantVotedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PARTICIPANTVOTED_EVENT, log);
                ParticipantVotedEventResponse typedResponse = new ParticipantVotedEventResponse();
                typedResponse.log = log;
                typedResponse._sidechainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._participant = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._action = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._voteTarget = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._votedFor = (Boolean) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ParticipantVotedEventResponse> participantVotedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PARTICIPANTVOTED_EVENT));
        return participantVotedEventFlowable(filter);
    }

    public List<VoteResultEventResponse> getVoteResultEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(VOTERESULT_EVENT, transactionReceipt);
        ArrayList<VoteResultEventResponse> responses = new ArrayList<VoteResultEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VoteResultEventResponse typedResponse = new VoteResultEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._sidechainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._action = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._voteTarget = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._result = (Boolean) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VoteResultEventResponse> voteResultEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, VoteResultEventResponse>() {
            @Override
            public VoteResultEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTERESULT_EVENT, log);
                VoteResultEventResponse typedResponse = new VoteResultEventResponse();
                typedResponse.log = log;
                typedResponse._sidechainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._action = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._voteTarget = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._result = (Boolean) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VoteResultEventResponse> voteResultEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTERESULT_EVENT));
        return voteResultEventFlowable(filter);
    }

    public List<Dump1EventResponse> getDump1Events(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DUMP1_EVENT, transactionReceipt);
        ArrayList<Dump1EventResponse> responses = new ArrayList<Dump1EventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            Dump1EventResponse typedResponse = new Dump1EventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.a = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.b = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.c = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<Dump1EventResponse> dump1EventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, Dump1EventResponse>() {
            @Override
            public Dump1EventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DUMP1_EVENT, log);
                Dump1EventResponse typedResponse = new Dump1EventResponse();
                typedResponse.log = log;
                typedResponse.a = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.b = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.c = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<Dump1EventResponse> dump1EventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DUMP1_EVENT));
        return dump1EventFlowable(filter);
    }

    public List<Dump2EventResponse> getDump2Events(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DUMP2_EVENT, transactionReceipt);
        ArrayList<Dump2EventResponse> responses = new ArrayList<Dump2EventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            Dump2EventResponse typedResponse = new Dump2EventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.a = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.b = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.c = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.d = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<Dump2EventResponse> dump2EventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, Dump2EventResponse>() {
            @Override
            public Dump2EventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DUMP2_EVENT, log);
                Dump2EventResponse typedResponse = new Dump2EventResponse();
                typedResponse.log = log;
                typedResponse.a = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.b = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.c = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.d = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<Dump2EventResponse> dump2EventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DUMP2_EVENT));
        return dump2EventFlowable(filter);
    }

    @Deprecated
    public static CrosschainCoordinationV1 load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CrosschainCoordinationV1(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static CrosschainCoordinationV1 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CrosschainCoordinationV1(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static CrosschainCoordinationV1 load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new CrosschainCoordinationV1(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static CrosschainCoordinationV1 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new CrosschainCoordinationV1(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<CrosschainCoordinationV1> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _votingAlg, BigInteger _votingPeriod) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _votingAlg), 
                new org.web3j.abi.datatypes.generated.Uint32(_votingPeriod)));
        return deployRemoteCall(CrosschainCoordinationV1.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<CrosschainCoordinationV1> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _votingAlg, BigInteger _votingPeriod) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _votingAlg), 
                new org.web3j.abi.datatypes.generated.Uint32(_votingPeriod)));
        return deployRemoteCall(CrosschainCoordinationV1.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<CrosschainCoordinationV1> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _votingAlg, BigInteger _votingPeriod) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _votingAlg), 
                new org.web3j.abi.datatypes.generated.Uint32(_votingPeriod)));
        return deployRemoteCall(CrosschainCoordinationV1.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<CrosschainCoordinationV1> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _votingAlg, BigInteger _votingPeriod) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _votingAlg), 
                new org.web3j.abi.datatypes.generated.Uint32(_votingPeriod)));
        return deployRemoteCall(CrosschainCoordinationV1.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class AddedSidechainEventResponse extends BaseEventResponse {
        public BigInteger _sidechainId;
    }

    public static class AddingSidechainMaskedParticipantEventResponse extends BaseEventResponse {
        public BigInteger _sidechainId;

        public byte[] _participant;
    }

    public static class AddingSidechainUnmaskedParticipantEventResponse extends BaseEventResponse {
        public BigInteger _sidechainId;

        public String _participant;
    }

    public static class ParticipantVotedEventResponse extends BaseEventResponse {
        public BigInteger _sidechainId;

        public String _participant;

        public BigInteger _action;

        public BigInteger _voteTarget;

        public Boolean _votedFor;
    }

    public static class VoteResultEventResponse extends BaseEventResponse {
        public BigInteger _sidechainId;

        public BigInteger _action;

        public BigInteger _voteTarget;

        public Boolean _result;
    }

    public static class Dump1EventResponse extends BaseEventResponse {
        public BigInteger a;

        public BigInteger b;

        public String c;
    }

    public static class Dump2EventResponse extends BaseEventResponse {
        public BigInteger a;

        public BigInteger b;

        public BigInteger c;

        public BigInteger d;
    }
}
