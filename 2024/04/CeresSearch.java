
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CeresSearch {

    private final char[] word;
    private final char[][] matrix;
    private final int matrix_rows;
    private final int matrix_columns;
    public int wordsInMatrix;

    public CeresSearch(String fileName, String word) {
        // Set the word as a char[]
        this.word = word.toCharArray();

        // Set the word search matrix and its bound parameters
        matrix = buildMatrixFromFile(fileName);
        matrix_rows = matrix.length;
        matrix_columns = matrix[0].length;

        wordsInMatrix = 0;
    }

    private char[][] buildMatrixFromFile(String fileName) {
        List<char[]> output = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line = null;
            while ((line = in.readLine()) != null) {
                output.add(line.toCharArray());
            }
        } catch (IOException e) {
            System.out.println("Invalid fileName: " + fileName);
        }
        return output.toArray(char[][]::new);
    }

    private void findWords() {
        // Search each charatcer in the matrix looking for the first letter of the word
        for (int row = 0; row < matrix_rows; row++) {
            for (int column = 0; column < matrix_columns; column++) {
                if (matrix[row][column] == word[0]) {  // If the first letter is found, test if it's part of a complete word
                    System.out.printf("First letter in (%d, %d)\n", row, column);
                    searchLetterInVicinities(row, column, 1, Direction.ANY);
                }
            }
        }
    }

    private void findXWords() {
        // Search each charatcer in the matrix looking for an 'A'
        for (int row = 0; row < matrix_rows; row++) {
            for (int column = 0; column < matrix_columns; column++) {
                if (matrix[row][column] == 'A') {  // If the 'A' is found, test if it's part of a X-MAS pattern
                    System.out.printf("First letter in (%d, %d)\n", row, column);
                    if (isXPattern(row, column)) {
                        System.out.println("\tIt's an x-pattern!");
                        wordsInMatrix++;
                    }
                }
            }
        }
    }

    private boolean isXPattern(int row, int column) {
        try {
            char[] foundPattern = new char[]{
                matrix[row - 1][column - 1],
                matrix[row - 1][column + 1],
                matrix[row + 1][column - 1],
                matrix[row + 1][column + 1]
            };
            List<List<Character>> validPatterns = Arrays.asList(
                    Arrays.asList('M', 'S', 'M', 'S'),
                    Arrays.asList('S', 'M', 'S', 'M'),
                    Arrays.asList('S', 'S', 'M', 'M'),
                    Arrays.asList('M', 'M', 'S', 'S')
            );
            List<Character> foundPatternList = Arrays.asList(
                    foundPattern[0], foundPattern[1], foundPattern[2], foundPattern[3]
            );
            return validPatterns.contains(foundPatternList);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    // Search the vicinities of matrix[row][column] looking for the letter in word[target]
    private void searchLetterInVicinities(int row, int column, int target, Direction targetDirection) {
        for (int i = Math.max(0, row - 1); i <= Math.min(matrix_rows - 1, row + 1); i++) {
            for (int j = Math.max(0, column - 1); j <= Math.min(matrix_columns - 1, column + 1); j++) {

                Direction currentDirection = setDirection(row, column, i, j);
                if (targetDirection != Direction.ANY && currentDirection != targetDirection) {
                    continue;
                }

                if (i == row && j == column) {  // Ignore the already found cell to avoi duplicate counts
                    continue;
                }

                if (matrix[i][j] == word[target]) {
                    if (target == word.length - 1) {    // If the found letter is the last one in word, it's a complete word
                        wordsInMatrix++;                // Increment the amount of found words and return
                        System.out.printf("\t%dnd letter in (%d, %d).\t", target + 1, i, j);
                        System.out.printf("Good job, the word is complete! Incrementing found words to %d and returning.\n", wordsInMatrix);
                    } else {
                        System.out.printf("\t%dnd letter in (%d, %d)\n", target + 1, i, j);
                        searchLetterInVicinities(i, j, target + 1, currentDirection);  // If the found word isn't the last, iteratively check it looking for the next one
                    }
                }
            }
        }
    }

    private Direction setDirection(int row, int column, int i, int j) {
        int horizontalDiff = row - i;
        int verticalDiff = column - j;

        if (horizontalDiff == 0) {
            if (horizontalDiff > 0) {
                return Direction.EAST;
            } else {
                return Direction.WEST;
            }
        } else if (verticalDiff == 0) {
            if (verticalDiff > 0) {
                return Direction.NORTH;
            } else {
                return Direction.SOUTH;
            }
        } else {
            if (horizontalDiff > 0 && verticalDiff > 0) {
                return Direction.NORTHEAST;
            } else if (horizontalDiff > 0 && verticalDiff < 0) {
                return Direction.SOUTHEAST;
            } else if (horizontalDiff < 0 && verticalDiff > 0) {
                return Direction.NORTHWEST;
            } else {  // if (horizontalDiff < 0 && verticalDiff < 0)
                return Direction.SOUTHWEST;
            }
        }
    }

    private enum Direction {
        ANY,
        NORTH,
        NORTHEAST,
        EAST,
        SOUTHEAST,
        SOUTH,
        SOUTHWEST,
        WEST,
        NORTHWEST
    }

    public static void main(String[] args) {
        String fileName = "C:\\Users\\Lucas\\Documents\\My Repos\\Advent-of-code\\2024\\04\\input.txt";
        CeresSearch c = new CeresSearch(fileName, "XMAS");
        c.findXWords();
        System.out.println("Total words found = " + c.wordsInMatrix);
    }

}
