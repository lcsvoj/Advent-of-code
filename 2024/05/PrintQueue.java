
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* New pages for the safety manuals must be printed in a very specific order.
 * The notation X|Y means that if both page number X and page number Y are to be updated, page number X must be printed at some point before page number Y, not necessarily immediately.
 * The Elf has for you both the page ordering rules and the pages to produce in each update (your puzzle input), but can't figure out whether each update has the pages in the right order.
 * In the input, the first section specifies the page ordering rules, one per line, and the second section specifies on each update (one per line) the page numbers to be updated.
 * The ordering rules involving not described page numbers are ignored.
 * 1. Start by identifying which updates are already in the right order (the described page order in the update follow all appliable rules).
 */
public class PrintQueue {

    private Integer[][] rules;
    private Integer[][] updates;

    public PrintQueue(String fileName) {
        computeInput(fileName);
    }

    private void computeInput(String fileName) {
        List<Integer[]> r = new ArrayList<>();
        List<Integer[]> u = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line = null;
            while ((line = in.readLine()) != null) {
                if (line.matches("[0-9]+\\|[0-9]+")) {
                    r.add(decode(line, 'r'));
                } else if (line.matches("([0-9]+,)+[0-9]+")) {
                    u.add(decode(line, 'u'));
                }
            }
            rules = r.toArray(Integer[][]::new);
            updates = u.toArray(Integer[][]::new);
        } catch (IOException e) {
            System.out.println("Invalid fileName: " + fileName);
        }
    }

    private Integer[] decode(String line, char type) {
        String splittingChar = (type == 'r') ? "\\|" : ",";
        String[] s = line.split(splittingChar);
        List<Integer> n = new ArrayList<>();
        for (String nStr : s) {
            n.add(Integer.valueOf(nStr));
        }
        return n.toArray(Integer[]::new);
    }

    public static void main(String[] args) {
        String fileName = "C:\\Users\\Lucas\\Documents\\My Repos\\Advent-of-code\\2024\\05\\test.txt";
        PrintQueue p = new PrintQueue(fileName);
        System.out.println("Rules: " + Arrays.toString(p.rules));
        System.out.println("Updates: " + Arrays.toString(p.updates));
    }
}
