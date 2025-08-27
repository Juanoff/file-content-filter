package edu.juanoff;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.*;
import java.util.concurrent.Callable;

@Command(name = "file_content_filter",
        mixinStandardHelpOptions = true,
        version = "file_content_filter 1.0",
        description = "Filtering content of files to output files and prints statistics in STDOUT.")
class FileContentFilter implements Callable<Integer> {

    @Parameters(paramLabel = "FILENAME", description = "One or more files whose need to filtering.", arity = "1..*")
    private String[] fileNames;

    @Option(names = {"-o"}, paramLabel = "DIRECTORY", description = "Directory to save output files.", defaultValue = "")
    private String saveDirectory;

    @Option(names = {"-s"}, description = "Short statistics.")
    private boolean shortStatistics;

    @Option(names = {"-f"}, description = "Full statistics.")
    private boolean fullStatistics;

    @Option(names = {"-p"}, paramLabel = "PREFIX", description = "Prefix name for output files.", defaultValue = "")
    private String prefix;

    @Option(names = {"-a"}, description = "Activate append mode.")
    private boolean append;

    @Override
    public Integer call() throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(saveDirectory + prefix + "integers.txt", append));

        for (String fileName : fileNames) {
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
