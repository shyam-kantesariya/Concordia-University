package util;

import data.CorrectWordList;
import model.Node;
import java.util.logging.Logger;

public class BKTree {
    Node root = null;

    public void addWord(String word) {
        if (root == null) {
            root = new Node(null, word, 0);
        } else {
            root.addChild(word);
        }
    }

    public String getWordCorrection(String word, CorrectWordList correctWordList, int maxDistance) {
        if (correctWordList.searchWord(word)) {
            return null;
        } else {
            return root.getSpellSuggestion(word, maxDistance);
        }
    }
}