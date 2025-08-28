package edu.juanoff;

public enum OutputFileName {
    INTEGERS("integers.txt"),
    FLOATS("floats.txt"),
    STRINGS("strings.txt");

    private final String fileName;

    OutputFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
