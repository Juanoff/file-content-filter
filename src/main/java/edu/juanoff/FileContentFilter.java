package edu.juanoff;

import edu.juanoff.validator.FloatTypeValidator;
import edu.juanoff.validator.IntegerTypeValidator;
import edu.juanoff.validator.StringTypeValidator;
import edu.juanoff.validator.TypeValidator;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.concurrent.Callable;

@Command(
        name = "file-content-filter",
        mixinStandardHelpOptions = true,
        version = "file-content-filter 1.0",
        description = "Filters content of input files to output files, " +
                "write results to output files and prints statistics to STDOUT."
)
public class FileContentFilter implements Callable<Integer> {
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
    public Integer call() {
        if (!filePrefix.matches("[a-zA-Z0-9_-]*")) {
            System.err.println("Invalid prefix: must contain only letters, numbers, underscores or hyphens.");
            return 1;
        }

        try {
            List<TypeValidator> typeValidators = List.of(
                    new IntegerTypeValidator(),
                    new FloatTypeValidator(),
                    new StringTypeValidator()
            );

            FileProcessor fileProcessor = new FileProcessor(typeValidators, shortStats, fullStats);
            fileProcessor.process(inputFiles, outputDir, filePrefix, appendMode);

            return 0;
        } catch (Exception e) {
            System.err.println("Error during processing: " + e.getMessage());
            return 1;
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FileContentFilter()).execute(args);
        System.exit(exitCode);
    }
}
