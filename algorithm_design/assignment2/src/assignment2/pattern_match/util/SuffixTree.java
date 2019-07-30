package assignment2.pattern_match.util;

import assignment2.pattern_match.model.Node;

public class SuffixTree {
    Node root = new Node(true);

    public SuffixTree(String string) {
        constructTree(string);
    }

    public void constructTree(String string) {
        int i, j = 0;
        boolean addNewChild;
        for (i = string.length(); i > 0; i--) {
            System.out.println("i val " + i);
            addNewChild = true;
            if (root.getChildren()[IndexedAlhpabets.getCharIndex(string.charAt(i - 1))] != null) {
                root.getChildren()[IndexedAlhpabets.getCharIndex(string.charAt(i - 1))].addSuffix(string.substring(i - 1), i);
            } else {
                Node node = new Node(string.substring(i - 1), i, root);
                root.addChild(node);
            }
        }
    }

    public int checkPattern(String pattern) {
        Node node = root.getChildren()[IndexedAlhpabets.getCharIndex(pattern.charAt(0))];
        return (node == null)?-1:node.checkForPattern(pattern);
    }

    public void printIndexes() {
        for (Node node : root.getChildren()) {
            System.out.println("Index for " + node.getValue() + " is " + node.getIndex());
        }
    }
}
