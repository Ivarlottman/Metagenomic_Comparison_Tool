package nl.bioinf;

import nl.bioinf.io.CommandLineParser;
import picocli.CommandLine;

public class Main {
    /**
     * Main class
     * This class starts the application by passing on the commandline arguments to a CommandLineParser object.
     * Which then starts the MCT process, once that is finisht it closes the aplicatien with the system exitcode
     * @author Ivar Lottman
     * //@version 0
     * */
    public static void main(String[] args) {
        CommandLineParser parser = new CommandLineParser();
        int exitcode = new CommandLine(parser).execute(args);
        System.out.println(exitcode + " exitcode");
        System.exit(exitcode);

    }
}