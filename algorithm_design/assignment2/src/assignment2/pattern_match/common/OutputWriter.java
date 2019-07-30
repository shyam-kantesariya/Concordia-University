package assignment2.pattern_match.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputWriter {
    String path;
    private BufferedWriter bw;
    private File file;
    private FileWriter fw;
    public OutputWriter(String path) {
        this(path, true);
    }

    public OutputWriter(String path, boolean deleteExisting) {
        this.path = path;
        file = new File(path);
        if (deleteExisting) {
            deleteExistingFile();
        }
        this.openFile();
    }
    private void deleteExistingFile() {
        if (file.delete()) {
            System.out.println("Deleted existing output file: " + path);
        } else {
            System.out.println("Error while deleting existing output file: " + path);
            System.out.println("However program will continue execution");
        }
    }

    public void openFile() {
        try {
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            // true = append file
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while opening output file. exiting the program");
            System.exit(1);
        }
    }
    public void writeLine(String record){
        try {
            bw.write(record);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void newLine(){
        try {
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void closeFile(){
        try{
            bw.flush();
            if(bw != null) bw.close();
            if(fw != null) fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}