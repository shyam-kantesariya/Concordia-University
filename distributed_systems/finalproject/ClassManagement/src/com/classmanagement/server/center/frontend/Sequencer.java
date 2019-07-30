package com.classmanagement.server.center.frontend;

import com.classmanagement.configuration.Constants;
import com.classmanagement.models.Counter;
import com.classmanagement.models.CounterType;
import com.classmanagement.models.Location;
import com.classmanagement.utils.RequestBuffer;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static com.classmanagement.configuration.ServerActionConstants.REQUEST_ID;


/**
 * Sequencer to send requests to replicas and achieve total ordering
 */
public class Sequencer {
    Logger logger;
    RequestBuffer requestBuffer;
    Object syncMonitor;
    HashMap<String, Counter> requestIdCounter;

    public Sequencer(RequestBuffer requestBuffer, Object syncMonitor){
        requestIdCounter = new HashMap<String, Counter>();
        for (Location lc : Location.values()) {
            requestIdCounter.put(lc.toString(), new Counter(0));
        }
        this.requestBuffer = requestBuffer;
        this.syncMonitor = syncMonitor;
    }

    //Send request to Replicas
    public String sendRequest(JSONObject request, Location location, Object callBackObj){
        String requestId = location.toString() + requestIdCounter.get(location.toString()).nextVal();
        int counter = 0;
        request.put(REQUEST_ID, requestId);
        requestBuffer.addSentRequestIdToAckQueue(requestId);
        registerCallbackObj(requestId, callBackObj);
        byte[] sentPacketdata = multicastRequest(request, location);

        //Check for acknowledgement else resend
        while (true) {
            if (requestBuffer.getPendingAckReplicaList(requestId).size() == 0 || counter >=
                    Constants.UDP_MAX_RETRY_ATTEMPT) {
                break;
            } else {
                try {
                    Thread.sleep(Constants.MULTICAST_RETRY_WAIT_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                multicastRequest(request, location);
                counter++;
            }
        }

        requestBuffer.addSentRequestToHistory(requestId, sentPacketdata);
        return requestId;
    }

    //Multicast request object to group IP
    private synchronized byte[] multicastRequest(JSONObject request, Location location){
        DatagramSocket ds = null;
        try {
            synchronized (syncMonitor){
                while (requestBuffer.pendingAckCount() > Constants.REQUEST_DELIVERY_BACKLOG_SIZE) {
                    try {
                        syncMonitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            ds = new DatagramSocket();
            byte[] data = request.toJSONString().getBytes();
            DatagramPacket dp = new DatagramPacket(data, data.length, InetAddress.getByName(Constants.UDP_MULTICAST_IP),
                    Constants.UDP_MULTICAST_PORT);
            ds.send(dp);
            return data;
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(!ds.isClosed()) ds.close();
        }
        return null;
    }

    //TODO: Handle this
    private byte[] getDataBytes(JSONObject request, Location location){
        //https://stackoverflow.com/questions/5683486/how-to-combine-two-byte-arrays
        byte[] data = request.toJSONString().getBytes();
        byte[] server = location.toString().getBytes();
        return ArrayUtils.addAll(server, data);
    }

    //Provide handle to interrupt FE wait on receiving all three responses
    private void registerCallbackObj(String requestId, Object callbackObj){
        requestBuffer.registerWaitInterruptObject(requestId, callbackObj);
    }
}