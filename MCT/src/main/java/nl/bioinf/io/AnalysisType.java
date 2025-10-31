package nl.bioinf.io;
/**
 * @author Ivar Lottman
 * @version 1
 * This class determent's the Analysis type when comparing the different sample groups
 * */
public enum AnalysisType {
    NUMBER("Changes between groups will be expressed between the count diffrance number"),
    PERCENTAGE("Changes between groups will be expressed in the percentage of the count diffrance"){
        @Override
        public int calculateDifferenceByAnalysisType(int countOne, int countTwo) {return countOne/countTwo*100;}
    };

    private final String AnalysisTypeDescription;

    public static AnalysisType fromString(String description) {
        switch (description) {
        case "NUMBER": return AnalysisType.NUMBER;
        case "PERCENTAGE":return AnalysisType.PERCENTAGE;
        default: throw new IllegalArgumentException("Unknown AnalysisType: " + description);}
    }

    /**
     * @param countOne Int first abundance value
     * @param countTwo Int Second abundance value
     * @return Int calculated number based on enum value
     * Base implementation is countOne - countTwo
     */
    public int calculateDifferenceByAnalysisType(int countOne, int countTwo) {
        return countOne - countTwo;
    }

    AnalysisType(String description) {this.AnalysisTypeDescription = description;}

    @Override
    public String toString() {return AnalysisTypeDescription;}
}
