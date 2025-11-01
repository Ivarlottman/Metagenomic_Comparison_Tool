package nl.bioinf.io;

import nl.bioinf.data_management.GroupAnalyser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import picocli.CommandLine.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Ivar Lottman
 * @version 1
 * This is the command line parser class that handles the commandline input and passes it into the GroupAnalyser
 * Controller class which then preforms the functionality of this tool
 */
@Command(name = "CommandLineParser")
public class CommandLineParser implements Runnable {
    private static final Logger logger = (Logger) LogManager.getLogger(CommandLineParser.class);

    @Option(names = {"-o", "-output"}, description = "output file, Default value: ${DEFAULT-VALUE}")
    String outputFile = System.getProperty("user.dir");

    @Option(names = {"-m", "-method-type"}, description = "Method to be used in analysis: Mean or Median." +
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

    @Option(names = {"-v, -verbosity"}, description = "Verbosity level")
    String verbosity = "";

    @Parameters(paramLabel = "Group 1", description = "First Config file for group 1")
    String configOne;

    @Parameters(paramLabel = "Group 2", description = "Second Config file for group 2")
    String configTwo;

    /**
     * @param filePath String filepath
     * @param validExtension String .Extension
     * @return Boolean
     * This method checks a filepath and its extension
     */
    public boolean checkFilePath(String filePath, String validExtension) {
            //Path outputPath = Paths.get(outputFile);
            //Path readCheck = outputPath.getParent();
            //    if (Files.isReadable(readCheck) == true) return outputPath;
            //    else throw new IllegalArgumentException("ouptput path is not readable");
        return false;
    }

    /**
     * This method Runs the commandline parser and validates the filepaths and then passes all the options into the
     * GroupAnalyser controller class
     */
    @Override
    public void run() {
        MctLogger Mctlogger = new MctLogger();
        //Mctlogger.applyLogger(verbosity.length());
        Mctlogger.applyLogger(2);
        logger.info("Starting command line parsing");


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
                methodType, analysisType, taxonLevel, countType, minAbundanceCount, outputFile);
        analyserObject.doStatistics();
    }
}