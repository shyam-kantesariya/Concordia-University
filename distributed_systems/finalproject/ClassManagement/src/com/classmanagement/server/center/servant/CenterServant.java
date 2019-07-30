package com.classmanagement.server.center.servant;

import com.classmanagement.configuration.Constants;
import com.classmanagement.configuration.LogMessages;
import com.classmanagement.exceptions.*;
import com.classmanagement.models.*;
import com.classmanagement.server.center.CenterServerInfo;
import com.classmanagement.server.corba.POA.CenterServerPOA;
import com.classmanagement.server.udp.unicast.UdpRequest;
import com.classmanagement.server.udp.unicast.UdpServer;
import com.classmanagement.server.udp.unicast.UdpTransferRecordRequest;
import com.classmanagement.utils.JournalLogger;
import com.classmanagement.utils.LoggerProvider;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.classmanagement.configuration.Constants.*;
import static java.lang.Character.isLetter;

/**
 * This class implements the RMI interface CenterServer
 */

public class CenterServant extends CenterServerPOA  implements  CenterServantInterface{
    public Location serverLocation;
    private Logger logger = null;
    private HashMap<String, List<RecordBase>> records;
    static HashMap<Location, CenterServerInfo> centralRepositoryOne = new HashMap<Location, CenterServerInfo>();
    static HashMap<Location, CenterServerInfo> centralRepositoryTwo = new HashMap<Location, CenterServerInfo>();
    static HashMap<Location, CenterServerInfo> centralRepositoryThree = new HashMap<Location, CenterServerInfo>();
    UdpServer udpServer;
    private Counter teacherIdCounter;
    private Counter studentIdCounter;
    private JournalLogger journalLogger;
    private List<String> lockRegister = new ArrayList<>();
    private String replicaName;

    public void shutDown() {
        udpServer.setShouldStop(true);
    }

    public CenterServant(Location loc, String replicaName) throws IOException {
        super();
        serverLocation = loc;
        this.replicaName = replicaName;
        initLogger(replicaName);
        records = new HashMap<String, List<RecordBase>>();
        udpServer = new UdpServer(this, replicaName);
        recordDetailsToCentralRepository();
        udpServer.start(); //Starts UDP server instance
//        teacherIdCounter = Counter.setCounterVal(CounterType.TEACHERID, serverLocation, logger);
//        studentIdCounter = Counter.setCounterVal(CounterType.STUDENTID, serverLocation, logger);
        teacherIdCounter = new Counter(0);
        studentIdCounter = new Counter(0);
        journalLogger = new JournalLogger(Constants.PROJECT_DIR + "\\" + Constants.BACKUP_DIR_NAME + "\\" +
                Constants.JOURNAL_DIR + "\\" + serverLocation.toString() + "\\" + replicaName + "_" +
                Constants.JOURNAL_FILE_NAME, serverLocation, replicaName);
    }

    private HashMap<Location, CenterServerInfo> getCentralRepository(){
        if(replicaName.equals(Constants.REPLICA_ONE)) {
            return centralRepositoryOne;
        } else if (replicaName.equals(Constants.REPLICA_TWO)) {
            return centralRepositoryTwo;
        } else {
            return centralRepositoryThree;
        }
    }

    public synchronized Integer getRecordCount() {
        //https://stackoverflow.com/questions/5496944/java-count-the-total-number-of-items-in-a-hashmapstring-arrayliststring
        return records.values().stream().mapToInt(List::size).sum();
    }

    private void recordDetailsToCentralRepository() {
        getCentralRepository().put(serverLocation, new CenterServerInfo(serverLocation, udpServer.getUdpPort(),
                udpServer.getInetAddress(), new Date()));
    }

    /*
     * Interface implementation block
     */


    /**
     * Creates a teacher record, adds it to the HashMap and returns the newly
     * created teacher record ID
     *
     * Adds the new record to the list of records present in HashMap
     * The first letter of lastname is used as the key and the record will be
     * inserted to list which corresponds to the key in the HashMap.
     *
     * @param firstName
     * @param lastName
     * @param address
     * @param phone
     * @param specialization
     * @param location
     * @return Teacher Record ID
     * @throws InvalidLastNameException when The first letter of the lastname is not an alphabet
     */
    public String createTRecord (String firstName, String lastName, String address, String phone, String specialization,
                                 String location, String managerId) throws InvalidLastNameException {
        String recordId = null;
        try {
            validate(lastName);
            recordId = TEACHER_RECORD_ID_PREFIX + String.format("%05d", teacherIdCounter.nextVal());
            Teacher teacher = new Teacher(recordId, firstName, lastName, address, phone, specialization,
                    Location.valueOf(location));
            this.addRecord(Character.toString(lastName.charAt(0)), teacher);
            //teacherIdCounter.serializeCounter(serverLocation, Constants.TEACHER_COUNTER_FILE_NAME);
            logger.log(Level.INFO, LogMessages.TEACHER_RECORD_CREATED, new String[] { recordId, managerId});
            journalLogger.log(teacher, JournalLogType.ADD_RECORD);
            return recordId;
        } catch (InvalidLastNameException e) {
            logger.log(Level.SEVERE, LogMessages.INVALID_LAST_NAME, lastName);
            throw e;
        } catch (InvalidJournalRecordType e) {
            logger.log(Level.WARNING, LogMessages.INVALID_JOURNAL_LOG_TYPE);
            return recordId;
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
            return recordId;
        } catch (Exception e) {
            logger.log(Level.SEVERE, LogMessages.RECORD_CREATION_FAILED);
            throw e;
        }
    }

    public String createSRecord (String firstName, String lastName, String coursesRegistered, String status,
                                 String statusDate, String managerId) throws InvalidLastNameException{
        String recordId = null;
        try {
            validate(lastName);
            recordId = STUDENT_RECORD_ID_PREFIX + String.format("%05d", studentIdCounter.nextVal());
            Student student = new Student(recordId, firstName, lastName, getCourseHashSet(coursesRegistered),
                    Status.valueOf(status), statusDate);
            this.addRecord(Character.toString(lastName.charAt(0)), student);
            //studentIdCounter.serializeCounter(serverLocation, Constants.STUDENT_COUNTER_FILE_NAME);
            logger.log(Level.INFO, LogMessages.STUDENT_RECORD_CREATED, new String[] { recordId, managerId+this.replicaName});
            journalLogger.log(student, JournalLogType.ADD_RECORD);
            return recordId;
        } catch (InvalidLastNameException e) {
            logger.log(Level.SEVERE, LogMessages.INVALID_LAST_NAME, lastName);
            throw e;
        } catch (InvalidJournalRecordType e) {
            logger.log(Level.WARNING, LogMessages.INVALID_JOURNAL_LOG_TYPE);
            return recordId;
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
            return recordId;
        } catch (Exception e) {
            logger.log(Level.SEVERE, LogMessages.RECORD_CREATION_FAILED);
            e.printStackTrace();
            throw e;
        }
    }

    public String getRecordCounts (String managerId){
        String recordCount = null;
        UdpRequest[] requestIssuedRef = new UdpRequest[Location.values().length - 1];
        int cntr = 0;
        for (Location lc : Location.values()) {
            if (lc == this.serverLocation) {
                recordCount = getCentralRepository().get(lc).getLocationDescription() + ": " +
                        this.getRecordCount();
            } else {
                try {
                    requestIssuedRef[cntr] = new UdpRequest(getCentralRepository().get(lc),
                            UdpRequestType.SEND_RECORD_COUNT, replicaName);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
                requestIssuedRef[cntr].start();
                cntr++;
            }
        }
        for (UdpRequest request : requestIssuedRef) {
            try {
                request.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            recordCount += " , " + request.getRemoteRecordCount().trim();
        }
        logger.log(Level.INFO, LogMessages.RECORD_COUNT, new String[]{recordCount, managerId});
        return recordCount;
    }

    public void editRecord (String recordId, String fieldName, String newValue, String managerId)
            throws FieldNotFoundException, RecordNotFoundException, InvalidValueException{
        try {
            RecordBase record = this.findRecord(recordId);
            record.edit(recordId, fieldName, newValue);
            journalLogger.log(recordId + Constants.FIELD_SEPARATOR + fieldName + Constants.FIELD_SEPARATOR +
                    newValue, JournalLogType.EDIT_RECORD);
            logger.log(Level.INFO, String.format(LogMessages.RECORD_EDITED, fieldName, newValue, recordId), managerId);
        } catch (FieldNotFoundException e) {
            logger.log(Level.SEVERE, LogMessages.FIELD_NOT_FOUND, fieldName);
            throw e;
        } catch (InvalidValueException e) {
            logger.log(Level.SEVERE, LogMessages.INVALID_VALUE, newValue);
            throw e;
        } catch (RecordNotFoundException e) {
            logger.log(Level.SEVERE, LogMessages.RECORD_NOT_FOUND, recordId);
            throw e;
        } catch (InvalidJournalRecordType e) {
            logger.log(Level.WARNING, LogMessages.INVALID_JOURNAL_LOG_TYPE);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, LogMessages.RECORD_EDITING_FAILED);
            throw e;
        }
    }

    public synchronized boolean acquireLock(String recordId) {
        long threadId =  Thread.currentThread().getId();
        String recordThread = recordId + threadId;
        if(lockRegister.stream().anyMatch(x -> x.startsWith(recordId))){
            return false;
        }
        else {
            lockRegister.add(recordThread);
            return true;
        }
    }

    public synchronized void releaseLock(String recordId) {
        long threadId =  Thread.currentThread().getId();
        recordId  += threadId;
        if(lockRegister.contains(recordId)){
            lockRegister.remove(recordId);
        }
    }

    public String transferRecord (String managerID, String recordID, String remoteCenterServerName)
            throws RecordNotFoundException, RecordBeingTransferredException{
        try {
            if (!acquireLock(recordID)){
                throw new RecordBeingTransferredException(String.format("Transfer for the record ID %s is already in progress",
                        recordID));
            }
            RecordBase record = this.findRecord(recordID);
            UdpTransferRecordRequest request = null;
            for (Location lc : Location.values()) {
                if (lc == Location.valueOf(remoteCenterServerName)) {
                    try {
                        String data = TRANSFER_RECORD_PREFIX + managerID + MANAGER_ID_DELIMITER + record.serialize();
                        request = new UdpTransferRecordRequest(getCentralRepository().get(lc), data, replicaName);
                        request.start();
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, LogMessages.RECORD_TRANSFER_FAILED);
                        logger.log(Level.SEVERE, e.getMessage());
                    }
                }
            }
            try {
                request.join();
                String reply = request.getResult().trim();
                if(reply.contains("Passed")) {
                    deleteRecord(record);
                    reply = reply.replace("Passed","");
                    logger.log(Level.INFO, LogMessages.RECORD_TRANSFERED, new String[] {recordID, this.serverLocation.toString(), remoteCenterServerName, reply, managerID});
                    return reply;
                }
                else {
                    logger.log(Level.SEVERE, LogMessages.RECORD_TRANSFER_FAILED);
                }
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, LogMessages.RECORD_TRANSFER_FAILED);
            }
        }
        catch (RecordNotFoundException e){
            logger.log(Level.SEVERE, LogMessages.RECORD_NOT_PRESENT, recordID);
            throw e;
        }
        catch (RecordBeingTransferredException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        finally {
            releaseLock(recordID);
        }
        return "";
    }
    /**
     * Returns the information of a record
     * @param recordId
     * @return Info about the Record
     * @throws RecordNotFoundException if the record is not found
     */
    public String show(String recordId) throws RecordNotFoundException {
        try {
            RecordBase record = this.findRecord(recordId);
            return record.toString();
        } catch (RecordNotFoundException e) {
            logger.log(Level.SEVERE, "Record Not found", recordId);
            throw e;
        }
    }

    private void initLogger(String replicaName) throws IOException {
        logger = LoggerProvider.getLogger(serverLocation, replicaName);
    }

    /**
     * Tries to find and return the record based on the recordID.
     *
     * This method is synchronised since different threads will try to access the HashMap
     * at the ssame time
     *
     * @param recordId
     * @return
     * @throws RecordNotFoundException If the record is not present in the HashMap
     */
    private synchronized RecordBase findRecord(String recordId) throws RecordNotFoundException {
        for (Map.Entry<String, List<RecordBase>> recordPair : records.entrySet()) {
            List<RecordBase> recordList = recordPair.getValue();
            if (recordList.size() > 0) {
                Optional<RecordBase> record = recordList.stream().filter(o -> o.getRecordId().equals(recordId)).findFirst();
                if (record.isPresent()) {
                    return record.get();
                }
            }
        }
        throw new RecordNotFoundException();
    }

    private boolean validate(String lastName) throws InvalidLastNameException {
        if (lastName != null && !lastName.isEmpty()) {
            char key = lastName.charAt(0);
            if (isLetter(key)) {
                return true;
            } else {
                throw new InvalidLastNameException();
            }
        }
        throw new InvalidLastNameException();
    }

    /**
     * Adds the record to the HashMap.
     * The record will be inserted to list which corresponds to the key in the
     * HashMap.
     *
     * This method is synchronised since different threads will try to access the HashMap
     * and edit the HashMap at the same time
     *
     * @param key
     * @param record
     */
    private synchronized void addRecord(String key, RecordBase record) {
        List<RecordBase> recordList = this.records.get(key.toUpperCase());
        if (recordList != null) {
            recordList.add(record);
        } else {
            recordList = new ArrayList<>();
            recordList.add(record);
            this.records.put(key.toUpperCase(), recordList);
        }
    }


    private synchronized void deleteRecord(RecordBase record) {
        String key = Character.toString(record.getLastName().charAt(0));
        List<RecordBase> recordList = this.records.get(key.toUpperCase());
        if (recordList != null) {
            if(recordList.size() > 0) {
                recordList.remove(record);
                if(recordList.size() == 0) {
                    this.records.remove(key);
                }
            }
        }
    }

    public String transfer(String data){
        try {
            data  = data.trim();
            String[] datas = data.split(MANAGER_ID_DELIMITER);
            String recordData  = datas[1];
            String managerId = datas[0] + "(Transfer Request)";
            if (recordData.startsWith(STUDENT_RECORD_IDENTIFIER)) {
                recordData = recordData.replace(STUDENT_RECORD_IDENTIFIER, "");
                String[] studentDetails = recordData.split(",");
                String courses = "," + studentDetails[2].replace(";",",");
                String recordId = this.createSRecord(studentDetails[0], studentDetails[1], courses , studentDetails[3], studentDetails[4],managerId);
                return "Passed" + recordId;
            }
            if(recordData.startsWith(TEACHER_RECORD_IDENTIFIER)) {
                recordData = recordData.replace(TEACHER_RECORD_IDENTIFIER, "");
                String[] teacherDetails = recordData.split(",");
                String recordId = this.createTRecord(teacherDetails[0], teacherDetails[1], teacherDetails[2], teacherDetails[3] ,teacherDetails[4], teacherDetails[5],managerId);
                return "Passed" + recordId;
            }
        }
        catch (Exception e){
            return "Failed";
        }
        return "Failed";
    }

    private HashSet<String> getCourseHashSet(String courseList){
        HashSet<String> courses = new HashSet<>(Arrays.asList(courseList.trim().substring(1).split(",")));
        return courses;
    }
}