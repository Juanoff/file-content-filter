package edu.juanoff.statistics;

public class IntegerStatisticsCollector implements StatisticsCollector {
    private long count = 0;
    private Long min, max, sum;

    @Override
    public void update(String value) {
        try {
            long val = Long.parseLong(value.trim());
            count++;
            sum = (sum == null ? 0 : sum) + val;
            min = min == null ? val : Math.min(min, val);
            max = max == null ? val : Math.max(max, val);
        } catch (NumberFormatException e) {
            System.out.println("Invalid integer value skipped: " + value);
        }
    }

    @Override
    public void print(boolean shortStats, boolean fullStats) {
        System.out.println("  Count: " + count);
        if (fullStats && count > 0) {
            System.out.println("  Min: " + min);
            System.out.println("  Max: " + max);
            System.out.println("  Sum: " + sum);
            System.out.println("  Average: " + (sum / (double) count));
        }
    }
}
