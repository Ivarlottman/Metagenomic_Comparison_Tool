package nl.bioinf.data_management;

import nl.bioinf.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static nl.bioinf.data_management.SampleGroup.makeNewKeyValue;
import static nl.bioinf.data_management.SampleGroup.replaceWithNewKeyValue;

/**
 * @author Ivar Lottman
 * @version 1
 * This is the Controller Class of MCT and initiates the reading of the config files and the sample's that follows
 * The functionality of this class is cald with .doStatistics which based on the input variables merges and
 * writes the created csv's
 */
public class GroupAnalyser {
    private static final Logger logger = LogManager.getLogger(GroupAnalyser.class);
    private Path[] groupPaths;
    private List<SampleGroup> sampleGroups;
    private AnalysisType analysisType;
    private int minAbundanceCount;
    private List<String> groupNames;
    private String outputFile;
    private File resultFolder;

    public GroupAnalyser(Path[] groupPaths, StatsMethodType statmethod,
                         AnalysisType analysisType, Taxon taxonLevel,
                         CountType countType, int minAbundanceCount, String outputFile) {

        this.groupPaths = groupPaths;
        this.sampleGroups = new ArrayList<>();
        this.groupNames = new ArrayList<>();
        this.outputFile = outputFile;
        this.analysisType = analysisType;
        this.minAbundanceCount = minAbundanceCount;
        logger.info("Initializing Sample Groups");
        init(groupPaths, statmethod, taxonLevel, countType, outputFile);
        logger.info("Finished building Sample Groups");
    }

    /**
     * Init of the GroupAnalyser constructor
     * @param groupPaths list of Path
     * @param statmethod StatsMethodType Enum
     * @param taxonLevel Taxon Enum
     * @param countType CountType Enum
     * @param outputFile String Outputfile
     * Init consists of 2 parts
     * The first part creates the output folders based on the outputFile param
     * The second part creates the sample group based on the amount of config files in the groupPaths param
     * The Sample group objects will then be made based on the StatMethod TaxonLevel and CountType enum params given
     */
    private void init(Path[] groupPaths, StatsMethodType statmethod, Taxon taxonLevel, CountType countType, String outputFile) {
        // Init result file object
        Path outputPath = Paths.get(outputFile);
        String outputAbsoluteStringPath = outputPath.toAbsolutePath().toString();
        File resultFolder = new File(outputAbsoluteStringPath+"_result","Dataframes");
        logger.debug(outputAbsoluteStringPath+"_result");

        resultFolder.mkdirs();
        this.resultFolder = resultFolder;

        // Init Sample group creation
        for (int i = 0; i < groupPaths.length; i++ ){
            String groupName = groupPaths[i].getFileName().toString();
            groupNames.add(groupName);
            SampleGroup group = new SampleGroup(groupReader(groupPaths[i]),groupName,
                    statmethod, countType, taxonLevel);
            sampleGroups.add(group);
        }
    }


    /**
     * @param inputPath Path inputpath
     * @return List of Paths
     * This method reads a Config file and passes all the sample paths to a list which is returned
     */
    private List<Path> groupReader(Path inputPath){
        List<Path> paths = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(inputPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                //TODO add other checks here
                if (line.isEmpty()) {continue;}

                Path p = Paths.get(line);
                if(Files.isReadable(p)){paths.add(p);}
                else throw new IOException("File not readable "+line);
            }
        } catch (IOException e) {
            logger.error("Error reading file "+ inputPath, e);
        }
        return paths;
    }


    /**
     *The doStatistics method is structured as follows
     * First it merges the Statframe of the samplegroup objects
     * After which it filters based on the minamum abundence amount and corrects the hashmap lengt
     * Based the Analysis type it wil then make a hashmap for the differences between the groups
     * After that based on the Absolute value a top 10 will be extracted from the hashmap
     * Lastly all the hashmaps get writen to csv
     */
    public void doStatistics(){
        logger.info("Started merging Sample groups");
        HashMap<NameAndGenus, List<Integer>> jointDataFrame = mergeSampleGroupData();

        filterlowAbundences(jointDataFrame);
        correctHashmapValueLength(jointDataFrame, sampleGroups.size());

        HashMap<NameAndGenus, List<Integer>> groupDifferanceDataFrame = calculateGroupDifferance(jointDataFrame);
        logger.info("Finished merging Sample groups");
        List<String> groupDifferanceColNames = getGroupDifferanceColNames();

        HashMap<NameAndGenus,List<Integer>> topTenDataFrame = getTopTen(groupDifferanceDataFrame);
        logger.info("Started Writing CSV files");
        printStatistics(groupDifferanceColNames, groupDifferanceDataFrame, topTenDataFrame, sampleGroups, jointDataFrame);
        logger.info("Finished writing CSV files");
    }

    /**
     * @return list of strings
     * Based on the sample group names it will make list of strings to match the group differance hashmap
     * example GroupOne_V_GroupTwo
     */
    private List<String> getGroupDifferanceColNames() {
        List<String> groupDifferanceColNames = new ArrayList<>();
        int startPosition = 0;
        for (int endPosition = 1; endPosition < sampleGroups.size(); endPosition++){
            String startString = groupNames.get(startPosition);
            String endString = groupNames.get(endPosition);
            StringBuilder newString = new StringBuilder();
            newString.append(startString);
            newString.append("_V_");
            newString.append(endString);
            groupDifferanceColNames.add(newString.toString());
        }
        logger.debug("groupDifferanceColNames: "+groupDifferanceColNames);
        return groupDifferanceColNames;
    }

    /**
     * @param jointDataFrame Hashmap< NameAndGenus record, List Integer>
     * @return groupDifferanceDataFrame
     * Based on the jointDataFrame hashmap this method makes a new hashmap comparing each group calculated with the
     * AnalysisType Enum.
     * The differences calculated are in order of the sample groups
     */
    private HashMap<NameAndGenus, List<Integer>> calculateGroupDifferance(HashMap<NameAndGenus, List<Integer>> jointDataFrame) {
        HashMap<NameAndGenus, List<Integer>> groupDifferanceDataFrame = new HashMap<>();
        for (NameAndGenus currentKey : jointDataFrame.keySet()){
            int startPosition = 0;
            List<Integer> currentList = jointDataFrame.get(currentKey);
            List<Integer> tempArray = new ArrayList<>();
            // Adds an int based on the differance of group(i) and group(j)
            for (int endPosition = 1; endPosition < currentList.size(); endPosition++){
                int startValue = currentList.get(startPosition);
                int endValue = currentList.get(endPosition);
                int differenceInGroupAbundance = analysisType.calculateDifferenceByAnalysisType(startValue, endValue);
                tempArray.add(differenceInGroupAbundance);
                startPosition++;
            }
            groupDifferanceDataFrame.put(currentKey,tempArray);
        }
        logger.debug("groupDifferanceDataFrame: "+groupDifferanceDataFrame);
        return groupDifferanceDataFrame;
    }

    /**
     * @param jointDataFrame HashMap NameAndGenus, List Integer
     * @param size Integer size of the expected int array in the hashmap
     * This function corrects missing values in the hashmap entry's that don't match the size of expected int array
     * Missing values are replaced with 0
     */
    static void correctHashmapValueLength(HashMap<NameAndGenus, List<Integer>> jointDataFrame, int size) {
        for(List<Integer> currentValues : jointDataFrame.values()){
            if(currentValues.size() < size){
                int arrayDifferance = size-currentValues.size();
                for(int k = 0; k < arrayDifferance; k++){currentValues.add(0);}
            }
        }
    }

    /**
     * @param jointDataFrame Hashmap NameAndGenus List<Integer>
     * This Method Makes a list of entry's to remove if all value's in the entry are below the minamum abundonce count
     * After which All the entry's in the list are removed from the Hashmap
     */
    private void filterlowAbundences(HashMap<NameAndGenus, List<Integer>> jointDataFrame) {
        List<NameAndGenus> removalList = new ArrayList<>();
        for(NameAndGenus currentKey : jointDataFrame.keySet()){
            List<Integer> currentList = jointDataFrame.get(currentKey);
            int count = 0;
            for(int i =0; i < currentList.size();i++){
                int currentValue = currentList.get(i);
                if (currentValue <= minAbundanceCount){ count++;}
            }
            if (count == currentList.size()){removalList.add(currentKey);}
        }
        for(int y = 0; y < removalList.size(); y++){
            jointDataFrame.remove(removalList.get(y));}
        logger.debug("removalList: "+removalList);
    }

    /**
     * @return JointDataFrame Hashmap NameAndGenus List Integer
     * This method merges all the samplegroup object statframe hashmap NameAndGenus, Int to a new NameAndGenus list
     * Integer Hashmap. Missing values in the array are replaced with 0
     */
    private HashMap<NameAndGenus, List<Integer>> mergeSampleGroupData() {
        HashMap<NameAndGenus, List<Integer>> jointDataFrame = new HashMap<>();
        for (int currentSampleGroupIndex = 0; currentSampleGroupIndex < sampleGroups.size(); currentSampleGroupIndex++ ){
            //Sample loop
            SampleGroup sampleCurrentGroup = sampleGroups.get(currentSampleGroupIndex);
            HashMap<NameAndGenus, Integer> currentHashMap = sampleCurrentGroup.getGroupStatframe();

            for (NameAndGenus currentKey : currentHashMap.keySet()) {
                //New hashmap loop inserting statframe hashmap values
                int currentValue = currentHashMap.get(currentKey);
                if (!jointDataFrame.containsKey(currentKey)){
                    makeNewKeyValue(currentSampleGroupIndex, currentValue, jointDataFrame, currentKey);
                }else {
                    replaceWithNewKeyValue(jointDataFrame, currentKey, currentSampleGroupIndex, currentValue);
                }
            }
        }
        logger.debug("Merged dataframe "+jointDataFrame);
        return jointDataFrame;


    }

    /**
     * @param abundanceList list of integers
     * @return integer result
     * the result integer is the sum of the absolute value of all the values in the param list
     */
    private int getAbsoluteValue(List<Integer> abundanceList){
        int result = 0;
        int sum = 0;
        int len = abundanceList.size();
        for (int j = 0; j < len; j++) {
            int absoluteValue = Math.abs(abundanceList.get(j));
            try {
                sum = Math.addExact(sum, absoluteValue);
            }catch (ArithmeticException e){logger.error("While comparing groups the sum of the absolute value exceeded integer capacity");}
        }
        result = sum;
        return result;
    }

    /**
     * @param dataFrame Hashmap NameAndGenus, List Integer
     * @return Hashmap[Size=10] NameAndGenus, List Integer
     * This Method sorts all the entry's in the Param hashmap based on the sum of the absolute values in the integer list
     * It returns a new Hashmap with the 10 most changed entry's
     */
    private HashMap<NameAndGenus, List<Integer>> getTopTen(HashMap<NameAndGenus, List<Integer>> dataFrame){
        HashMap<NameAndGenus, Integer> unsortedHashMap = new HashMap<>();
        for ( Map.Entry<NameAndGenus, List<Integer>> entry: dataFrame.entrySet()){
            unsortedHashMap.put(entry.getKey(),getAbsoluteValue(entry.getValue()));
        }

        List<Map.Entry<NameAndGenus, Integer>> sortingList = new ArrayList<>(unsortedHashMap.entrySet());
        sortingList.sort(Map.Entry.comparingByValue());

        HashMap<NameAndGenus, List<Integer>> topTenSortedHashMap = new LinkedHashMap<>(10);
        // Sorted list is set from low to high so last 10 will be used in the hashmap
        int sortingListSize = sortingList.size();
        int sortingListIndex = sortingList.size() - 10;
        
        for (int x = sortingListIndex; x < sortingListSize; x++) {
            NameAndGenus currentNameAndGenus = sortingList.get(x).getKey();
            List<Integer> currentValues = dataFrame.get(currentNameAndGenus);
            topTenSortedHashMap.put(currentNameAndGenus, currentValues);
        }
        return topTenSortedHashMap;
    }

    /**
     * @param compareColNames List of Strings containing coll names of the compare hashmap
     * @param compareHashmap Hashmap NameAndGenus list Integer
     * @param topTenSortedHashMap Hashmap NameAndGenus list Integer [Size=10] based on compareHashMap
     * @param sampleGroups List of SampleGroup Object
     * @param jointGroupDataframe Hashmap NameAndGenus List Integer containing the abundances of all the groups pre
     * stat calculation
     * This passes the hashmaps made in the doStatistic method and the groupDataFrame's in the SampleGroup objects
     * into the MctFileWriter Along with the coll names that match the index of the integer list
     *                            and the outputResultFolder
     */
    private void printStatistics(List<String> compareColNames, HashMap<NameAndGenus, List<Integer>> compareHashmap,
                                 HashMap<NameAndGenus, List<Integer>> topTenSortedHashMap, List<SampleGroup> sampleGroups,
                                 HashMap<NameAndGenus,List<Integer>> jointGroupDataframe) {

        String topTenFileName = "TopTen";
        MctFileWriter topTenFileWriter = new MctFileWriter(compareColNames,topTenSortedHashMap, resultFolder, topTenFileName);
        topTenFileWriter.printDataFrame();

        String compareFileName = "GroupComparison";
        MctFileWriter compareFileWriter = new MctFileWriter(compareColNames,compareHashmap,resultFolder, compareFileName);
        compareFileWriter.printDataFrame();

        String groupStatFileName = "GroupStat";
        MctFileWriter groupStatFileWriter = new MctFileWriter(groupNames,jointGroupDataframe,resultFolder,groupStatFileName);
        groupStatFileWriter.printDataFrame();

        for  (SampleGroup sampleGroup : sampleGroups) {
            String fileName = sampleGroup.getGroupName();
            List<String> colNames = sampleGroup.getSampleNames();
            HashMap<NameAndGenus,List<Integer>> dataFrame = sampleGroup.getGroupDataframe();
            MctFileWriter sampleFileWriter = new MctFileWriter(colNames,dataFrame,resultFolder,fileName);
            sampleFileWriter.printDataFrame();
        }

    }





}
