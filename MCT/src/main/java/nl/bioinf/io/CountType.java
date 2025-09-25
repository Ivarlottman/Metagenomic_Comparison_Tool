package nl.bioinf.io;

public enum CountType {
    ALL("All fragment counts coverd by chosen clade"),
    DIRECT("The direct fragment counts coverd by chosen clade");

    private final String countTypeDescription;
    CountType(String description) {this.countTypeDescription = description;}

    public static CountType fromString(String countType) {
        switch (countType) {
            case "All": return CountType.ALL;
            case "Direct": return CountType.DIRECT;
            default: throw new IllegalArgumentException("Unknown CountType: " + countType);
        }
    }
    @Override
    public String toString() {return countTypeDescription;}

}
