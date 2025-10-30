package nl.bioinf.io;

import nl.bioinf.data_management.Sample;
import nl.bioinf.data_management.TaxonCount;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// placeholder locations in data
//System.out.println(splitLine[0]+" pos0 to root");
//System.out.println(splitLine[1]+" pos1 clade all");
//System.out.println(splitLine[2]+" pos2 clade direct");
//System.out.println(splitLine[3]+" pos3 taxon");
//System.out.println(splitLine[4]+" pos4 id");
//System.out.println(splitLine[5]+" pos5 name");


/**
 *
 */
public class FileReader {
    /**
     * @author Ivar Lottman
     * @version 0
     * This class reads in the kraport file from the kraken2 tool and puts it into a Sample record with
     * A TaconCount record containing an abundancie count, taxon enum and a sientific name.
     * */

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
    private Sample readSample(){
        /**
         * @author Ivar Lottman
         * @version 0
         * @param this.samplePath
         * @param this.taxonLevel
         * @param this.countType
         * @return this.sample object
         * This method uses the samplepath parameter in a Files.BufferdReader object to read in the file
         * Based of the taxonLevel provided to this class it will make a selection of which taxons to put
         * in the sample object, after which based on the countType provided it will add a taxon to the taxonlist
         * of the sample object once the file is done reading the sample object can be retrieved by other classes.
         *
         */
        Sample newSample = new Sample("placeholder",new ArrayList<TaxonCount>());
        String sampleName = samplePath.getFileName().toString();

        try(BufferedReader reader = Files.newBufferedReader(samplePath)) {

            String line;
            List<TaxonCount> taxonCounts = new ArrayList<>();
            int counter = 0; //TODO Add in later error msgs

            while ((line = reader.readLine()) != null) {
                counter++;

                String[] splitLine = line.split("\t");
                Taxon taxonValue = Taxon.fromString(splitLine[3]);
                int count = 0;

                // All path
                if (taxonLevel == Taxon.A){
                    addTaxon(count, splitLine, taxonValue, taxonCounts);
                }
                // Specific path
                else if (taxonLevel == taxonValue) {
                    addTaxon(count, splitLine, taxonValue, taxonCounts);
                }else continue;

            }
            newSample = new Sample(sampleName, taxonCounts);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newSample;
    }

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

    public Sample getSample() {return sample;}
}
