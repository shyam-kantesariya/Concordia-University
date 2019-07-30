/*
This implementation has no reference from anywhere.
The only challange is it takes so much amount of time to build SuffixTree
 */

package assignment2.pattern_match.main;

import assignment2.pattern_match.common.InputPattern;
import assignment2.pattern_match.common.InputString;
import assignment2.pattern_match.common.OutputWriter;
import assignment2.pattern_match.config.Constants;
import assignment2.pattern_match.util.SuffixTree;

import java.util.Iterator;

public class SuffixTreePatternMatcher {
    public static void main(String[] args){
        InputPattern patterns = new InputPattern(Constants.patternFilePath);
        InputString inputString = new InputString(Constants.stringFilePath);
        OutputWriter writer = new OutputWriter(Constants.outputFilePath);
        SuffixTree tree = new SuffixTree(inputString.getString());
        boolean matched;
        String pattern;
        Iterator<String> iter = patterns.getPatterns().iterator();
        int index;
        long startTime = System.currentTimeMillis();
        long endTime;
        while(iter.hasNext()){
            pattern=iter.next();
            index=tree.checkPattern(pattern);
            writer.writeLine(""+(index==-1?-1:(index-1)));
            writer.newLine();
        }
        endTime = System.currentTimeMillis();
        System.out.println("Runtime of SuffixTree without Edges: " + (endTime - startTime));
        writer.closeFile();
        //tree.printIndexes();
    }
}