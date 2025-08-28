package edu.juanoff.statistics;

import edu.juanoff.OutputFileName;

import java.util.HashMap;
import java.util.Map;

public class Statistics {
    private final Map<String, StatisticsCollector> collectors = new HashMap<>();

    public Statistics() {
        collectors.put(OutputFileName.INTEGERS.getFileName(), new IntegerStatisticsCollector());
        collectors.put(OutputFileName.FLOATS.getFileName(), new FloatStatisticsCollector());
        collectors.put(OutputFileName.STRINGS.getFileName(), new StringStatisticsCollector());
    }

    public void update(String fileName, String value) {
        StatisticsCollector collector = collectors.get(fileName);
        if (collector != null) {
            collector.update(value);
        }
    }

    public void print(boolean shortStats, boolean fullStats) {
        if (!shortStats && !fullStats) {
            return;
        }
        for (Map.Entry<String, StatisticsCollector> entry : collectors.entrySet()) {
            System.out.println("Statistics for " + entry.getKey() + " (newly added elements):");
            entry.getValue().print(shortStats, fullStats);
        }
    }
}
