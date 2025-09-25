package nl.bioinf.io;

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
    @Override
    public String toString() {return statsMethodTypeDescription;}

}
