package nl.bioinf.data_management;

import nl.bioinf.io.Taxon;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



class SampleGroupTest {

    TaxonCount rowOneSampleOne = new TaxonCount(100, Taxon.S,"monkey");
    TaxonCount rowTwoSampleOne = new TaxonCount(100, Taxon.S,"donkey");
    TaxonCount rowTreeSampleOne = new TaxonCount(100, Taxon.S,"cat");
    TaxonCount rowFourSampleOne = new TaxonCount(100, Taxon.G,"dog");

    TaxonCount rowOneSampleTwo = new TaxonCount(100, Taxon.S,"monkey");
    TaxonCount rowTwoSampleTwo = new TaxonCount(100, Taxon.S,"cat");
    TaxonCount rowTreeSampleTwo = new TaxonCount(100, Taxon.S,"rat");
    TaxonCount rowFourSampleTwo = new TaxonCount(100, Taxon.G,"dog");

    TaxonCount rowOneSampleTree = new TaxonCount(100, Taxon.S,"monkey");
    TaxonCount rowTwoSampleTree = new TaxonCount(100, Taxon.S,"donkey");
    TaxonCount rowTreeSampleTree = new TaxonCount(100, Taxon.S,"dog");
    TaxonCount rowFourSampleTree = new TaxonCount(100, Taxon.G,"dog");

    @Test
    void testMakeDataframe(){
        /**
         * Species name for behavior path
         * Monkey = Happy path Present in all samples
         * Donkey = Interrupted path Not present in the 2nd sample [must add 0 value]
         * cat = incomplete end path Not present in the 3rd sample [must add 0 value at end]
         * rat = incomplete path Not present in 1st and 3rd sample [must add 0 value at beginning and end]
         * dog = incomplete beginning path Not present in 1st and 2nd sample[must add 0 value inplace]
         * dog G = same string name diffrent genus to test record class
         * */

        HashMap<NameAndGenus, List<Integer>> expectedDataframe = new HashMap<>();
        ArrayList<Integer> monkey = new ArrayList<>();
        monkey.add(100);
        monkey.add(100);
        monkey.add(100);
        ArrayList<Integer> donkey = new ArrayList<>();
        donkey.add(100);
        donkey.add(0);
        donkey.add(100);
        ArrayList<Integer> cat = new ArrayList<>();
        cat.add(100);
        cat.add(100);
        cat.add(0);
        ArrayList<Integer> rat = new ArrayList<>();
        rat.add(0);
        rat.add(100);
        rat.add(0);
        ArrayList<Integer> dog = new ArrayList<>();
        dog.add(0);
        dog.add(0);
        dog.add(100);
        NameAndGenus monkeyrec = new NameAndGenus("monkey",Taxon.S);
        NameAndGenus donkeyrec = new NameAndGenus("donkey",Taxon.S);
        NameAndGenus catrec = new NameAndGenus("cat", Taxon.S);
        NameAndGenus ratrec = new NameAndGenus("rat", Taxon.S);
        NameAndGenus dogrec = new NameAndGenus("dog", Taxon.S);

        expectedDataframe.put(monkeyrec,monkey);
        expectedDataframe.put(donkeyrec,donkey);
        expectedDataframe.put(catrec,cat);
        expectedDataframe.put(ratrec,rat);
        expectedDataframe.put(dogrec,dog);

        List<TaxonCount> sampleOneList = new ArrayList<>();
        sampleOneList.add(rowOneSampleOne);
        sampleOneList.add(rowTwoSampleOne);
        sampleOneList.add(rowTreeSampleOne);
        //sampleOneList.add(rowFourSampleOne);
        List<TaxonCount> sampleTwoList = new ArrayList<>();
        sampleTwoList.add(rowOneSampleTwo);
        sampleTwoList.add(rowTwoSampleTwo);
        sampleTwoList.add(rowTreeSampleTwo);
        //sampleTwoList.add(rowFourSampleTwo);
        List<TaxonCount> sampleThreeList = new ArrayList<>();
        sampleThreeList.add(rowOneSampleTree);
        sampleThreeList.add(rowTwoSampleTree);
        sampleThreeList.add(rowTreeSampleTree);
        //sampleThreeList.add(rowFourSampleTree);
        Sample sampleOne = new Sample("testSampleOne",sampleOneList);
        Sample sampleTwo = new Sample("testSampleTwo",sampleTwoList);
        Sample sampleThree = new Sample("testSampleThree",sampleThreeList);
        List<Sample> testSamples = new ArrayList<>();
        testSamples.add(sampleOne);
        testSamples.add(sampleTwo);
        testSamples.add(sampleThree);

        SampleGroup test = new SampleGroup(testSamples, "testgroup");

        HashMap<NameAndGenus, List<Integer>> testframe = test.getDataframe();
        assertThat(testframe).isEqualTo(expectedDataframe);
        System.out.println(testframe);
        System.out.println(expectedDataframe);
    }



}