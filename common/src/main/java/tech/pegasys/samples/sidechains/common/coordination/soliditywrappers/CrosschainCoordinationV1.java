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
    private static final String BINARY = "60806040523480156200001157600080fd5b5060405162001f6c38038062001f6c833981810160405260408110156200003757600080fd5b50805160209182015160408051938401905260008084529192909162000070908463ffffffff851682856001600160e01b036200007916565b50505062000327565b6000858152602081905260409020547401000000000000000000000000000000000000000090046001600160401b031615620000b457600080fd5b6000836001600160401b031611620000cb57600080fd5b6040805186815290517f1edd0fcf19330896f6a214cd6a5129243c6b865b9b9725ffcc74dcbd9d02850d9181900360200190a16000858152602081815260408083208054600160a01b600160e01b031916740100000000000000000000000000000000000000006001600160401b0389811691909102919091176001600160a01b03199081166001600160a01b038b161783556002830180546001818101835591885286882001805433931683179055908652600383018552928520805460ff1916841790558985529390925290810180546001600160401b03198116908416909201909216179055620001c1858383620001c8565b5050505050565b6000838152602081815260408083206001600160401b038616845260080190915290205415620001f757600080fd5b6000838152602081815260408083206001600160401b038616845260080182529091204381558251620002339260019092019184019062000282565b50506000918252602082905260409091206007018054600160401b600160801b031981166001600160401b039182166801000000000000000002176001600160401b0319169216919091179055565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620002c557805160ff1916838001178555620002f5565b82800160010185558215620002f5579182015b82811115620002f5578251825591602001919060010190620002d8565b506200030392915062000307565b5090565b6200032491905b808211156200030357600081556001016200030e565b90565b611c3580620003376000396000f3fe608060405234801561001057600080fd5b506004361061014d5760003560e01c806366ac16f5116100c3578063a6db327a1161007c578063a6db327a146105fa578063b1d50a4814610626578063cd941f6e146106a0578063d2a3d0a4146106df578063dcca364c146106fc578063e4a4c5661461071f5761014d565b806366ac16f5146104a5578063708f5a6b146104d15780637ac52862146104f45780637d75219c1461051157806398bd99911461051957806399740360146105365761014d565b806332f402611161011557806332f40261146102d35780633352b8f81461034d5780633c4dc185146103895780633e9b1adc1461042557806342cbb15c146104565780634dbdfb1b146104705761014d565b80630d8e6e2c1461015257806310fb72a614610171578063117368bc146101aa57806317ff78be146102365780631e18d51b146102b0575b600080fd5b61015a610748565b6040805161ffff9092168252519081900360200190f35b61018e6004803603602081101561018757600080fd5b503561074e565b604080516001600160401b039092168252519081900360200190f35b610234600480360360a08110156101c057600080fd5b81359161ffff6020820135169160408201359160608101359181019060a081016080820135600160201b8111156101f657600080fd5b82018360208201111561020857600080fd5b803590602001918460018302840111600160201b8311171561022957600080fd5b509092509050610770565b005b6102346004803603606081101561024c57600080fd5b813591602081013591810190606081016040820135600160201b81111561027257600080fd5b82018360208201111561028457600080fd5b803590602001918460018302840111600160201b831117156102a557600080fd5b509092509050610a2e565b610234600480360360408110156102c657600080fd5b5080359060200135610ac9565b610234600480360360808110156102e957600080fd5b813591602081013591810190606081016040820135600160201b81111561030f57600080fd5b82018360208201111561032157600080fd5b803590602001918460018302840111600160201b8311171561034257600080fd5b919350915035611071565b6103706004803603604081101561036357600080fd5b50803590602001356110f0565b6040805163ffffffff9092168252519081900360200190f35b610234600480360360a081101561039f57600080fd5b8135916001600160a01b03602082013516916001600160401b03604083013581169260608101359091169181019060a081016080820135600160201b8111156103e757600080fd5b8201836020820111156103f957600080fd5b803590602001918460018302840111600160201b8311171561041a57600080fd5b5090925090506111bd565b6104426004803603602081101561043b57600080fd5b5035611243565b604080519115158252519081900360200190f35b61045e611267565b60408051918252519081900360200190f35b6102346004803603608081101561048657600080fd5b5080359061ffff6020820135169060408101359060600135151561126b565b610442600480360360408110156104bb57600080fd5b50803590602001356001600160a01b0316611358565b61045e600480360360408110156104e757600080fd5b5080359060200135611385565b61045e6004803603602081101561050a57600080fd5b50356113c3565b61045e6113d8565b61045e6004803603602081101561052f57600080fd5b50356113dd565b6105626004803603604081101561054c57600080fd5b50803590602001356001600160401b03166113f2565b60405180846001600160401b03166001600160401b0316815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156105bd5781810151838201526020016105a5565b50505050905090810190601f1680156105ea5780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b6104426004803603604081101561061057600080fd5b50803590602001356001600160401b03166114b9565b6102346004803603606081101561063c57600080fd5b813591602081013591810190606081016040820135600160201b81111561066257600080fd5b82018360208201111561067457600080fd5b803590602001918460018302840111600160201b8311171561069557600080fd5b5090925090506114e5565b6106c3600480360360408110156106b657600080fd5b5080359060200135611578565b604080516001600160a01b039092168252519081900360200190f35b610562600480360360208110156106f557600080fd5b50356115b1565b61045e6004803603604081101561071257600080fd5b50803590602001356115f7565b6102346004803603606081101561073557600080fd5b5080359060208101359060400135611627565b60015b90565b600090815260208190526040902054600160a01b90046001600160401b031690565b600086815260208181526040808320338452600301909152902054869060ff1661079957600080fd5b60008661ffff1660058111156107ab57fe5b9050600080898152602081815260408083208a845260060190915290205460ff1660058111156107d757fe5b146107e157600080fd5b60018160058111156107ef57fe5b141561081d5760008881526020818152604080832089845260050190915290205460ff161561081d57600080fd5b600281600581111561082b57fe5b14156108935760008881526020818152604080832089845260050190915290205460ff16151560011461085d57600080fd5b856000808a8152602001908152602001600020600401868154811061087e57fe5b90600052602060002001541461089357600080fd5b60038160058111156108a157fe5b14156108d8576000888152602081815260408083206001600160a01b038a16845260030190915290205460ff16156108d857600080fd5b60048160058111156108e657fe5b1415610984576000888152602081815260408083206001600160a01b038a168452600301909152902054869060ff16151560011461092357600080fd5b6001600160a01b03811633141561093957600080fd5b806001600160a01b03166000808b8152602001908152602001600020600201878154811061096357fe5b6000918252602090912001546001600160a01b03161461098257600080fd5b505b600581600581111561099257fe5b50506000888152602081815260408083208984526006019091529020805482919060ff191660018360058111156109c557fe5b021790555060008881526020818152604080832080548a85526006909101909252909120600160a01b9091046001600160401b03164301600182015560028101869055610a16906003018585611ab3565b50610a2488888860016117b7565b5050505050505050565b60408051602080820187905281830186905282518083038401815260609092019092528051910120600160008281526001602052604090205460ff166003811115610a7557fe5b14610a7f57600080fd5b60008181526001602081905260409091200154431115610a9e57600080fd5b6000818152600160208190526040909120805460039260ff1990911690835b02179055505050505050565b600082815260208181526040808320338452600301909152902054829060ff16610af257600080fd5b60008381526020818152604080832085845260060190915281205460ff1690816005811115610b1d57fe5b1415610b2857600080fd5b6000848152602081815260408083208684526006019091529020600101544311610b5157600080fd5b600084815260208181526040808320805460018201548886526006909201845282852060050154835163a81ce84760e01b81526001600160401b0393841660048201528382166024820152600160401b909104909216604483015291516001600160a01b039092169392849263a81ce847926064808201939291829003018186803b158015610bdf57600080fd5b505afa158015610bf3573d6000803e3d6000fd5b505050506040513d6020811015610c0957600080fd5b505190507faeb5b7640625260f6f8914a13e4cd86b256b00761645b6466e94ccb8f02d0fd486846005811115610c3b57fe5b6040805192835261ffff90911660208301528181018890528315156060830152519081900360800190a18015610f4f57600086815260208181526040808320888452600601909152902060020154856003856005811115610c9857fe5b1415610d2457600088815260208181526040808320600281018054600180820183559186528486200180546001600160a01b0319166001600160a01b0388169081179091558552600382018452918420805460ff1916831790558b845292909152908101805467ffffffffffffffff1981166001600160401b0391821690930116919091179055610f4c565b6001856005811115610d3257fe5b1415610d7c5760008881526020818152604080832060048101805460018181018355918652848620018c90558b85526005909101909252909120805460ff19169091179055610f4c565b6004856005811115610d8a57fe5b1415610e22576000888152602081905260409020600201805483908110610dad57fe5b600091825260208083209190910180546001600160a01b031916905589825281815260408083206001600160a01b0385168452600381018352908320805460ff191690558a8352919052600101805467ffffffffffffffff1981166001600160401b0391821660001901909116179055610f4c565b6002856005811115610e3057fe5b1415610e86576000888152602081905260409020600401805483908110610e5357fe5b6000918252602080832090910182905589825281815260408083208a84526005019091529020805460ff19169055610f4c565b6005856005811115610e9457fe5b1415610f4c576000888152602081815260408083208a84526006018252918290206002808201546003909201805485516001821615610100026000190190911692909204601f8101859004850283018501909552848252610f4c948d94830182828015610f425780601f10610f1757610100808354040283529160200191610f42565b820191906000526020600020905b815481529060010190602001808311610f2557829003601f168201915b50505050506118d4565b50505b60005b60008781526020819052604090206002015481101561100f576000878152602081905260408120600201805483908110610f8857fe5b6000918252602090912001546001600160a01b031614611007576000878152602081815260408083208984526006810183529083208a84529183905260020180546004909201929184908110610fda57fe5b60009182526020808320909101546001600160a01b031683528201929092526040019020805460ff191690555b600101610f52565b506000868152602081815260408083208884526006019091528120805460ff1916815560018101829055600281018290559061104e6003830182611b31565b5060050180546fffffffffffffffffffffffffffffffff19169055505050505050565b604080516020808201889052818301879052825180830384018152606090920190925280519101206000808281526001602052604090205460ff1660038111156110b757fe5b146110c157600080fd5b4382116110cd57600080fd5b6000908152600160208190526040909120805460ff191682178155015550505050565b60408051602080820185905281830184905282518083038401815260609092019092528051910120600090600260008281526001602052604090205460ff16600381111561113a57fe5b141561114b5760025b9150506111b7565b60008181526001602052604081205460ff16600381111561116857fe5b1415611175576000611143565b60008181526001602081905260409091200154431115611196576003611143565b60008181526001602052604090205460ff1660038111156111b357fe5b9150505b92915050565b3360009081527fad3228b676f7d3cd4284a5443f17f1962b36e491b30a40b2405849e597ba5fb8602052604081205460ff166111f857600080fd5b606083838080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929350610a2492508a9150899050888885611988565b600090815260208190526040902054600160a01b90046001600160401b0316151590565b4390565b600084815260208181526040808320338452600301909152902054849060ff1661129457600080fd5b60008461ffff1660058111156112a657fe5b90508060058111156112b457fe5b60008781526020818152604080832088845260060190915290205460ff1660058111156112dd57fe5b146112e757600080fd5b600086815260208181526040808320878452600601825280832033845260040190915290205460ff161561131a57600080fd5b60008681526020818152604080832087845260060190915290206001015443111561134457600080fd5b611350868686866117b7565b505050505050565b6000828152602081815260408083206001600160a01b038516845260030190915290205460ff1692915050565b604080516020808201949094528082019290925280518083038201815260609092018152815191830191909120600090815260019283905220015490565b60009081526020819052604090206004015490565b600081565b60009081526020819052604090206002015490565b6000828152602081815260408083206001600160401b038516845260080182528083208054600191820180548451600294821615610100026000190190911693909304601f81018690048602840186019094528383528594606094929385939092918301828280156114a55780601f1061147a576101008083540402835291602001916114a5565b820191906000526020600020905b81548152906001019060200180831161148857829003601f168201915b50999b959a50929850939650505050505050565b6000828152602081815260408083206001600160401b0385168452600801909152902054151592915050565b60408051602080820187905281830186905282518083038401815260609092019092528051910120600160008281526001602052604090205460ff16600381111561152c57fe5b1461153657600080fd5b6000818152600160208190526040909120015443111561155557600080fd5b6000818152600160208190526040909120805460029260ff199091169083610abd565b600082815260208190526040812060020180548390811061159557fe5b6000918252602090912001546001600160a01b03169392505050565b60008060606115c16000806113f2565b5050506000848152602081905260409020600701546115ea9085906001600160401b03166113f2565b9250925092509193909250565b600082815260208190526040812060040180548390811061161457fe5b9060005260206000200154905092915050565b600083815260208190526040812060040180548490811061164457fe5b60009182526020918290200154604080513360601b818501526034808201879052825180830390910181526054909101909152805192019190912090915080821461168e57600080fd5b60008581526020818152604080832033845260030190915290205460ff16611764576040805186815233602082015281517f960e98814ae9a46102dc8e9663fd6016b08497e5cf8c33f8255f83ca06f5a9ef929181900390910190a160008581526020818152604080832060028101805460018082018355918652848620018054336001600160a01b031990911681179091558552600382018452918420805460ff19168317905588845292909152908101805467ffffffffffffffff1981166001600160401b03918216909301169190911790555b600085815260208190526040902060040180548590811061178157fe5b600091825260208083209091018290559581528086526040808220938252600590930190955250909220805460ff191690555050565b6040805185815233602082015261ffff85168183015260608101849052821515608082015290517fef19f8b95a20f38d0a8a745702c10e0ef86b204ddcef51d7fba0b0da2d8aebf29181900360a00190a160008481526020818152604080832085845260060182528083203384526004019091529020805460ff19166001179055801561188157600084815260208181526040808320858452600601909152902060050180546001600160401b038082166001011667ffffffffffffffff199091161790556118ce565b6000848152602081815260408083208584526006019091529020600501805460016001600160401b03600160401b808404821692909201160267ffffffffffffffff60401b199091161790555b50505050565b6000838152602081815260408083206001600160401b03861684526008019091529020541561190257600080fd5b6000838152602081815260408083206001600160401b03861684526008018252909120438155825161193c92600190920191840190611b78565b5050600091825260208290526040909120600701805467ffffffffffffffff60401b1981166001600160401b03918216600160401b021767ffffffffffffffff19169216919091179055565b600085815260208190526040902054600160a01b90046001600160401b0316156119b157600080fd5b6000836001600160401b0316116119c757600080fd5b6040805186815290517f1edd0fcf19330896f6a214cd6a5129243c6b865b9b9725ffcc74dcbd9d02850d9181900360200190a1600085815260208181526040808320805467ffffffffffffffff60a01b1916600160a01b6001600160401b0389811691909102919091176001600160a01b03199081166001600160a01b038b161783556002830180546001818101835591885286882001805433931683179055908652600383018552928520805460ff19168417905589855293909252908101805467ffffffffffffffff198116908416909201909216179055611aac8583836118d4565b5050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611af45782800160ff19823516178555611b21565b82800160010185558215611b21579182015b82811115611b21578235825591602001919060010190611b06565b50611b2d929150611be6565b5090565b50805460018160011615610100020316600290046000825580601f10611b575750611b75565b601f016020900490600052602060002090810190611b759190611be6565b50565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611bb957805160ff1916838001178555611b21565b82800160010185558215611b21579182015b82811115611b21578251825591602001919060010190611bcb565b61074b91905b80821115611b2d5760008155600101611bec56fea265627a7a723058206bde5a62e09facb8c6fec52251c5df3ce6563ae236e24582b1a53b15d0675d4f64736f6c634300050a0032";

    public static final String FUNC_GETVERSION = "getVersion";

    public static final String FUNC_GETVOTINGPERIOD = "getVotingPeriod";

    public static final String FUNC_PROPOSEVOTE = "proposeVote";

    public static final String FUNC_IGNORE = "ignore";

    public static final String FUNC_ACTIONVOTES = "actionVotes";

    public static final String FUNC_START = "start";

    public static final String FUNC_GETCROSSCHAINTRANSACTIONSTATUS = "getCrosschainTransactionStatus";

    public static final String FUNC_ADDSIDECHAIN = "addSidechain";

    public static final String FUNC_GETSIDECHAINEXISTS = "getSidechainExists";

    public static final String FUNC_GETBLOCKNUMBER = "getBlockNumber";

    public static final String FUNC_VOTE = "vote";

    public static final String FUNC_ISUNMASKEDSIDECHAINPARTICIPANT = "isUnmaskedSidechainParticipant";

    public static final String FUNC_GETCROSSCHAINTRANSACTIONTIMEOUT = "getCrosschainTransactionTimeout";

    public static final String FUNC_GETMASKEDSIDECHAINPARTICIPANTSSIZE = "getMaskedSidechainParticipantsSize";

    public static final String FUNC_MANAGEMENT_PSEUDO_SIDECHAIN_ID = "MANAGEMENT_PSEUDO_SIDECHAIN_ID";

    public static final String FUNC_GETUNMASKEDSIDECHAINPARTICIPANTSSIZE = "getUnmaskedSidechainParticipantsSize";

    public static final String FUNC_GETPUBLICKEY = "getPublicKey";

    public static final String FUNC_PUBLICKEYEXISTS = "publicKeyExists";

    public static final String FUNC_COMMIT = "commit";

    public static final String FUNC_GETUNMASKEDSIDECHAINPARTICIPANT = "getUnmaskedSidechainParticipant";

    public static final String FUNC_GETACTIVEPUBLICKEY = "getActivePublicKey";

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

    public RemoteFunctionCall<TransactionReceipt> addSidechain(BigInteger _sidechainId, String _votingAlgorithmContract, BigInteger _votingPeriod, BigInteger _keyVersion, byte[] _publicKey) {
        final Function function = new Function(
                FUNC_ADDSIDECHAIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId), 
                new org.web3j.abi.datatypes.Address(160, _votingAlgorithmContract), 
                new org.web3j.abi.datatypes.generated.Uint64(_votingPeriod), 
                new org.web3j.abi.datatypes.generated.Uint64(_keyVersion), 
                new org.web3j.abi.datatypes.DynamicBytes(_publicKey)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public RemoteFunctionCall<Tuple3<BigInteger, BigInteger, byte[]>> getPublicKey(BigInteger _sidechainId, BigInteger _keyVersion) {
        final Function function = new Function(FUNC_GETPUBLICKEY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId), 
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

    public RemoteFunctionCall<Boolean> publicKeyExists(BigInteger _sidechainId, BigInteger _keyVersion) {
        final Function function = new Function(FUNC_PUBLICKEYEXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId), 
                new org.web3j.abi.datatypes.generated.Uint64(_keyVersion)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
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

    public RemoteFunctionCall<Tuple3<BigInteger, BigInteger, byte[]>> getActivePublicKey(BigInteger _sidechainId) {
        final Function function = new Function(FUNC_GETACTIVEPUBLICKEY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sidechainId)), 
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
