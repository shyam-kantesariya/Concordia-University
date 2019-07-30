package com.classmanagement.models;

import com.classmanagement.exceptions.FieldNotFoundException;
import com.classmanagement.exceptions.InvalidLastNameException;
import com.classmanagement.exceptions.InvalidValueException;

import java.rmi.RemoteException;
import java.util.HashSet;

import static com.classmanagement.configuration.Constants.TEACHER_RECORD_IDENTIFIER;

public class Teacher extends RecordBase {

    private String address;
    private String phone;
    private String specialization; // (e.g. french, maths, etc)
    private Location location; // (mtl, lvl, ddo)

    public Teacher(String recordID, String firstName, String lastName, String address, String phone, String specialization, Location location) {
        super(recordID, firstName, lastName);
        this.setAddress(address);
        this.setPhone(phone);
        this.setLocation(location);
        this.setSpecialization(specialization);
    }

    public String serialize() {
        String data = TEACHER_RECORD_IDENTIFIER + getFirstName() + "," + getLastName() + "," + getSpecialization() + "," + getPhone() + "," + getAddress() + "," + getLocation();
        return data;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void edit(String recordID, String fieldName, String newValue) throws RemoteException, FieldNotFoundException, InvalidValueException {
        switch (fieldName.toLowerCase()) {
            case "address":
                this.setAddress(newValue);
                break;
            case "phone":
                this.setPhone(newValue);
                break;
            case "location":
                this.setLocation(this.convert(newValue));
                break;
            default:
                throw new FieldNotFoundException();
        }
    }

    public String toString() {
        HashSet<String> data = new HashSet<>();
        data.add("First Name: " + this.getFirstName());
        data.add("Last name: " + this.getLastName());
        data.add("Specialization: " + this.getSpecialization());
        data.add("Phone:" + this.getPhone());
        data.add("Address:" + this.getAddress());
        data.add("Location:" + this.getLocation());
        return data.toString();
    }

    private Location convert(String value) throws InvalidValueException {
        try {
            return Location.valueOf(value);
        } catch (Exception exception) {
            throw new InvalidValueException();
        }
    }
}
