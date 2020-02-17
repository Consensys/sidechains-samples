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
import org.web3j.abi.datatypes.DynamicArray;
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
    private static final String BINARY = "";

    public static final String FUNC_STARTDEBUG = "startDebug";

    public static final String FUNC_MANAGEMENT_PSEUDO_BLOCKCHAIN_ID = "MANAGEMENT_PSEUDO_BLOCKCHAIN_ID";

    public static final String FUNC_GETVERSION = "getVersion";

    public static final String FUNC_GETVOTINGPERIOD = "getVotingPeriod";

    public static final String FUNC_PROPOSEVOTE = "proposeVote";

    public static final String FUNC_GETUNMASKEDBLOCKCHAINPARTICIPANT = "getUnmaskedBlockchainParticipant";

    public static final String FUNC_ignore = "ignore";

    public static final String FUNC_ACTIONVOTES = "actionVotes";

    public static final String FUNC_ADDBLOCKCHAIN = "addBlockchain";

    public static final String FUNC_ISUNMASKEDBLOCKCHAINPARTICIPANT = "isUnmaskedBlockchainParticipant";

    public static final String FUNC_GETCROSSCHAINTRANSACTIONSTATUS = "getCrosschainTransactionStatus";

    public static final String FUNC_GETBLOCKCHAINEXISTS = "getBlockchainExists";

    public static final String FUNC_GETBLOCKNUMBER = "getBlockNumber";

    public static final String FUNC_VOTE = "vote";

    public static final String FUNC_GETMASKEDBLOCKCHAINPARTICIPANT = "getMaskedBlockchainParticipant";

    public static final String FUNC_GETMASKEDBLOCKCHAINPARTICIPANTSSIZE = "getMaskedBlockchainParticipantsSize";

    public static final String FUNC_GETCROSSCHAINTRANSACTIONTIMEOUT = "getCrosschainTransactionTimeout";

    public static final String FUNC_GETPUBLICKEY = "getPublicKey";

    public static final String FUNC_START = "start";

    public static final String FUNC_PUBLICKEYEXISTS = "publicKeyExists";

    public static final String FUNC_commit = "commit";

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

    public static final Event START_EVENT = new Event("Start", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint64>() {}));
    ;

    public static final Event DUMP1_EVENT = new Event("Dump1", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event DUMP2_EVENT = new Event("Dump2", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event DUMP3_EVENT = new Event("Dump3", 
            Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));
    ;

    public static final Event ALG_EVENT = new Event("Alg", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {}));
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

    public RemoteFunctionCall<TransactionReceipt> startDebug(BigInteger _originatingBlockchainId, BigInteger _crosschainTransactionId, BigInteger _hashOfMessage, BigInteger _transactionTimeoutBlock) {
        final Function function = new Function(
                FUNC_STARTDEBUG, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingBlockchainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId), 
                new org.web3j.abi.datatypes.generated.Uint256(_hashOfMessage), 
                new org.web3j.abi.datatypes.generated.Uint256(_transactionTimeoutBlock)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public RemoteFunctionCall<TransactionReceipt> ignore(BigInteger _originatingBlockchainId, BigInteger _crosschainTransactionId, byte[] _signedIgnoreMessage) {
        final Function function = new Function(
                FUNC_ignore, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingBlockchainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId), 
                new org.web3j.abi.datatypes.DynamicBytes(_signedIgnoreMessage)), 
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

    public RemoteFunctionCall<TransactionReceipt> addBlockchain(BigInteger _blockchainId, String _votingAlgorithmContract, BigInteger _votingPeriod) {
        final Function function = new Function(
                FUNC_ADDBLOCKCHAIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId), 
                new org.web3j.abi.datatypes.Address(160, _votingAlgorithmContract), 
                new org.web3j.abi.datatypes.generated.Uint64(_votingPeriod)), 
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

    public RemoteFunctionCall<TransactionReceipt> ignore(BigInteger _originatingBlockchainId, BigInteger _crosschainTransactionId, BigInteger _hashOfMessage, BigInteger _keyVersion, byte[] _signature) {
        final Function function = new Function(
                FUNC_ignore, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingBlockchainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId), 
                new org.web3j.abi.datatypes.generated.Uint256(_hashOfMessage), 
                new org.web3j.abi.datatypes.generated.Uint64(_keyVersion), 
                new org.web3j.abi.datatypes.DynamicBytes(_signature)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple3<BigInteger, BigInteger, List<BigInteger>>> getPublicKey(BigInteger _blockchainId, BigInteger _keyVersion) {
        final Function function = new Function(FUNC_GETPUBLICKEY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId), 
                new org.web3j.abi.datatypes.generated.Uint64(_keyVersion)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint32>() {}, new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<Tuple3<BigInteger, BigInteger, List<BigInteger>>>(function,
                new Callable<Tuple3<BigInteger, BigInteger, List<BigInteger>>>() {
                    @Override
                    public Tuple3<BigInteger, BigInteger, List<BigInteger>> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, BigInteger, List<BigInteger>>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                convertToNative((List<Uint256>) results.get(2).getValue()));
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> start(BigInteger _originatingBlockchainId, BigInteger _crosschainTransactionId, BigInteger _hashOfMessage, BigInteger _transactionTimeoutBlock, BigInteger _keyVersion, byte[] _signature) {
        final Function function = new Function(
                FUNC_START, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingBlockchainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId), 
                new org.web3j.abi.datatypes.generated.Uint256(_hashOfMessage), 
                new org.web3j.abi.datatypes.generated.Uint256(_transactionTimeoutBlock), 
                new org.web3j.abi.datatypes.generated.Uint64(_keyVersion), 
                new org.web3j.abi.datatypes.DynamicBytes(_signature)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> publicKeyExists(BigInteger _blockchainId, BigInteger _keyVersion) {
        final Function function = new Function(FUNC_PUBLICKEYEXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId), 
                new org.web3j.abi.datatypes.generated.Uint64(_keyVersion)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> commit(BigInteger _originatingBlockchainId, BigInteger _crosschainTransactionId, BigInteger _hashOfMessage, BigInteger _keyVersion, byte[] _signature) {
        final Function function = new Function(
                FUNC_commit, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingBlockchainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId), 
                new org.web3j.abi.datatypes.generated.Uint256(_hashOfMessage), 
                new org.web3j.abi.datatypes.generated.Uint64(_keyVersion), 
                new org.web3j.abi.datatypes.DynamicBytes(_signature)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> commit(BigInteger _originatingBlockchainId, BigInteger _crosschainTransactionId, byte[] _signedCommitMessage) {
        final Function function = new Function(
                FUNC_commit, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_originatingBlockchainId), 
                new org.web3j.abi.datatypes.generated.Uint256(_crosschainTransactionId), 
                new org.web3j.abi.datatypes.DynamicBytes(_signedCommitMessage)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple3<BigInteger, BigInteger, List<BigInteger>>> getActivePublicKey(BigInteger _blockchainId) {
        final Function function = new Function(FUNC_GETACTIVEPUBLICKEY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_blockchainId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint32>() {}, new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<Tuple3<BigInteger, BigInteger, List<BigInteger>>>(function,
                new Callable<Tuple3<BigInteger, BigInteger, List<BigInteger>>>() {
                    @Override
                    public Tuple3<BigInteger, BigInteger, List<BigInteger>> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, BigInteger, List<BigInteger>>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                convertToNative((List<Uint256>) results.get(2).getValue()));
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

    public List<StartEventResponse> getStartEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(START_EVENT, transactionReceipt);
        ArrayList<StartEventResponse> responses = new ArrayList<StartEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            StartEventResponse typedResponse = new StartEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._originatingBlockchainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._crosschainTransactionId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._hashOfMessage = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._transactionTimeoutBlock = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._keyVersion = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<StartEventResponse> startEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, StartEventResponse>() {
            @Override
            public StartEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(START_EVENT, log);
                StartEventResponse typedResponse = new StartEventResponse();
                typedResponse.log = log;
                typedResponse._originatingBlockchainId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._crosschainTransactionId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._hashOfMessage = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._transactionTimeoutBlock = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._keyVersion = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<StartEventResponse> startEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(START_EVENT));
        return startEventFlowable(filter);
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

    public List<Dump3EventResponse> getDump3Events(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DUMP3_EVENT, transactionReceipt);
        ArrayList<Dump3EventResponse> responses = new ArrayList<Dump3EventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            Dump3EventResponse typedResponse = new Dump3EventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.a = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<Dump3EventResponse> dump3EventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, Dump3EventResponse>() {
            @Override
            public Dump3EventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DUMP3_EVENT, log);
                Dump3EventResponse typedResponse = new Dump3EventResponse();
                typedResponse.log = log;
                typedResponse.a = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<Dump3EventResponse> dump3EventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DUMP3_EVENT));
        return dump3EventFlowable(filter);
    }

    public List<AlgEventResponse> getAlgEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ALG_EVENT, transactionReceipt);
        ArrayList<AlgEventResponse> responses = new ArrayList<AlgEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AlgEventResponse typedResponse = new AlgEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.alg = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AlgEventResponse> algEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AlgEventResponse>() {
            @Override
            public AlgEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ALG_EVENT, log);
                AlgEventResponse typedResponse = new AlgEventResponse();
                typedResponse.log = log;
                typedResponse.alg = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AlgEventResponse> algEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ALG_EVENT));
        return algEventFlowable(filter);
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

    public static class StartEventResponse extends BaseEventResponse {
        public BigInteger _originatingBlockchainId;

        public BigInteger _crosschainTransactionId;

        public BigInteger _hashOfMessage;

        public BigInteger _transactionTimeoutBlock;

        public BigInteger _keyVersion;
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

    public static class Dump3EventResponse extends BaseEventResponse {
        public byte[] a;
    }

    public static class AlgEventResponse extends BaseEventResponse {
        public BigInteger alg;
    }
}
