package com.classmanagement.models;

import com.classmanagement.exceptions.FieldNotFoundException;
import com.classmanagement.exceptions.InvalidValueException;

import java.rmi.RemoteException;

public abstract class RecordBase {

    public RecordBase(String recordId, String firstName, String lastName) {
        this.setRecordId(recordId);
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    private String recordId;
    private String firstName;
    private String lastName;

    public abstract void edit(String recordId, String fieldName, String newValue) throws RemoteException, FieldNotFoundException, InvalidValueException;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}