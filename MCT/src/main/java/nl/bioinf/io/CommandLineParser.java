package nl.bioinf.io;

import nl.bioinf.data_management.GroupAnalyser;
import picocli.CommandLine.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Command(name = "CommandLineParser")
public class CommandLineParser implements Runnable {
    /**
     * Java documentation style class lvl
     * located in /build/libs
     * testcommand
     * java -jar MTC-1.0-SNAPSHOT-all.jar /home/ivar-lottman/Desktop/bioinformatica/jaar_3/java_ap/Metagenomic_Comparison_Tool/Recources/config_1.txt /home/ivar-lottman/Desktop/bioinformatica/jaar_3/java_ap/Metagenomic_Comparison_Tool/Recources/config_2.txt
     *
     * @author Ivar
     * @version 0
     * */
    @Option(names = {"-o", "-output"}, description = "output file, Default value: ${DEFAULT-VALUE}")
    String outputFile = System.getProperty("user.dir");

    @Option(names = {"-m", "method-type"}, description = "Method to be used in analysis: Mean or Median." +
            "Default value: ${DEFAULT-VALUE}")
    StatsMethodType methodType = StatsMethodType.MEAN;

    @Option(names = {"-a", "-analysis-type"}, description = "Type of analysis: number change or" +
            " percentage change. Default value: ${DEFAULT-VALUE}")
    AnalysisType analysisType = AnalysisType.NUMBER;

    @Option(names = {"-t", "-taxon"}, description =
            "Level of taxon (U)nclassified, (R)oot, (D)omain, (K)ingdom, " +
                    "(P)hylum, (C)lass, (O)rder, (F)amily, (G)enus, (S)pecies, (PC)Primitive Clasification " +
                    "and (A)LL. " +
                    "Default value: ${DEFAULT-VALUE}")
    Taxon taxonLevel = Taxon.A;

    @Option(names = {"-ct", "-countType"}, description = "Clade all or direct." +
            " Default value: ${DEFAULT-VALUE}")
     CountType countType = CountType.ALL;

    @Option(names = {"-ac","-abundanceCount"}, description = "minimal abundance count. " +
            "Default value: ${DEFAULT-VALUE}")
    int minAbundanceCount = 0;

    @Parameters(paramLabel = "Group 1", description = "First Config file for group 1")
    String configOne;

    @Parameters(paramLabel = "Group 2", description = "Second Config file for group 2")
    String configTwo;

    public Path getOutputFile(String outputFile) {
            Path outputPath = Paths.get(outputFile);
            Path readCheck = outputPath.getParent();
                if (Files.isReadable(readCheck) == true) return outputPath;
                else throw new IllegalArgumentException("ouptput path is not readable");
    }

    @Override
    public void run() {
        System.out.println("Running command line parser");
        //try catch

        // TODO build testcases for min abundance count
        // TODO move filereading if statement to method
        Path configPathOne = Paths.get(configOne);
        if (!Files.isReadable(configPathOne))
        {throw new IllegalArgumentException("config file= "+configPathOne+ " is not readable");}

        Path configPathTwo = Paths.get(configTwo);
        if (!Files.isReadable(configPathTwo))
        {throw new IllegalArgumentException("config file= "+configPathOne+ " is not readable");}

        Path[] placeholder = {configPathOne, configPathTwo};

        GroupAnalyser analyserObject = new GroupAnalyser(placeholder,
                methodType, analysisType, taxonLevel, countType, minAbundanceCount);
        analyserObject.doStatistics();
    }
}