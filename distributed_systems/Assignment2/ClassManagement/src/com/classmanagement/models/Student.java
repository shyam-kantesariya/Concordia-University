package com.classmanagement.models;

import com.classmanagement.exceptions.FieldNotFoundException;
import com.classmanagement.exceptions.InvalidValueException;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import static com.classmanagement.configuration.Constants.STUDENT_RECORD_IDENTIFIER;

public class Student extends RecordBase {

    private HashSet<String> coursesRegistered; //(maths/french/science)
    private Status status; // (active/inactive)
    private String statusDate; /*(date when student became active (if status is active) or date when
                               * student became inactive (if status is inactive)) */
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public Student(String recordId, String firstName, String lastName, HashSet<String> courseRegistered, Status status, String statusDate) {
        super(recordId, firstName, lastName);
        this.setCoursesRegistered(courseRegistered);
        this.setStatus(status);
        this.setStatusDate(statusDate);
    }

    public String serialize() {
        String listString = String.join(";", getCoursesRegistered());
        String data = STUDENT_RECORD_IDENTIFIER + this.getFirstName() +  "," + getLastName() + ","  + listString +  "," + this.getStatus() + "," + this.getStatusDate();
        return data;
    }


    public HashSet<String> getCoursesRegistered() {
        return this.coursesRegistered;
    }

    public void setCoursesRegistered(HashSet<String> value) {
        this.coursesRegistered = value;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status value) {
        this.status = value;
    }

    public String getStatusDate() {
        return this.statusDate;
    }

    public void setStatusDate(String value) {
        this.statusDate = value;
    }

    public void edit(String recordID, String fieldName, String newValue) throws RemoteException, FieldNotFoundException, InvalidValueException {
        switch (fieldName.toLowerCase()) {
            case "statusdate":
                this.setStatusDate(newValue);
                break;
            case "status":
                if (this.convert(newValue) == this.getStatus()){
                    break;
                } else {
                    this.setStatus(this.convert(newValue));
                    this.setStatusDate(dateFormat.format(new Date()));
                }
                break;
            case "coursesregistered":
                String[] courses = newValue.split(",");
                this.setCoursesRegistered(new HashSet<>(Arrays.asList(courses)));
                break;
            default:
                throw new FieldNotFoundException();
        }
    }

    public String toString() {
        HashSet<String> data = new HashSet<>();
        data.add("First Name: " + this.getFirstName());
        data.add("Last name: " + this.getLastName());
        data.add("Courses: " + this.getCoursesRegistered());
        data.add("Status:" + this.getStatus());
        data.add("Date:" + this.getStatusDate());
        return data.toString();
     }

    private Status convert(String value) throws InvalidValueException {
        try {
            return Status.valueOf(value);
        } catch (Exception exception) {
            throw new InvalidValueException();
        }
    }
}