package nl.bioinf.io;

import java.util.List;

/**
 * text
 * @author Ivar Lottman
 * @version 0
 * */
public enum StatsMethodType {
    MEAN("Differences in groups wil be calculated with the group average"),
    MEDIAN("Differences in groups wil be calculated with the group meadian");

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

    public int calculateByStatMethod(List<Integer> inputList){
        int result = 0;
        if(this == MEAN){
            int sum = 0;
            int len = inputList.size();
            for (int j = 0; j < len; j++) {
                sum = Math.addExact(sum, inputList.get(j));
            }
            int mean = sum / len;
            result = mean;
        }
        else if(this == MEDIAN){

        }

        return result;
    }

    @Override
    public String toString() {return statsMethodTypeDescription;}

}
