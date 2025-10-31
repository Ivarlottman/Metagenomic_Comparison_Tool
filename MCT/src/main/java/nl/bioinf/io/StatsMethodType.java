package nl.bioinf.io;

import java.util.List;

/**
 * @author Ivar Lottman
 * @version 1
 * This Class decides which method gets used to compare groups
 * */
public enum StatsMethodType {
    MEAN("Differences in groups wil be calculated with the group average"),
    MEDIAN("Differences in groups wil be calculated with the group meadian"){
        @Override
        public int calculateByStatMethod(List<Integer> inputList) {
            int result = 0;
            int median = inputList.size()/2;
            result = median;
            return result;
        }
    };


    private final String statsMethodTypeDescription;
    StatsMethodType(String statsMethodTypeDescription) {
        this.statsMethodTypeDescription = statsMethodTypeDescription;
    }

    public static StatsMethodType fromString(String statsMethodTypeDescription) {
        switch (statsMethodTypeDescription) {
            case "MEAN": return StatsMethodType.MEAN;
            case "MEDIAN": return StatsMethodType.MEDIAN;
            default: throw new IllegalArgumentException("Unknown StatsMethodType: " + statsMethodTypeDescription);
        }
    }

    /**
     * @param inputList List of Integers
     * @return int result
     * This method calculates the mean or median of the inputlist based on the enums value.
     */
    public int calculateByStatMethod(List<Integer> inputList){
        int result = 0;
        int sum = 0;
        int len = inputList.size();
        for (int j = 0; j < len; j++) {
            sum = Math.addExact(sum, inputList.get(j));
            }
        int mean = sum / len;
        result = mean;
        return result;
    }

    @Override
    public String toString() {return statsMethodTypeDescription;}

}
