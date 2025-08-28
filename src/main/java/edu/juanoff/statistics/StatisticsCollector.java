package edu.juanoff.statistics;

public interface StatisticsCollector {
    void update(String value);

    void print(boolean shortStats, boolean fullStats);
}
