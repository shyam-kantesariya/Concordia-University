package com.classmanagement.server.center.servant;

import com.classmanagement.exceptions.*;

import java.io.IOException;
import java.rmi.RemoteException;


public interface CenterServantInterface {
    public String createSRecord(String firstName, String lastName, String coursesRegistered,
                              String status, String statusDate, String managerId) throws  IOException, InvalidJournalRecordType, InvalidLastNameException;

    public String createTRecord(String firstName, String lastName, String address,
                              String phone, String specialization, String location,String managerId) throws IOException, InvalidJournalRecordType,InvalidLastNameException;

    public void editRecord(String recordID, String fieldName, String newValue, String managerID)
            throws FieldNotFoundException, RecordNotFoundException, InvalidValueException, IOException, InvalidJournalRecordType;

    public String getRecordCounts(String managerId) throws IOException;

    public String transferRecord(String managerId, String recordId, String remoteCenterServerName) throws RecordNotFoundException, IOException, RecordBeingTransferredException;

    public void shutDown();
}
