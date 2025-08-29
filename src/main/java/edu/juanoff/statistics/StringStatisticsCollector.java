package edu.juanoff.statistics;

class StringStatisticsCollector implements StatisticsCollector {
    private long count = 0;
    private Integer minLength, maxLength;

    @Override
    public void update(String value) {
        count++;
        int len = value.length();
        minLength = minLength == null ? len : Math.min(minLength, len);
        maxLength = maxLength == null ? len : Math.max(maxLength, len);
    }

    @Override
    public void print(boolean shortStats, boolean fullStats) {
        System.out.println("  Count: " + count);
        if (fullStats && count > 0) {
            System.out.println("  Shortest length: " + minLength);
            System.out.println("  Longest length: " + maxLength);
        }
    }
}
