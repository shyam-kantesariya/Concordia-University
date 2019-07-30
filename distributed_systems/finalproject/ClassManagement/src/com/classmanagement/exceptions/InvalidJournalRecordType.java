package com.classmanagement.exceptions;

public class InvalidJournalRecordType extends Exception {
    public String getMessage(){
        return "Record type is invalid to log Journal";
    }
}
