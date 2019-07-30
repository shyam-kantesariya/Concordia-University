package com.classmanagement.models;

import com.classmanagement.configuration.Constants;

import java.io.*;

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
}