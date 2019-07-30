package com.classmanagement.server.udp.unicast;

import com.classmanagement.configuration.LogMessages;
import com.classmanagement.server.center.CenterServerInfo;
import com.classmanagement.utils.LoggerProvider;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to generate a UDP Request
 */
public class UdpTransferRecordRequest extends Thread {
    private CenterServerInfo serverInfo;
    private String record;
    private String result;
    private Logger logger;
    private String replicaName;

    /** Send a transfer record request to UDP server of details provided in serverInfo object
     * @param serverInfo
     * @throws IOException in case of failure to initialize logger instance
     */
    public UdpTransferRecordRequest(CenterServerInfo serverInfo, String record, String replicaName) throws IOException {
        this.serverInfo = serverInfo;
        this.record = record;
        this.replicaName = replicaName;
        initLogger();
    }

    /**
     * @return Transfer record result received from UDP server
     */
    public String getResult() {
        return result;
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
            logger.log(Level.INFO, String.format(LogMessages.INT_TRANSFER_RECORD_SERVER_REQ, serverInfo.getLocation(),
                    address, port));
            socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(record.getBytes(), record.getBytes().length, address, port);
            socket.send(packet);
            byte[] data = new byte[100];
            socket.receive(new DatagramPacket(data, data.length));
            result = new String(data);
            logger.log(Level.INFO, String.format(LogMessages.COMPLETED_TRANSFER_RECORD_SERVER_REQ, serverInfo.getLocation(),
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
        logger = LoggerProvider.getLogger(serverInfo.getLocation(), replicaName);
    }
}