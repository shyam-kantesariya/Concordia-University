package com.classmanagement.interfaces;

import com.classmanagement.exceptions.FieldNotFoundException;
import com.classmanagement.exceptions.InvalidLastNameException;
import com.classmanagement.exceptions.InvalidValueException;
import com.classmanagement.exceptions.RecordNotFoundException;
import com.classmanagement.models.Location;
import com.classmanagement.models.Status;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.rmi.Remote;

public interface CenterServer extends Remote {

    /**
     * Creates a teacher record and returns the newly created teacher record ID
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
    public String createTRecord(String firstName, String lastName, String address, String phone, String specialization, Location location) throws RemoteException, InvalidLastNameException;

    /**
     * Create a student record and returns the newly created student record ID
     *
     * @param firstName
     * @param lastName
     * @param coursesRegistered
     * @param status
     * @param statusDate
     * @return Student Record ID
     * @throws RemoteException
     * @throws InvalidLastNameException when The first letter of the lastname is not an alphabet
     */
    public String createSRecord(String firstName, String lastName, HashSet<String> coursesRegistered, Status status, String statusDate) throws RemoteException, InvalidLastNameException;


    /**
     * @return Get the total record count
     * It should return the collective count of records from all the servers
     * @throws RemoteException
     */
    public String getRecordCounts() throws RemoteException;

    /**
     * Edit an existing recordID by providing property name and property value
     *
     *
     * @param recordID
     * @param fieldName
     * @param newValue
     * @throws RemoteException
     * @throws FieldNotFoundException if the input field name is not valid
     * @throws RecordNotFoundException if the record is not found
     * @throws InvalidValueException if the provided value is not valid (Non type castable)
     */
    public void editRecord(String recordID, String fieldName, String newValue) throws RemoteException, FieldNotFoundException, RecordNotFoundException, InvalidValueException;

    /**
     * Returns the information of a record
     * @param recordId
     * @return Info about the Record
     * @throws RemoteException
     * @throws RecordNotFoundException if the record is not found
     */
    public String show(String recordId) throws RemoteException, RecordNotFoundException;
}