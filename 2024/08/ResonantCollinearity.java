
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ResonantCollinearity {

    private final char[][] map;
    private final Map<List<Integer>, Character> antennas;  // Map<coordinates, frequency>
    private final Set<List<Integer>> antinodes;
    private final int max_row;
    private final int max_col;
    private int antinodesCount;

    public ResonantCollinearity(String fileName) {
        map = buildMap(fileName);
        max_row = map.length - 1;
        max_col = map[0].length - 1;

        antennas = findAntennas();

        antinodes = new HashSet<>();
        findAntinodes();
        antinodesCount = antinodes.size();
    }

    private char[][] buildMap(String fileName) {
        List<char[]> m = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line = null;
            while ((line = in.readLine()) != null) {
                m.add(line.toCharArray());
            }
        } catch (IOException e) {
            System.out.println("Invalid fileName: " + fileName);
        }
        return m.toArray(char[][]::new);
    }

    private Map<List<Integer>, Character> findAntennas() {
        Map<List<Integer>, Character> ant = new HashMap<>();
        for (int i = 0; i <= max_row; i++) {
            for (int j = 0; j <= max_col; j++) {
                char c = map[i][j];
                if (Character.isAlphabetic(c) || Character.isDigit(c)) {
                    ant.put(List.of(i, j), c);
                }
            }
        }
        return ant;
    }

    private void findAntinodes() {
        // List<List<List<Integer>>> alreadyAnalyzed = new ArrayList<>();
        for (List<Integer> antenna : antennas.keySet()) {
            List<List<Integer>> pairs = antennas.keySet().stream().filter(k -> (!k.equals(antenna) && Objects.equals(antennas.get(k), antennas.get(antenna)))).toList();
            for (List<Integer> pair : pairs) {
                // List<List<Integer>> analyzing = List.of(antenna, pair);
                // if (alreadyAnalyzed.contains(analyzing) || alreadyAnalyzed.contains(analyzing.reversed())) {
                //     continue;
                // }
                setAntinodes(antenna, pair);
                // alreadyAnalyzed.add(analyzing);
            }
        }
    }

    private void setAntinodes(List<Integer> antenna, List<Integer> pair) {

        // Calculate vertical and horizontal displacement
        int v = Math.abs(antenna.get(0) - pair.get(0));
        int h = Math.abs(antenna.get(1) - pair.get(1));

        // Antena-side antinode coordinates
        int xa = (antenna.get(0) < pair.get(0)) ? antenna.get(0) - v : antenna.get(0) + v;
        int ya = (antenna.get(1) < pair.get(1)) ? antenna.get(1) - h : antenna.get(1) + h;
        setAntinode(xa, ya);

        // Pair-side antinode coordinates
        int xp = (pair.get(0) > antenna.get(0)) ? pair.get(0) + v : pair.get(0) - v;
        int yp = (pair.get(1) > antenna.get(1)) ? pair.get(1) + h : pair.get(1) - h;
        setAntinode(xp, yp);

    }

    private void setAntinode(int x, int y) {
        if (!(x < 0 || y < 0 || x > max_row || y > max_col)) {
            List<Integer> antinode = List.of(x, y);
            if (!antinodes.contains(antinode)) {
                antinodes.add(antinode);
            }
        }
        // this.print();
    }

    public void print() {
        for (char[] line : map) {
            System.out.println(Arrays.toString(line));
        }

        // System.out.printf(" Rows: %d \n Columns: %d\n", max_row, max_col);
        // System.out.println(" Antennas:");
        // for (int[] key : antennas.keySet()) {
        //     System.out.printf("\t%s [%d, %d]\n", antennas.get(key), key[0], key[1]);
        // }
    }

    public static void main(String[] args) {
        String fileName = "C:\\Users\\Lucas\\Documents\\My Repos\\Advent-of-code\\2024\\08\\input.txt";
        ResonantCollinearity m = new ResonantCollinearity(fileName);
        // m.print();
        System.out.println(m.antinodesCount);
        // m.print();
    }

}
