package edu.juanoff;

import edu.juanoff.validator.TypeValidator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileProcessor {

    private final List<TypeValidator> typeValidators;

    public FileProcessor(List<TypeValidator> typeValidators) {
        this.typeValidators = typeValidators;
    }

    public void process(String[] inputFiles, String outputDir, String filePrefix, boolean appendMode) throws IOException {
        Path path = outputDir.isEmpty() ? Paths.get(".") : Paths.get(outputDir);
        Map<String, BufferedWriter> writers = openWriters(path, filePrefix, appendMode);

        try {
            for (String file : inputFiles) {
                processFile(file, writers);
            }
        } finally {
            closeWriters(writers);
        }
    }

    private Map<String, BufferedWriter> openWriters(Path path, String filePrefix, boolean appendMode) throws IOException {
        Map<String, BufferedWriter> writers = new HashMap<>();
        for (TypeValidator typeValidator : typeValidators) {
            OpenOption[] openOptions = appendMode
                    ? new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE}
                    : new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE};

            Path prefixPath = path.resolve(filePrefix + typeValidator.getOutputFileName());
            writers.put(typeValidator.getOutputFileName(), Files.newBufferedWriter(prefixPath, openOptions));
        }
        return writers;
    }

    private void processFile(String file, Map<String, BufferedWriter> writers) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file))) {
            String line = reader.readLine();
            while (line != null) {
                processLine(line, writers);
                line = reader.readLine();
            }
        }
    }

    private void processLine(String line, Map<String, BufferedWriter> writers) throws IOException {
        for (TypeValidator typeValidator : typeValidators) {
            if (typeValidator.isValid(line)) {
                writers.get(typeValidator.getOutputFileName()).write(line);
                writers.get(typeValidator.getOutputFileName()).newLine();
                break;
            }
        }
    }

    private void closeWriters(Map<String, BufferedWriter> writers) throws IOException {
        for (TypeValidator typeValidator : typeValidators) {
            writers.get(typeValidator.getOutputFileName()).close();
        }
    }
}
