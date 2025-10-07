package nl.bioinf.DataManagement;

import nl.bioinf.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GroupAnalyser {
    Path groupOnePath;
    Path groupTwoPath;
    SampleGroup sampleGroupOne;
    SampleGroup sampleGroupTwo;
    String groupOneName;
    String groupTwoName;


    public GroupAnalyser(Path groupOnePath, Path groupTwoPath, StatsMethodType statmethod,
                         AnalysisType analysisType, Taxon taxonLevel,
                         CountType countType, int minAbundanceCount) {
        this.groupOnePath = groupOnePath;
        this.groupOneName = groupOnePath.getFileName().toString();
        this.groupTwoPath = groupTwoPath;
        this.groupTwoName = groupTwoPath.getFileName().toString();
        this.sampleGroupOne = new SampleGroup(groupReader(groupOnePath),
                groupOneName, statmethod, countType, taxonLevel);
        this.sampleGroupTwo = new SampleGroup(groupReader(groupTwoPath),
                groupTwoName, statmethod, countType, taxonLevel);

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

    private void doStatistics(){}
    private void printStatistics(){
        nl.bioinf.io.FileWriter x = new FileWriter();
    }
    private void printTopTen(){
        nl.bioinf.io.FileWriter x = new FileWriter();
    }




}
