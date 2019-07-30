package com.classmanagement.server.udp;

import com.classmanagement.configuration.Constants;
import com.classmanagement.configuration.LogMessages;
import com.classmanagement.server.center.CenterServerImp;
import com.classmanagement.utils.LoggerProvider;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *  Provides UDP Server instance for Center Server.
 */
public class UdpServer extends Thread {
    private CenterServerImp centerServer;
    private int udpPort;
    private Logger logger;
    private DatagramSocket udpSocket = null;

    /**Constructor definition to create an Object
     * @param centerServer
     * @throws IOException
     */
    public UdpServer(CenterServerImp centerServer) throws IOException {
        this.centerServer = centerServer;
        initLogger();
        decideUdpServerPort();
    }

    /**
     * @return UDP port number where server is listening any request
     */
    public int getUdpPort() {
        return udpPort;
    }

    /**
     * @return IP address of UDP server instance. Localhost IP address for this specific implementation
     */
    public InetAddress getInetAddress() {
        try {
            return InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Dynamically decides port number falling under private port range for server DatagramSocket
     * Gives maximum PORT_CHECK_RETRY_VAL tries
     */
    private void decideUdpServerPort() {
        Random rnd = new Random();
        boolean caughtOpenPort = false;
        int retryCount = Constants.PORT_CHECK_RETRY_VAL;

        while (!caughtOpenPort && retryCount > 0) {
            udpPort = rnd.nextInt(Constants.OPEN_PORT_MAX_VAL - Constants.OPEN_PORT_MIN_VAL + 1) + Constants.OPEN_PORT_MIN_VAL;
            try {
                udpSocket = new DatagramSocket(udpPort);
                caughtOpenPort = true;
            } catch (IOException e) {
                logger.log(Level.SEVERE, LogMessages.SOCKET_CONNECTION_ERROR, udpPort);
                logger.log(Level.SEVERE, e.getMessage());
            }
            retryCount--;
        }
    }

    /**
     * run method of thread.
     * It accepts a UDP request and delegates it to ServerUdpRequest object
     */
    @Override
    public void run() {
        Thread.currentThread().setName("UDP Server: " + centerServer.serverLocation.toString());
        logger.log(Level.INFO, LogMessages.UDP_SERVER_STARTED, centerServer.serverLocation);
        byte[] packetBuffer;
        DatagramPacket packet;
        try {
            while (true) {
                try {
                    packetBuffer = new byte[100];
                    packet = new DatagramPacket(packetBuffer, packetBuffer.length);
                    udpSocket.receive(packet);
                    //Start a new thread on each request
                    new ServeUdpRequest(packet, centerServer).start();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
        } finally {
            if (udpSocket != null) udpSocket.close();
        }
    }

    /** Initialize Logger object
     * @throws IOException
     */
    private void initLogger() throws IOException {
        logger = LoggerProvider.getLogger(centerServer.serverLocation);
    }
}