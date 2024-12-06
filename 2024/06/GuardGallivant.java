
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* The map (your puzzle input) shows the current position of the guard with an arrow (^, v, <, or >) to indicate the direction they're facing.
 * Any obstructions are shown as #. Guards follow a very strict patrol protocol which involves repeatedly following these steps:
 *  - If there is something directly in front of you, turn right 90 degrees.
 *  - Otherwise, take a step forward.
 * This process continues for a while, but the guard eventually leaves the mapped area.
 * By predicting the guard's route, you can determine which specific positions in the lab will be in the patrol path.
 * Including the guard's starting position, the positions visited by the guard before leaving the area are marked with an X.
 * Predict the path of the guard. How many distinct positions will the guard visit before leaving the mapped area?
 */
public class GuardGallivant {

    private final char[][] map;
    private int positionX;
    private int positionY;
    private char direction;
    private boolean isOutOfMap;
    private int walkedCells;

    public GuardGallivant(String fileName) {
        // Buil map from text input
        this.map = buildMap(fileName);

        // Set initial position and diretction
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                switch (map[x][y]) {
                    case '^', 'v', '>', '<' -> {
                        positionX = x;
                        positionY = y;
                        direction = map[x][y];
                    }
                }
            }
        }

        walkedCells = 0;    // The guard has walked 0 cells initially
        isOutOfMap = false; // The guard is initially inside the map

    }

    private char[][] buildMap(String fileName) {
        List<char[]> ls = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line = null;
            while ((line = in.readLine()) != null) {
                ls.add(line.toCharArray());
            }
        } catch (IOException e) {
            System.out.println("Invalid fileName: " + fileName);
        }
        return ls.toArray(char[][]::new);
    }

    private void patrol() {
        // this.print();
        while (!isOutOfMap) {
            if (pathIsClear()) {
                walk();
            } else {
                turnRight();
            }
            patrol();
        }

    }

    private boolean pathIsClear() {
        int nextX = positionX;
        int nextY = positionY;
        switch (direction) {
            case '^' ->
                nextX = positionX - 1;
            case 'v' ->
                nextX = positionX + 1;
            case '<' ->
                nextY = positionY - 1;
            case '>' ->
                nextY = positionY + 1;
            default ->
                throw new IllegalArgumentException("Unexpected value: " + direction);
        }

        return (nextX < 0 || nextY < 0 || nextX >= map.length || nextY >= map[0].length) ? true : !(map[nextX][nextY] == '#');
    }

    private void turnRight() {
        direction = switch (direction) {
            case '^' ->
                '>';
            case '>' ->
                'v';
            case 'v' ->
                '<';
            case '<' ->
                '^';
            default ->
                throw new IllegalArgumentException("Unexpected value: " + direction);
        };
    }

    private void walk() {
        map[positionX][positionY] = 'X';
        switch (direction) {
            case '^' -> {
                if ((positionX--) < 0) {
                    isOutOfMap = true;
                }
            }
            case 'v' -> {
                if ((positionX++) >= map.length) {
                    isOutOfMap = true;
                }
            }
            case '<' -> {
                if ((positionY--) < 0) {
                    isOutOfMap = true;
                }
            }
            case '>' -> {
                if ((positionY++) >= map[0].length) {
                    isOutOfMap = true;
                }
            }
        }

        if (positionX < 0 || positionX >= map.length || positionY < 0 || positionY >= map[0].length) {
            isOutOfMap = true;
            walkedCells++;
            // System.out.println("Guard has moved out of the map.");
            return;
        }

        if (map[positionX][positionY] != 'X') {
            walkedCells++;
        }

        map[positionX][positionY] = direction;
    }

    public void print() {
        System.out.printf("\nPos: (%d, %d)\t\tDirection: %c\n", positionX, positionY, direction);
        for (char[] line : map) {
            System.out.println(Arrays.toString(line));
        }
    }

    public static void main(String[] args) {
        String fileName = "C:\\Users\\Lucas\\Documents\\My Repos\\Advent-of-code\\2024\\06\\test.txt";
        GuardGallivant g = new GuardGallivant(fileName);
        g.patrol();
        System.out.println("Walked cells: " + g.walkedCells);
    }
}
