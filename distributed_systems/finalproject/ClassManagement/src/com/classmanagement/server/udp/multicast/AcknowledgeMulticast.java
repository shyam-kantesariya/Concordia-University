package com.classmanagement.server.udp.multicast;

import com.classmanagement.configuration.Constants;
import com.classmanagement.models.QueueBuffer;

import java.io.IOException;
import java.net.*;
import java.util.Random;

/**
 * Created by Sparta on 7/23/2017.
 */
public class AcknowledgeMulticast implements Runnable{
    QueueBuffer ackQueue;
    String requestId;

    public AcknowledgeMulticast(QueueBuffer queue, String requestId){
        this.ackQueue = queue;
        this.requestId = requestId;
    }

    public void run(){
        DatagramSocket datagramSocket;
        try {
            datagramSocket = new DatagramSocket();
            byte[] data = requestId.getBytes();
            datagramSocket.send(new DatagramPacket(data, data.length,
                    InetAddress.getByName(Constants.FRONT_END_UDP_SERVER_IP), Constants.FRONT_END_UDP_SERVER_PORT));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
