package com.classmanagement.exceptions;

import java.util.HashMap;

//Exception list for Clients and Front End

public class ExceptionList {
    public static HashMap<String, Exception> exceptionList = new HashMap<String, Exception>();
    static {
        exceptionList.put("InvalidLastNameException", new InvalidLastNameException());
        exceptionList.put("FieldNotFoundException", new FieldNotFoundException());
        exceptionList.put("RecordNotFoundException", new RecordNotFoundException());
        exceptionList.put("InvalidValueException", new InvalidValueException());
        exceptionList.put("RecordNotFoundException", new RecordNotFoundException());
        exceptionList.put("RecordBeingTransferredException", new RecordBeingTransferredException());
    }
}
