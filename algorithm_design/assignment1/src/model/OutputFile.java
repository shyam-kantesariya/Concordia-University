package model;

import util.LoggerProvider;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

//Generic class to Write output to any file
public class OutputFile {
    String path;
    BufferedWriter bw;
    File file;
    FileWriter fw;
    Logger logger = LoggerProvider.getLogger(this.getClass().getName());

    public OutputFile(String path) {
        this(path, false);
    }

    public OutputFile(String path, boolean deleteExisting) {
        this.path = path;
        file = new File(path);
        if (deleteExisting) {
            deleteExistingFile();
        }
    }

    private void deleteExistingFile() {
        if (file.delete()) {
            logger.log(Level.INFO, "Deleted existing output file: " + path);
        } else {
            logger.log(Level.WARNING, "Error while deleting existing output file: " + path);
            logger.log(Level.WARNING, "However program will continue execution");
        }
    }

    public BufferedWriter openFileToWrite() {
        try {
            //https://www.mkyong.com/java/how-to-append-content-to-file-in-java/
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // true = append file
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE,"Error while opening output file. exiting the program");
            System.exit(1);
        }
        return bw;
    }

    public void closeAllFileHandles() {
        try {
            if (fw != null) fw.close();
            if (bw != null) bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
