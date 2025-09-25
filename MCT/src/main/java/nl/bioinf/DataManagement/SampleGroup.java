package nl.bioinf.DataManagement;

import nl.bioinf.io.StatsMethodType;
import nl.bioinf.io.Taxon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SampleGroup {
    Sample[] samples;
    String groupName;
    StatsMethodType applyStatsMethod;



    public SampleGroup(Sample[] samples, String groupName, StatsMethodType applyStatsMethod) {
        this.samples = samples;
        this.groupName = groupName;
        this.applyStatsMethod = applyStatsMethod;
    }


    public void createDataFrame(){
        //TODO refactor hashmap string to nameandgenus record
        record nameAndGenus(String name, Taxon genus){};
        HashMap<String, ArrayList<Integer>> dataframe = new HashMap<>();

        for (int i = 0; i < samples.length; i++) {
            //samples[i].sampleData()
            TaxonCount[] taxonlist = samples[i].getSampleData();
            for (int j = 0; j < taxonlist.length; j++) {

                String namekey = taxonlist[j].getScientificName();
                Integer keyvalue = taxonlist[j].getAbundanceFragments();

                if (dataframe.get(namekey) == null) {
                    if(i == 0){
                        ArrayList<Integer> emptyarraylist = new ArrayList<>();
                        emptyarraylist.add(keyvalue);
                        dataframe.put(namekey, emptyarraylist);
                } else if (i > 0) {
                        ArrayList<Integer> intarray = new ArrayList<>();
                        for(int k = 0; k < i; k++){
                            intarray.add(0);
                        }
                        intarray.add(keyvalue);
                        dataframe.put(namekey,intarray);
                    }


                }


            }

        }
    }





}
