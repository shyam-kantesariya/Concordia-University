package com.classmanagement.utils;

import com.classmanagement.configuration.Constants;

import java.util.Properties;

public class PropertyManager {
    private static Properties properties = System.getProperties();
    static {
        properties.put("org.omg.CORBA.ORBInitialPort", new Integer(Constants.ORB_INITIAL_PORT).toString());
        properties.put("org.omg.CORBA.ORBInitialHost", Constants.ORB_INITIAL_HOST);
    }
    public static Properties getProperties(){
        return properties;
    }
}