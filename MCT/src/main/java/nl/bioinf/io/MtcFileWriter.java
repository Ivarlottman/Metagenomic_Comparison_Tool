package nl.bioinf.io;

import nl.bioinf.data_management.NameAndGenus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVWriter;

public class MtcFileWriter {
    /**
     * text
     * @author Ivar Lottman
     * @version 0
     * */
    private List<String> header;
    private HashMap<NameAndGenus, List<Integer>> body;
    private File resultFolder;
    private String fileName;

    public MtcFileWriter(List<String> header, HashMap<NameAndGenus, List<Integer>> body, File resultFolder, String fileName) {
        this.header = header;
        this.body = body;
        this.resultFolder = resultFolder;
        this.fileName = fileName;
    }


    public void printDataFrame(){
        File fileLocation = new File(resultFolder,fileName+".csv");

        String[] completeHeader = new String[header.size()+2];

        for(int i=0 ; i < 2 + header.size(); i++){
            int index = i-2;
            if(i==0){completeHeader[i] = "Name";}
            else if(i==1){completeHeader[i] = "Taxon";}
            else if(i>=2){completeHeader[i] = header.get(index);}
        }

        try{
        fileLocation.createNewFile();
        FileWriter writer = new FileWriter(fileLocation);
        CSVWriter csvWriter = new CSVWriter(writer);
        csvWriter.writeNext(completeHeader);
        for(Map.Entry<NameAndGenus, List<Integer>> entry : body.entrySet()){
            String name = entry.getKey().name();
            String taxon = entry.getKey().genus().toString();
            List<Integer> abundanceValueList = entry.getValue();
            String[] line = new String[abundanceValueList.size()+2];
            line[0] = name;
            line[1] = taxon;
            for(int i=2 ; i < 2 + abundanceValueList.size(); i++){
                int index2 = i-2;
                if (i>=2){line[i] = abundanceValueList.get(index2).toString();}
            }
            //System.out.println(Arrays.toString(line));
            csvWriter.writeNext(line);

        }

        csvWriter.close();


        } catch (IOException e){e.printStackTrace();}
    }
}

