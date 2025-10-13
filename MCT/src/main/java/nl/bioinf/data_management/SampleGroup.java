package nl.bioinf.data_management;

import nl.bioinf.io.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SampleGroup {
    /**
     * text
     * @author Ivar Lottman
     * @version 0
     * */

    List<Sample> samples;
    String groupName;
    StatsMethodType applyStatsMethod;
    List<Path> samplePaths;
    HashMap<NameAndGenus, List<Integer>> groupDataframe;
    HashMap<NameAndGenus, Integer> groupStatframe;

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
        this.groupDataframe = createDataFrame();
        this.groupStatframe = extractStatFrame();
    }

    private HashMap<NameAndGenus, Integer> extractStatFrame() {
        HashMap<NameAndGenus, Integer> groupStatframe = new HashMap<>();

        //TODO try catch
        for (NameAndGenus i : groupDataframe.keySet()) {
            if(applyStatsMethod == StatsMethodType.MEAN) {
                int sum = 0;
                int len = groupDataframe.get(i).size();
                for (int j = 0; j < len; j++) {
                    //sum += groupDataframe.get(i).get(j);
                    sum = Math.addExact(sum, groupDataframe.get(i).get(j));
                }
                int mean = sum / len;
                groupStatframe.put(i, mean);
            }
            else if(applyStatsMethod == StatsMethodType.MEDIAN){
                int len = groupDataframe.get(i).size()/2;
                int median = groupDataframe.get(i).get(len);
                groupStatframe.put(i, median);
            }
        }
        return groupStatframe;
    }


    private HashMap<NameAndGenus, List<Integer>> createDataFrame(){

        HashMap<NameAndGenus, List<Integer>> dataframe = new HashMap<>();

        for (int i = 0; i < samples.size(); i++) {

            List<TaxonCount> taxonlist = samples.get(i).sampleData();
            for (int j = 0; j < taxonlist.size(); j++) {
                TaxonCount currentTaxon = taxonlist.get(j);
                String namekey = currentTaxon.scientificName().strip();
                Taxon taxonkey = currentTaxon.taxonLevel();
                NameAndGenus nameAndGenus = new NameAndGenus(namekey, taxonkey);
                Integer keyvalue = currentTaxon.abundanceFragments();

                if (! dataframe.containsKey(nameAndGenus)){
                    makeNewKeyValue(i, keyvalue, dataframe, nameAndGenus);
                }else {
                    replaceWithNewKeyValue(dataframe, nameAndGenus, i, keyvalue);
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
        return  dataframe;
    }

    static void replaceWithNewKeyValue(HashMap<NameAndGenus, List<Integer>> dataframe, NameAndGenus nameAndGenus, int i, Integer keyvalue) {
        List<Integer> tempArray = dataframe.get(nameAndGenus);
        if (tempArray.size()-1 == i){
            // append value to existing key
            tempArray.add(keyvalue);
            dataframe.put(nameAndGenus,tempArray);
        }else if (tempArray.size()-1 < i){
            // append value based on missing lengt
            int arraydif = i -tempArray.size();
            for(int k = 0; k < arraydif; k++){
                tempArray.add(0);
            }
            tempArray.add(keyvalue);
            dataframe.put(nameAndGenus,tempArray);
        }
    }

    static void makeNewKeyValue(int i, Integer keyvalue, HashMap<NameAndGenus, List<Integer>> dataframe, NameAndGenus nameAndGenus) {
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
    }


    private void printGroupResults(){
        nl.bioinf.io.FileWriter x = new FileWriter();
    }

    //public HashMap<NameAndGenus, List<Integer>> getDataframe() {return dataframe;}
}
