package nl.bioinf.DataManagement;

import nl.bioinf.io.Taxon;

import java.util.List;

public record Sample(String sampleName, List<TaxonCount> sampleData) {

    public String getSampleName() {return sampleName;}
    //public String getGroupName() {return groupName;}
    public List<TaxonCount> getSampleData() {return sampleData;}
}
