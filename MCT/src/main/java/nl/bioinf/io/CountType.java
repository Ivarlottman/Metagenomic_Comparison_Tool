package nl.bioinf.io;
/**
 * text
 * @author Ivar Lottman
 * @version 0
 * */
public enum CountType {
    ALL("All, descrip= fragment counts covered by chosen clade"),
    DIRECT("The direct fragment counts covered by chosen clade");

    private final String countTypeDescription;
    CountType(String description) {this.countTypeDescription = description;}

    public static CountType fromString(String countType) {
        switch (countType) {
            case "ALL": return CountType.ALL;
            case "DIRECT": return CountType.DIRECT;
            default: throw new IllegalArgumentException("Unknown CountType: " + countType);
        }
    }
    @Override
    public String toString() {return countTypeDescription;}

}
