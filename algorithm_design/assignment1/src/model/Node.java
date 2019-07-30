package model;

import util.DistanceVector;
import util.LoggerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//This class represents a Node in BKTree
public class Node {
    static Logger logger;
    Node root;
    int distanceFromRoot;
    String wordText;
    List<Node> children = new ArrayList<Node>();
    DistanceVector dv = new DistanceVector();

    public Node(Node root, String text, int distance){
        this.root = root;
        this.wordText = text;
        this.distanceFromRoot = distance;
        logger = LoggerProvider.getLogger(this.getClass().getName());
    }

    //Add word as Child node
    public void addChild(String word){
        if (word.length() <= 0){
            return;
        }
        int distance = dv.getDistance(this.wordText, word);
        boolean equalDistanceExists = false;
        for(Node node: children){
            if(node.distanceFromRoot == distance)
            {
                equalDistanceExists = true;
                node.addChild(word);
                break;
            }
        }
        if(!equalDistanceExists){
            //finally we found the right place. Create new child Node and add it
            Node child = new Node(this, word, distance);
            children.add(child);
        }
    }

    //Get spell suggestions starting root. Traverse whole tree untiall distance is between min and max
    public String getSpellSuggestion(String word, int maxDistance){
        String suggestions = "";
        //if distance is less than maxDistance then mark it as one of the suggested word
        if (isWordCloser(word, wordText, maxDistance)){
            suggestions += " " + wordText + ",";
        }
        LevenshteinDistanceRange nextRange = getDistanceRange(word, wordText, maxDistance);
        for (Node child: children){
            if(child.distanceFromRoot >= nextRange.getMinVal() && child.distanceFromRoot <= nextRange.getMaxVal()){
                suggestions += child.getSpellSuggestion(word, maxDistance);
            }
        }
        return suggestions;
    }

    private boolean isWordCloser(String inputWord, String nodeText, int maxDistance){
        return (dv.getDistance(inputWord, nodeText) <= maxDistance);
    }

    private LevenshteinDistanceRange getDistanceRange(String inputWord, String vocabWord, int maxDistance){
        int distance = dv.getDistance(inputWord, vocabWord);
        return new LevenshteinDistanceRange(distance, maxDistance);
    }

    //Traverse whole tree. Though not being used anywhere in the algorithm
    public void traverseNode(Node rootNode){
        Node currentNode = rootNode;
        for(Node child: rootNode.children){
           //System.out.println("Traversing node " + currentNode.wordText);
           currentNode = child;
           currentNode.traverseNode(currentNode);
           System.out.println("Done Traversing node " + currentNode.wordText);
        }
    }
}