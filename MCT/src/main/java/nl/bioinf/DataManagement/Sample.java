package nl.bioinf.DataManagement;

public record Sample(String sampleName, String groupName, TaxonCount[] sampleData) {

    public String getSampleName() {return sampleName;}
    public String getGroupName() {return groupName;}
    public TaxonCount[] getSampleData() {return sampleData;}
}
