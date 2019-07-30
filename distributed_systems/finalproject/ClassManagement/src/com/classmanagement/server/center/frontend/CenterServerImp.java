package com.classmanagement.server.center.frontend;

import com.classmanagement.configuration.Constants;
import com.classmanagement.configuration.ServerActionConstants;
import com.classmanagement.exceptions.*;
import com.classmanagement.models.*;
import com.classmanagement.server.corba.POA.CenterServerPOA;
import com.classmanagement.server.udp.frontendreceiver.ResponseListener;
import com.classmanagement.server.udp.reliable.UdpSender;
import com.classmanagement.utils.LoggerProvider;
import com.classmanagement.utils.RequestBuffer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.classmanagement.configuration.ServerActionConstants.*;
import static com.classmanagement.models.ServerActions.*;


/**
 * This class implements the CORBA interface CenterServer
 */
public class CenterServerImp extends CenterServerPOA {
    private Logger logger = null;
    private List<String> lockRegister = new ArrayList<>();
    private Sequencer sequencer;
    public Object syncMonitor;
    public RequestBuffer requestBuffer;
    public ResponseListener responseReceiver;

    public CenterServerImp() throws IOException {
        super();
        initLogger();
        syncMonitor = new Object();
        requestBuffer = new RequestBuffer(syncMonitor);
        sequencer = new Sequencer(requestBuffer, syncMonitor);
        responseReceiver = new ResponseListener(Constants.FRONT_END_UDP_SERVER_IP, Constants.FRONT_END_UDP_SERVER_PORT,
                requestBuffer);
        new Thread(responseReceiver).start();
    }

    /*
     * Interface implementation block
     */

    /**
     * Creates a teacher record, adds it to the HashMap and returns the newly
     * created teacher record ID
     *
     * Adds the new record to the list of records present in HashMap
     * The first letter of lastname is used as the key and the record will be
     * inserted to list which corresponds to the key in the HashMap.
     *
     * @param firstName
     * @param lastName
     * @param address
     * @param phone
     * @param specialization
     * @param location
     * @return Teacher Record ID
     * @throws InvalidLastNameException when The first letter of the lastname is not an alphabet
     */
    public String createTRecord (String firstName, String lastName, String address, String phone,
                                 String specialization, String location, String managerId)
            throws InvalidLastNameException {
        JSONObject requestObj = new JSONObject();
        requestObj.put(REQUEST_TYPE,CREATE_T_RECORD.toString());
        requestObj.put(REQUEST_SERVER_NAME, getServerLocationByManagerId(managerId));
        requestObj.put(FIRST_NAME, firstName);
        requestObj.put(LAST_NAME, lastName);
        requestObj.put(ADDRESS, address);
        requestObj.put(PHONE, phone);
        requestObj.put(SPECIALIZATION, specialization);
        requestObj.put(LOCATION, location);
        requestObj.put(MANAGER_ID, managerId);
        return sendRequest(requestObj, managerId);
    }

    //Method to create student record
    public String createSRecord (String firstName, String lastName, String coursesRegistered, String status,
                                 String statusDate, String managerId) throws InvalidLastNameException{
        JSONObject requestObj = new JSONObject();
        requestObj.put(REQUEST_TYPE,CREATE_S_RECORD.toString());
        requestObj.put(REQUEST_SERVER_NAME, getServerLocationByManagerId(managerId));
        requestObj.put(FIRST_NAME, firstName);
        requestObj.put(LAST_NAME, lastName);
        requestObj.put(COURSES, coursesRegistered);
        requestObj.put(STATUS, status);
        requestObj.put(STATUS_DATE, statusDate);
        requestObj.put(MANAGER_ID, managerId);
        return sendRequest(requestObj, managerId);
    }

    //Method to receive record count
    public String getRecordCounts (String managerId){
        JSONObject requestObj = new JSONObject();
        requestObj.put(REQUEST_TYPE, GET_COUNT.toString());
        requestObj.put(REQUEST_SERVER_NAME, getServerLocationByManagerId(managerId));
        requestObj.put(MANAGER_ID, managerId);
        return sendRequest(requestObj, managerId);
    }

    //Method to edit existing record details
    public void editRecord (String recordId, String fieldName, String newValue, String managerId)
            throws FieldNotFoundException, RecordNotFoundException, InvalidValueException{
        JSONObject requestObj = new JSONObject();
        requestObj.put(REQUEST_TYPE, EDIT_RECORD.toString());
        requestObj.put(REQUEST_SERVER_NAME, getServerLocationByManagerId(managerId));
        requestObj.put(RECORD_ID, recordId);
        requestObj.put(FIELD_NAME, fieldName);
        requestObj.put(FIELD_VALUE, newValue);
        requestObj.put(MANAGER_ID, managerId);
        String responseString = sendRequest(requestObj, managerId);
        if(responseString.contains("FieldNotFoundException")){
            throw new FieldNotFoundException();
        } else if (responseString.contains("RecordNotFoundException")){
            throw new RecordNotFoundException();
        } else if (responseString.contains("InvalidValueException")) {
            throw new InvalidValueException();
        }
    }

    //Method to transfer record from one server to another
    public String transferRecord (String managerId, String recordId, String remoteCenterServerName)
            throws RecordNotFoundException, RecordBeingTransferredException{
        JSONObject requestObj = new JSONObject();
        requestObj.put(REQUEST_TYPE, TRANSFER_RECORD.toString());
        requestObj.put(REQUEST_SERVER_NAME, getServerLocationByManagerId(managerId));
        requestObj.put(RECORD_ID, recordId);
        requestObj.put(REMOTE_SERVER_NAME, remoteCenterServerName);
        requestObj.put(MANAGER_ID, managerId);
        return sendRequest(requestObj, managerId);
    }

    /**
     * Returns the information of a record
     * @param recordId
     * @return Info about the Record
     * @throws RecordNotFoundException if the record is not found
     */
    public String show(String recordId) throws RecordNotFoundException {
        JSONObject requestObj = new JSONObject();
        requestObj.put(REQUEST_TYPE, SHOW_RECORD.toString());
        requestObj.put(RECORD_ID, recordId);
        return sendRequest(requestObj);
    }

    //Method to send request to Replica Implementations
    private String sendRequest(JSONObject requestObj) {
        requestObj.put(MANAGER_ID, Constants.DEFAULT_MANAGER_ID);
        requestObj.put(REQUEST_SERVER_NAME, getServerLocationByManagerId(Constants.DEFAULT_MANAGER_ID));
        return this.sendRequest(requestObj, Constants.DEFAULT_MANAGER_ID);
    }

    //Method to send request to Replica Implementations, which in turn calls on multicaster
    private String sendRequest(JSONObject requestObj, String managerId){
        Object callBackObj = new Object();
        String requestId = sequencer.sendRequest(requestObj, Location.valueOf(getServerLocationByManagerId(managerId)),
                callBackObj);
        String result = null;
        synchronized (callBackObj){
            try {
                callBackObj.wait(Constants.RESPONSE_MAX_WAIT_TIME);
            } catch (InterruptedException e) {
                System.out.println("Wait time reached beyond max limit. Interrupting thread");
            }
            result = processReceivedResponses(requestId);
        }
        //System.out.println("Result: " + result);
        return result;
    }

    //Method to process any received request response or acknowledgement
    private String processReceivedResponses(String requestId){
        Result[] results = new Result[Constants.NO_OF_REPLICAS];
        int counter = 0;
        if(noResponseFromReplicas(requestId)){
            return null;
        }

        //Getting responses from history buffer where FE UDP server has stored all received response for a request
        List<JSONObject> responses = requestBuffer.getResponseFromHistoryBuffer(requestId);

        for (JSONObject response : responses){
            String result = (String) response.get(ServerActionConstants.REPLY);
            String senderId = (String) response.get(ServerActionConstants.REPLICA_ID);
            String responseSeverity = (String) response.get(ServerActionConstants.RESPONSE_SEVERITY);
            results[counter] = new Result(result, senderId, responseSeverity);
            counter++;
            }
        return getComparedResult(requestId, results, counter);
    }

    //Check if there is at least one response received from Replica
    private boolean noResponseFromReplicas(String requestId){
        if (requestBuffer.getResponseCount(requestId) == 0) {
            reportNoResponseFromAnyReplica(requestId);
            return true;
        } else {
            return false;
        }
    }

    //Inform Replica Manager that FE didn't receive any response from any Replica
    private void reportNoResponseFromAnyReplica(String requestId){
        List<String> errorReplicas = new LinkedList<String>();
        addSenderToCorruptedList(errorReplicas, Constants.REPLICA_ONE);
        addSenderToCorruptedList(errorReplicas, Constants.REPLICA_TWO);
        addSenderToCorruptedList(errorReplicas, Constants.REPLICA_THREE);
        sendErrorToReplicaManager(requestId, errorReplicas, ReplicaErrorType.NO_RESPONSE);
    }

    //If FE received just one response
    private String processOneResponse(Result[] results, List receivedResponse){
        receivedResponse.remove(results[0].senderId);
        return results[0].result;
    }

    //If FE received just two responses
    private String processTwoResponses(Result[] results, List receivedResponse){
        receivedResponse.remove(results[0].senderId);
        receivedResponse.remove(results[1].senderId);
        return results[0].result;
    }

    //If FE received all three responses. Assumption is that we are expecting only one byzantine failure
    private String processThreeResponses(Result[] results, List receivedResponse, List errorReplicas){
        receivedResponse.remove(results[0].senderId);
        receivedResponse.remove(results[1].senderId);
        receivedResponse.remove(results[2].senderId);
        String result = null;
        if (results[0].compare(results[1]) == UdpResponseType.ERROR && results[0].compare(results[2]) ==
                UdpResponseType.OK){
            addSenderToCorruptedList(errorReplicas, results[1].senderId);
            result = results[0].result;
        } else if (results[0].compare(results[1]) == UdpResponseType.OK && results[0].compare(results[2]) ==
                UdpResponseType.ERROR){
            addSenderToCorruptedList(errorReplicas, results[2].senderId);
            result = results[1].result;
        } else if (results[0].compare(results[1]) == UdpResponseType.ERROR && results[0].compare(results[2]) ==
                UdpResponseType.ERROR){
            addSenderToCorruptedList(errorReplicas, results[0].senderId);
            result = results[1].result;
        } else {
            result = results[1].result;
        }
        return result;
    }

    //Compare all received responses and report error to Replica manager in case of any descrepencies
    private String getComparedResult(String requestId, Result[] results, int totalNoOfResults){
        String result = null;
        List<String> errorReplicas = new LinkedList<String>();
        List<String> receivedResponse = new LinkedList<String>();
        initializeReplicaResponseReceivedList(receivedResponse);
        switch(totalNoOfResults){
            case 1:
                result = processOneResponse(results, receivedResponse);
                break;
            case 2:
                result = processTwoResponses(results, receivedResponse);
                break;
            case 3:
                result = processThreeResponses(results, receivedResponse, errorReplicas);
        }
        reportErrorToReplicaManager(requestId, receivedResponse, errorReplicas);
        return result;
    }

    //Assume that we didn't receive any response at the first stage
    private void initializeReplicaResponseReceivedList(List<String> responseReceived){
        responseReceived.add(Constants.REPLICA_ONE);
        responseReceived.add(Constants.REPLICA_TWO);
        responseReceived.add(Constants.REPLICA_THREE);
    }

    //Report Error reports to Replica Manager
    private void reportErrorToReplicaManager(String requestId, List<String> noReponseReplicas,
                                             List<String> errorProducingReplica){
        if(noReponseReplicas.size() != 0){
            sendErrorToReplicaManager(requestId, noReponseReplicas, ReplicaErrorType.NO_RESPONSE);
        } else if (errorProducingReplica.size() != 0) {
            sendErrorToReplicaManager(requestId, errorProducingReplica, ReplicaErrorType.WRONG_RESULT);
        }
    }

    //Send Error reports to Replica Manager in a separate reliable UDP sender
    private void sendErrorToReplicaManager(String requestId, List<String> replicaList, ReplicaErrorType errorType){
        JSONObject errorJson = new JSONObject();
        String regex = "\\[|\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(replicaList.toString());

        errorJson.put(ServerActionConstants.REQUEST_ID, requestId);
        errorJson.put(ServerActionConstants.REPLICA_ERROR_TYPE, errorType.toString());
        errorJson.put(ServerActionConstants.ERROR_REPLICA_LIST, matcher.replaceAll(""));
        System.out.println("Sending Replica Error: " + errorJson.toJSONString());
        new Thread(new UdpSender(errorJson.toJSONString(), Constants.REPLICA_MANAGER_HOST_NAME,
                Constants.REPLICA_MANAGER_PORT)).start();
    }

    //Add sender to corrupted replica list
    private void addSenderToCorruptedList(List<String> corruptedSenderList, String senderId){
        corruptedSenderList.add(senderId);
    }


    private String getServerLocationByManagerId(String managerId){
        return managerId.substring(0,3);
    }

    //Initialize logger file
    private void initLogger() throws IOException {
        logger = LoggerProvider.getLogger(Location.MTL, null);
    }
}