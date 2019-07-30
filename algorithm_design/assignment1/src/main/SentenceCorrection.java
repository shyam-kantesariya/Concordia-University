package main;

import config.Constants;
import data.CorrectWordList;
import model.InputFile;
import util.BKTree;
import util.LoggerProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

//Main class which instanciates both Linear or BKTree algo on bases of user's choice
public class SentenceCorrection {
    Logger logger;
    CorrectWordList wordList;
    int maxDistance;
    String[] inputWords;
    BKTree bkTree = new BKTree();

    public SentenceCorrection() {
        logger = LoggerProvider.getLogger(this.getClass().getName());
        //Read vocab.txt file and prepare correct word list
        this.wordList = this.preparedCorrectWordList();
        //Read MaxDistance.txt file to get k value
        this.maxDistance = this.readMaxDistance();
        //Read input sentence and prepare all words
        this.inputWords = this.getInputWords();
    }

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int algoType = 1;
        SentenceCorrection sentenceCorrection = new SentenceCorrection();
        //Command line interactive program code
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Let me know your choice");
            System.out.print("1. Linear, 2. BK-Tree, 3. Exit: ");
            try {
                algoType = Integer.parseInt(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (algoType) {
                case 1:
                    //Linear Search
                    LinearSearch linear = new LinearSearch(sentenceCorrection);
                    long startTime = System.currentTimeMillis();
                    linear.processInputWords();
                    long endTime   = System.currentTimeMillis();
                    long totalTime = endTime - startTime;
                    https://stackoverflow.com/questions/5204051/how-to-calculate-the-running-time-of-my-program
                    System.out.println("Runtime of Linear Search is: " + totalTime);
                    break;
                case 2:
                    //BKTree search
                    System.out.println("Building BKTree...");
                    BKTreeSearch bkTreeSearch = new BKTreeSearch(sentenceCorrection.bkTree, sentenceCorrection.wordList);
                    System.out.println("Congrats..! Built BKTree successfully");
                    long startTime2 = System.currentTimeMillis();
                    bkTreeSearch.processInputWords(sentenceCorrection.inputWords, sentenceCorrection.maxDistance);
                    long endTime2   = System.currentTimeMillis();
                    long totalTime2 = endTime2 - startTime2;
                    System.out.println("Runtime of BKTree Search is: " + totalTime2);
                    break;
                default:
                    //Exit
                    System.out.println();
                    System.out.println("Seems like you have done enough tests. See you soon. Good Bye!");
                    System.exit(0);
            }
        }
    }


    private CorrectWordList preparedCorrectWordList() {
        CorrectWordList wordList = new CorrectWordList();
        InputFile correctWordInputFile = new InputFile(Constants.vocabFilePath);
        String nextWord;
        BufferedReader br = correctWordInputFile.getReadHandle();
        try {
            while ((nextWord = br.readLine()) != null) {
                wordList.addWord(nextWord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            correctWordInputFile.closeAllIOHandles();
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (wordList == null) {
            this.logger.log(Level.SEVERE, "Could not prepare correct word list. Exiting the program");
            System.exit(1);
        } else {
            this.logger.log(Level.INFO, "Prepared correct word list successfully");
        }

        return wordList;
    }

    private int readMaxDistance() {
        InputFile maxDistanceInputFile = new InputFile(Constants.maxDistanceFilePath);
        int maxDist = -1;
        BufferedReader br = maxDistanceInputFile.getReadHandle();
        try {
            maxDist = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            maxDistanceInputFile.closeAllIOHandles();
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (maxDist < 0) {
            this.logger.log(Level.SEVERE, "Could not get MaxDistance value. Exiting the program");
            System.exit(1);
        } else {
            this.logger.log(Level.INFO, "MaxDistance Value is: " + maxDist);
        }

        return maxDist;
    }

    private String[] getInputWords() {
        InputFile sentenceInputFile = new InputFile(Constants.sentenceFilePath);
        String[] tokens = null;
        BufferedReader br = sentenceInputFile.getReadHandle();
        try {
            tokens = br.readLine().trim().replaceAll("\\s{2,}", " ").split(" ");
            //https://stackoverflow.com/questions/2932392/java-how-to-replace-2-or-more-spaces-with-single-space-in-string-and-delete-lead
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sentenceInputFile.closeAllIOHandles();
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (tokens == null) {
            this.logger.log(Level.SEVERE, "Could not read input sentence. Exiting the program");
            System.exit(1);
        } else {
            this.logger.log(Level.INFO, "Prepared Input sentence");
        }
        return tokens;
    }
}