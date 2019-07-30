package assignment2.pattern_match.main;

import assignment2.pattern_match.common.InputPattern;
import assignment2.pattern_match.common.InputString;
import assignment2.pattern_match.common.OutputWriter;
import assignment2.pattern_match.config.Constants;

import java.util.Iterator;

public class LinearPatternMatcher {
    public static void main(String[] args){
        InputPattern patterns = new InputPattern(Constants.patternFilePath);
        InputString inputString = new InputString(Constants.stringFilePath);
        OutputWriter writer = new OutputWriter(Constants.outputFilePath);
        char[] string = inputString.getString().toCharArray();
        char[] pattern;
        int i,j;
        boolean matched;
        Iterator<String> iter = patterns.getPatterns().iterator();
        long startTime = System.currentTimeMillis(), endTime;
        while(iter.hasNext()){
            matched = false;
            pattern=iter.next().toCharArray();
            for(i=0; i<=string.length-pattern.length; i++){
                if(matched){
                    break;
                }
                if(pattern[0] == string[i]){
                    for(j=1; j<pattern.length; j++){
                        if(pattern[j] != string[i+j])
                            break;
                    }
                    if(j==pattern.length){
                        writer.writeLine(""+i);
                        writer.newLine();
                        matched=true;
                    }
                }
            }
            if(!matched){
                writer.writeLine(""+ (-1));
                writer.newLine();
            }
        }
        endTime   = System.currentTimeMillis();
        System.out.println("Runtime of Linear Pattern matcher: " + (endTime - startTime));
        writer.closeFile();
    }
}