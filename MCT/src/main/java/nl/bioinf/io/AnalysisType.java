package nl.bioinf.io;
/**
 * text
 * @author Ivar Lottman
 * @version 0
 * */
public enum AnalysisType {

    NUMBER("Changes between groups will be expressed between the count diffrance number"),
    PERCENTAGE("Changes between groups will be expressed in the percentage of the count diffrance");

    private final String AnalysisTypeDescription;
    AnalysisType(String description) {this.AnalysisTypeDescription = description;}

    public static AnalysisType fromString(String description) {
        switch (description) {
        case "NUMBER": return AnalysisType.NUMBER;
        case "PERCENTAGE":return AnalysisType.PERCENTAGE;
        default: throw new IllegalArgumentException("Unknown AnalysisType: " + description);}
    }
    @Override
    public String toString() {return AnalysisTypeDescription;}
}
