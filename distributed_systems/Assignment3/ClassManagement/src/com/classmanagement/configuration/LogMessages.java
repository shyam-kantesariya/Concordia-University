package com.classmanagement.configuration;

public class LogMessages {
    public static String RECORD_NOT_FOUND = "Failed to Update Record: Record {0} Not Found";
    public static String INVALID_VALUE = "Failed to Update Record: Field Value {0} is invalid";
    public static String FIELD_NOT_FOUND = "Failed to Update Record: Field {0} is Invalid";
    public static String INVALID_LAST_NAME = "Record Creation Failed: Invalid Last Name {0}";
    public static String STUDENT_RECORD_CREATED = "Created Student Record: Record ID = {0} by {1}";
    public static String TEACHER_RECORD_CREATED = "Created Teacher Record: Record ID = {0} by {1}";
    public static String RECORD_EDITED = "Updated field %s with the value %s for the Record %s by {0}";
    public static String UDP_SERVER_STARTED = "UDP server has been started and running for {0} Data center";
    public static String RECORD_EDITING_FAILED = "Record Editing Failed";
    public static String RECORD_CREATION_FAILED = "Record Creation Failed";
    public static String RECORD_COUNT = "Get Record Count: Count={0} by {1}";
    public static String RECORD_COUNT_FAILED = "Failed to get record Count";
    public static String SOCKET_CONNECTION_ERROR = "Could not connect to port {0}";
    public static String INT_RECORD_COUNT_SERVER_REQ = "UDP Request initiated to get record count from %s Server on " +
            "Address %s and port %s";
    public static String COMPLETED_RECORD_COUNT_SERVER_REQ = "UDP Request completed to get record count from %s Server " +
            "on Address %s and port %s";
    public static String UDP_REQ_RECEIVED = "UDP %s request received from Address %s and port %s";
    public static String UDP_RESPONSE_SENT = "UDP %s response sent to Address %s and port %s";
    public static String INVALID_JOURNAL_LOG_TYPE = "Invalid journal log type. Skipped to record Journal log";
    public static String INT_TRANSFER_RECORD_SERVER_REQ = "UDP Request initiated to transfer record from %s Server on " +
            "Address %s and port %s";
    public static String COMPLETED_TRANSFER_RECORD_SERVER_REQ = "UDP Request completed to transfer record from %s Server " +
            "on Address %s and port %s";
    public static String RECORD_TRANSFER_FAILED = "Record transfer failed";
    public static String RECORD_NOT_PRESENT = "The record {0} is not found";

    public static String INVALID_LOCATION = "The location is invalid. Location should be either (MTL/LVL/DDO)";

    public static String RECORD_TRANSFERED = "Record {0} transferred succesfully from {1} to {2} with new record Id: {3} " +
            "by the manager {4}";
}