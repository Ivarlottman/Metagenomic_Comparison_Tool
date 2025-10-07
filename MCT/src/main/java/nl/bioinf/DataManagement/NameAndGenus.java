package nl.bioinf.DataManagement;

import nl.bioinf.io.Taxon;

public record NameAndGenus(String name, Taxon genus) {

    public String getName(){return name;}
    public Taxon getGenus(){return genus;}
}
