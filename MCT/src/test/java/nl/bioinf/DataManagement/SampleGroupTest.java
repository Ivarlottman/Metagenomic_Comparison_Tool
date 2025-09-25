package nl.bioinf.DataManagement;

import nl.bioinf.io.Taxon;
import nl.bioinf.DataManagement.TaxonCount;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class SampleGroupTest {

    TaxonCount rowOneSampleOne = new TaxonCount(100, Taxon.S,"monkey");
    TaxonCount rowTwoSampleOne = new TaxonCount(100, Taxon.S,"donkey");
    TaxonCount rowTreeSampleOne = new TaxonCount(100, Taxon.S,"cat");
    TaxonCount rowOneSampleTwo = new TaxonCount(100, Taxon.S,"monkey");
    TaxonCount rowTwoSampleTwo = new TaxonCount(100, Taxon.S,"donkey");
    TaxonCount rowTreeSampleTwo = new TaxonCount(100, Taxon.S,"dog");
    // Row 3 does not match for testcase

    TaxonCount[] sampleOneRows ={rowOneSampleOne,rowTwoSampleOne,rowTreeSampleOne};
    TaxonCount[] sampleTwoRows ={rowOneSampleTwo,rowTwoSampleTwo,rowTreeSampleTwo};

    Sample sampleOne = new Sample("Sample","GroupOne", sampleOneRows);
    Sample sampleTwo = new Sample("Sample","GroupTwo", sampleTwoRows);
    Sample[] testsamples = {sampleOne, sampleTwo};





}