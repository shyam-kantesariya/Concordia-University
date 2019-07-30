package com.classmanagement.models;

import com.classmanagement.configuration.Constants;
import com.classmanagement.exceptions.InvalidRecordType;
import com.classmanagement.utils.FileManager;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Counter implements Serializable{
    private int counter;
    public Counter(int cntr){
        counter = cntr;
    }
    public Counter(String cntrStrng){
        counter = Integer.parseInt(cntrStrng);
    }
    public Counter(){
        counter = 0;
    }
    public synchronized Integer readCounter(){
        return counter;
    }
    public synchronized Integer nextVal()
    {
        return ++counter;
    }
    public synchronized void serializeCounter(Location location, String fileName){
        OutputStream file = null;
        String dir = Constants.PROJECT_DIR + "\\" + Constants.BACKUP_DIR_NAME + "\\" + Constants.COUNTER_DIR_NAME;
        try {
            file = new FileOutputStream(dir + "\\" + location.toString().trim() + "\\" + fileName);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream (buffer);
            output.writeObject(this);
            buffer.flush();
            buffer.close();
            output.close();
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Counter setCounterVal(CounterType counterType, Location location) {
        Counter counter = null;
        String dir = Constants.PROJECT_DIR + "\\" + Constants.BACKUP_DIR_NAME + "\\" + Constants.COUNTER_DIR_NAME + "\\" +
                location.toString().trim() + "\\";
        try {
            switch (counterType) {
                case STUDENTID:
                    counter = (Counter) FileManager.ReadFile(dir + Constants.STUDENT_COUNTER_FILE_NAME);
                    break;
                case TEACHERID:
                    counter = (Counter) FileManager.ReadFile(dir + Constants.TEACHER_COUNTER_FILE_NAME);
                    break;
                case MANAGERID:
                    counter = (Counter) FileManager.ReadFile(dir + Constants.TEACHER_COUNTER_FILE_NAME);
                    break;
                case REQUESTID:
                    counter = (Counter) FileManager.ReadFile(dir + Constants.REQUEST_COUNTER_FILE_NAME);
                    break;
                default:
                    throw new InvalidRecordType();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(System.out);
        } catch (InvalidRecordType e) {
            e.printStackTrace(System.out);
        } finally {
            if (counter == null) {
                counter = new Counter();
            }
        }
        return counter;
    }
    public static Counter setCounterVal(CounterType counterType, Location location, Logger logger) {
        Counter counter = null;
        String dir = Constants.PROJECT_DIR + "\\" + Constants.BACKUP_DIR_NAME + "\\" + Constants.COUNTER_DIR_NAME + "\\" +
                location.toString().trim() + "\\";
        try {
            switch (counterType) {
                case STUDENTID:
                    counter = (Counter) FileManager.ReadFile(dir + Constants.STUDENT_COUNTER_FILE_NAME);
                    break;
                case TEACHERID:
                    counter = (Counter) FileManager.ReadFile(dir + Constants.TEACHER_COUNTER_FILE_NAME);
                    break;
                case MANAGERID:
                    counter = (Counter) FileManager.ReadFile(dir + Constants.TEACHER_COUNTER_FILE_NAME);
                    break;
                case REQUESTID:
                    counter = (Counter) FileManager.ReadFile(dir + Constants.REQUEST_COUNTER_FILE_NAME);
                    break;
                default:
                    throw new InvalidRecordType();
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } catch (InvalidRecordType e) {
            logger.log(Level.SEVERE, e.getMessage(counterType.toString()));
        } finally {
            if (counter == null) {
                counter = new Counter();
            }
        }
        return counter;
    }
}