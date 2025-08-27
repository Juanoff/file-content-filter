package edu.juanoff;

import edu.juanoff.validator.FloatTypeValidator;
import edu.juanoff.validator.IntegerTypeValidator;
import edu.juanoff.validator.StringTypeValidator;
import edu.juanoff.validator.TypeValidator;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        List<TypeValidator> typeValidators = List.of(
                new IntegerTypeValidator(),
                new FloatTypeValidator(),
                new StringTypeValidator()
        );

        Path path = outputDir.isEmpty() ? Paths.get(".") : Paths.get(outputDir);
        Map<String, BufferedWriter> writers = new HashMap<>();
        for (TypeValidator typeValidator : typeValidators) {
            OpenOption[] openOptions = appendMode
                    ? new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE}
                    : new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE};

            Path prefixPath = path.resolve(filePrefix + typeValidator.getOutputFileName());
            writers.put(
                    typeValidator.getOutputFileName(),
                    Files.newBufferedWriter(prefixPath, openOptions)
            );
        }

        for (String file : inputFiles) {
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(file))) {
                String line = reader.readLine();

                while (line != null) {
                    for (TypeValidator typeValidator : typeValidators) {
                        if (typeValidator.isValid(line)) {
                            writers.get(typeValidator.getOutputFileName()).write(line);
                            writers.get(typeValidator.getOutputFileName()).newLine();
                            break;
                        }
                    }
                    line = reader.readLine();
                }
            }
        }

        for (TypeValidator typeValidator : typeValidators) {
            writers.get(typeValidator.getOutputFileName()).close();
        }
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FileContentFilter()).execute(args);
        System.exit(exitCode);
    }
}
