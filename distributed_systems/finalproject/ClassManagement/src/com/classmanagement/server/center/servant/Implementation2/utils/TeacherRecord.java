package com.classmanagement.server.center.servant.Implementation2.utils;

@SuppressWarnings("serial")
public class TeacherRecord extends Record {

    private String address;
    private String phone;
    private String specialization;
    private String Location;

    public TeacherRecord(String RecordID, String firstName, String lastname, String address, String phone,
                         String Specialization, String location) {
        // TODO Auto-generated constructor stub
        super(RecordID, firstName, lastname);
        this.setAddress(address);
        this.setPhone(phone);
        this.setSpecialization(Specialization);
        this.setLocation(location);
    }
    public String serialize(){
    	return "Teacher" + getRecordID() + "," + getFirstName()+"," + getLastname()+ ","
    			+ getAddress()+ "," + getPhone()+ "," + getSpecialization() + "," +getLocation() ;
    }

    public String toString() {
        return this.getRecordID() + " " + this.getFirstName() + " " + this.getLastname() + " " + this.getAddress() + " "
                + this.getPhone() + " " + this.getSpecialization() + " " + this.getLocation();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
	private String getAddress() {
		return address;
	}
	public  void setAddress(String address) {
		this.address = address;
	}

}