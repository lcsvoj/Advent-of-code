
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CeresSearch {

    private final char[] word;
    private final char[][] matrix;
    private final int matrix_rows;
    private final int matrix_columns;
    private int wordsInMatrix;

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

    private int searchWords() {
        int foundWords = 0;
        for (int row = 0; row < matrix_rows; row++) {
            for (int column = 0; column < matrix_columns; column++) {
                // If the first letter is found, analyze its viciniti looking for the next letter
                if (matrix[row][column] == word[0]) {
                    System.out.printf("Found the first letter in (%d, %d), let's follow it...\n", row, column);
                    foundWords += followClue(row, column, 1, 0);
                }
            }
        }
        return foundWords;
    }

    // followClue analyzes the vincinities of the given (row, column) in the matrix looking for the next letter in the word
    // If it finds it, it recursively analyzes the vicinities of the newly found word letter looking for its successor, and so on...
    private int followClue(int row, int column, int targetIndex, int foundWords) {
        if (targetIndex == word.length) {
            foundWords++;
            System.out.printf("\t\tGood job, the word is complete! Incrementing found words to %d and returning.\n", foundWords);
            return foundWords; // When all letters were found, count as a complete word
        }

        // Set the boundries to respect the matrix limits
        for (int i = Math.max(0, row - 1); i <= Math.min(matrix_rows - 1, row + 1); i++) {
            for (int j = Math.max(0, column - 1); j <= Math.min(matrix_columns - 1, column + 1); j++) {
                if (i == row && j == column) {  // Ignore the already found cell to avoi duplicate counts
                    continue;
                }
                if (matrix[i][j] == word[targetIndex]) {  // Recursive call if a neighbor cell matches
                    System.out.printf("\tFound the %dnd letter in (%d, %d), let's follow its clue...\n", targetIndex + 1, i, j);
                    return followClue(i, j, targetIndex + 1, foundWords);
                }
            }
        }
        return foundWords;
    }

    public static void main(String[] args) {
        String fileName = "C:\\Users\\Lucas\\Documents\\My Repos\\Advent-of-code\\2024\\04\\test.txt";
        CeresSearch c = new CeresSearch(fileName, "XMAS");
        System.out.println("Total words found = " + c.searchWords());
    }

}
