package edu.juanoff;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "file_content_filter",
        mixinStandardHelpOptions = true,
        version = "file_content_filter 1.0",
        description = "Filtering content of files to output files and prints statistics in STDOUT.")
class FileContentFilter implements Callable<Integer> {

    @Parameters(paramLabel = "FILENAME", description = "One or more files whose need to filtering.")
    private String[] fileNames;

    @Option(names = {"-o"}, paramLabel = "DIRECTORY", description = "Directory to save output files.")
    private String saveDirectory;

    @Option(names = {"-s"}, description = "Short statistics.")
    private boolean shortStatistics;

    @Option(names = {"-f"}, description = "Full statistics.")
    private boolean fullStatistics;

    @Option(names = {"-p"}, paramLabel = "PREFIX", description = "Prefix name for output files.")
    private String prefix;

    @Override
    public Integer call() throws Exception {
        // some code...
        return 1;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FileContentFilter()).execute(args);
        System.exit(exitCode);
    }
}
