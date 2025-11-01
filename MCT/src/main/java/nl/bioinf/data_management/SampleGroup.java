package nl.bioinf.data_management;

import nl.bioinf.io.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Ivar Lottman
 * @version 1
 * This class builds a sample group based on the samplepaths provided
 */
public class SampleGroup {
    private static final Logger logger = Logger.getLogger(SampleGroup.class.getName());
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
        logger.info("Creating sample group:"+ groupName);
        for (int i = 0; i < samplePaths.size(); i++) {
            Path samplePath = samplePaths.get(i);
            FileReader newSampleReader = new FileReader(samplePath, countType, taxon);
            samples.add(newSampleReader.getSample());
            sampleNames.add(newSampleReader.getSample().sampleName());
        }
        logger.info("Creating group dataframe");
        this.groupDataframe = createDataFrame();
        this.groupStatframe = extractStatFrame();
        logger.info("Finish creating group:"+groupName);
    }

    /**
     * param Hashmap NameAndGenus List Integer
     * @return Hashmap NameAndGenus, int
     * This method Makes a new NameAndGenus int hashmap with the applyStatMehtod enum
     */
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


    /**
     * param Samples List of samples
     * @return Hashmap NameAndGenus, List integer
     * This method creates a NameAndGenus record, List integer hashmap that for each sample adds the abundance value
     * to the list per NameAndGenus record which consists of String Name and Taxon texonlevel
     * if a NameAndGenus does not exist in a sample a 0 will be added in its place in the integer list
     */
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

    /**
     * @param dataframe  Hashmap NameAndGenus
     * @param nameAndGenus NameAndGenus record
     * @param i int sample index
     * @param keyvalue int abundance to be added
     * This method replaces the value of a NameAndGenus in the dataframe hashmap with an updated list of integers
     * if the sample index is one larger than the value list the keyvalue gets added and if the index is larger than one
     * 0 values get added till the abundance key value takes up the last space.
     */
    static void replaceWithNewKeyValue(HashMap<NameAndGenus, List<Integer>> dataframe, NameAndGenus nameAndGenus, int i, Integer keyvalue) {
        List<Integer> tempArray = dataframe.get(nameAndGenus);
        if (tempArray.size()-1 == i){
            // append value to existing key
            tempArray.add(keyvalue);
            dataframe.put(nameAndGenus,tempArray);
        }else if (tempArray.size()-1 < i){
            // append value based on missing length
            int arraydif = i -tempArray.size();
            for(int k = 0; k < arraydif; k++){
                tempArray.add(0);
            }
            tempArray.add(keyvalue);
            dataframe.put(nameAndGenus,tempArray);
        }
    }

    /**
     * @param i int sample index
     * @param keyvalue int abundance to be added to the list
     * @param dataframe Hashmap NameAndGenus to be added to
     * @param nameAndGenus NameAndGenus Record to be added to the hashmap
     * This method makes adds a new Key Value to the dataframe hashmap based on the index of the sample loop
     * if this is the first loop a new array is made and is put in with the NameAndGenus key and if not it adds 0 value
     * to the array until the abundance value can be placed in the last spot of the index
     */
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
