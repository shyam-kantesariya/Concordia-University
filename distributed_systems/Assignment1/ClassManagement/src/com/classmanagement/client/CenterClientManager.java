package com.classmanagement.client;

import com.classmanagement.configuration.LogMessages;
import com.classmanagement.configuration.Constants;
import com.classmanagement.exceptions.*;
import com.classmanagement.interfaces.CenterServer;
import com.classmanagement.models.Location;
import com.classmanagement.models.Status;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashSet;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Manages all the RMI client - server transactions.
 *
 * Logs the different actions (Addding/Editing/Viewing/Getting Count) done by the
 * Manager in a separate log file
 *
 * Exceptions will also be logged.
 *
 */
public class CenterClientManager {

    private Logger logger = null;
    private String managerId;
    CenterServer server = null;
    public String serverLocation = "";

    public CenterClientManager(String managerId) throws Exception {
        this.managerId = managerId;
        this.server = GetServer(managerId);
        this.initLogger();
    }

    private CenterServer GetServer(String managerId) throws Exception {
        Registry registry = LocateRegistry.getRegistry(Constants.REGISTRY_PORT);
        if(managerId.startsWith("MTL")){
            this.serverLocation = "Montreal Server";
            return (CenterServer) registry.lookup("MontrealServer");
        }
        else if(managerId.startsWith("LVL")){
            this.serverLocation = "Laval Server";
            return  (CenterServer) registry.lookup("LavalServer");
        }
        else if(managerId.startsWith("DDO")){
            this.serverLocation = "DDO Server";
            return  (CenterServer) registry.lookup("DDOServer");
        }
        else {
            throw new ManagerInvalidException();
        }
    }

    private void initLogger() throws IOException {
        logger = Logger.getLogger(CenterClientManager.class.getName() + this.managerId);
        Path path = Paths.get(Constants.PROJECT_DIR,Constants.BACKUP_DIR_NAME, "Client", this.managerId+".log");
        FileHandler fileHandler = new FileHandler(path.toString(), true);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.INFO);
    }


    /**
     * Creates a teacher record by invoking the remote method.
     *
     * Logs exceptions if any in the corresponding manager file.
     *
     * @param firstName
     * @param lastName
     * @param address
     * @param phone
     * @param specialization
     * @param location
     * @return Teacher Record ID
     */

    public void createTRecord(String firstName, String lastName, String address, String phone, String specialization, Location location){
        try {
            String recordId = server.createTRecord(firstName, lastName, address, phone, specialization, location);
            logger.log(Level.INFO, LogMessages.TEACHER_RECORD_CREATED, recordId);
        }
        catch (InvalidLastNameException e){
            logger.log(Level.SEVERE, LogMessages.INVALID_LAST_NAME, lastName);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, LogMessages.RECORD_CREATION_FAILED);
        }
    }

    /**
     * Creates a Student Record by invoking the remote method
     *
     * Logs thr exception if any in the corresponding Managers log file
     *
     * @param firstName
     * @param lastName
     * @param coursesRegistered
     * @param status
     * @param statusDate
     */
    public void createSRecord(String firstName, String lastName, HashSet<String> coursesRegistered, Status status, String statusDate){
        try {
            String recordId = server.createSRecord(firstName, lastName, coursesRegistered, status, statusDate);
            logger.log(Level.INFO, LogMessages.STUDENT_RECORD_CREATED, recordId);
        }
        catch (InvalidLastNameException e){
            logger.log(Level.SEVERE, LogMessages.INVALID_LAST_NAME, lastName);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, LogMessages.RECORD_CREATION_FAILED);
            e.printStackTrace();
        }
    }

    /**
     * Gets the record count by invoking the remote method
     *
     * Logs exception if any in the corresponding manager's file
     *
     * @return Record Count
     */
    public String getRecordCounts() {
        try {
            String recordCount = server.getRecordCounts();
            logger.log(Level.INFO, LogMessages.RECORD_COUNT, recordCount);
            return recordCount;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, LogMessages.RECORD_COUNT_FAILED);
        }
        return "";
    }

    /**
     * Edit a Student/Teacher record by taking recordID as Parameter
     *
     * Logs exceptions if any in the corresponding manager's file
     *
     * @param recordID
     * @param fieldName
     * @param newValue
     */
    public void editRecord(String recordID, String fieldName, String newValue) {
        try {
            server.editRecord(recordID, fieldName, newValue);
            logger.log(Level.INFO, String.format(LogMessages.RECORD_EDITED, fieldName, newValue, recordID));
        }
        catch (FieldNotFoundException e) {
            logger.log(Level.SEVERE, LogMessages.FIELD_NOT_FOUND, fieldName);
        }
        catch (InvalidValueException e) {
            logger.log(Level.SEVERE, LogMessages.INVALID_VALUE, newValue);
        }
        catch (RecordNotFoundException e){
            logger.log(Level.SEVERE, LogMessages.RECORD_NOT_FOUND, recordID);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, LogMessages.RECORD_EDITING_FAILED);
        }
    }

    /**
     * Displays the record information by calling the remote method
     *
     * @param recordId
     */
    public void show(String recordId) {
        try {
            System.out.println(server.show(recordId));
        }
        catch (RecordNotFoundException e){
            logger.log(Level.SEVERE, "Record Not found {0}", recordId);
        }

        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

    }
}
