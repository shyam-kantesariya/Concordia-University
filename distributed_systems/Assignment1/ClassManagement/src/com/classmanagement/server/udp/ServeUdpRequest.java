package com.classmanagement.server.udp;

import com.classmanagement.configuration.Constants;
import com.classmanagement.configuration.LogMessages;
import com.classmanagement.exceptions.InvalidUdpRequestType;
import com.classmanagement.models.UdpRequestTypes;
import com.classmanagement.server.center.CenterServerImp;
import com.classmanagement.utils.LoggerProvider;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Worker class to server a UDP request.
 * Receives request packet and reply to that request
 */
public class ServeUdpRequest extends Thread {
    private DatagramPacket receivedPacket;
    private CenterServerImp centerServer;
    private Logger logger;

    /**
     * @param packet DatagramPacket which contains request message
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
        UdpRequestTypes udpMessage;
        byte[] responseData;
        DatagramSocket udpSocket = null;
        udpMessage = UdpRequestTypes.valueOf(new String(receivedPacket.getData()).trim());
        try {
            udpSocket = new DatagramSocket();
            logger.log(Level.INFO, String.format(LogMessages.UDP_REQ_RECEIVED, udpMessage, receivedPacket.getAddress(),
                    receivedPacket.getPort()));
            switch (udpMessage) {
                case PING:
                    responseData = "Hi, How can I help you?".getBytes();
                    udpSocket.send(new DatagramPacket(responseData, responseData.length, receivedPacket.getAddress(),
                            receivedPacket.getPort()));
                    break;
                case SEND_RECORD_COUNT:
                    responseData = centerServer.getRecordCount().toString().getBytes();
                    udpSocket.send(new DatagramPacket(responseData, responseData.length, receivedPacket.getAddress(),
                            receivedPacket.getPort()));
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

    /**Initialize Logger instance
     * @throws IOException
     */
    private void initLogger() throws IOException {
        logger = LoggerProvider.getLogger(centerServer.serverLocation);
    }
}