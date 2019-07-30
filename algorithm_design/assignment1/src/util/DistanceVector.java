package util;

import java.util.logging.Logger;

//Calculate distance vector

public class DistanceVector {
    Logger logger = LoggerProvider.getLogger(this.getClass().getName());
    public int getDistance(String word1, String word2){
        int rows = word1.length() + 1;
        int cols = word2.length() + 1;
        char[] firstWord = word1.trim().toLowerCase().toCharArray(),
               secondWord = word2.trim().toLowerCase().toCharArray();
        int[][] vector = new int[rows][cols];
        for(int i=0; i<rows; i++){
            vector[i][0] = i;
        }
        for(int j=0; j<cols; j++){
            vector[0][j] = j;
        }
        for(int i = 1; i<rows; i++){
            for(int j=1; j<cols; j++){
                if(firstWord[i-1] == secondWord[j-1]){
                    vector[i][j] = vector[i-1][j-1];
                } else {
                    vector[i][j] = Math.min(vector[i][j-1], Math.min(vector[i-1][j], vector[i-1][j-1])) + 1;
                }
            }
        }
        //logger.log(Level.INFO, "Distance between " + word1 + " and "+ word2 + " is " + vector[rows-1][cols-1]);
        return vector[rows-1][cols-1];
    }
}
