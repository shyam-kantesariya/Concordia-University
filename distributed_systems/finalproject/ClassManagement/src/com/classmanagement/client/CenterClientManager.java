package com.classmanagement.client;

import com.classmanagement.configuration.LogMessages;
import com.classmanagement.configuration.Constants;
import com.classmanagement.exceptions.*;
import com.classmanagement.interfaces.CenterServer;
import com.classmanagement.models.Location;
import com.classmanagement.models.Status;
import com.classmanagement.utils.PropertyManager;
import com.classmanagement.utils.corba.helper.CenterServerHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
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
    public Location currentLocation;
    private ORB orb;
    public CenterClientManager(String args[], String managerId) throws Exception {
        this.managerId = managerId;
        orb = ORB.init(args, PropertyManager.getProperties());
        this.server = GetServer(managerId);
        this.initLogger();
    }

    private CenterServer GetServer( String managerId) throws Exception {
        org.omg.CORBA.Object nameServiceRef = orb.resolve_initial_references("NameService");
        NamingContextExt nameService = NamingContextExtHelper.narrow(nameServiceRef);
        if(managerId.startsWith("MTL") || managerId.startsWith("LVL") || managerId.startsWith("DDO")){
            return (CenterServer) CenterServerHelper.narrow(nameService.resolve_str(Constants.FRONT_END_CORBA_OBJ_NAME));
        } else {
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

    public void createTRecord(String firstName, String lastName, String address, String phone, String specialization,
                              Location location, String managerId){
        try {
            String recordId = server.createTRecord(firstName, lastName, address, phone, specialization, location.toString(),
                    managerId);
            checkForException(recordId);
            logger.log(Level.INFO, LogMessages.TEACHER_RECORD_CREATED, new String[] { recordId, managerId});
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
    public void createSRecord(String firstName, String lastName, String coursesRegistered, Status status,
                              String statusDate, String managerId){
        try {
            String recordId = server.createSRecord(firstName, lastName, coursesRegistered, status.toString(), statusDate,
                    managerId);
            checkForException(recordId);
            logger.log(Level.INFO, LogMessages.STUDENT_RECORD_CREATED, new String[] { recordId, managerId});
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
    public String getRecordCounts(String managerId) {
        try {
            String recordCount = server.getRecordCounts(managerId);
            checkForException(recordCount);
            logger.log(Level.INFO, LogMessages.RECORD_COUNT, new String[]{recordCount, managerId});
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
     * @param recordId
     * @param fieldName
     * @param newValue
     */
    public void editRecord(String recordId, String fieldName, String newValue, String managerId) {
        try {
            server.editRecord(recordId, fieldName, newValue, managerId);
            logger.log(Level.INFO, String.format(LogMessages.RECORD_EDITED, fieldName, newValue, recordId), managerId);
        }
        catch (FieldNotFoundException e) {
            logger.log(Level.SEVERE, LogMessages.FIELD_NOT_FOUND, fieldName);
        }
        catch (InvalidValueException e) {
            logger.log(Level.SEVERE, LogMessages.INVALID_VALUE, newValue);
        }
        catch (RecordNotFoundException e){
            logger.log(Level.SEVERE, LogMessages.RECORD_NOT_FOUND, recordId);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, LogMessages.RECORD_EDITING_FAILED);
        }
    }

    /**
     *
     * @param managerId
     * @param recordId
     * @param remoteCenterServerName
     * @throws RemoteException
     * @throws RecordNotFoundException Tries to transfer record from one server to other, throws recordNotfoundException
     * if it fails
     */

    public void  transferRecord (String managerId, String recordId, String remoteCenterServerName){
        try {
            Location loc = Location.valueOf(remoteCenterServerName);
            if(loc == this.currentLocation) {
                throw new Exception("The source and destination server should be different");
            }
            String newRecordId = server.transferRecord(managerId, recordId, remoteCenterServerName);
            checkForException(newRecordId);
            logger.log(Level.INFO, LogMessages.RECORD_TRANSFERED, new String[] {recordId, this.serverLocation.toString(),
                    remoteCenterServerName, newRecordId, managerId});
        }
        catch (IllegalArgumentException e){
          logger.log(Level.SEVERE, LogMessages.INVALID_LOCATION);
        }
        catch (RecordNotFoundException e) {
            logger.log(Level.SEVERE, LogMessages.RECORD_NOT_PRESENT, recordId);
        }
        catch (RecordBeingTransferredException e){
            logger.log(Level.WARNING, String.format("Transfer for the record ID %s is already in progress",
                    recordId));
        }
        catch (Exception e){
            logger.log(Level.SEVERE, e.getMessage());
            logger.log(Level.SEVERE, LogMessages.RECORD_TRANSFER_FAILED);
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

    private void checkForException(String result) throws Exception{
        for(String exceptionName: ExceptionList.exceptionList.keySet()){
            if(result.contains(exceptionName)){
                throw ExceptionList.exceptionList.get(exceptionName);
            }
        }
    }
}
