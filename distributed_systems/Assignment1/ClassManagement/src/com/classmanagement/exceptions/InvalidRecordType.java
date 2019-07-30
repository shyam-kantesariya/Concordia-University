package com.classmanagement.exceptions;

/**
 * Created by Sparta on 6/4/2017.
 */
public class InvalidRecordType extends Exception {
    public String getMessage(String recordType){
        return "Invalid record type: " + recordType;
    }
}
