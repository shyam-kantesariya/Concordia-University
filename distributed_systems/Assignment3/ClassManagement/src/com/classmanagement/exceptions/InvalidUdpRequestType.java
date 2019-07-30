package com.classmanagement.exceptions;

public class InvalidUdpRequestType extends Exception {
    public String getMessage(String requestType) {
        return "Invalid request type: " + requestType;
    }
}
