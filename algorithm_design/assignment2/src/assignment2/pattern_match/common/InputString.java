package assignment2.pattern_match.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InputString {
    String string;
    public InputString(String path) {
        this.readString(path);
    }

    public String getString(){
        return string;
    }
    public void readString(String path) {
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);
            if ((string = br.readLine()) != null) {
                System.out.println("string is: " + string);
                string = string.toUpperCase();
            } else {
                System.out.println("No string in input file. Terminating execution with error code 1");
                System.exit(1);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) fr.close();
                if (br != null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}