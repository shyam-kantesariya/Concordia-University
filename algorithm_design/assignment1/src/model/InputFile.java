package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//Generic class to Read data from any InputFile
public class InputFile {
    String path;
    BufferedReader br = null;
    FileReader fr = null;

    public InputFile(String path){
        this.path = path;
    }
    public BufferedReader getReadHandle(){
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);
            String sCurrentLine;
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return br;
    }

    public void closeAllIOHandles(){
        try{
            if(br != null) br.close();
            if(fr != null) fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
