
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class part1 {

    private final List<String> disk;
    private final List<String> compactedDisk;
    private final long checksum;

    part1(String fileName) {
        disk = new ArrayList<>(buildDisk(fileName));
        compactedDisk = compactDisk();
        checksum = computeChecksum();
    }

    private List<String> buildDisk(String fileName) {
        List<String> disk = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            char[] line = in.readLine().toCharArray();
            int fileID = 0;
            for (int i = 0; i < line.length; i++) {
                int n = Character.getNumericValue(line[i]);
                if (i % 2 == 0) { // file block
                    for (int count = 0; count < n; count++) {
                        disk.add(Integer.toString(fileID));
                    }
                    fileID++;
                } else { // empty block
                    for (int count = 0; count < n; count++) {
                        disk.add(".");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Invalid fileName: " + fileName);
        }
        return disk;
    }

    private List<String> compactDisk() {
        List<String> cd = new ArrayList<>(this.disk);
        int leftPointer = cd.indexOf(".");
        int rightPointer = cd.size() - 1;
        while (rightPointer > leftPointer) {
            boolean isDigit = cd.get(rightPointer).matches("[0-9]+");
            boolean isPoint = cd.get(leftPointer).equals(".");
            if (!isDigit) {
                rightPointer--;
            }
            if (!isPoint) {
                leftPointer++;
            }
            if (isDigit && isPoint) {
                String n = cd.get(rightPointer);
                cd.set(leftPointer, n);
                cd.set(rightPointer, ".");
                rightPointer--;
                leftPointer++;
            }
        }
        return cd;
    }

    private long computeChecksum() {
        long sum = 0;
        for (int i = 0; i < compactedDisk.size(); i++) {
            if (compactedDisk.get(i).equals(".")) {
                break;
            }
            sum += i * Integer.parseInt(compactedDisk.get(i));
        }
        return sum;
    }

    public static void main(String[] args) {
        String fileName = "C:/Users/Lucas/Documents/My Repos/Advent-of-code/2024/09/input.txt";
        part1 part1 = new part1(fileName);
        // System.err.println("Initial disk state: \n" + part1.disk + "\n");
        // System.err.println("Compacted disk: \n" + part1.compactedDisk + "\n");
        System.out.println("Checksum = " + part1.checksum + "\n");
    }

}
