package com.classmanagement.server.udp.frontendreceiver;

import com.classmanagement.utils.RequestBuffer;

import java.io.IOException;
import java.net.*;

/**
 * Receiver to handle FE requests
 */
public class ResponseListener implements Runnable{

    String hostName;
    int port;
    RequestBuffer requestBuffer;

    public ResponseListener(String hostName, int port, RequestBuffer requestBuffer){
        this.hostName = hostName;
        this.port = port;
        this.requestBuffer = requestBuffer;
    }

    @Override
    public void run() {
        DatagramSocket datagramSocket;
        try{
            datagramSocket = new DatagramSocket(port, InetAddress.getByName(hostName));
            while (true){
                byte[] data = new byte[65000];
                DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
                datagramSocket.receive(datagramPacket);
                new Thread(new ResponseHandler(datagramPacket, requestBuffer)).start();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}