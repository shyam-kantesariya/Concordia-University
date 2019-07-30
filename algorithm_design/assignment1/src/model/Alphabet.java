package model;

import config.Constants;
import util.IndexedAlphabets;

//This class represents a node in our correct word tree
public class Alphabet {
    Alphabet[] nextChar = new Alphabet[26];
    private boolean wordExist;
    public Alphabet(){
        wordExist = false;
    }

    public void setWordExist(boolean val){
        this.wordExist = val;
    }

    public boolean getWordExist(){
        return wordExist;
    }

    public Alphabet addNextChar(char character){
        int index = IndexedAlphabets.getCharIndex(character);
        if(isAlphabetPresent(index)){
            nextChar[index] = new Alphabet();
        }
        return  nextChar[index];
    }

    private boolean isAlphabetPresent(int index){
        return nextChar[index] == null;
    }

    public Alphabet getNextAlphabet(char character){
        return nextChar[IndexedAlphabets.getCharIndex(character)];
    }
}