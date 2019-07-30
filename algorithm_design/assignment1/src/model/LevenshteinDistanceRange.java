package model;

//Min and Max rangle for Levenshtein Distance calculation
public class LevenshteinDistanceRange {
    private int minVal, maxVal;
    public LevenshteinDistanceRange(int distance, int maxDistance){
        minVal = distance - maxDistance;
        maxVal = distance + maxDistance;
    }

    public int getMinVal(){
        return minVal;
    }

    public int getMaxVal(){
        return maxVal;
    }
}
