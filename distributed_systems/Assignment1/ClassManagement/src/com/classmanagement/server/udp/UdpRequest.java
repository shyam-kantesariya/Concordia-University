package com.classmanagement.server.udp;

import com.classmanagement.configuration.Constants;
import com.classmanagement.configuration.LogMessages;
import com.classmanagement.models.UdpRequestTypes;
import com.classmanagement.server.center.CenterServerImp;
import com.classmanagement.server.center.CenterServerInfo;
import com.classmanagement.utils.LoggerProvider;

import java.io.IOException;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Class to generate a UDP Request
 */
public class UdpRequest extends Thread {
    private CenterServerInfo serverInfo;
    private String recordCount;
    private Logger logger;

    /** Send a get record count request to UDP server of details provided in serverInfo object
     * @param serverInfo
     * @throws IOException in case of failure to initialize logger instance
     */
    public UdpRequest(CenterServerInfo serverInfo) throws IOException {
        this.serverInfo = serverInfo;
        initLogger();
    }

    /**
     * @return record count details received from UDP server
     */
    public String getRemoteRecordCount() {
        return recordCount;
    }

    /**
     * Fetch UDP server details from serverInfo object and send a get record count request to it
     * Store reply message in field being returned by getRemoteRecordCount method
     */
    @Override
    public void run() {
        InetAddress address = serverInfo.getInetAddress();
        int port = serverInfo.getUdpPort();
        DatagramSocket socket = null;
        try {
            logger.log(Level.INFO, String.format(LogMessages.INT_RECORD_COUNT_SERVER_REQ, serverInfo.getLocation(),
                    address, port));
            socket = new DatagramSocket();
            byte[] data = UdpRequestTypes.SEND_RECORD_COUNT.toString().getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            data = new byte[100];
            socket.receive(new DatagramPacket(data, data.length));
            recordCount = serverInfo.getLocationDescription() + ": " + new String(data);
            logger.log(Level.INFO, String.format(LogMessages.COMPLETED_RECORD_COUNT_SERVER_REQ, serverInfo.getLocation(),
                    address, port));
        } catch (SocketException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    /** Initialize Logger instance
     * @throws IOException
     */
    private void initLogger() throws IOException {
        logger = LoggerProvider.getLogger(serverInfo.getLocation());
    }
}