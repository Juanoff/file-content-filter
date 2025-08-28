package edu.juanoff;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class WriterManager implements AutoCloseable {
    private final Path path;
    private final String filePrefix;
    private final OpenOption[] openOptions;
    private final Map<String, BufferedWriter> writers = new HashMap<>();

    public WriterManager(Path path, String filePrefix, boolean appendMode) {
        this.path = path;
        this.filePrefix = filePrefix;
        this.openOptions = new OpenOption[]{
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                appendMode ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING
        };
    }

    public BufferedWriter getWriter(String outputFileName) throws IOException {
        return writers.computeIfAbsent(outputFileName, fileName -> {
            try {
                Path prefixPath = path.resolve(filePrefix + outputFileName);
                return Files.newBufferedWriter(prefixPath, openOptions);
            } catch (IOException ex) {
                throw new RuntimeException("Failed to create writer for " + fileName + ": " + ex.getMessage(), ex);
            }
        });
    }

    @Override
    public void close() throws IOException {
        if (writers.isEmpty()) {
            return;
        }

        IOException primary = null;
        for (BufferedWriter writer : writers.values()) {
            try {
                writer.close();
            } catch (IOException ex) {
                if (primary == null) {
                    primary = ex;
                } else {
                    primary.addSuppressed(ex);
                }
            }
        }

        writers.clear();

        if (primary != null) {
            throw primary;
        }
    }
}
