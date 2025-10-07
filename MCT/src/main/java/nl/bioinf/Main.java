package nl.bioinf;
import nl.bioinf.io.CommandLineParser;
import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        CommandLineParser parser = new CommandLineParser();
        int exitcode = new CommandLine(parser).execute(args);
        System.out.println(exitcode+" exitcode");
        System.exit(exitcode);

        }
    }