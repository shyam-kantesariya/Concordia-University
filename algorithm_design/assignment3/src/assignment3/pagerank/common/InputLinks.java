package assignment3.pagerank.common;

import assignment3.pagerank.config.Constants;
import assignment3.pagerank.model.Link;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class InputLinks {

    public static void readLinks(Map<String, Link> linkMap) {
        BufferedReader br = null;
        FileReader fr = null;
        String nextLine, links[];
        Link link, refLink;
        try {
            fr = new FileReader(Constants.inputFilePath);
            br = new BufferedReader(fr);
            while ((nextLine = br.readLine()) != null) {
                links = nextLine.split(",");
                link = linkMap.get(links[0].trim());
                refLink = linkMap.get(links[1].trim());
                if (link == null) {
                    link = new Link(links[0].trim());
                    linkMap.put(link.getUrl(), link);
                }
                if (refLink != null) {
                    link.addOutGoingLink(refLink);
                    refLink.addInComingLink(link);
                } else {
                    refLink = new Link(links[1].trim());
                    linkMap.put(refLink.getUrl(), refLink);
                    link.addOutGoingLink(refLink);
                    refLink.addInComingLink(link);
                }
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