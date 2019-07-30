package util;

import java.util.logging.Logger;

//Provide Java inbuilt logger
public class LoggerProvider {
    public static Logger getLogger(String className){
        return Logger.getLogger(className);
    }
}
