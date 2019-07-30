package com.classmanagement.server.center;

import com.classmanagement.configuration.Constants;
import com.classmanagement.configuration.LogMessages;
import com.classmanagement.exceptions.*;
import com.classmanagement.interfaces.CenterServer;
import com.classmanagement.models.*;
import com.classmanagement.server.udp.UdpRequest;
import com.classmanagement.server.udp.UdpServer;
import com.classmanagement.utils.FileManager;
import com.classmanagement.utils.JournalLogger;
import com.classmanagement.utils.LoggerProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.classmanagement.configuration.Constants.STUDENT_RECORD_ID_PREFIX;
import static com.classmanagement.configuration.Constants.TEACHER_RECORD_ID_PREFIX;
import static java.lang.Character.isLetter;

/**
 * This class implements the RMI interface CenterServer
 */
public class CenterServerImp extends UnicastRemoteObject implements CenterServer {

    public Location serverLocation;
    private Logger logger = null;
    private HashMap<String, List<RecordBase>> records;
    static HashMap<Location, CenterServerInfo> centralRepository = new HashMap<Location, CenterServerInfo>();
    UdpServer udpServer;
    private Counter teacherIdCounter;
    private Counter studentIdCounter;
    private JournalLogger journalLogger;

    public CenterServerImp(Location loc) throws RemoteException, IOException {
        super();
        serverLocation = loc;
        initLogger();
        records = new HashMap<String, List<RecordBase>>();
        udpServer = new UdpServer(this);
        recordDetailsToCentralRepository();
        udpServer.start(); //Starts UDP server instance
        teacherIdCounter = setCounterVal(RecordType.TEACHER);
        studentIdCounter = setCounterVal(RecordType.STUDENT);
        journalLogger = new JournalLogger(Constants.PROJECT_DIR + "\\" + Constants.BACKUP_DIR_NAME + "\\" +
                Constants.JOURNAL_DIR + "\\" + serverLocation.toString() + "\\" + Constants.JOURNAL_FILE_NAME,
                serverLocation);
    }

    public synchronized Integer getRecordCount() {
        //https://stackoverflow.com/questions/5496944/java-count-the-total-number-of-items-in-a-hashmapstring-arrayliststring
        return records.values().stream().mapToInt(List::size).sum();
    }

    private Counter setCounterVal(RecordType recordType) {
        Counter counter = null;
        String dir = Constants.PROJECT_DIR + "\\" + Constants.BACKUP_DIR_NAME + "\\" + Constants.COUNTER_DIR_NAME + "\\" +
                serverLocation.toString().trim() + "\\";
        try {
            switch (recordType) {
                case STUDENT:
                    counter = (Counter) FileManager.ReadFile(dir + Constants.STUDENT_COUNTER_FILE_NAME);
                    break;
                case TEACHER:
                    counter = (Counter) FileManager.ReadFile(dir + Constants.TEACHER_COUNTER_FILE_NAME);
                    break;
                case MANAGER:
                    counter = (Counter) FileManager.ReadFile(dir + Constants.TEACHER_COUNTER_FILE_NAME);
                    break;
                default:
                    throw new InvalidRecordType();
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } catch (InvalidRecordType e) {
            logger.log(Level.SEVERE, e.getMessage(recordType.toString()));
        } finally {
            if (counter == null) {
                counter = new Counter();
            }
        }
        return counter;
    }


    private void recordDetailsToCentralRepository() {
        CenterServerImp.centralRepository.put(serverLocation, new CenterServerInfo(serverLocation, udpServer.getUdpPort(),
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
     * @throws RemoteException
     * @throws InvalidLastNameException when The first letter of the lastname is not an alphabet
     */
    public String createTRecord(String firstName, String lastName, String address, String phone, String specialization, Location location) throws InvalidLastNameException {
        String recordId = null;
        try {
            validate(lastName);
            recordId = TEACHER_RECORD_ID_PREFIX + String.format("%05d", teacherIdCounter.nextVal());
            Teacher teacher = new Teacher(recordId, firstName, lastName, address, phone, specialization, location);
            this.addRecord(Character.toString(lastName.charAt(0)), teacher);
            teacherIdCounter.serializeCounter(serverLocation, Constants.TEACHER_COUNTER_FILE_NAME);
            logger.log(Level.INFO, LogMessages.TEACHER_RECORD_CREATED, recordId);
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

    /**
     * Creates a students record, adds it to the HashMap and returns the
     * newly created student record ID
     *
     * Adds the new record to the list of records present in HashMap
     * The first letter of lastname is used as the key and the record will be
     * inserted to list which corresponds to the key in the HashMap.
     *
     * @param firstName
     * @param lastName
     * @param coursesRegistered
     * @param status
     * @param statusDate
     * @return Teacher Record ID
     * @throws RemoteException
     * @throws InvalidLastNameException when The first letter of the lastname is not an alphabet
     */
    public String createSRecord(String firstName, String lastName, HashSet<String> coursesRegistered, Status status, String statusDate) throws InvalidLastNameException {
        String recordId = null;
        try {
            validate(lastName);
            recordId = STUDENT_RECORD_ID_PREFIX + String.format("%05d", studentIdCounter.nextVal());
            Student student = new Student(recordId, firstName, lastName, coursesRegistered, status, statusDate);
            this.addRecord(Character.toString(lastName.charAt(0)), student);
            studentIdCounter.serializeCounter(serverLocation, Constants.STUDENT_COUNTER_FILE_NAME);
            logger.log(Level.INFO, LogMessages.STUDENT_RECORD_CREATED, recordId);
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


    /**
     * @return Get the total record count
     * Initiates UDP requests to other servers and gets the record count from other servers and
     * return the collective count of records.
     * @throws RemoteException
     */
    public String getRecordCounts() {
        String recordCount = null;
        UdpRequest[] requestIssuedRef = new UdpRequest[Location.values().length - 1];
        int cntr = 0;
        for (Location lc : Location.values()) {
            if (lc == this.serverLocation) {
                recordCount = CenterServerImp.centralRepository.get(lc).getLocationDescription() + ": " +
                        this.getRecordCount();
            } else {
                try {
                    requestIssuedRef[cntr] = new UdpRequest(CenterServerImp.centralRepository.get(lc));
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
        logger.log(Level.INFO, LogMessages.RECORD_COUNT, recordCount);
        return recordCount;
    }

    /**
     * Edit an existing recordID by providing property name and property value
     *
     *
     * @param recordId
     * @param fieldName
     * @param newValue
     * @throws RemoteException
     * @throws FieldNotFoundException if the input field name is not valid
     * @throws RecordNotFoundException if the record is not found
     * @throws InvalidValueException if the provided value is not valid (Non type castable)
     */
    public void editRecord(String recordId, String fieldName, String newValue) throws RemoteException, FieldNotFoundException, RecordNotFoundException, InvalidValueException {
        try {
            RecordBase record = this.findRecord(recordId);
            record.edit(recordId, fieldName, newValue);
            journalLogger.log(recordId + Constants.FIELD_SEPARATOR + fieldName + Constants.FIELD_SEPARATOR +
                    newValue, JournalLogType.EDIT_RECORD);
            logger.log(Level.INFO, String.format(LogMessages.RECORD_EDITED, fieldName, newValue, recordId));
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

    /**
     * Returns the information of a record
     * @param recordId
     * @return Info about the Record
     * @throws RemoteException
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

    private void initLogger() throws IOException {
        /*logger = Logger.getLogger(CenterServerImp.class.getName() + this.serverLocation);
        Path path = Paths.get(Constants.PROJECT_DIR, Constants.LOG_DIR_NAME,
                this.serverLocation.toString(), "logger.log");
        FileHandler fileHandler = new FileHandler(path.toString(), true);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.INFO);*/
        logger = LoggerProvider.getLogger(serverLocation);
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
}