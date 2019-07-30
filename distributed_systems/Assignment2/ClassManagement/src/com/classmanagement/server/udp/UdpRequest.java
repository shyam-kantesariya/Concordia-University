package com.classmanagement.server.udp;

import com.classmanagement.configuration.Constants;
import com.classmanagement.exceptions.RecordTransferError;
import com.classmanagement.models.RecordBase;
import com.classmanagement.models.UdpRequestType;
import com.classmanagement.server.center.CenterServerInfo;
import com.classmanagement.utils.LoggerProvider;
import org.apache.commons.lang.SerializationUtils;


import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to generate a UDP Request
 */
public class UdpRequest extends Thread {
    private CenterServerInfo serverInfo;
    private String recordCount;
    private Logger logger;
    private UdpRequestType requestType;
    private RecordBase record;

    /**
     * Send a get record count request to UDP server of details provided in serverInfo object
     *
     * @param serverInfo
     * @throws IOException in case of failure to initialize logger instance
     */
    public UdpRequest(CenterServerInfo serverInfo, UdpRequestType requestType) throws IOException {
        this.serverInfo = serverInfo;
        this.requestType = requestType;
        initLogger();
    }

    public UdpRequest(CenterServerInfo serverInfo, UdpRequestType requestType, RecordBase record) throws IOException {
        this.serverInfo = serverInfo;
        this.requestType = requestType;
        this.record = record;
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
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            switch (requestType) {
                case TRANSFER_RECORD:
                    processRecordTransferRequest(socket);
                    break;
                case SEND_RECORD_COUNT:
                    processRecordCountRequest(socket);
                    break;
                case PING:
                    pingServer();
                    break;
                default:
                    System.out.println("Not a UDP request type");
            }
        } catch (SocketException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } catch (RecordTransferError recordTransferError) {
            recordTransferError.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    private void sendPacket(DatagramSocket socket, byte[] data) throws IOException {
        InetAddress address = serverInfo.getInetAddress();
        int port = serverInfo.getUdpPort();
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        socket.send(packet);
    }

    private DatagramPacket receivePacket(DatagramSocket socket, int bufferSize) throws IOException {
        byte[] data = new byte[bufferSize];
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
        socket.receive(new DatagramPacket(data, data.length));
        return datagramPacket;
    }

    private void processRecordCountRequest(DatagramSocket socket) throws IOException {
        byte[] data = UdpRequestType.SEND_RECORD_COUNT.toString().getBytes();
        sendPacket(socket, data);
        DatagramPacket recordCountResponse = receivePacket(socket, Constants.DEFAULT_UDP_BUFFER_SIZE);
        recordCount = serverInfo.getLocationDescription() + ": " + new String(recordCountResponse.getData());
        //logger.log(Level.INFO, String.format(LogMessages.COMPLETED_RECORD_COUNT_SERVER_REQ, serverInfo.getLocation(), address, port));
    }

    private void checkResponseStatus(DatagramPacket datagramPacket) throws RecordTransferError {

    }

    private void initRequest(DatagramSocket socket) throws IOException, RecordTransferError {
        byte[] data = UdpRequestType.TRANSFER_RECORD.toString().getBytes();
        sendPacket(socket, data);
        checkResponseStatus(receivePacket(socket, Constants.DEFAULT_UDP_BUFFER_SIZE));
    }

    private void informBufferSize(DatagramSocket socket, int size) throws IOException, RecordTransferError {
        byte[] data = new Integer(size).toString().getBytes();
        sendPacket(socket, data);
        checkResponseStatus(receivePacket(socket, Constants.DEFAULT_UDP_BUFFER_SIZE));
    }

    private void sendObjectByteArray(DatagramSocket socket, byte[] data) throws IOException, RecordTransferError {
        sendPacket(socket, data);
        checkResponseStatus(receivePacket(socket, Constants.DEFAULT_UDP_BUFFER_SIZE));
    }

    private void processRecordTransferRequest(DatagramSocket socket) throws IOException, RecordTransferError {
        //logger.log(Level.INFO, String.format(LogMessages.INT_RECORD_COUNT_SERVER_REQ, serverInfo.getLocation(), address, port));
        DatagramPacket receivedDatagramPacket;
        initRequest(socket);
        byte[] objectByteArray = getObjectByteArray();
        informBufferSize(socket, objectByteArray.length);
        sendObjectByteArray(socket, objectByteArray);
        //logger.log(Level.INFO, String.format(LogMessages.COMPLETED_RECORD_COUNT_SERVER_REQ, serverInfo.getLocation(), address, port));
    }

    private byte[] getObjectByteArray() {
        byte[] data = SerializationUtils.serialize(record);
        return data;
    }

    private void pingServer() {    }

    /**
     * Initialize Logger instance
     *
     * @throws IOException
     */
    private void initLogger() throws IOException {
        logger = LoggerProvider.getLogger(serverInfo.getLocation());
    }
}