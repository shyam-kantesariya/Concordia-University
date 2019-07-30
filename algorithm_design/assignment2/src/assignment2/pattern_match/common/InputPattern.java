package assignment2.pattern_match.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class InputPattern {
    private List<String> patterns = new LinkedList<String>();
    private int patternCount;
    private String initials="";
    public InputPattern(String path){
        this.readPatterns(path);
    }

    public List<String> getPatterns(){
        return patterns;
    }

    public void readPatterns(String path){
        BufferedReader br = null;
        FileReader fr = null;
        String nextWord;
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);
            if((patternCount = new Integer(br.readLine()).intValue()) != 0){
                while ((nextWord = br.readLine()) != null) {
                    patterns.add(nextWord.toUpperCase());
                }
                if(patternCount != patterns.size()){
                    System.out.println("Expected " + patternCount + " patterns but found " + patterns.size());
                    System.out.println("Exiting program");
                    System.exit(1);
                } else {
                    System.out.println("Total patterns are: "+ patternCount);
                }
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fr != null) fr.close();
                if(br != null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
