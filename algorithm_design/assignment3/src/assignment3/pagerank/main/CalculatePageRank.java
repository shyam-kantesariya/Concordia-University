package assignment3.pagerank.main;

import assignment3.pagerank.common.InputLinks;
import assignment3.pagerank.config.Constants;
import assignment3.pagerank.model.Link;
import assignment3.pagerank.util.PageRankCalculator;

import java.util.HashMap;
import java.util.Map;

public class CalculatePageRank {
    public static void main(String[] args){
        Map<String, Link> graph = new HashMap<String, Link>();
        InputLinks.readLinks(graph);
        long startTime = System.currentTimeMillis();
        PageRankCalculator.calculateRank(graph, Constants.convergeFactor, Constants.saclingFactor, Constants.maxIter);
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        https://stackoverflow.com/questions/5204051/how-to-calculate-the-running-time-of-my-program
        System.out.println("Runtime is: " + totalTime);
    }
}