package nl.bioinf.DataManagement;

import nl.bioinf.io.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SampleGroup {
    List<Sample> samples;
    String groupName;
    StatsMethodType applyStatsMethod;
    List<Path> samplePaths;
    HashMap<NameAndGenus, List<Integer>> dataframe;
    // TODO add private hashmap and generete it in contstuctor

    public SampleGroup(List<Path> samplePaths, String groupName,
                       StatsMethodType applyStatsMethod, CountType countType,  Taxon taxon) {

        this.groupName = groupName;
        this.applyStatsMethod = applyStatsMethod;
        this.samplePaths = samplePaths;
        this.samples = new ArrayList<>();
        for (int i = 0; i < samplePaths.size(); i++) {
            Path samplePath = samplePaths.get(i);
            FileReader newSampleReader = new FileReader(samplePath, countType, taxon);
            samples.add(newSampleReader.getSample());
        }
        this.dataframe = createDataFrame();
        System.out.println(dataframe);
    }
    public SampleGroup(List<Sample> samples, String groupName){
        // Testcase
        this.groupName = groupName;
        this.samples = samples;
        this.dataframe = createDataFrame();
    }

    private HashMap<NameAndGenus, List<Integer>> createDataFrame(){

        HashMap<NameAndGenus, List<Integer>> dataframe = new HashMap<>();

        for (int i = 0; i < samples.size(); i++) {

            List<TaxonCount> taxonlist = samples.get(i).getSampleData();
            for (int j = 0; j < taxonlist.size(); j++) {
                TaxonCount currentTaxon = taxonlist.get(j);
                String namekey = currentTaxon.getScientificName();
                Taxon taxonkey = currentTaxon.getTaxonLevel();
                NameAndGenus nameAndGenus = new NameAndGenus(namekey, taxonkey);
                Integer keyvalue = currentTaxon.getAbundanceFragments();

                // contains key if statement
                if (! dataframe.containsKey(nameAndGenus)){
                    if(i == 0){
                        // New key value
                        ArrayList<Integer> emptyarraylist = new ArrayList<>();
                        emptyarraylist.add(keyvalue);
                        dataframe.put(nameAndGenus, emptyarraylist);
                    } else if (i > 0) {
                        // New key value after first iteration
                        ArrayList<Integer> intarray = new ArrayList<>();
                        for(int k = 0; k < i; k++){
                            intarray.add(0);
                        }
                        intarray.add(keyvalue);
                        dataframe.put(nameAndGenus,intarray);
                    }

                }else {
                    List<Integer> tempArray = dataframe.get(nameAndGenus);
                    if (tempArray.size()-1 == i){
                        // append value to existing key
                        tempArray.add(keyvalue);
                        dataframe.put(nameAndGenus,tempArray);
                    }else if (tempArray.size()-1 < i){
                        // append value based on missing lengt
                        int arraydif = i-tempArray.size();
                        for(int k = 0; k < arraydif; k++){
                            tempArray.add(0);
                        }
                        tempArray.add(keyvalue);
                        dataframe.put(nameAndGenus,tempArray);
                    }
                }
            }
        }
        // Post loop correction
        for(List<Integer> currentValues : dataframe.values()){
            if(currentValues.size() < samples.size()){
                int arrydif = samples.size()-currentValues.size();
                for(int k = 0; k < arrydif; k++){currentValues.add(0);}
            }
        }
        //System.out.println(dataframe);
        return  dataframe;
    }


    private void printGroupResults(){
        nl.bioinf.io.FileWriter x = new FileWriter();
    }

    public HashMap<NameAndGenus, List<Integer>> getDataframe() {return dataframe;}
}
