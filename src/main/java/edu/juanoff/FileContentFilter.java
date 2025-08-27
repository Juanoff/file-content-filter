package edu.juanoff;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.*;
import java.util.concurrent.Callable;

@Command(
        name = "file-content-filter",
        mixinStandardHelpOptions = true,
        version = "file-content-filter 1.0",
        description = "Filters content of input files to output files, " +
                "write results to output files and prints statistics to STDOUT."
)
class FileContentFilter implements Callable<Integer> {

    @Parameters(
            paramLabel = "FILES",
            description = "One or more input files to filter.",
            arity = "1..*"
    )
    private String[] inputFiles;

    @Option(
            names = {"-o", "--output-dir"},
            paramLabel = "DIRECTORY",
            description = "Directory where output files will be saved. Defaults to current directory.",
            defaultValue = ""
    )
    private String outputDir;

    @Option(
            names = {"-s", "--short-stats"},
            description = "Print short statistics."
    )
    private boolean shortStats;

    @Option(
            names = {"-f", "--full-stats"},
            description = "Print full statistics."
    )
    private boolean fullStats;

    @Option(
            names = {"-p", "--prefix"},
            paramLabel = "PREFIX",
            description = "Prefix for naming output files.",
            defaultValue = ""
    )
    private String filePrefix;

    @Option(
            names = {"-a", "--append"},
            description = "Enable append mode (append results to existing output files instead of overwriting)."
    )
    private boolean appendMode;

    @Override
    public Integer call() throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputDir + filePrefix + "integers.txt", appendMode));

        for (String fileName : inputFiles) {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            while (line != null) {
                try {
                    int num = Integer.parseInt(line);
                    writer.write(String.valueOf(num));
                    writer.newLine();
                } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage());
                } catch (NumberFormatException e) {
                    // skip this ...
                }

                line = reader.readLine();
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FileContentFilter()).execute(args);
        System.exit(exitCode);
    }
}
