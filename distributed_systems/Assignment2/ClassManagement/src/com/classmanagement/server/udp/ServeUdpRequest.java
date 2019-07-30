package com.classmanagement.server.udp;

import com.classmanagement.configuration.Constants;
import com.classmanagement.configuration.LogMessages;
import com.classmanagement.exceptions.InvalidUdpRequestType;
import com.classmanagement.models.UdpRequestType;
import com.classmanagement.models.UdpResponseType;
import com.classmanagement.server.center.CenterServerImp;
import com.classmanagement.utils.LoggerProvider;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.classmanagement.configuration.Constants.TRANSFER_RECORD_PREFIX;

/**
 * Worker class to server a UDP request.
 * Receives request packet and reply to that request
 */
public class ServeUdpRequest extends Thread {
    private DatagramPacket receivedPacket;
    private CenterServerImp centerServer;
    private Logger logger;

    /**
     * @param packet       DatagramPacket which contains request message
     * @param centerServer Center Server reference to server record count request
     * @throws IOException
     */
    public ServeUdpRequest(DatagramPacket packet, CenterServerImp centerServer) throws IOException {
        receivedPacket = packet;
        this.centerServer = centerServer;
        initLogger();
    }

    /**
     * Method which unmarshals request packet, processes it and replies back
     */
    @Override
    public void run() {
        UdpRequestType udpMessage;
        String data = new String(receivedPacket.getData());
        if (data.contains(TRANSFER_RECORD_PREFIX))
        {
            data = data.replace(TRANSFER_RECORD_PREFIX,"");
            udpMessage = UdpRequestType.TRANSFER_RECORD;
        }
        else {
            udpMessage = UdpRequestType.valueOf(new String(receivedPacket.getData()).trim());
        }
        DatagramSocket udpSocket = null;
        try {
            udpSocket = new DatagramSocket();
            logger.log(Level.INFO, String.format(LogMessages.UDP_REQ_RECEIVED, udpMessage, receivedPacket.getAddress(),
                    receivedPacket.getPort()));
            switch (udpMessage) {
                case PING:
                    servePingRequest(udpSocket);
                    break;
                case SEND_RECORD_COUNT:
                    serveRecordCountRequest(udpSocket);
                    break;
                case TRANSFER_RECORD:
                    serveRecordTransferRequest(data, udpSocket);
                    break;
                default:
                    throw new InvalidUdpRequestType();
            }
            logger.log(Level.INFO, String.format(LogMessages.UDP_RESPONSE_SENT, udpMessage, receivedPacket.getAddress(),
                    receivedPacket.getPort()));
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } catch (InvalidUdpRequestType e) {
            logger.log(Level.SEVERE, e.getMessage(udpMessage.toString()));
        } finally {
            if (udpSocket != null) udpSocket.close();
        }
    }

    private void servePingRequest(DatagramSocket udpSocket) throws IOException {
        udpSocket = new DatagramSocket();
        byte[] responseData = "Hi, How can I help you?".getBytes();
        sendPacket(udpSocket, responseData);
    }

    private void serveRecordCountRequest(DatagramSocket udpSocket) throws IOException {
        byte[] responseData = centerServer.getRecordCount().toString().getBytes();
        sendPacket(udpSocket, responseData);
    }

    private void serveRecordTransferRequest(String data, DatagramSocket udpSocket) throws IOException {
        byte[] responseData = centerServer.Transfer(data).getBytes();
        sendPacket(udpSocket, responseData);
        //sendOkResponse(udpSocket);
        //int length = getObjectByteArrayLength(udpSocket);
        //storeReceivedObject(udpSocket, length);
    }

    private void storeReceivedObject(DatagramSocket udpSocket, int bufferSize) throws IOException {
        DatagramPacket objectByteArray = receivePacket(udpSocket, bufferSize);

    }

    private int getObjectByteArrayLength(DatagramSocket udpSocket) throws IOException {
        DatagramPacket lengthInfoPacket = receivePacket(udpSocket, Constants.DEFAULT_UDP_BUFFER_SIZE);
        int objectLength = Integer.parseInt(lengthInfoPacket.getData().toString());
        sendOkResponse(udpSocket);
        return objectLength;
    }

    private void sendOkResponse(DatagramSocket udpSocket) throws IOException {
        byte[] responseData = UdpResponseType.OK.toString().getBytes();
        sendPacket(udpSocket, responseData);
    }
    private void sendPacket(DatagramSocket udpSocket, byte[] responseData) throws IOException {
        udpSocket.send(new DatagramPacket(responseData, responseData.length, receivedPacket.getAddress(),
                receivedPacket.getPort()));
    }

    private DatagramPacket receivePacket(DatagramSocket udpSocket, int bufferSize) throws IOException {
        byte[] data = new byte[bufferSize];
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
        udpSocket.receive(datagramPacket);
        return datagramPacket;
    }

    /**
     * Initialize Logger instance
     *
     * @throws IOException
     */
    private void initLogger() throws IOException {
        logger = LoggerProvider.getLogger(centerServer.serverLocation);
    }
}