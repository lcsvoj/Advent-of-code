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
import java.util.List;

class RedNosedReports {

    private final List<List<Integer>> reports;

    RedNosedReports(String fileName) {
        this.reports = getInput(fileName);
    }

    private List<List<Integer>> getInput(String fileName) {
        List<List<Integer>> rs = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String report = null;
            while ((report = in.readLine()) != null) {
                String[] levelsStr = report.split("\\s+");
                List<Integer> r = new ArrayList<>();
                for (String v : levelsStr) {
                    r.add(Integer.valueOf(v));
                }
                rs.add(r);
            }
        } catch (IOException e) {
            System.out.println("Invalid fileName");
        }
        return rs;
    }

    private int countSafeReports() {
        int safeReports = 0;
        for (List<Integer> report : reports) {
            if (isSafe(report)) {
                safeReports++;
            }
        }
        return safeReports;
    }

    private boolean isSafe(List<Integer> report) {
        if (report.size() < 2) {
            return false;
        }
        if (testReport(report)) {
            return true;
        } else {
            for (int i = 0; i < report.size(); i++) {
                List<Integer> newReport = new ArrayList<>(report);
                newReport.remove(i);
                if (testReport(newReport)) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean testReport(List<Integer> report) {
        boolean shouldIncrease = report.get(1) - report.get(0) > 0;
        for (int i = 1; i < report.size(); i++) {

            // The levels are either all increasing or all decreasing.
            int dif = report.get(i) - report.get(i - 1);
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
