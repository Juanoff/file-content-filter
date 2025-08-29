package edu.juanoff.statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;

class FloatStatisticsCollector implements StatisticsCollector {
    private long count = 0;
    private BigDecimal min, max, sum;

    @Override
    public void update(String value) {
        try {
            BigDecimal val = new BigDecimal(value.trim());
            count++;
            sum = (sum == null ? BigDecimal.ZERO : sum).add(val);
            min = min == null ? val : min.min(val);
            max = max == null ? val : max.max(val);
        } catch (NumberFormatException e) {
            System.out.println("Invalid float value skipped: " + value);
        }
    }

    @Override
    public void print(boolean shortStats, boolean fullStats) {
        System.out.println("  Count: " + count);
        if (fullStats && count > 0) {
            System.out.println("  Min: " + min);
            System.out.println("  Max: " + max);
            System.out.println("  Sum: " + sum);
            System.out.println("  Average: " + sum.divide(BigDecimal.valueOf(count), 10, RoundingMode.HALF_UP));
        }
    }
}
