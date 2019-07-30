package assignment2.pattern_match.model;

import assignment2.pattern_match.util.IndexedAlhpabets;

import java.util.ArrayList;
import java.util.List;

public class Node {
    String value;
    int index;
    Node[] children = new Node[26];
    boolean isRoot = false;
    Node parent = null;
    public Node(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public Node(String val, int startIndex, Node parent) {
        this.parent = parent;
        value = val;
        index = startIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Node[] getChildren() {
        return children;
    }

    public void setChildren(Node[] children) {
        this.children = children;
    }

    public void addChild(Node node) {
        children[IndexedAlhpabets.getCharIndex(node.getValue().charAt(0))] = node;
    }

    public String getValue() {
        return value;
    }

    public void addSuffix(String val, int startIndex) {
        int i = 0;
        while (i < Math.min(val.length(), value.length())) {
            if (val.charAt(i) == value.charAt(i)) {
                i++;
            } else {
                break;
            }
        }
        splitNodeString(val, startIndex, i);
        this.index = startIndex;
    }

    private void processSuffix(String val, Node parent, int startIndex) {
        if (parent.getChildren()[IndexedAlhpabets.getCharIndex(val.charAt(0))] != null) {
            parent.getChildren()[IndexedAlhpabets.getCharIndex(val.charAt(0))].addSuffix(val, startIndex);
        } else {
            parent.addChild(new Node(val, startIndex, parent));
        }
    }

    private void splitNodeString(String val, int startIndex, int splitIndex) {
        if (splitIndex == value.length()) {
            this.processSuffix(val.substring(splitIndex), this, startIndex);
        } else {
            Node[] newChildrenList = new Node[26];
            newChildrenList[IndexedAlhpabets.getCharIndex(value.charAt(splitIndex))]= new Node(value.substring(splitIndex), index, this);
            newChildrenList[IndexedAlhpabets.getCharIndex(val.charAt(splitIndex))] = new Node(val.substring(splitIndex), startIndex, this);
            newChildrenList[IndexedAlhpabets.getCharIndex(value.charAt(splitIndex))].setChildren(this.getChildren());
            this.value = value.substring(0, splitIndex);
            this.setChildren(newChildrenList);
        }
    }

    public int checkForPattern(String pattern) {
        if (pattern.length() == 0) {
            return 1;
        }
        if (this.value.startsWith(pattern)) {
            return this.index;
        }
        if (pattern.startsWith(this.value)) {
            Node node = this.getChildren()[IndexedAlhpabets.getCharIndex(pattern.charAt(this.value.length()))];
            return node==null?-1:node.checkForPattern(pattern.substring(this.value.length()));
        }
        return -1;
    }
}