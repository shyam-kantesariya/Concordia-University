package com.classmanagement.utils;

import com.classmanagement.configuration.Constants;
import com.classmanagement.exceptions.InvalidJournalRecordType;
import com.classmanagement.models.JournalLogType;
import com.classmanagement.models.Location;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.logging.Logger;

/**
 * Provide Journal Functionality
 *
 * It will helpful when we want to recover the data during server crash
 */
public class JournalLogger {

    private FileWriter journalFileWriter = null;
    private BufferedWriter journalBufferedWriter = null;
    private PrintWriter journalPrintWriter = null;
    private Logger logger;
    private String replicaName;
    /**
     * @param journalFile
     * @param serverLocation
     * @throws IOException
     */
    public JournalLogger(String journalFile, Location serverLocation, String replicaName) throws IOException {
        this.replicaName = replicaName;
        initLogger(serverLocation);
        journalFileWriter = new FileWriter(journalFile, true);
        journalBufferedWriter = new BufferedWriter(journalFileWriter);
        journalPrintWriter = new PrintWriter(journalBufferedWriter);
    }

    /** Log a journal entry in journal file on persistent storage
     * @param obj
     * @param type
     * @throws InvalidJournalRecordType
     * @throws IOException
     */
    public synchronized void log(Object obj, JournalLogType type) throws InvalidJournalRecordType, IOException{
        switch (type){
            case ADD_RECORD:
                journalPrintWriter.println(Constants.ADD_RECORD_IDENTIFIER + Constants.FIELD_SEPARATOR + getFieldString(obj));
                break;
            case EDIT_RECORD:
                journalPrintWriter.println(Constants.EDIT_RECORD_IDENTIFIER + Constants.FIELD_SEPARATOR + getFieldString(obj));
                break;
            default:
                throw new InvalidJournalRecordType();
        }
        journalBufferedWriter.flush();
    }


    /** Built on top of Reflection API to read field attributes of an object along with its values
     * <p>In case of any private member it goes in IllegalAccessException and just return toString value
     * of give object</>
     * @param obj
     * @return
     */
    private String getFieldString(Object obj){
        String dataString = "";
        Class<?> c = obj.getClass();
        Field[] fields = c.getDeclaredFields();
        for( Field field : fields ){
            try {
                 dataString += field.getName().toString() + Constants.FIELD_SEPARATOR + field.get(obj);
            } catch (IllegalArgumentException e1) {
                return obj.toString();
            } catch (IllegalAccessException e1) {
                return obj.toString();
            }
        }
        return dataString;
    }

    /** Returns toString values of given String object
     * @param obj
     * @return
     */
    private String getFieldString(String obj){
        return obj.toString();
    }

    /** Initialize logger object
     * @param location
     * @throws IOException
     */
    private void initLogger(Location location) throws IOException {
        logger = LoggerProvider.getLogger(location, replicaName);
    }
}