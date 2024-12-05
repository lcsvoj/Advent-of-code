
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* New pages for the safety manuals must be printed in a very specific order.
 * The notation X|Y means that if both page number X and page number Y are to be updated, page number X must be printed at some point before page number Y, not necessarily immediately.
 * The Elf has for you both the page ordering rules and the pages to produce in each update (your puzzle input), but can't figure out whether each update has the pages in the right order.
 * In the input, the first section specifies the page ordering rules, one per line, and the second section specifies on each update (one per line) the page numbers to be updated.
 * The ordering rules involving undescribed page numbers are ignored.
 * 1. Start by identifying which updates are already in the right order (the described page order in the update follow all appliable rules).
 */
public class PrintQueue {

    private List<List<Integer>> rules;
    private List<Update> updates;

    public PrintQueue(String fileName) {
        rules = new ArrayList<>();
        updates = new ArrayList<>();
        computeInput(fileName);
        // validUpdates = updates.stream().filter(u -> isValid(u)).toList();
    }

    private class Update {

        private final List<Integer> pages;
        public boolean isValid;
        public int middlePage;

        public Update(List<Integer> update) {
            pages = update;
            isValid = validateUpdate();
            middlePage = pages.get(Math.floorDiv(pages.size(), 2));
        }

        @SuppressWarnings("UnnecessaryContinue")
        private boolean validateUpdate() {
            for (List<Integer> rule : rules) {
                int x = rule.get(0);
                int y = rule.get(1);
                if (!(pages.contains(x) && pages.contains(y))) {
                    continue;  // The ordering rules involving undescribed page numbers are ignored.
                } else if (!(pages.indexOf(x) < pages.indexOf(y))) {
                    return false;
                }
            }
            return true;
        }

    }

    private void computeInput(String fileName) {
        List<List<Integer>> r = new ArrayList<>();
        List<Update> u = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line = null;
            while ((line = in.readLine()) != null) {
                if (line.matches("[0-9]+\\|[0-9]+")) {
                    rules.add(decode(line, 'r'));
                } else if (line.matches("([0-9]+,)+[0-9]+")) {
                    updates.add(new Update(decode(line, 'u')));
                }
            }
        } catch (IOException e) {
            System.out.println("Invalid fileName: " + fileName);
        }
    }

    private List<Integer> decode(String line, char type) {
        String splittingChar = (type == 'r') ? "\\|" : ",";
        String[] s = line.split(splittingChar);
        List<Integer> n = new ArrayList<>();
        for (String nStr : s) {
            n.add(Integer.valueOf(nStr));
        }
        return n;
    }

    private int sumValidUpdates() {
        int sum = 0;
        for (Update u : updates) {
            if (u.isValid) {
                // System.out.printf("The update %s is valid, adding %d to the sum.\n", u.toString(), u.middlePage);
                sum += u.middlePage;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        String fileName = "C:\\Users\\Lucas\\Documents\\My Repos\\Advent-of-code\\2024\\05\\input.txt";
        PrintQueue p = new PrintQueue(fileName);
        // System.out.println("Rules: " + (p.rules.toString()));
        System.out.println(p.sumValidUpdates());
    }
}
