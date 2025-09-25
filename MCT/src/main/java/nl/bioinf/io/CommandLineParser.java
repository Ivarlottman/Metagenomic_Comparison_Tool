package nl.bioinf.io;

import picocli.CommandLine.*;
import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Java documentation style class lvl
 *
 * @author Ivar
 * @version 0
 * */

@Command(name = "CommandLineParser")
public class CommandLineParser implements Runnable {
    @Option(names = {"-o", "-output"}, description = "output file")
    String outputFile;

    @Option(names = {"-m", "method-type"}, description = "Method to be used in analysis: Mean or Median")
    String methodType;

    @Option(names = {"-a", "-analysis-type"}, description = "Type of analysis: number change or %")
    String analysisType;

    @Option(names = {"-t", "-taxon"}, description =
            "Level of taxon (U)nclassified, (R)oot, (D)omain, (K)ingdom, " +
                    "(P)hylum, (C)lass, (O)rder, (F)amily, (G)enus, (S)pecies or ALL.")
    String taxonLevel;

    @Option(names = {"-ct", "-countType"}, description = "Clade all or direct")
    String countType;

    @Option(names = {"-ac","-abundanceCount"}, description = "minimal abundance count")
    int minAbundanceCount;

    @Parameters(paramLabel = "Group 1", description = "First Config file for group 1")
    String configOne;

    @Parameters(paramLabel = "Group 2", description = "Second config file for group 2")
    String configTwo;

    public Path getOutputFile(String outputFile) {
            Path outputPath = Paths.get(outputFile);
            Path readCheck = outputPath.getParent();
                if (Files.isReadable(readCheck) == true) return outputPath;
                else throw new IllegalArgumentException("ouptput path is not readable");
    }

    @Override
    public void run() {

    }
}