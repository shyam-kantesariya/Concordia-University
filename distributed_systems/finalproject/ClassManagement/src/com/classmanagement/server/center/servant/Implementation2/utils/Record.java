package com.classmanagement.server.center.servant.Implementation2.utils;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Record implements Serializable{
    private String RecordID;
    private String firstName;
    private String lastname;

    public Record(String RecordID, String firstName, String lastname) {
        this.setRecordID(RecordID);
        this.setFirstName(firstName);
        this.setLastname(lastname);

        // TODO Auto-generated constructor stub
    }

    public String getRecordID() {
        return RecordID;
    }

    public void setRecordID(String recordID) {
        RecordID = recordID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public abstract String serialize();

}
