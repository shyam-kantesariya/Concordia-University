package assignment3.pagerank.util;

import assignment3.pagerank.common.OutputWriter;
import assignment3.pagerank.config.Constants;
import assignment3.pagerank.model.Link;
import java.util.List;
import java.util.Map;

public class PageRankCalculator {

    public static void calculateRank(Map<String, Link> graph, double convergeFactor, double scalingFactor, int maxIter){
        PageRankCalculator calcObj = new PageRankCalculator();
        int N = graph.size();
        double scale = (1.0 - scalingFactor);
        double totalWeight;
        calcObj.setInitialPageRank(graph, N);
        double updatedPageRank ;
        int maxIteration = maxIter;

        boolean converged = false;
        while(maxIteration>0){
            if(converged){
                System.out.println("Converged after iteration: " + (maxIter - maxIteration));
                calcObj.storePageRanks(graph);
                break;
            } else {
                converged = true;
            }

            for(Link link: graph.values()){
                totalWeight = calcObj.getTotalWeight(graph, link.getInComingLinks());
                updatedPageRank = scale + (Constants.saclingFactor * totalWeight);
                if(Math.abs(updatedPageRank - link.getPageRank()) > convergeFactor){
                    converged = false;
                }
                link.setCalculatedPageRank(updatedPageRank);
            }
            maxIteration--;
            calcObj.applyCalculatedPageRank(graph);
        }
        if(!converged)
            calcObj.storePageRanks(graph);
    }

    public void applyCalculatedPageRank(Map<String, Link> graph) {
        for (Link link : graph.values()) {
            link.updatePageRank();
        }
    }

    public void setInitialPageRank(Map<String, Link> graph, int N){
        double initialPageRank = 1.0/N;
        for(Link link: graph.values()){
            link.setPageRank(initialPageRank);
        }
    }

    public double getTotalWeight(Map<String, Link> graph, List<Link> links){
        double totalWeight=0.0;
        if(graph == null || links == null || links.size() <1 || graph.size() <1)
            return totalWeight;
        Link link;
        for(int i=0; i<links.size(); i++){
            link = graph.get(links.get(i).getUrl());
            totalWeight += link.getPageRank()/link.getOutGoingLinkCount();
        }
        return totalWeight;
    }
    public void storePageRanks(Map<String, Link> graph){
        Link[] sortedList = new Link[graph.size()];
        int i=0;
        OutputWriter out=null;
        for(Link link: graph.values()){
            sortedList[i] = link;
            i++;
        }
        mergeSort(sortedList, 0, graph.size());
        try{
            out = new OutputWriter(Constants.outputFilePath, true);
            out.openFile();
            for(i=0;i<graph.size();i++){
                out.writeLine(sortedList[i].getUrl() + "," + sortedList[i].getPageRank());
                out.newLine();
            }
        } finally {
            if(out != null){
                out.closeFile();
            }
        }
    }
    private void mergeSort(Link[] elements, int i, int j){  //i=0, j=5 total size
        if(i<j-1){
            int q=(int)Math.floor((j+i)/2);  //q=2
            mergeSort(elements,i,q);
            mergeSort(elements,q,j);
            mergeSortMerge(elements, i, q, j);  //i=0, q=2, j=5
        }
    }

    private void mergeSortMerge(Link[] elements, int i, int q, int j){
        Link[] left = new Link[q-i];
        Link[] right = new Link[j-q];
        int k, m=0, n=0;
        for(k=0; k<q-i;k++){
            left[k] = elements[i+k];
        }
        for(k=0; k<j-q;k++){
            right[k] = elements[k+q];
        }
        for(k=i;k<j;k++){
            if(m<q-i && n<j-q) {
                if (left[m].getPageRank() >= right[n].getPageRank()) {
                    elements[k] = left[m];
                    m++;

                } else {
                    elements[k] = right[n];
                    n++;
                }
            } else if (m<q-i){
                elements[k] = left[m];
                m++;
            } else {
                elements[k] = right[n];
                n++;
            }
        }
    }
}