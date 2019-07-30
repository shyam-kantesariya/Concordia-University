package main;

import config.Constants;
import data.CorrectWordList;
import model.OutputFile;
import util.BKTree;
import util.LoggerProvider;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

//Implements BKTree search
public class BKTreeSearch {
    Logger logger;
    BKTree bkTree;
    CorrectWordList correctWordList;

    public BKTreeSearch(BKTree bkTree, CorrectWordList vocab) {
        this.bkTree = bkTree;
        correctWordList = vocab;
        correctWordList.fillBKTree(bkTree);
        logger = LoggerProvider.getLogger(this.getClass().getName());
    }

    //Process input words in sentence.txt file
    public void processInputWords(String[] words, int maxDistance) {
        String suggestions;
        //Open output file, delete if already exists
        OutputFile output = new OutputFile(Constants.outputFilePath, true);
        BufferedWriter bw = output.openFileToWrite();
        boolean zeroIncorrectWord = true;
        try {
            for (String word : words) {
                if ((suggestions = bkTree.getWordCorrection(word, correctWordList, maxDistance)) == null) {
                    //correct word
                    logger.log(Level.INFO, "Word " + word + " is correct");
                    continue;
                }
                zeroIncorrectWord = false;
                //write suggestions to the output file
                String outputline = word + ":" + suggestions.trim().replaceAll("(,)*$", "");
                //https://stackoverflow.com/questions/7806709/remove-trailing-comma-from-comma-separated-string
                bw.write(outputline);
                logger.log(Level.INFO, "Suggestions for Word " + word + " is as following: " );
                logger.log(Level.INFO,(outputline));
                bw.newLine();
            }
            if (zeroIncorrectWord) {
                bw.write("0");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
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
}
