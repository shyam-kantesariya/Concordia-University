package com.classmanagement.utils;

import java.io.*;

/**
 * Handle any read/write operation on disc file
 * Currently it provides only read functionality
 */
public class FileManager {

    /** Static method to read a file Object
     * @param filePath
     * @return ObjectInput
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object ReadFile(String filePath) throws IOException, ClassNotFoundException {
        InputStream file = new FileInputStream(filePath);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);
        return input.readObject();
    }
}
