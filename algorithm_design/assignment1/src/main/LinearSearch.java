package main;

import config.Constants;
import data.CorrectWordList;
import model.OutputFile;
import util.DistanceVector;
import util.LoggerProvider;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

//Implements Linear Search
public class LinearSearch {
    SentenceCorrection sentenceCorrection;
    DistanceVector dv = new DistanceVector();
    Logger logger = LoggerProvider.getLogger(this.getClass().getName());

    public LinearSearch(SentenceCorrection sentenceCorrection) {
        this.sentenceCorrection = sentenceCorrection;
    }

    //Process each word mentioned in input sentence.txt file
    public void processInputWords() {
        //Open output file, delete if already exists
        OutputFile output = new OutputFile(Constants.outputFilePath, true);
        BufferedWriter bw = output.openFileToWrite();
        boolean zeroIncorrectWord = true;
        try {
            for (String word : sentenceCorrection.inputWords) {
//                System.out.println("Word "+ word);
                if (sentenceCorrection.wordList.searchWord(word)) {
                    //correct word
                    logger.log(Level.INFO, "Word " + word + " is correct");
                    continue;
                } else {
                    zeroIncorrectWord = false;
                    //https://stackoverflow.com/questions/7806709/remove-trailing-comma-from-comma-separated-string
                    String outputline = word + ":" + getCorrectWords(word, sentenceCorrection.maxDistance).trim().replaceAll("(,)*$", "");
                    //write suggestions to the output file
                    bw.write(outputline);
                    logger.log(Level.INFO, "Suggestions for Word " + word + " is as following: " );
                    logger.log(Level.INFO,(outputline));
                    bw.newLine();
                }
            }
            if(zeroIncorrectWord){
                bw.write("0");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //make sure to close all file handles
                bw.flush();
                bw.close();
                output.closeAllFileHandles();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getCorrectWords(String sentenceWord, int maxDistance) {
        String suggestedWords = "";
        for (String word : sentenceCorrection.wordList.getVocabWordList()) {
            if (dv.getDistance(word, sentenceWord) <= maxDistance) {
                suggestedWords += " " + word + ",";
            }
        }
        return suggestedWords;
    }
}