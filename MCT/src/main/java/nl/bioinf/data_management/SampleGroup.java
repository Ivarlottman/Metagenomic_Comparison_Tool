package nl.bioinf.data_management;

import nl.bioinf.io.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SampleGroup {
    /**
     * text
     * @author Ivar Lottman
     * @version 0
     * */

    private List<Sample> samples;
    private String groupName;
    private StatsMethodType applyStatsMethod;
    private List<Path> samplePaths;
    private List<String> sampleNames;
    private HashMap<NameAndGenus, List<Integer>> groupDataframe;
    private HashMap<NameAndGenus, Integer> groupStatframe;

    public SampleGroup(List<Path> samplePaths, String groupName,
                       StatsMethodType applyStatsMethod, CountType countType,  Taxon taxon) {

        this.groupName = groupName;
        this.applyStatsMethod = applyStatsMethod;
        this.samplePaths = samplePaths;
        this.samples = new ArrayList<>();
        this.sampleNames = new ArrayList<>();
        for (int i = 0; i < samplePaths.size(); i++) {
            Path samplePath = samplePaths.get(i);
            FileReader newSampleReader = new FileReader(samplePath, countType, taxon);
            samples.add(newSampleReader.getSample());
            sampleNames.add(newSampleReader.getSample().sampleName());
        }
        this.groupDataframe = createDataFrame();
        this.groupStatframe = extractStatFrame();
    }

    private HashMap<NameAndGenus, Integer> extractStatFrame() {
        HashMap<NameAndGenus, Integer> groupStatframe = new HashMap<>();

        //TODO try catch
        for (NameAndGenus i : groupDataframe.keySet()) {
            List<Integer> currentList = groupDataframe.get(i);
            int statResult = applyStatsMethod.calculateByStatMethod(currentList);
            groupStatframe.put(i, statResult);
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
        GroupAnalyser.correctHashmapValueLength(dataframe, samples.size());
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

    public HashMap<NameAndGenus, Integer> getGroupStatframe() {
            HashMap<NameAndGenus, Integer> returndGroupStatframe = groupStatframe;
        return returndGroupStatframe;
    }

    public HashMap<NameAndGenus, List<Integer>> getGroupDataframe() {
        HashMap<NameAndGenus, List<Integer>> returndGroupDataframe = groupDataframe;
        return returndGroupDataframe;
    }

    public String getGroupName() {
        String returnString = groupName;
        return returnString;
    }
    public List<String> getSampleNames() {
        List<String> returnSampleNames = sampleNames;
        return returnSampleNames;
    }
}
