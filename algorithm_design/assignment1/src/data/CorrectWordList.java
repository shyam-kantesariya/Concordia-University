package data;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import config.Constants;
import model.Alphabet;
import util.BKTree;
import util.IndexedAlphabets;

import java.util.ArrayList;
import java.util.List;

//This class is holding the words in vocab file
public class CorrectWordList {
    //preparing a tree of correct words so that any search operation would be on O(L) where L is the length of word
    Alphabet[] wordList = new Alphabet[Constants.totalAlphabets];
    //Side by side we are preparing the linear list as well just to support our linear algorithm
    List<String> linearWordList = new ArrayList<String>();

    //Dump all correct words in BKTree
    public void fillBKTree(BKTree bkTree){
        for(String word: linearWordList){
            bkTree.addWord(word);
        }
    }

    public List<String> getVocabWordList(){
        return linearWordList;
    }

    //Add one word to our tree shape data structure
    public void addWord(String word){
        char[] wordCharacters = word.trim().toLowerCase().toCharArray();
        linearWordList.add(word.trim());
        if (wordCharacters.length == 0){
            return;
        }
        Alphabet currentAlphabet = setFirstCharacter(wordCharacters[0]);
        for(int wordIndex=1; wordIndex<wordCharacters.length; wordIndex++){
            //System.out.println("Iteration: " + wordIndex);
            currentAlphabet = currentAlphabet.addNextChar(wordCharacters[wordIndex]);
            //System.out.println("Done Iteration: " + wordIndex);
        }
        //while coming to the end of word we will set the flag to true which will tell us that such word exists while
        // searching later on
        currentAlphabet.setWordExist(true);
    }

    //Search word in tree
    public boolean searchWord(String word){
        char[] wordCharacters = word.trim().toLowerCase().toCharArray();
        //discard any zero length word
        if (wordCharacters.length == 0){
            System.out.println("Zero length word: " + word);
            return false;
        }

        //Get root node of tree
        Alphabet currentAlphabet = getFirstCharHandle(wordCharacters[0]);
        if(currentAlphabet == null){
            return false;
        }

        //Traverse through the tree
        for(int wordIndex=1; wordIndex<wordCharacters.length; wordIndex++){
            currentAlphabet = currentAlphabet.getNextAlphabet(wordCharacters[wordIndex]);
            if (currentAlphabet == null){
                return false;
            }
        }

        //return the flag of leaf node in tree. If that is true then such word exists else not
        return currentAlphabet.getWordExist();
    }

    //Setting root for each alphabet
    private Alphabet setFirstCharacter(char character){
        int index = IndexedAlphabets.getCharIndex(character);
        if (wordList[index] == null){
            wordList[index] = new Alphabet();
        }
        //System.out.println("In setFirstCharacter");
        return wordList[index];
    }

    private Alphabet getFirstCharHandle(char character){
        return wordList[IndexedAlphabets.getCharIndex(character)];
    }
}
