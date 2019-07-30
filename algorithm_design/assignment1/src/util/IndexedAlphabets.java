package util;

import config.Constants;

import java.util.HashMap;

//Prepare a hashmap for each alphabets for faster lookup in correct word list tree

public class IndexedAlphabets {
    static HashMap<Character,Integer> alphabetIndexMap = new HashMap<Character, Integer>();
    static{
        //https://stackoverflow.com/questions/17575840/better-way-to-generate-array-of-all-letters-in-the-alphabet
        for(int i = 0; i < Constants.totalAlphabets; i++){
            alphabetIndexMap.put((char)(Constants.asciiIndexRoot + i), i);
        }
    }

    public static int getCharIndex(char character){
        return alphabetIndexMap.get(character);
    }

    public static void printAllAlphbetsIndexes(){
        for(char ch: alphabetIndexMap.keySet()){
            System.out.println(ch + "->" + alphabetIndexMap.get(ch));
        }
    }
}
