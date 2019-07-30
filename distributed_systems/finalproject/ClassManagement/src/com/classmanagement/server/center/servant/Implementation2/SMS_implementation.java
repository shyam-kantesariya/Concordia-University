package com.classmanagement.server.center.servant.Implementation2;


import com.classmanagement.exceptions.InvalidJournalRecordType;
import com.classmanagement.server.center.servant.CenterServantInterface;
import com.classmanagement.server.center.servant.Implementation2.configuration.Constants;
import com.classmanagement.server.center.servant.Implementation2.utils.*;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class implements the RMI interface Implementation
 */
@SuppressWarnings("serial")
public class SMS_implementation implements CenterServantInterface {
    private static Logger logger;
    HashMap<String, List<Record>> map = new HashMap<String, List<Record>>();
    private int Mport = 6780;
    private int Lport = 6781;
    private int Dport = 6782;
    private String location;
    private int counter_strec = 0;
    private int counter_trec = 0;
    private String replicaName;
    private UDPreply r = null;
    public Thread ser;

    public SMS_implementation(String location, String replicaName) throws IOException {
        super();
        this.replicaName = replicaName;
        this.location = location.toString();
        // TODO Auto-generated constructor stub

        JournalLogger journal;
        if (this.location == "MTL") {
            r = new UDPreply(this, Mport);
        } else if (this.location == "LVL") {
            r = new UDPreply(this, Lport);

        } else if (this.location == "DDO") {
            r = new UDPreply(this, Dport);

        } else {
            System.out.println("Unknown");
        }
        ser = new Thread(r);

        ser.start();
        System.out.println("Thread " + ser.getId() + " for " + location);
    }

    /**
     * Creates a teacher record and inserts into HashMap
     * <p>
     * Adds the new record to the list of records present in HashMap The first
     * letter of lastName is used as the key and rest is used as a value in
     * HashMap
     *
     * @param managerId
     * @param firstName
     * @param lastName
     * @param address
     * @param phone
     * @param specialization
     * @param location
     * @throws IOException
     * @throws InvalidJournalRecordType
     * @throws RemoteException          We are using managerId to add it in the log and wont be
     *                                  inserted into HashMap
     */

    public String createTRecord(String firstName, String lastName, String address, String phone,
                                String specialization, String location, String managerId) throws IOException, InvalidJournalRecordType {
        // TODO Auto-generated method stub
        String sequnce_t = null;
        try {
            setLogger();
            JournalLogger journalLogger = new JournalLogger(Constants.PROJECT_DIR + "\\" + Constants.BACKUP_DIR_NAME + "\\"
                    + Constants.JOURNAL_DIR + "\\" + this.location.toString() + "\\" + replicaName + "_"
                    + Constants.JOURNAL_FILE_NAME, this.location);
            counter_trec = counter_trec + 1;
            List<Record> records = new ArrayList<Record>();
            sequnce_t = String.format("TR0%04d", counter_trec);
            System.out.println(counter_trec);
            TeacherRecord teacher = new TeacherRecord(sequnce_t, firstName, lastName, address, phone, specialization,
                    location);
            String key = lastName.substring(0, 1);
            logger.info("Checking whether to add the teacher list to existing key or to a new key\t" + managerId);
            List<Record> recordList = map.get(key);
            if (recordList != null) {
                recordList.add(teacher);
            } else {
                records.add(teacher);
                map.put(key, records);
            }
            journalLogger.log(teacher, JournalLogType.ADD_RECORD);
            logger.info("Teacher Record inserted\t" + managerId);
        } catch (final Exception err) {
            err.printStackTrace();
        }
        return sequnce_t;
    }

    /**
     * Creates a Student record and inserts into HashMap
     * <p>
     * Adds the new record to the list of records present in HashMap The first
     * letter of lastName is used as the key and rest is used as a value in
     * HashMap
     *
     * @param managerId
     * @param firstName
     * @param lastName
     * @param coursesRegistered
     * @param status
     * @param statusDate
     * @throws IOException
     * @throws InvalidJournalRecordType
     * @throws RemoteException          We are using managerId to add it in the log and wont be
     *                                  inserted into HashMap
     */

    public synchronized String createSRecord(String firstName, String lastName, String coursesRegistered,
                                             String status, String statusDate, String managerId) throws InvalidJournalRecordType, IOException {
        String sequnce_st = null;
        try {
            JournalLogger journalLogger = new JournalLogger(
                    Constants.PROJECT_DIR + "\\" + Constants.BACKUP_DIR_NAME + "\\" + Constants.JOURNAL_DIR + "\\"
                            + this.location.toString() + "\\" + replicaName + "_" + Constants.JOURNAL_FILE_NAME, this.location);
            setLogger();
            counter_strec = counter_strec + 1;
            sequnce_st = String.format("SR0%04d", counter_strec);
            //System.out.println(sequence + " " + coursesRegistered);
            StudentRecord student = new StudentRecord(sequnce_st, firstName, lastName,
                    getCourseHashSet(coursesRegistered), status, statusDate);
            String key = lastName.substring(0, 1);
            logger.info("Checking whether to add the student list to existing key or to a new key\t" + managerId);
            List<Record> recordList = map.get(key);
            if (recordList != null) {
                recordList.add(student);
            } else {
                List<Record> records = new ArrayList<Record>();
                records.add(student);
                map.put(key, records);
            }
            System.out.println("Created Student record with id " + sequnce_st);
            journalLogger.log(student, JournalLogType.ADD_RECORD);
            logger.info("Student Record inserted\t" + managerId);
        } catch (final Exception err) {
            err.printStackTrace();
            System.out.println("Caught exception in student creation: " + err.getMessage());
        }
        return sequnce_st;
    }

    public synchronized void editRecord(String recordID, String fieldName, String newValue, String managerId)
            throws RemoteException, IOException, InvalidJournalRecordType {
        // TODO Auto-generated method stub
        try {
            JournalLogger journalLogger = new JournalLogger(
                    Constants.PROJECT_DIR + "\\" + Constants.BACKUP_DIR_NAME + "\\" + Constants.JOURNAL_DIR + "\\"
                            + this.location.toString() + "\\" + Constants.JOURNAL_FILE_NAME,
                    this.location);
            setLogger();
            for (Entry<String, List<Record>> entry : map.entrySet()) {
                System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue().toString());
            }
            logger.info("Updating the records\t" + managerId);
            for (Entry<String, List<Record>> entry : map.entrySet()) {
                List<Record> lst = entry.getValue();
                Optional<Record> r = lst.stream().filter(x -> x.getRecordID().equals(recordID)).findFirst();

                if (r.isPresent() && fieldName.toLowerCase().equals("phone")) {
                    ((TeacherRecord) r.get()).setPhone(newValue);
                    journalLogger.log(
                            recordID + ":" + fieldName + ":" + newValue,
                            JournalLogType.EDIT_RECORD);
                    journalLogger.log(
                            recordID + ":" + fieldName + ":" + newValue,
                            JournalLogType.EDIT_RECORD);
                    logger.info("Updated the records\t" + managerId);
                    break;
                } else if (r.isPresent() && fieldName.toLowerCase().equals("address")) {
                    ((TeacherRecord) r.get()).setAddress(newValue);
                    journalLogger.log(
                            recordID + ":" + fieldName + ":" + newValue,
                            JournalLogType.EDIT_RECORD);
                    logger.info("Updated the records\t" + managerId);
                    break;
                } else if (r.isPresent() && fieldName.equals("location")) {
                    ((TeacherRecord) r.get()).setLocation(newValue);
                    journalLogger.log(
                            recordID + ":" + fieldName + ":" + newValue,
                            JournalLogType.EDIT_RECORD);
                    logger.info("Updated the records\t" + managerId);
                    break;
                } else if (r.isPresent() && fieldName.equals("coursesregistered")) {
                    String[] courses = newValue.split(",");
                    ((StudentRecord) r.get()).setCourseRegistered(new HashSet<>(Arrays.asList(courses)));
                    journalLogger.log(
                            recordID + ":" + fieldName + ":" + newValue,
                            JournalLogType.EDIT_RECORD);
                    logger.info("Updated the records\t" + managerId);
                    break;
                } else if (r.isPresent() && fieldName.toLowerCase().equals("status")) {
                    ((StudentRecord) r.get()).setStatus(newValue);
                    journalLogger.log(
                            recordID + ":" + fieldName + ":" + newValue,
                            JournalLogType.EDIT_RECORD);
                    logger.info("Updated the records\t" + managerId);
                    break;
                } else if (r.isPresent() && fieldName.equals("statusdate")) {
                    ((StudentRecord) r.get()).setStatusDate(newValue);
                    journalLogger.log(
                            recordID + ":" + fieldName + ":" + newValue,
                            JournalLogType.EDIT_RECORD);
                    logger.info("Updated the records\t" + managerId);
                break;
                } else {
                    logger.info("Record not found\t" + managerId);
                    System.out.println("Record not found");
                }

            }
            for (Entry<String, List<Record>> entry : map.entrySet()) {
                System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue().toString());
            }
        } catch (final Exception err) {
            err.printStackTrace();
        }

    }

    public int getCurrentCount() {
        setLogger();
        logger.info("calling the server count function using UDP\t");
        int i = 0;
        for (Entry<String, List<Record>> entry : map.entrySet()) {
            List<Record> lst = entry.getValue();
            long l = lst.stream().distinct().count();
            i = (int) (i + l);

        }
        return i;

    }

    /**
     * @return Get the server record count in a arraylist Initiates UDP requests
     * to other servers and gets the record count from other servers.
     * @throws RemoteException
     */

    public synchronized String getRecordCounts(String managerID) {
        this.setLogger();
        logger.info("getting the current server count function\t");
        int i = 0;
        String[] record_count = new String[6];
        for (Entry<String, List<Record>> entry : map.entrySet()) {
            List<Record> lst = entry.getValue();
            long l = lst.stream().distinct().count();
            i = (int) (i + l);
        }
        record_count[0] = this.location;
        record_count[1] = Integer.toString(i);
        System.out.println(this.location + " record count:" + i);
        String w;
        String mynewstring;
        if (this.location.equals("MTL")) {
            logger.info("Requesting the other servers count using UDP\t");
            w = this.raiseRequest(this.Lport, "LVL");
            mynewstring = this.raiseRequest(this.Dport, "DDO");
            record_count[2] = "LVL";
            record_count[3] = w.trim();
            record_count[4] = "DDO";
            record_count[5] = mynewstring.trim();
        } else if (this.location.equals("LVL")) {
            logger.info("Requesting the other servers count using UDP\t");
            w = this.raiseRequest(this.Mport, "MTL");
            mynewstring = this.raiseRequest(this.Dport, "DDO");
            record_count[2] = "MTL";
            record_count[3] = w.trim();
            record_count[4] = "DDO";
            record_count[5] = mynewstring.trim();
        } else if (this.location.equals("DDO")) {
            logger.info("Requesting the other servers count using UDP\t");
            w = this.raiseRequest(this.Lport, "LVL");
            mynewstring = this.raiseRequest(this.Mport, "MTL");
            record_count[2] = "LVL";
            record_count[3] = w.trim();
            record_count[4] = "MTL";
            record_count[5] = mynewstring.trim();
        } else {
            logger.info("unknown error while requesting the other servers count using UDP\t");
            System.out.println("Unknown");
        }

        //System.out.println("before stringbuffer " + new String(record_count.toString()));
        StringBuffer result = new StringBuffer();

        for (int j = 0; j < record_count.length; ++j) {
            result.append(record_count[j]);
        }

        mynewstring = result.toString();
        return mynewstring;
    }

    public synchronized String transferRecord(String ManagerID, String recordID, String ServerName) {
        setLogger();
        String status;
        String Status = null;
        try {
            JournalLogger journalLogger = new JournalLogger(
                    Constants.PROJECT_DIR + "\\" + Constants.BACKUP_DIR_NAME + "\\" + Constants.JOURNAL_DIR + "\\"
                            + this.location.toString() + "\\" + Constants.JOURNAL_FILE_NAME,
                    this.location);
            logger.info("Initiated the record Transfer by " + ManagerID + " to the server " + ServerName);
            for (Entry<String, List<Record>> entry : map.entrySet()) {
                List<Record> lst = entry.getValue();
                logger.info("Searching for Record using" + recordID);
                Optional<Record> r = lst.stream().filter(x -> x.getRecordID().equals(recordID)).findFirst();
                logger.info("Checking for the record existance and validation of server name");
                if (r.isPresent() && ServerName.equals("LVL")) {
                    logger.info("Calling the UDP client Program)");
                    status = TransferRequest(Lport, this.location, r.get().serialize().getBytes());
                    logger.info("Checking for server return state and initiating record removal from original HashMap");
                    if (status.trim().equals("true") && lst.stream().distinct().count() > 1) {
                        lst.remove(r.get());
                        logger.info("Record Deleted from hashMap");
                        Status = "true";
                    } else if (status.trim().equals("true") && lst.stream().distinct().count() == 1) {
                        logger.info("Both Key and Value is deleted from HashMap");
                        map.remove(entry.getKey());
                        Status = "true";
                    } else {
                        logger.info("Record transfer Failed");
                        System.out.println("error");
                        Status = "false";
                    }
                } else if (r.isPresent() && ServerName.equals("MTL")) {
                    logger.info("Calling the UDP client Program)");
                    status = TransferRequest(Mport, this.location, r.get().serialize().getBytes());
                    logger.info("Checking for server return state and initiating record removal from original HashMap");
                    if (status.trim().equals("true") && lst.stream().distinct().count() > 1) {
                        lst.remove(r.get());
                        logger.info("Record Deleted from hashMap");
                        Status = "true";
                    } else if (status.trim().equals("true") && lst.stream().distinct().count() == 1) {
                        logger.info("Both Key and Value is deleted from HashMap");
                        map.remove(entry.getKey());
                        Status = "true";
                    } else {
                        logger.info("Record transfer Failed");
                        System.out.println("error");
                        Status = "false";
                    }
                } else if (r.isPresent() && ServerName.equals("DDO")) {
                    logger.info("Calling the UDP client Program)");
                    status = TransferRequest(Dport, this.location, r.get().serialize().getBytes());
                    logger.info("Checking for server return state and initiating record removal from original HashMap");
                    if (status.trim().equals("true") && lst.stream().distinct().count() > 1) {
                        lst.remove(r.get());
                        logger.info("Record Deleted from hashMap");
                        Status = "true";
                    } else if (status.trim().equals("true") && lst.stream().distinct().count() == 1) {
                        logger.info("Both Key and Value is deleted from HashMap");
                        map.remove(entry.getKey());
                        Status = "true";
                    } else {
                        logger.info("Record transfer Failed");
                        System.out.println("error");
                        Status = "false";
                    }
                } else {
                    System.out.println("Invalid recordID");
                    Status = "false";
                }
                if (Status.equals("true")) {
                    journalLogger.log(
                            recordID + Constants.FIELD_SEPARATOR + ManagerID,
                            JournalLogType.DELETE_RECORD);
                }
            }
        } catch (final Exception err) {
            err.printStackTrace();
        }

        return Status;

    }

    /**
     * @return the individual Server Record count Initiates UDP requests to
     * other servers and gets the record count from other server.
     * @throws RemoteException
     */

    private String raiseRequest(int port, String serverloc) {
        setLogger();
        String Count = null;
        logger.info("calling the UDP client program\t");
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            byte[] m = new byte[1000];
            InetAddress aHost = InetAddress.getByName("localhost");
            int serverPort = port;
            DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            Count = new String(reply.getData());
            logger.info("getting the other servers count using UDP reply packet\t");
            System.out.println(serverloc + " record count:" + new String(reply.getData()));

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
        return Count;
    }

    private String TransferRequest(int port, String serverloc, byte[] Value) {
        setLogger();
        String status = null;
        logger.info("calling the UDP client program\t");
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            byte[] m = Value;
            InetAddress aHost = InetAddress.getByName("localhost");
            int serverPort = port;
            logger.info("Creating the datagram packet with data in terms of Byte Stream ");
            DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            logger.info("Server Response for transfer request");
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            status = new String(reply.getData());
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
        return status;
    }

    /**
     * Called by UDP Server by passing Byte stream as a parameter to insert the
     * record into the server
     *
     * @return status of the request to the UDP server.
     * @throws InvalidJournalRecordType
     */

    public String ServerTransfer(byte[] Val) throws IOException, ClassNotFoundException, InvalidJournalRecordType {
        setLogger();
        logger.info("Calling the server transfer function");
        logger.info("Converting byte stream to String array");
        String recordData = new String(Val);
        logger.info("Checking the record Type");
        if (recordData.startsWith("Teacher")) {
            recordData = recordData.replaceAll("Teacher", "");
            recordData = recordData.trim();
            String[] recovered_data = recordData.split(",");
            logger.info("Calling the create Teacher record function)");
            String tRecordId = createTRecord(recovered_data[1], recovered_data[2], recovered_data[3],
                    recovered_data[4], recovered_data[5], recovered_data[6], "MTL0000");
//            for (Entry<String, List<Record>> entry : map.entrySet()) {
//                System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue().toString());
//            }
            return "true";
        } else if (recordData.startsWith("Student")) {
            recordData = recordData.replaceAll("Student", "");
            recordData = recordData.trim();
            System.out.println(recordData);
            String[] recovered_data = recordData.split("\\]?,(?!\\s)\\[?");
            System.out.println(recovered_data[3] + recovered_data[4]);
            logger.info("Calling the Student creation function");
            String sRecordId = createSRecord(recovered_data[1],
                    recovered_data[2], recovered_data[3],
                    recovered_data[4], recovered_data[5], "MTL0000");
//            for (Entry<String, List<Record>> entry : map.entrySet()) {
//                System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue().toString());
//
//            }
            return "true";
        } else {
            System.out.println("Record not inserted");
            return "false";
        }

    }

    public synchronized void delete(String recordID) {
        for (Entry<String, List<Record>> entry : map.entrySet()) {
            List<Record> lst = entry.getValue();
            Optional<Record> r = lst.stream().filter(x -> x.getRecordID().equals(recordID)).findFirst();
            if (r.isPresent() && lst.stream().distinct().count() > 1) {
                lst.remove(r.get());;
                break;
            } else if (r.isPresent() && lst.stream().distinct().count() == 1) {
                map.remove(entry.getKey());
                break;
            }
        }
    }

    private HashSet<String> getCourseHashSet(String courseList) {
        HashSet<String> courses = new HashSet<>(Arrays.asList(courseList.trim().substring(0).split(",")));
        return courses;
    }

    public void shutDown() {
        r.setShouldStop(true);
        System.out.println("I am stopping thread " + ser.getId());
        ser.interrupt();
    }

    /**
     * initiates the logger function
     */
    private void setLogger() {
        // TODO Auto-generated method stub
        String logFile = "D:\\Distributed Systems\\dist_syst_finalproject\\LOGS\\" + this.location.toString() + "\\" +
                replicaName+ "_logger.txt";
        logger = Logger.getLogger(logFile);
        if (logger.getHandlers().length == 0) {
            FileHandler fileHandler = null;
            try {
                fileHandler = new FileHandler(logFile, true);
                SimpleFormatter textFormatter = new SimpleFormatter();
                fileHandler.setFormatter(textFormatter);
                logger.addHandler(fileHandler);
                logger.setUseParentHandlers(false);
                logger.setLevel(Level.INFO);
            } catch (SecurityException | IOException e) {
                System.out.println("Logger initialization error. Check File Permissions!!!");
                e.printStackTrace();
            } finally {
            }
        }
    }
}