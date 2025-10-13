package nl.bioinf.data_management;

import nl.bioinf.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static nl.bioinf.data_management.SampleGroup.makeNewKeyValue;
import static nl.bioinf.data_management.SampleGroup.replaceWithNewKeyValue;

public class GroupAnalyser {
    /**
     * text
     * @author Ivar Lottman
     * @version 0
     * */
    Path[] groupPaths;
    //List<String> groupNames;
    List<SampleGroup> sampleGroups;
    //List<ConfigGroup> configGroups;


    public GroupAnalyser(Path[] groupPaths, StatsMethodType statmethod,
                         AnalysisType analysisType, Taxon taxonLevel,
                         CountType countType, int minAbundanceCount) {

        this.groupPaths = groupPaths;
        this.sampleGroups = new ArrayList<>();
        // TODO bespreek met michiel of het nog nodig is om een group record te maken
        for (int i = 0; i < groupPaths.length; i++ ){
            String groupName = groupPaths[i].getFileName().toString();
            SampleGroup group = new SampleGroup(groupReader(groupPaths[i]),groupName,
                    statmethod, countType, taxonLevel);
            sampleGroups.add(group);
        }

    }


    private List<Path> groupReader(Path inputpath){
        List<Path> paths = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(inputpath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                //TODO add other checks here
                if (line.isEmpty()) {continue;}

                Path p = Paths.get(line);
                if(Files.isReadable(p)){paths.add(p);}
                else throw new IOException("File not readable "+line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }


    public void doStatistics(){

        HashMap<NameAndGenus, List<Integer>> jointDataFrame = new HashMap<>();

        for (int i = 0; i < sampleGroups.size(); i++ ){
            SampleGroup sampleCurrentGroup = sampleGroups.get(i);
            HashMap<NameAndGenus, Integer> currentHashMap = sampleCurrentGroup.groupStatframe;

            for (NameAndGenus j : currentHashMap.keySet()) {
                int keyvalue = currentHashMap.get(j);
                if (!jointDataFrame.containsKey(j)){
                    makeNewKeyValue(i, keyvalue, jointDataFrame, j);
                }else {
                    replaceWithNewKeyValue(jointDataFrame, j, i, keyvalue);
                }
            }
        }
        for(List<Integer> currentValues : jointDataFrame.values()){
            if(currentValues.size() < sampleGroups.size()){
                int arrydif = sampleGroups.size()-currentValues.size();
                for(int k = 0; k < arrydif; k++){currentValues.add(0);}
            }
        }
        
    }
    private void printStatistics(){
        nl.bioinf.io.FileWriter x = new FileWriter();
    }
    private void printTopTen(){
        nl.bioinf.io.FileWriter x = new FileWriter();
    }




}
