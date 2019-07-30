package com.classmanagement.server.center.servant.Implementation2.utils;

import java.util.HashSet;

@SuppressWarnings("serial")
public class StudentRecord extends Record {

    private HashSet<String> courseRegistered;
    private String status;
    private String statusDate;

    public StudentRecord(String RecordID, String firstname, String lastname, HashSet<String> coursesRegistered, String status,
                         String statusDate) {
        // TODO Auto-generated constructor stub
        super(RecordID, firstname, lastname);
        this.setCourseRegistered(coursesRegistered);
        this.setStatus(status);
        this.setStatusDate(statusDate);
    }
    
    public String serialize(){
    	return "Student" + getRecordID() + "," + getFirstName()+"," + getLastname()+ "," + getCourseRegistered()+ "," + getStatus()+ "," + getStatusDate();
    }

    public String toString() {
        return this.getRecordID() + " " + this.getFirstName() + " " + this.getLastname() + " "
                + this.getCourseRegistered() + " " + this.getStatus() + " " + this.getStatusDate();
    }

    public HashSet<String> getCourseRegistered() {
        //System.out.println(courseRegistered);
    	return courseRegistered;
        
    }

    public void setCourseRegistered(HashSet<String> coursesRegistered) {
        this.courseRegistered = coursesRegistered;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

}
