/* The unusual data (your puzzle input) consists of many reports, one report per line.
 * Each report is a list of numbers called levels that are separated by spaces.
 * So, a report only counts as safe if both of the following are true:
 * - The levels are either all increasing or all decreasing.
 * - Any two adjacent levels differ by at least one and at most three.
 * Analyze the unusual data from the engineers. How many reports are safe?
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class RedNosedReports {

    private final List<List<String>> reports;

    RedNosedReports(String fileName) {
        this.reports = getInput(fileName);
    }

    private List<List<String>> getInput(String fileName) {
        List<List<String>> r = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String report = null;
            while ((report = in.readLine()) != null) {
                r.add(Arrays.asList(report.split("\\s+")));
            }
        } catch (IOException e) {
            System.out.println("Invalid fileName");
        }
        return r;
    }

    private int countSafeReports() {
        int safeReports = 0;
        for (List<String> report : reports) {
            if (isSafe(report)) {
                safeReports++;
            }
        }
        return safeReports;
    }

    private boolean isSafe(List<String> report) {
        boolean shouldIncrease = Integer.valueOf(report.get(1)) - Integer.valueOf(report.get(0)) > 0;
        for (int i = 1; i < report.size(); i++) {

            // The levels are either all increasing or all decreasing.
            int dif = Integer.valueOf(report.get(i)) - Integer.valueOf(report.get(i - 1));
            if (shouldIncrease && dif < 0 || !shouldIncrease && dif > 0) {
                return false;
            }

            // Any two adjacent levels differ by at least one and at most three.
            int absoluteDif = Math.abs(dif);
            if (absoluteDif < 1 || absoluteDif > 3) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        RedNosedReports reports = new RedNosedReports("C:\\Users\\Lucas\\Documents\\My Repos\\Advent-of-code\\2024\\02\\input.txt");
        System.out.println(reports.countSafeReports());
    }

}
