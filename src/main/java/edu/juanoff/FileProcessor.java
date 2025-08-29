package edu.juanoff;

import edu.juanoff.statistics.Statistics;
import edu.juanoff.validator.TypeValidator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileProcessor {
    private final List<TypeValidator> typeValidators;
    private final Statistics statistics;
    private final boolean shortStats;
    private final boolean fullStats;

    public FileProcessor(List<TypeValidator> typeValidators, boolean shortStats, boolean fullStats) {
        this.typeValidators = typeValidators;
        statistics = new Statistics();
        this.shortStats = shortStats;
        this.fullStats = fullStats;
    }

    public void process(String[] inputFiles, String outputDir, String filePrefix, boolean appendMode) throws IOException {
        Path path = prepareOutputPath(outputDir);
        processFiles(inputFiles, path, filePrefix, appendMode);
        statistics.print(shortStats, fullStats);
    }

    private Path prepareOutputPath(String outputDir) throws IOException {
        Path path = outputDir.isEmpty() ? Paths.get(".") : Paths.get(outputDir).normalize();
        try {
            Files.createDirectories(path);
            if (!Files.isDirectory(path)) {
                throw new IOException("Output path is not a directory: " + path);
            }
            if (!Files.isWritable(path)) {
                throw new IOException("Output directory is not writable: " + path);
            }
            return path;
        } catch (IOException e) {
            throw new IOException("Failed to prepare output directory " + path + ": " + e.getMessage(), e);
        }
    }

    private void processFiles(String[] inputFiles, Path path, String filePrefix, boolean appendMode) throws IOException {
        try (WriterManager writerManager = new WriterManager(path, filePrefix, appendMode)) {
            for (String file : inputFiles) {
                try {
                    processFile(file, writerManager);
                } catch (IOException e) {
                    System.err.println("Error processing file " + file + ": " + e.getMessage());
                }
            }
        }
    }

    private void processFile(String file, WriterManager writerManager) throws IOException {
        Path filePath = Paths.get(file);
        if (!Files.exists(filePath)) {
            System.err.println("Input file does not exist: " + file);
            return;
        }
        if (!Files.isReadable(filePath)) {
            System.err.println("Input file is not readable: " + file);
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;
                processLine(line, writerManager);
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + file + ": " + e.getMessage());
        }
    }

    private void processLine(String line, WriterManager writerManager) {
        for (TypeValidator typeValidator : typeValidators) {
            if (typeValidator.isValid(line)) {
                try {
                    BufferedWriter writer = writerManager.getWriter(typeValidator.getOutputFileName());
                    writer.write(line);
                    writer.newLine();
                    statistics.update(typeValidator.getOutputFileName(), line);
                } catch (IOException e) {
                    System.err.println("Error writing line '" + line + "': " + e.getMessage());
                }
                return;
            }
        }
        System.err.println("Unrecognized line skipped: " + line);
    }
}
