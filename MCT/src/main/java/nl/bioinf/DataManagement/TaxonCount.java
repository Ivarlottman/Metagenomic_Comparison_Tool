package nl.bioinf.DataManagement;

import nl.bioinf.io.Taxon;

public record TaxonCount(int abundanceFragments, Taxon taxonLevel, String scientificName) {
    public int getAbundanceFragments() {return abundanceFragments;}
    public Taxon getTaxonLevel() {return taxonLevel;}
    public String getScientificName() {return scientificName;}
}
