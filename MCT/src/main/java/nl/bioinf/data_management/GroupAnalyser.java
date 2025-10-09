package nl.bioinf.data_management;

import nl.bioinf.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
                System.out.println(p);
                if(Files.isReadable(p)){paths.add(p);}
                else throw new IOException("File not readable "+line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }

    private void doStatistics(){}
    private void printStatistics(){
        nl.bioinf.io.FileWriter x = new FileWriter();
    }
    private void printTopTen(){
        nl.bioinf.io.FileWriter x = new FileWriter();
    }




}
