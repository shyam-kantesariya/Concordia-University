package com.classmanagement.utils;

import com.classmanagement.configuration.Constants;
import com.classmanagement.models.QueueBuffer;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Sparta on 7/23/2017.
 */
public class RequestBuffer {
    private HashMap<String, byte[]> requests;
    private HashMap<String, QueueBuffer> pendingAckQueue;
    private HashMap<String, List<JSONObject>> responses;
    private HashMap<String, Object> waitInterruptObject;
    private Object syncMonitor;

    public RequestBuffer(Object syncMonitor){
        requests = new HashMap<String, byte[]>();
        pendingAckQueue = new HashMap<String, QueueBuffer>();
        responses = new HashMap<String, List<JSONObject>>();
        waitInterruptObject = new HashMap<String, Object>();
        this.syncMonitor = syncMonitor;
    }

    public int pendingAckCount(){
        int maxQueue=-1;
        for(QueueBuffer queue: pendingAckQueue.values()){
            maxQueue = Integer.max(maxQueue,queue.getSize());
        }
        return maxQueue;
    }

    public void addSentRequestIdToAckQueue ( String requestId) {
        for (QueueBuffer queue : pendingAckQueue.values())
        queue.addElement(requestId);
    }

    public void addSentRequestToHistory(String requestId, byte[] requestByteData){
        requests.put(requestId, requestByteData);
    }

    public void removeAcknowledgedPacket (String replicaId, String requestId){
        if (pendingAckQueue.get(replicaId) != null) {
            pendingAckQueue.get(replicaId).remove(requestId);
        }
    }

    public List<String> getPendingAckReplicaList(String requestId){
        List<String> replicaList = new LinkedList<String>();
        for(String key: pendingAckQueue.keySet()){
            if(pendingAckQueue.get(key).contains(requestId)) {
                replicaList.add(key);
            }
        }
        return replicaList;
    }

    public void storeResponse(String requestId, JSONObject response){
        List receivedResponseList = getResponseFromHistoryBuffer(requestId);
     //   System.out.println("@@@@@@@@@@@@@@Value of receviedResponseList is: " + receivedResponseList.toString());
        addResponseToList(receivedResponseList, response);
     //   System.out.println("@@@@@@@@@@@@@@Now Value of receviedResponseList is: " + receivedResponseList.toString());
     //   System.out.println("@@@@@@@@@@@@@@Value of response map is: " + responses.toString());
    }

    public synchronized void addToResponseBuffer(String requestId, List<JSONObject> responseList){
        responses.put(requestId, responseList);
    }

    public synchronized List<JSONObject> getResponseFromHistoryBuffer(String requestId){
        if (responses.getOrDefault(requestId, null) == null){
            List<JSONObject> newList = new LinkedList<JSONObject>();
            responses.put(requestId, newList);
            return newList;
        } else {
            return responses.get(requestId);
        }
    }

    public synchronized void registerWaitInterruptObject(String requestId, Object object){
        waitInterruptObject.put(requestId, object);
    }

    public synchronized int getResponseCount(String requestId){
        if (responses.get(requestId) == null) {
            return 0;
        } else {
            return responses.get(requestId).size();
        }
    }

    public synchronized void notifyFrontEndOnReceivingAllResponses(String requestId){
        Object waitObj = waitInterruptObject.get(requestId);
        synchronized (waitObj){
            waitObj.notify();
        }
    }

    private synchronized void addResponseToList(List responseList, JSONObject response) {
        responseList.add(response);
    }

    private void initializeReplicaAckQueue(String replicaId){
        pendingAckQueue.put(replicaId, new QueueBuffer());
    }

}