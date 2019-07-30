package com.classmanagement.exceptions;

/**
 * Created by Sparta on 6/19/2017.
 */
public class RecordTransferError extends Exception {
    public String getMessage(){
        return "Could not transfer the record";
    }
}
