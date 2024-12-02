
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Pair up the smallest number in the left list with the smallest number in the right list, then the second-smallest left number with the second-smallest right number, and so on.
// Within each pair, figure out how far apart the two numbers are; you'll need to add up all of those distances. For example, if you pair up a 3 from the left list with a 7 from the right list, the distance apart is 4; if you pair up a 9 with a 3, the distance apart is 6.
class HistorianHysteria {

    private final List<Integer> leftList;
    private final List<Integer> rightList;

    public HistorianHysteria(String fileName) {
        leftList = new ArrayList<>();
        rightList = new ArrayList<>();
        try {
            setLists(fileName);
        } catch (IOException e) {
            System.out.println("Invalid fileName");
        }
    }

    private void setLists(String fileName) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String linea = null;
            while ((linea = in.readLine()) != null) {
                leftList.add(Integer.valueOf(linea.split("\\s+")[0]));
                rightList.add(Integer.valueOf(linea.split("\\s+")[1]));
            }
            leftList.sort(Comparator.naturalOrder());
            rightList.sort(Comparator.naturalOrder());
        } catch (IOException e) {
            throw new IOException("Invalid fileName");
        }
    }

    public int getDifference() {
        int sum = 0;
        for (int i = 0; i < leftList.size(); i++) {
            sum += Math.abs(leftList.get(i) - rightList.get(i));
        }
        return sum;
    }

    // Calculate a total similarity score by adding up each number in the left list after
    // multiplying it by the number of times that number appears in the right list.
    public long getSimilarityScore() {
        long score = 0L;
        for (Integer v : leftList) {
            long adjustedValue = v * rightList.stream()
                    .filter(x -> x.equals(v))
                    .count();
            score += adjustedValue;
        }
        return score;
    }

    public static void main(String[] args) {
        HistorianHysteria hs = new HistorianHysteria("C:\\Users\\Lucas\\Documents\\My Repos\\Advent-of-code\\2024\\01\\input.txt");
        System.out.println("Sum of differences: " + hs.getDifference());
        System.out.println("Similarity score: " + hs.getSimilarityScore());
    }

}
