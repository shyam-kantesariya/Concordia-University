/**
 * This implementation is derived from Geekforgeeks portal.
 */

package assignment2.pattern_match.main;

import assignment2.pattern_match.common.InputPattern;
import assignment2.pattern_match.common.InputString;
import assignment2.pattern_match.common.OutputWriter;
import assignment2.pattern_match.config.Constants;
import assignment2.pattern_match.util.SuffixTreeWithEdges;
import java.util.Iterator;

public class SuffixTreePatternMatcherWithEdges {

    private static SuffixTreeWithEdges st;

    public static void main(String[] args) {
        InputPattern patterns = new InputPattern(Constants.patternFilePath);
        InputString inputString = new InputString(Constants.stringFilePath);
        OutputWriter writer = new OutputWriter(Constants.outputFilePath);
        Iterator<String> iter = patterns.getPatterns().iterator();
        //create the suffix tree
        st = new SuffixTreeWithEdges(inputString.getString());
        String pattern;
        long startTime = System.currentTimeMillis();
        while (iter.hasNext()) {
            pattern = iter.next();
            {
                writer.writeLine("" + st.indexOf(pattern));
                writer.newLine();
            }
        }
        long endTime   = System.currentTimeMillis();
        System.out.println("Runtime of SuffixTree with edges: " + (endTime - startTime));
        writer.closeFile();
    }
}
