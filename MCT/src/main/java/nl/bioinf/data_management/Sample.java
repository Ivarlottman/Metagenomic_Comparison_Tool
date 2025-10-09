package nl.bioinf.data_management;

import java.util.List;

public record Sample(String sampleName, List<TaxonCount> sampleData) {
    /**
     * text
     * @author Ivar Lottman
     * @version 0
     * */
}
