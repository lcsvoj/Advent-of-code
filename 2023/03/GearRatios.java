/* If you can add up all the part numbers in the engine schematic, it should be easy to work out which 
 * part is missing. The engine schematic (your puzzle input) consists of a visual representation of the engine. 
 * There are lots of numbers and symbols you don't really understand, but apparently any number adjacent to a 
 * symbol, even diagonally, is a "part number" and should be included in your sum. (Periods (.) do not count as a symbol.)
 * What is the sum of all of the part numbers in the engine schematic?
 * 
 * A gear is any '*' symbol that is adjacent to exactly two part numbers. Its gear ratio is the result of multiplying those two numbers together.
 * You also need to find the gear ratio of every gear and add them all up so that the engineer can figure out which gear needs to be replaced.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GearRatios {

    private List<Cell> symbols;
    private List<Cell> gears;
    private List<Cell> numbers;

    public GearRatios(String fileName) {
        symbols = new ArrayList<>();
        numbers = new ArrayList<>();
        readCharacters(fileName);
        gears = new ArrayList<>(symbols.stream().filter(c -> c.value.equals("*")).toList());
    }

    private void readCharacters(String fileName) {
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line = null;
            int row = 0;
            while ((line = in.readLine()) != null) {

                // Find numbers
                Matcher numberMatcher = Pattern.compile("[0-9]+").matcher(line);
                while (numberMatcher.find()) {
                    String number = numberMatcher.group();
                    if (!number.isBlank()) {
                        numbers.add(new Cell(row, numberMatcher.start(), number));
                    }
                }

                // Find symbols
                Matcher symbolMatcher = Pattern.compile("[^0-9\\.]").matcher(line);
                while (symbolMatcher.find()) {
                    String symbol = symbolMatcher.group();
                    if (!symbol.isBlank()) {
                        symbols.add(new Cell(row, symbolMatcher.start(), symbol));
                    }
                }

                row++;
            }
        } catch (IOException e) {
            System.out.println("File not found: " + fileName);
        }
    }

    private class Cell {

        public final int row;
        public final int initialColumn;
        public final int finalColumn;
        public final String value;

        public Cell(int row, int column, String value) {
            this.row = row;
            initialColumn = column;
            finalColumn = initialColumn + (value.length() - 1);
            this.value = value;
        }

        @Override
        public String toString() {
            return "(%s [%d, %d])".formatted(value, row, initialColumn);
        }

    }

    public long getSumOfParts() {
        return numbers.stream()
                .filter(n -> (countSymbolsNear(n) > 0))
                .mapToLong(n -> Integer.valueOf(n.value))
                .sum();
    }

    public long getSumOfGears() {
        long sumGears = 0;
        for (Cell gear : gears) {
            List<Cell> nearNumbers = findNumbersNear(gear);
            if (nearNumbers.size() == 2) {
                sumGears += nearNumbers.stream().mapToLong(n -> Long.valueOf(n.value)).reduce(1, (a, b) -> a * b);
            }
        }
        return sumGears;
    }

    private long countSymbolsNear(Cell number) {
        return (symbols.stream()
                .filter(c -> ((number.row >= c.row - 1) && (number.row <= c.row + 1) && (number.finalColumn >= c.initialColumn - 1) && (number.initialColumn <= c.initialColumn + 1)))
                .count());
    }

    private List<Cell> findNumbersNear(Cell symbol) {
        return (numbers.stream()
                .filter(n -> ((n.row >= symbol.row - 1) && (n.row <= symbol.row + 1) && (n.finalColumn >= symbol.initialColumn - 1) && (n.initialColumn <= symbol.initialColumn + 1)))
                .toList());
    }

    public static void main(String[] args) {
        GearRatios gr = new GearRatios("C:\\Users\\Lucas\\Documents\\My Repos\\Advent-of-code\\2023\\3\\input.txt");
        // System.out.println("Symbols: " + gr.symbols.toString());
        // System.out.println("Numbers: " + gr.numbers.toString());
        // System.out.println("Gears: " + gr.gears.toString());
        System.out.println("Sum: " + gr.getSumOfParts());
        System.out.println("Sum of gears: " + gr.getSumOfGears());
    }

}
