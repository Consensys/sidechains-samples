package tech.pegasys.samples.sidechains.common.coordination.soliditywrappers;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
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
import org.web3j.tuples.generated.Tuple3;
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
    private static final String BINARY = "60806040523480156200001157600080fd5b5060405162001f5e38038062001f5e833981810160405260408110156200003757600080fd5b50805160209182015160408051938401905260008084529192909162000070908463ffffffff851682856001600160e01b036200007916565b50505062000327565b6000858152602081905260409020547401000000000000000000000000000000000000000090046001600160401b031615620000b457600080fd5b6000836001600160401b031611620000cb57600080fd5b6040805186815290517f06b7e169fa5b89e7c3d111aa6a1930c579aa884091b94cba624be47c9de04b1b9181900360200190a16000858152602081815260408083208054600160a01b600160e01b031916740100000000000000000000000000000000000000006001600160401b0389811691909102919091176001600160a01b03199081166001600160a01b038b161783556002830180546001818101835591885286882001805433931683179055908652600383018552928520805460ff1916841790558985529390925290810180546001600160401b03198116908416909201909216179055620001c1858383620001c8565b5050505050565b6000838152602081815260408083206001600160401b038616845260080190915290205415620001f757600080fd5b6000838152602081815260408083206001600160401b038616845260080182529091204381558251620002339260019092019184019062000282565b50506000918252602082905260409091206007018054600160401b600160801b031981166001600160401b039182166801000000000000000002176001600160401b0319169216919091179055565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620002c557805160ff1916838001178555620002f5565b82800160010185558215620002f5579182015b82811115620002f5578251825591602001919060010190620002d8565b506200030392915062000307565b5090565b6200032491905b808211156200030357600081556001016200030e565b90565b611c2780620003376000396000f3fe608060405234801561001057600080fd5b506004361061014d5760003560e01c806336e554f4116100c3578063997403601161007c578063997403601461057b578063a6db327a1461063f578063b1d50a481461066b578063d2a3d0a4146106e5578063dca864e614610702578063e4a4c5661461071f5761014d565b806336e554f41461043f57806342cbb15c146104db5780634dbdfb1b146104e357806359463dbc14610518578063636edc891461053b578063708f5a6b146105585761014d565b806317ff78be1161011557806317ff78be1461028f5780631e18d51b146103095780633112818e1461032c57806332f402611461036c5780633352b8f8146103e657806334940384146104225761014d565b8063081ca05c146101525780630d8e6e2c1461016c57806310fb72a61461018b578063117368bc146101c457806314ab729f14610250575b600080fd5b61015a610748565b60408051918252519081900360200190f35b61017461074d565b6040805161ffff9092168252519081900360200190f35b6101a8600480360360208110156101a157600080fd5b5035610753565b604080516001600160401b039092168252519081900360200190f35b61024e600480360360a08110156101da57600080fd5b81359161ffff6020820135169160408201359160608101359181019060a081016080820135600160201b81111561021057600080fd5b82018360208201111561022257600080fd5b803590602001918460018302840111600160201b8311171561024357600080fd5b509092509050610775565b005b6102736004803603604081101561026657600080fd5b5080359060200135610a33565b604080516001600160a01b039092168252519081900360200190f35b61024e600480360360608110156102a557600080fd5b813591602081013591810190606081016040820135600160201b8111156102cb57600080fd5b8201836020820111156102dd57600080fd5b803590602001918460018302840111600160201b831117156102fe57600080fd5b509092509050610a6e565b61024e6004803603604081101561031f57600080fd5b5080359060200135610b09565b6103586004803603604081101561034257600080fd5b50803590602001356001600160a01b03166110b1565b604080519115158252519081900360200190f35b61024e6004803603608081101561038257600080fd5b813591602081013591810190606081016040820135600160201b8111156103a857600080fd5b8201836020820111156103ba57600080fd5b803590602001918460018302840111600160201b831117156103db57600080fd5b9193509150356110de565b610409600480360360408110156103fc57600080fd5b508035906020013561115d565b6040805163ffffffff9092168252519081900360200190f35b6103586004803603602081101561043857600080fd5b5035611228565b61024e600480360360a081101561045557600080fd5b8135916001600160a01b03602082013516916001600160401b03604083013581169260608101359091169181019060a081016080820135600160201b81111561049d57600080fd5b8201836020820111156104af57600080fd5b803590602001918460018302840111600160201b831117156104d057600080fd5b50909250905061124c565b61015a6112d2565b61024e600480360360808110156104f957600080fd5b5080359061ffff602082013516906040810135906060013515156112d6565b61015a6004803603604081101561052e57600080fd5b50803590602001356113c3565b61015a6004803603602081101561055157600080fd5b50356113f3565b61015a6004803603604081101561056e57600080fd5b5080359060200135611408565b6105a76004803603604081101561059157600080fd5b50803590602001356001600160401b0316611446565b60405180846001600160401b03166001600160401b0316815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156106025781810151838201526020016105ea565b50505050905090810190601f16801561062f5780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b6103586004803603604081101561065557600080fd5b50803590602001356001600160401b031661150d565b61024e6004803603606081101561068157600080fd5b813591602081013591810190606081016040820135600160201b8111156106a757600080fd5b8201836020820111156106b957600080fd5b803590602001918460018302840111600160201b831117156106da57600080fd5b509092509050611539565b6105a7600480360360208110156106fb57600080fd5b50356115cc565b61015a6004803603602081101561071857600080fd5b5035611604565b61024e6004803603606081101561073557600080fd5b5080359060208101359060400135611619565b600081565b60015b90565b600090815260208190526040902054600160a01b90046001600160401b031690565b600086815260208181526040808320338452600301909152902054869060ff1661079e57600080fd5b60008661ffff1660058111156107b057fe5b9050600080898152602081815260408083208a845260060190915290205460ff1660058111156107dc57fe5b146107e657600080fd5b60018160058111156107f457fe5b14156108225760008881526020818152604080832089845260050190915290205460ff161561082257600080fd5b600281600581111561083057fe5b14156108985760008881526020818152604080832089845260050190915290205460ff16151560011461086257600080fd5b856000808a8152602001908152602001600020600401868154811061088357fe5b90600052602060002001541461089857600080fd5b60038160058111156108a657fe5b14156108dd576000888152602081815260408083206001600160a01b038a16845260030190915290205460ff16156108dd57600080fd5b60048160058111156108eb57fe5b1415610989576000888152602081815260408083206001600160a01b038a168452600301909152902054869060ff16151560011461092857600080fd5b6001600160a01b03811633141561093e57600080fd5b806001600160a01b03166000808b8152602001908152602001600020600201878154811061096857fe5b6000918252602090912001546001600160a01b03161461098757600080fd5b505b600581600581111561099757fe5b50506000888152602081815260408083208984526006019091529020805482919060ff191660018360058111156109ca57fe5b021790555060008881526020818152604080832080548a85526006909101909252909120600160a01b9091046001600160401b03164301600182015560028101869055610a1b906003018585611aa5565b50610a2988888860016117a9565b5050505050505050565b6000828152602081905260408120600201805483908110610a5057fe5b6000918252602090912001546001600160a01b031690505b92915050565b60408051602080820187905281830186905282518083038401815260609092019092528051910120600160008281526001602052604090205460ff166003811115610ab557fe5b14610abf57600080fd5b60008181526001602081905260409091200154431115610ade57600080fd5b6000818152600160208190526040909120805460039260ff1990911690835b02179055505050505050565b600082815260208181526040808320338452600301909152902054829060ff16610b3257600080fd5b60008381526020818152604080832085845260060190915281205460ff1690816005811115610b5d57fe5b1415610b6857600080fd5b6000848152602081815260408083208684526006019091529020600101544311610b9157600080fd5b600084815260208181526040808320805460018201548886526006909201845282852060050154835163a81ce84760e01b81526001600160401b0393841660048201528382166024820152600160401b909104909216604483015291516001600160a01b039092169392849263a81ce847926064808201939291829003018186803b158015610c1f57600080fd5b505afa158015610c33573d6000803e3d6000fd5b505050506040513d6020811015610c4957600080fd5b505190507faeb5b7640625260f6f8914a13e4cd86b256b00761645b6466e94ccb8f02d0fd486846005811115610c7b57fe5b6040805192835261ffff90911660208301528181018890528315156060830152519081900360800190a18015610f8f57600086815260208181526040808320888452600601909152902060020154856003856005811115610cd857fe5b1415610d6457600088815260208181526040808320600281018054600180820183559186528486200180546001600160a01b0319166001600160a01b0388169081179091558552600382018452918420805460ff1916831790558b845292909152908101805467ffffffffffffffff1981166001600160401b0391821690930116919091179055610f8c565b6001856005811115610d7257fe5b1415610dbc5760008881526020818152604080832060048101805460018181018355918652848620018c90558b85526005909101909252909120805460ff19169091179055610f8c565b6004856005811115610dca57fe5b1415610e62576000888152602081905260409020600201805483908110610ded57fe5b600091825260208083209190910180546001600160a01b031916905589825281815260408083206001600160a01b0385168452600381018352908320805460ff191690558a8352919052600101805467ffffffffffffffff1981166001600160401b0391821660001901909116179055610f8c565b6002856005811115610e7057fe5b1415610ec6576000888152602081905260409020600401805483908110610e9357fe5b6000918252602080832090910182905589825281815260408083208a84526005019091529020805460ff19169055610f8c565b6005856005811115610ed457fe5b1415610f8c576000888152602081815260408083208a84526006018252918290206002808201546003909201805485516001821615610100026000190190911692909204601f8101859004850283018501909552848252610f8c948d94830182828015610f825780601f10610f5757610100808354040283529160200191610f82565b820191906000526020600020905b815481529060010190602001808311610f6557829003601f168201915b50505050506118c6565b50505b60005b60008781526020819052604090206002015481101561104f576000878152602081905260408120600201805483908110610fc857fe5b6000918252602090912001546001600160a01b031614611047576000878152602081815260408083208984526006810183529083208a8452918390526002018054600490920192918490811061101a57fe5b60009182526020808320909101546001600160a01b031683528201929092526040019020805460ff191690555b600101610f92565b506000868152602081815260408083208884526006019091528120805460ff1916815560018101829055600281018290559061108e6003830182611b23565b5060050180546fffffffffffffffffffffffffffffffff19169055505050505050565b6000828152602081815260408083206001600160a01b038516845260030190915290205460ff1692915050565b604080516020808201889052818301879052825180830384018152606090920190925280519101206000808281526001602052604090205460ff16600381111561112457fe5b1461112e57600080fd5b43821161113a57600080fd5b6000908152600160208190526040909120805460ff191682178155015550505050565b60408051602080820185905281830184905282518083038401815260609092019092528051910120600090600260008281526001602052604090205460ff1660038111156111a757fe5b14156111b85760025b915050610a68565b60008181526001602052604081205460ff1660038111156111d557fe5b14156111e25760006111b0565b600081815260016020819052604090912001544311156112035760036111b0565b60008181526001602052604090205460ff16600381111561122057fe5b949350505050565b600090815260208190526040902054600160a01b90046001600160401b0316151590565b3360009081527fad3228b676f7d3cd4284a5443f17f1962b36e491b30a40b2405849e597ba5fb8602052604081205460ff1661128757600080fd5b606083838080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929350610a2992508a915089905088888561197a565b4390565b600084815260208181526040808320338452600301909152902054849060ff166112ff57600080fd5b60008461ffff16600581111561131157fe5b905080600581111561131f57fe5b60008781526020818152604080832088845260060190915290205460ff16600581111561134857fe5b1461135257600080fd5b600086815260208181526040808320878452600601825280832033845260040190915290205460ff161561138557600080fd5b6000868152602081815260408083208784526006019091529020600101544311156113af57600080fd5b6113bb868686866117a9565b505050505050565b60008281526020819052604081206004018054839081106113e057fe5b9060005260206000200154905092915050565b60009081526020819052604090206004015490565b604080516020808201949094528082019290925280518083038201815260609092018152815191830191909120600090815260019283905220015490565b6000828152602081815260408083206001600160401b038516845260080182528083208054600191820180548451600294821615610100026000190190911693909304601f81018690048602840186019094528383528594606094929385939092918301828280156114f95780601f106114ce576101008083540402835291602001916114f9565b820191906000526020600020905b8154815290600101906020018083116114dc57829003601f168201915b50999b959a50929850939650505050505050565b6000828152602081815260408083206001600160401b0385168452600801909152902054151592915050565b60408051602080820187905281830186905282518083038401815260609092019092528051910120600160008281526001602052604090205460ff16600381111561158057fe5b1461158a57600080fd5b600081815260016020819052604090912001544311156115a957600080fd5b6000818152600160208190526040909120805460029260ff199091169083610afd565b60008181526020819052604081206007015481906060906115f79085906001600160401b0316611446565b9250925092509193909250565b60009081526020819052604090206002015490565b600083815260208190526040812060040180548490811061163657fe5b60009182526020918290200154604080513360601b818501526034808201879052825180830390910181526054909101909152805192019190912090915080821461168057600080fd5b60008581526020818152604080832033845260030190915290205460ff16611756576040805186815233602082015281517f340f3af47c95ed6ef6db88a578a410e987399279edcda9b5dc2c393a5d3b9d55929181900390910190a160008581526020818152604080832060028101805460018082018355918652848620018054336001600160a01b031990911681179091558552600382018452918420805460ff19168317905588845292909152908101805467ffffffffffffffff1981166001600160401b03918216909301169190911790555b600085815260208190526040902060040180548590811061177357fe5b600091825260208083209091018290559581528086526040808220938252600590930190955250909220805460ff191690555050565b6040805185815233602082015261ffff85168183015260608101849052821515608082015290517fef19f8b95a20f38d0a8a745702c10e0ef86b204ddcef51d7fba0b0da2d8aebf29181900360a00190a160008481526020818152604080832085845260060182528083203384526004019091529020805460ff19166001179055801561187357600084815260208181526040808320858452600601909152902060050180546001600160401b038082166001011667ffffffffffffffff199091161790556118c0565b6000848152602081815260408083208584526006019091529020600501805460016001600160401b03600160401b808404821692909201160267ffffffffffffffff60401b199091161790555b50505050565b6000838152602081815260408083206001600160401b0386168452600801909152902054156118f457600080fd5b6000838152602081815260408083206001600160401b03861684526008018252909120438155825161192e92600190920191840190611b6a565b5050600091825260208290526040909120600701805467ffffffffffffffff60401b1981166001600160401b03918216600160401b021767ffffffffffffffff19169216919091179055565b600085815260208190526040902054600160a01b90046001600160401b0316156119a357600080fd5b6000836001600160401b0316116119b957600080fd5b6040805186815290517f06b7e169fa5b89e7c3d111aa6a1930c579aa884091b94cba624be47c9de04b1b9181900360200190a1600085815260208181526040808320805467ffffffffffffffff60a01b1916600160a01b6001600160401b0389811691909102919091176001600160a01b03199081166001600160a01b038b161783556002830180546001818101835591885286882001805433931683179055908652600383018552928520805460ff19168417905589855293909252908101805467ffffffffffffffff198116908416909201909216179055611a9e8583836118c6565b5050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611ae65782800160ff19823516178555611b13565b82800160010185558215611b13579182015b82811115611b13578235825591602001919060010190611af8565b50611b1f929150611bd8565b5090565b50805460018160011615610100020316600290046000825580601f10611b495750611b67565b601f016020900490600052602060002090810190611b679190611bd8565b50565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611bab57805160ff1916838001178555611b13565b82800160010185558215611b13579182015b82811115611b13578251825591602001919060010190611bbd565b61075091905b80821115611b1f5760008155600101611bde56fea265627a7a72305820d3709291934827933debccc99ddd31aff4a4195e02531b9753cc7670516bb3b364736f6c634300050a0032";

    public static final String FUNC_MANAGEMENT_PSEUDO_BLOCKCHAIN_ID = "MANAGEMENT_PSEUDO_BLOCKCHAIN_ID";

    public static final String FUNC_GETVERSION = "getVersion";

    public static final String FUNC_GETVOTINGPERIOD = "getVotingPeriod";

    public static final String FUNC_PROPOSEVOTE = "proposeVote";

    public static final String FUNC_GETUNMASKEDBLOCKCHAINPARTICIPANT = "getUnmaskedBlockchainParticipant";

    public static final String FUNC_IGNORE = "ignore";

    public static final String FUNC_ACTIONVOTES = "actionVotes";

    public static final String FUNC_ISUNMASKEDBLOCKCHAINPARTICIPANT = "isUnmaskedBlockchainParticipant";

    public static final String FUNC_START = "start";

    public static final String FUNC_GETCROSSCHAINTRANSACTIONSTATUS = "getCrosschainTransactionStatus";

    public static final String FUNC_GETBLOCKCHAINEXISTS = "getBlockchainExists";

    public static final String FUNC_ADDBLOCKCHAIN = "addBlockchain";

    public static final String FUNC_GETBLOCKNUMBER = "getBlockNumber";

    public static final String FUNC_VOTE = "vote";

    public static final String FUNC_GETMASKEDBLOCKCHAINPARTICIPANT = "getMaskedBlockchainParticipant";

    public static final String FUNC_GETMASKEDBLOCKCHAINPARTICIPANTSSIZE = "getMaskedBlockchainParticipantsSize";

    public static final String FUNC_GETCROSSCHAINTRANSACTIONTIMEOUT = "getCrosschainTransactionTimeout";

    public static final String FUNC_GETPUBLICKEY = "getPublicKey";

    public static final String FUNC_PUBLICKEYEXISTS = "publicKeyExists";

    public static final String FUNC_COMMIT = "commit";

    public static final String FUNC_GETACTIVEPUBLICKEY = "getActivePublicKey";

    public static final String FUNC_GETUNMASKEDBLOCKCHAINPARTICIPANTSSIZE = "getUnmaskedBlockchainParticipantsSize";

    public static final String FUNC_UNMASK = "unmask";

    public static final Event ADDEDBLOCKCHAIN_EVENT = new Event("AddedBlockchain", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event ADDINGBLOCKCHAINMASKEDPARTICIPANT_EVENT = new Event("AddingBlockchainMaskedParticipant", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}));
    ;

    public static final Event ADDINGBLOCKCHAINUNMASKEDPARTICIPANT_EVENT = new Event("AddingBlockchainUnmaskedParticipant", 
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

    public RemoteFunctionCall<BigInteger> MANAGEMENT_PSEUDO_BLOCKCHAIN_ID() {
        final Function function = new Function(FUNC_MANAGEMENT_PSEUDO_BLOCKCHAIN_ID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getVersion() {
        final Function function = new Function(FUNC_GETVERSION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint16>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getVotingPeriod(BigInteger _blockchainId) {
        final Function function = new Function(FUNC_GETVOTINGPERIOD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> proposeVote(BigInteger _blockchainId, BigInteger _action, BigInteger _voteTarget, BigInteger _additionalInfo1, byte[] _additionalInfo2) {
        final Function function = new Function(
                FUNC_PROPOSEVOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId), 
                new org.web3j.abi.datatypes.generated.Uint16(_action), 
                new org.web3j.abi.datatypes.generated.Uint256(_voteTarget), 
                new org.web3j.abi.datatypes.generated.Uint256(_additionalInfo1), 
                new org.web3j.abi.datatypes.DynamicBytes(_additionalInfo2)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getUnmaskedBlockchainParticipant(BigInteger _blockchainId, BigInteger _index) {
        final Function function = new Function(FUNC_GETUNMASKEDBLOCKCHAINPARTICIPANT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> ignore(BigInteger _originatingBlockchainId, BigInteger _crosschainTransactionId, byte[] param2) {
        final Function function = new Function(
                FUNC_IGNORE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingBlockchainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId), 
                new org.web3j.abi.datatypes.DynamicBytes(param2)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> actionVotes(BigInteger _blockchainId, BigInteger _voteTarget) {
        final Function function = new Function(
                FUNC_ACTIONVOTES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_voteTarget)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> isUnmaskedBlockchainParticipant(BigInteger _blockchainId, String _participant) {
        final Function function = new Function(FUNC_ISUNMASKEDBLOCKCHAINPARTICIPANT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId), 
                new org.web3j.abi.datatypes.Address(160, _participant)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> start(BigInteger _originatingBlockchainId, BigInteger _crosschainTransactionId, byte[] param2, BigInteger _transactionTimeoutBlock) {
        final Function function = new Function(
                FUNC_START, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingBlockchainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId), 
                new org.web3j.abi.datatypes.DynamicBytes(param2), 
                new org.web3j.abi.datatypes.generated.Uint256(_transactionTimeoutBlock)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getCrosschainTransactionStatus(BigInteger _originatingBlockchainId, BigInteger _crosschainTransactionId) {
        final Function function = new Function(FUNC_GETCROSSCHAINTRANSACTIONSTATUS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingBlockchainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> getBlockchainExists(BigInteger _blockchainId) {
        final Function function = new Function(FUNC_GETBLOCKCHAINEXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> addBlockchain(BigInteger _blockchainId, String _votingAlgorithmContract, BigInteger _votingPeriod, BigInteger _keyVersion, byte[] _publicKey) {
        final Function function = new Function(
                FUNC_ADDBLOCKCHAIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId), 
                new org.web3j.abi.datatypes.Address(160, _votingAlgorithmContract), 
                new org.web3j.abi.datatypes.generated.Uint64(_votingPeriod), 
                new org.web3j.abi.datatypes.generated.Uint64(_keyVersion), 
                new org.web3j.abi.datatypes.DynamicBytes(_publicKey)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getBlockNumber() {
        final Function function = new Function(FUNC_GETBLOCKNUMBER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> vote(BigInteger _blockchainId, BigInteger _action, BigInteger _voteTarget, Boolean _voteFor) {
        final Function function = new Function(
                FUNC_VOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId), 
                new org.web3j.abi.datatypes.generated.Uint16(_action), 
                new org.web3j.abi.datatypes.generated.Uint256(_voteTarget), 
                new org.web3j.abi.datatypes.Bool(_voteFor)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getMaskedBlockchainParticipant(BigInteger _blockchainId, BigInteger _index) {
        final Function function = new Function(FUNC_GETMASKEDBLOCKCHAINPARTICIPANT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getMaskedBlockchainParticipantsSize(BigInteger _blockchainId) {
        final Function function = new Function(FUNC_GETMASKEDBLOCKCHAINPARTICIPANTSSIZE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getCrosschainTransactionTimeout(BigInteger _originatingBlockchainId, BigInteger _crosschainTransactionId) {
        final Function function = new Function(FUNC_GETCROSSCHAINTRANSACTIONTIMEOUT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingBlockchainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple3<BigInteger, BigInteger, byte[]>> getPublicKey(BigInteger _blockchainId, BigInteger _keyVersion) {
        final Function function = new Function(FUNC_GETPUBLICKEY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId), 
                new org.web3j.abi.datatypes.generated.Uint64(_keyVersion)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
        return new RemoteFunctionCall<Tuple3<BigInteger, BigInteger, byte[]>>(function,
                new Callable<Tuple3<BigInteger, BigInteger, byte[]>>() {
                    @Override
                    public Tuple3<BigInteger, BigInteger, byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, BigInteger, byte[]>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (byte[]) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Boolean> publicKeyExists(BigInteger _blockchainId, BigInteger _keyVersion) {
        final Function function = new Function(FUNC_PUBLICKEYEXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId), 
                new org.web3j.abi.datatypes.generated.Uint64(_keyVersion)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> commit(BigInteger _originatingBlockchainId, BigInteger _crosschainTransactionId, byte[] param2) {
        final Function function = new Function(
                FUNC_COMMIT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingBlockchainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId), 
                new org.web3j.abi.datatypes.DynamicBytes(param2)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple3<BigInteger, BigInteger, byte[]>> getActivePublicKey(BigInteger _blockchainId) {
        final Function function = new Function(FUNC_GETACTIVEPUBLICKEY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
        return new RemoteFunctionCall<Tuple3<BigInteger, BigInteger, byte[]>>(function,
                new Callable<Tuple3<BigInteger, BigInteger, byte[]>>() {
                    @Override
                    public Tuple3<BigInteger, BigInteger, byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, BigInteger, byte[]>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (byte[]) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getUnmaskedBlockchainParticipantsSize(BigInteger _blockchainId) {
        final Function function = new Function(FUNC_GETUNMASKEDBLOCKCHAINPARTICIPANTSSIZE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> unmask(BigInteger _blockchainId, BigInteger _index, BigInteger _salt) {
        final Function function = new Function(
                FUNC_UNMASK, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_index), 
                new org.web3j.abi.datatypes.generated.Uint256(_salt)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<AddedBlockchainEventResponse> getAddedBlockchainEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDEDBLOCKCHAIN_EVENT, transactionReceipt);
        ArrayList<AddedBlockchainEventResponse> responses = new ArrayList<AddedBlockchainEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddedBlockchainEventResponse typedResponse = new AddedBlockchainEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._blockchainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AddedBlockchainEventResponse> addedBlockchainEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AddedBlockchainEventResponse>() {
            @Override
            public AddedBlockchainEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDEDBLOCKCHAIN_EVENT, log);
                AddedBlockchainEventResponse typedResponse = new AddedBlockchainEventResponse();
                typedResponse.log = log;
                typedResponse._blockchainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddedBlockchainEventResponse> addedBlockchainEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDEDBLOCKCHAIN_EVENT));
        return addedBlockchainEventFlowable(filter);
    }

    public List<AddingBlockchainMaskedParticipantEventResponse> getAddingBlockchainMaskedParticipantEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDINGBLOCKCHAINMASKEDPARTICIPANT_EVENT, transactionReceipt);
        ArrayList<AddingBlockchainMaskedParticipantEventResponse> responses = new ArrayList<AddingBlockchainMaskedParticipantEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddingBlockchainMaskedParticipantEventResponse typedResponse = new AddingBlockchainMaskedParticipantEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._blockchainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._participant = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AddingBlockchainMaskedParticipantEventResponse> addingBlockchainMaskedParticipantEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AddingBlockchainMaskedParticipantEventResponse>() {
            @Override
            public AddingBlockchainMaskedParticipantEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDINGBLOCKCHAINMASKEDPARTICIPANT_EVENT, log);
                AddingBlockchainMaskedParticipantEventResponse typedResponse = new AddingBlockchainMaskedParticipantEventResponse();
                typedResponse.log = log;
                typedResponse._blockchainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._participant = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddingBlockchainMaskedParticipantEventResponse> addingBlockchainMaskedParticipantEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDINGBLOCKCHAINMASKEDPARTICIPANT_EVENT));
        return addingBlockchainMaskedParticipantEventFlowable(filter);
    }

    public List<AddingBlockchainUnmaskedParticipantEventResponse> getAddingBlockchainUnmaskedParticipantEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDINGBLOCKCHAINUNMASKEDPARTICIPANT_EVENT, transactionReceipt);
        ArrayList<AddingBlockchainUnmaskedParticipantEventResponse> responses = new ArrayList<AddingBlockchainUnmaskedParticipantEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddingBlockchainUnmaskedParticipantEventResponse typedResponse = new AddingBlockchainUnmaskedParticipantEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._blockchainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._participant = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AddingBlockchainUnmaskedParticipantEventResponse> addingBlockchainUnmaskedParticipantEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AddingBlockchainUnmaskedParticipantEventResponse>() {
            @Override
            public AddingBlockchainUnmaskedParticipantEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDINGBLOCKCHAINUNMASKEDPARTICIPANT_EVENT, log);
                AddingBlockchainUnmaskedParticipantEventResponse typedResponse = new AddingBlockchainUnmaskedParticipantEventResponse();
                typedResponse.log = log;
                typedResponse._blockchainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._participant = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddingBlockchainUnmaskedParticipantEventResponse> addingBlockchainUnmaskedParticipantEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDINGBLOCKCHAINUNMASKEDPARTICIPANT_EVENT));
        return addingBlockchainUnmaskedParticipantEventFlowable(filter);
    }

    public List<ParticipantVotedEventResponse> getParticipantVotedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(PARTICIPANTVOTED_EVENT, transactionReceipt);
        ArrayList<ParticipantVotedEventResponse> responses = new ArrayList<ParticipantVotedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ParticipantVotedEventResponse typedResponse = new ParticipantVotedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._blockchainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
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
                typedResponse._blockchainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
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
            typedResponse._blockchainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
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
                typedResponse._blockchainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
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

    public static class AddedBlockchainEventResponse extends BaseEventResponse {
        public BigInteger _blockchainId;
    }

    public static class AddingBlockchainMaskedParticipantEventResponse extends BaseEventResponse {
        public BigInteger _blockchainId;

        public byte[] _participant;
    }

    public static class AddingBlockchainUnmaskedParticipantEventResponse extends BaseEventResponse {
        public BigInteger _blockchainId;

        public String _participant;
    }

    public static class ParticipantVotedEventResponse extends BaseEventResponse {
        public BigInteger _blockchainId;

        public String _participant;

        public BigInteger _action;

        public BigInteger _voteTarget;

        public Boolean _votedFor;
    }

    public static class VoteResultEventResponse extends BaseEventResponse {
        public BigInteger _blockchainId;

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
