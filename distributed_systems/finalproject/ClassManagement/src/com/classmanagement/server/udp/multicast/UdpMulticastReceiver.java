package com.classmanagement.server.udp.multicast;

import com.classmanagement.configuration.Constants;
import com.classmanagement.models.Location;
import com.classmanagement.server.center.servant.CenterServant;
import com.classmanagement.server.replica.Replica;
import com.classmanagement.server.replica.ReplicaUDPActionExecutor;
import com.classmanagement.server.udp.multicast.ProcessMulticastUdpRequest;

import java.io.IOException;
import java.net.*;

/**
 * Created by Sparta on 7/23/2017.
 */
public class UdpMulticastReceiver implements Runnable{

    private Replica replica;
    public  boolean shouldStop = false;
    public UdpMulticastReceiver(Replica replica) throws UnknownHostException {
        this.replica = replica;
        new Thread(this).start();
    }

    public synchronized void setShouldStop(boolean value) {
        this.shouldStop = value;
    }

    public void run(){
        MulticastSocket receivingSocket = null;
        System.out.println("Listening to receive Multicast request from fronted");
        try{
            receivingSocket = new MulticastSocket(Constants.UDP_MULTICAST_PORT);
            InetAddress group = InetAddress.getByName(Constants.UDP_MULTICAST_IP);
            receivingSocket.joinGroup(group);
            while (!shouldStop){
                byte[] data = new byte[65000];
                DatagramPacket dataPacket = new DatagramPacket(data, data.length);
                try {
                    receivingSocket.receive(dataPacket);
                    Thread actionExecutor = new ReplicaUDPActionExecutor(replica, dataPacket);
                    actionExecutor.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(!receivingSocket.isClosed()) receivingSocket.close();
        }
    }
}