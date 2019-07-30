package com.classmanagement.server.replica;

import com.classmanagement.configuration.Constants;
import com.classmanagement.configuration.ServerActionConstants;
import com.classmanagement.models.ServerActions;
import com.classmanagement.models.UdpResponseType;
import com.classmanagement.server.center.servant.CenterServantInterface;
import com.classmanagement.server.udp.reliable.UdpSender;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static com.classmanagement.configuration.Constants.FRONT_END_UDP_SERVER_IP;
import static com.classmanagement.configuration.Constants.FRONT_END_UDP_SERVER_PORT;
import static com.classmanagement.configuration.ServerActionConstants.*;

public class ReplicaUDPActionExecutor extends Thread {
    private Replica replica;
    private JSONObject requestObject;
    private DatagramPacket dataPacket;

    public ReplicaUDPActionExecutor(Replica replica, DatagramPacket dataPacket) {
        this.replica = replica;
        this.dataPacket = dataPacket;
    }

    private void buildJson() {
        JSONParser parser = new JSONParser();
        try {
            this.requestObject = (JSONObject) parser.parse(new String(dataPacket.getData()).trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //TODO: Shyam Added this
    private void sendAcknowledgement(String requestId){
        JSONObject ackObj = new JSONObject();
        ackObj.put(ServerActionConstants.REPLICA_ID, replica.replicaId);
        ackObj.put(ServerActionConstants.REQUEST_ID, requestId);
        ackObj.put(ServerActionConstants.REPLICA_RESPONSE_TYPE, UdpResponseType.SERVANT_MULTICAST_ACKNOWLEDGEMENT.toString());
        byte[] data = ackObj.toJSONString().getBytes();
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            InetAddress hostIp = InetAddress.getByName(Constants.FRONT_END_UDP_SERVER_IP);
            datagramSocket.send(new DatagramPacket(data, data.length, hostIp, Constants.FRONT_END_UDP_SERVER_PORT));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        buildJson();
        String requestType = (String) requestObject.get(REQUEST_TYPE);
        String serverName = (String) requestObject.get(REQUEST_SERVER_NAME);
        String requestId = (String) requestObject.get(REQUEST_ID);
        //System.out.println("Performing request" + requestId);
        sendAcknowledgement(requestId);
        try {
            CenterServantInterface server = getServer(serverName);
            ServerActions action = ServerActions.valueOf(requestType.toUpperCase());
            System.out.println("ReplicaId: " + replica.replicaId+ "Exceuting Request: " + requestObject.toJSONString().toString());
            switch (action) {
                case CREATE_S_RECORD:
                    String firstName = (String) requestObject.get(FIRST_NAME);
                    String lastName = (String) requestObject.get(LAST_NAME);
                    String Courses = (String) requestObject.get(COURSES);
                    String status = (String) requestObject.get(STATUS);
                    String statusDate = (String) requestObject.get(STATUS_DATE);
                    String managerId = (String) requestObject.get(MANAGER_ID);
                    String studentRecordID = server.createSRecord(firstName, lastName, Courses, status, statusDate, managerId);
                    sendReply(requestId, serverName, studentRecordID, false);
                    break;

                case CREATE_T_RECORD:
                    String teacherFirstName = (String) requestObject.get(FIRST_NAME);
                    String teacherlastName = (String) requestObject.get(LAST_NAME);
                    String teacherManagerId = (String) requestObject.get(MANAGER_ID);
                    String address = (String) requestObject.get(ADDRESS);
                    String phone = (String) requestObject.get(PHONE);
                    String location = (String) requestObject.get(LOCATION);
                    String specialization = (String) requestObject.get(SPECIALIZATION);
                    String teacherRecordID = server.createTRecord(teacherFirstName, teacherlastName, address, phone, specialization, location, teacherManagerId);
                    sendReply(requestId, serverName,teacherRecordID, false);
                    break;

                case TRANSFER_RECORD:
                    String managerID = (String) requestObject.get(MANAGER_ID);
                    String recordID = (String) requestObject.get(RECORD_ID);
                    String remoteCenterServerName = (String) requestObject.get(REMOTE_SERVER_NAME);
                    String transferredRecordID = server.transferRecord(managerID, recordID, remoteCenterServerName);
                    sendReply(requestId, serverName,transferredRecordID, false);
                    break;

                case GET_COUNT:
                    String manager = (String) requestObject.get(MANAGER_ID);
                    String recordCount = server.getRecordCounts(manager);
                    sendReply(requestId, serverName,recordCount, false);
                    break;

                case EDIT_RECORD:
                    managerID = (String) requestObject.get(MANAGER_ID);
                    recordID = (String) requestObject.get(RECORD_ID);
                    String fieldName = (String) requestObject.get(FIELD_NAME);
                    String fieldValue = (String) requestObject.get(FIELD_VALUE);
                    server.editRecord(recordID, fieldName, fieldValue, managerID);
                    sendReply(requestId, serverName,"Success", false);
                    break;

                default:
                    break;
            }
        }
        catch (Exception e){
            sendReply(requestId, serverName, e.getMessage(), true);
        }
    }

    private CenterServantInterface getServer(String serverName) {
        if(serverName.equals("MTL"))
            return this.replica.mtlServant;
        if(serverName.equals("LVL"))
            return this.replica.lvlServant;
        if(serverName.equals("DDO"))
            return this.replica.ddoServant;
        return null;
    }

    private void sendReply(String requestId , String serverName, String responseData, boolean isError) {
        try {
            if(responseData == null){
                throw new Exception("Null response value");
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(REQUEST_ID, requestId);
            jsonObject.put(REPLICA_ID, this.replica.replicaId);
            jsonObject.put(REQUEST_SERVER_NAME, serverName);

            jsonObject.put(REPLY, responseData);
            jsonObject.put(RESPONSE_SEVERITY, isError ? RESPONSE_SEVERITY_ERROR : RESPONSE_SEVERITY_INFO);
            jsonObject.put(REPLICA_RESPONSE_TYPE, UdpResponseType.SERVANT_RESPONSE.toString());
            //System.out.println("Sending Response as: " + jsonObject.toJSONString().toString());
            byte[] emptyPrefix = new byte[0];
            UdpSender sender = new UdpSender(jsonObject.toJSONString().getBytes(), emptyPrefix, FRONT_END_UDP_SERVER_IP,
                    FRONT_END_UDP_SERVER_PORT, false);
            Thread thread = new Thread(sender);
            thread.start();
        }
        catch (Exception e){
            //TODO: HANDLE IT
        }
    }
}
