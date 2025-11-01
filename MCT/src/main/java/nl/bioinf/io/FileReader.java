package nl.bioinf.io;

import nl.bioinf.data_management.GroupAnalyser;
import nl.bioinf.data_management.Sample;
import nl.bioinf.data_management.TaxonCount;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Ivar Lottman
 * @version 1
 * This class reads in a krapport file and builds a Sample object out of it
 * The sample object is made based on the Taxon and countType enum provided
 */
public class FileReader {
    private static final Logger logger = LogManager.getLogger(FileReader.class);
    private Sample sample;
    private Path samplePath;
    private Taxon taxonLevel;
    private CountType countType;


    public FileReader(Path inputSamplePath, CountType countType, Taxon taxonLevel) {
        this.samplePath = inputSamplePath;
        this.taxonLevel = taxonLevel;
        this.countType = countType;
        this.sample = readSample();

    }

    /**
     * @return newSample Sample Record object
     * param SamplePath Path filepath
     * param taxonLevel Taxon Enum
     * param countType CountType Enum
     * This method reads in the sample file with the path provided.
     * It makes a Sample object with the filename from the filepath and the List TaxonCount record from
     * each entry in the krapport. The countType is determend by the countType enum which coresponds with the description
     * of the enum and decides the type of abundance used in the TaxonCount. The taxonLevel enum decides which taxons will
     * be put in if the value is All every entry will be put in the taxonCount list otherwise only the specified TaxonValue
     * will be appended in the TaxonCount list
     */
    private Sample readSample(){

        Sample newSample = new Sample("placeholder",new ArrayList<TaxonCount>());
        String sampleName = samplePath.getFileName().toString();
        logger.info("Reading sample file: "+ sampleName);

        try(BufferedReader reader = Files.newBufferedReader(samplePath)) {

            String line;
            List<TaxonCount> taxonCounts = new ArrayList<>();
            int counter = 0; //TODO Add in later error msgs

            while ((line = reader.readLine()) != null) {
                counter++;

                String[] splitLine = line.split("\t");
                Taxon taxonValue = Taxon.fromString(splitLine[3]);
                int count = 0;

                // All taxon path
                if (taxonLevel == Taxon.A){
                    addTaxon(count, splitLine, taxonValue, taxonCounts);
                }
                // Specific taxon path
                else if (taxonLevel == taxonValue) {
                    addTaxon(count, splitLine, taxonValue, taxonCounts);
                }else continue;

            }
            newSample = new Sample(sampleName, taxonCounts);
        } catch (IOException e) {
            logger.error("Failed to read sample file: " + samplePath); }

        return newSample;
    }

    /**
     * @param count Abundance count int
     * @param splitLine Split String [Size=5]
     * @param taxonValue Taxon enum
     * @param taxonCounts List of taxonCount record
     * This method adds a TaxonCount record to the TaxonCount list of a sample based on the CountType index
     */
    private void addTaxon(int count, String[] splitLine, Taxon taxonValue, List<TaxonCount> taxonCounts) {
        if(countType == CountType.DIRECT){
            count = Integer.parseInt(splitLine[2]);
        }
        else if (countType == CountType.ALL){
            count = Integer.parseInt(splitLine[1]);
        }
        String name = splitLine[5];
        TaxonCount newTaxon = new TaxonCount(count, taxonValue, name);
        taxonCounts.add(newTaxon);
    }

    public Sample getSample() {
        Sample copySample = sample;
        return copySample;}
}
