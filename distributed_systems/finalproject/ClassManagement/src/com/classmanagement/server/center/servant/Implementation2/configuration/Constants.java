package com.classmanagement.server.center.servant.Implementation2.configuration;

public class Constants {
    public static final int OPEN_PORT_MIN_VAL = 49152;
    public static final int OPEN_PORT_MAX_VAL = 65535;
    public static final int PORT_CHECK_RETRY_VAL = 20;
    public static final String PROJECT_DIR = "D:\\Distributed Systems\\dist_syst_finalproject";
    public static final int REGISTRY_PORT = 1234;
    public static final String BACKUP_DIR_NAME = "DATA_BACKUP";
    public static final String LOG_DIR_NAME = "LOGS";
    public static final String COUNTER_DIR_NAME = "COUNTERS";
    public static final String TEACHER_COUNTER_FILE_NAME = "TeacherCounter.ser";
    public static final String STUDENT_COUNTER_FILE_NAME = "StudentCounter.ser";
    public static final String MANAGER_COUNTER_FILE_NAME = "ManagerCounter.ser";
    public static final String TEACHER_RECORD_ID_PREFIX = "TR";
    public static final String STUDENT_RECORD_ID_PREFIX = "SR";
    public static final String JOURNAL_DIR = "JOURNAL";
    public static final String JOURNAL_FILE_NAME = "Journal.log";
    public static final String ADD_RECORD_IDENTIFIER = "ADD";
    public static final String EDIT_RECORD_IDENTIFIER = "EDIT";
    public static final String FIELD_SEPARATOR = "|";
    public static final int ORB_INITIAL_PORT = 1050;
    public static final String ORB_INITIAL_HOST = "localhost";
    public static final int DEFAULT_UDP_BUFFER_SIZE = 100;
    public static final String TRANSFER_RECORD_PREFIX = "Transfer";
    public static final String STUDENT_RECORD_IDENTIFIER = "Student";
    public static final String TEACHER_RECORD_IDENTIFIER = "Teacher";
    public static final String MANAGER_ID_DELIMITER = "::";
	public static final String DELETE_RECORD_IDENTIFIER = "DELETE";
}