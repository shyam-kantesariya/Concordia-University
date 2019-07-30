package assignment3.pagerank.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Link {
    String url;
    List<Link> outGoingLinks = new LinkedList<Link>();
    List<Link> inComingLinks = new LinkedList<Link>();
    int outGoingLinkCount = 0;
    double pageRank, calculatedPageRank;
    public Link(String Url){
        this.url = Url;
    }
    public String getUrl(){
        return url;
    }
    public void addOutGoingLink(Link link){
        if(!outGoingLinks.contains(link)){
            outGoingLinks.add(link);
            outGoingLinkCount++;
        }
    }

    public void addInComingLink(Link link){
        if(!inComingLinks.contains(link)){
            inComingLinks.add(link);
        }
    }

    public List<Link> getInComingLinks(){
        return inComingLinks;
    }

    public double getPageRank(){
        return pageRank;
    }
    public int getOutGoingLinkCount(){
        return (outGoingLinkCount==0)?1:outGoingLinkCount;
    }
    public void setPageRank(double updatedPageRank) { this.pageRank = updatedPageRank; }
    public void printOutGoingRefLinks(){
        Iterator myLinks = outGoingLinks.iterator();
        Link lnk;
        while(myLinks.hasNext()){
            lnk = (Link)myLinks.next();
            System.out.println(lnk.url);
        }
    }
    public void printPageRank(){
        System.out.println(url + " , " + pageRank);
    }
    public void updatePageRank(){
        this.pageRank = this.calculatedPageRank;
    }
    public void setCalculatedPageRank(double rank){
        this.calculatedPageRank = rank;
    }
}