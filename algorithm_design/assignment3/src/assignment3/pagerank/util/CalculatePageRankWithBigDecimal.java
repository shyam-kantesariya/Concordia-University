package assignment3.pagerank.util;

import assignment3.pagerank.common.InputLinks;
import assignment3.pagerank.config.Constants;
import assignment3.pagerank.model.Link;
import assignment3.pagerank.util.PageRankCalculator;

import java.util.HashMap;
import java.util.Map;

public class CalculatePageRankWithBigDecimal {
    public static void main(String[] args){
        Map<String, Link> graph = new HashMap<String, Link>();
        InputLinks.readLinks(graph);
        for(Link link: graph.values()){
            System.out.println(link.getUrl() + " , " + link.getOutGoingLinkCount());
            link.printOutGoingRefLinks();
        }
        PageRankCalculator.calculateRank(graph, Constants.convergeFactor, Constants.saclingFactor, Constants.maxIter);
    }
}