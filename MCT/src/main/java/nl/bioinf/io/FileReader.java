package nl.bioinf.io;

import nl.bioinf.DataManagement.Sample;
import nl.bioinf.DataManagement.TaxonCount;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
    Sample sample;
    Path samplePath;
    Taxon taxonLevel;
    CountType countType;


    public FileReader(Path inputSamplePath, CountType countType, Taxon taxonLevel) {
        this.samplePath = inputSamplePath;
        this.taxonLevel = taxonLevel;
        this.countType = countType;
        this.sample = readSample();

    }
    private Sample readSample(){
        Sample newSample = new Sample("placeholder",new ArrayList<TaxonCount>());
        String sampleName = samplePath.getFileName().toString();

        try(BufferedReader reader = Files.newBufferedReader(samplePath)) {
            String line;
            List<TaxonCount> taxonCounts = new ArrayList<>();
            int counter = 0; //TODO Add in later error msgs
            while ((line = reader.readLine()) != null) {
                counter++;
                String[] splitLine = line.split("\t");
                int count = 0;
                // All path
                if (taxonLevel == Taxon.A){
                    Taxon taxonvalue = Taxon.fromString(splitLine[3]);
                    if(countType == CountType.DIRECT){count = Integer.parseInt(splitLine[2]);
                    }
                    else if (countType == CountType.ALL){count = Integer.parseInt(splitLine[1]);
                    }
                    String name = splitLine[5];
                    TaxonCount newTaxon = new TaxonCount(count, taxonvalue, name);
                    taxonCounts.add(newTaxon);
                }
                // Specific path
                else if (taxonLevel == Taxon.fromString(splitLine[3])) {
                    Taxon taxonvalue = Taxon.fromString(splitLine[3]);
                    if(countType == CountType.DIRECT){count = Integer.parseInt(splitLine[2]);
                    }
                    else if (countType == CountType.ALL){count = Integer.parseInt(splitLine[1]);
                    }
                    String name = splitLine[5];
                    TaxonCount newTaxon = new TaxonCount(count, taxonvalue, name);
                    taxonCounts.add(newTaxon);
                }else continue;

                // placeholder locations
                //System.out.println(splitLine[0]+" pos0 to root");
                //System.out.println(splitLine[1]+" pos1 clade all");
                //System.out.println(splitLine[2]+" pos2 clade direct");
                //System.out.println(splitLine[3]+" pos3 taxon");
                //System.out.println(splitLine[4]+" pos4 id");
                //System.out.println(splitLine[5]+" pos5 name");


            }
            newSample = new Sample(sampleName, taxonCounts);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newSample;
    }

    public Sample getSample() {return sample;}
}
