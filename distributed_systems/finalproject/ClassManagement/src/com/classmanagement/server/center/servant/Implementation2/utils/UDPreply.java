package com.classmanagement.server.center.servant.Implementation2.utils;


import com.classmanagement.exceptions.InvalidJournalRecordType;
import com.classmanagement.server.center.servant.Implementation2.SMS_implementation;

import java.io.IOException;
import java.net.*;


public class UDPreply implements Runnable {

    public int port;
    private SMS_implementation inter;
    private volatile boolean shouldStop = false;

    public UDPreply(SMS_implementation interace, int port) {
        // TODO Auto-generated constructor stub
        this.inter = interace;
        this.port = port;
    }

    public synchronized void setShouldStop(boolean value) {
        System.out.println("Thread " + Thread.currentThread().getId() +" Setting stop flag to: " + value);
        this.shouldStop = value;
        try {
            DatagramSocket ds = new DatagramSocket();
            byte[] data = "Dummy".getBytes();
            DatagramPacket dp = new DatagramPacket(data, data.length, InetAddress.getByName("localhost"), port);
            ds.send(dp);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket(this.port);
            while (!this.shouldStop) {
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                if (this.shouldStop){
                    break;
                }
                String s = new String(buffer);
                if (s.trim().length() == 0) {
                    String count = Integer.toString(this.inter.getCurrentCount());
                    DatagramPacket reply = new DatagramPacket(count.getBytes(), count.length(), request.getAddress(),
                            request.getPort());
                    aSocket.send(reply);
                } else {
                    String transfer = this.inter.ServerTransfer(buffer);
                    DatagramPacket reply = new DatagramPacket(transfer.getBytes(), transfer.length(), request.getAddress(),
                            request.getPort());
                    aSocket.send(reply);
                }
            }
            System.out.println("Thread " + Thread.currentThread().getId() +" is out of while: " + shouldStop);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
            System.out.println("I am in exception: " + Thread.currentThread().getId());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidJournalRecordType e) {

        }
        finally {
            if (aSocket != null) {
                aSocket.disconnect();
                aSocket.close();
            }
        }
    }

}