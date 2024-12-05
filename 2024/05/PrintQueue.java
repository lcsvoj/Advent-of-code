
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
 * For some reason, the Elves also need to know the middle page number of each update being printed.
 * 1. What do you get if you add up the middle page number from those correctly-ordered updates?
 * 2. What do you get if you add up the middle page numbers after correctly ordering only the initially incorrectly ordered updates?
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

    @SuppressWarnings("UnnecessaryContinue")
    private Update fixUpdate(Update u) {

        if (u.isValid) {
            return u;
        }

        Update newU = new Update(u.pages);
        for (List<Integer> rule : rules) {
            int x = rule.get(0);
            int y = rule.get(1);
            if (!(u.pages.contains(x) && u.pages.contains(y))) {
                continue;  // The ordering rules involving undescribed page numbers are ignored.
            } else if (!(u.pages.indexOf(x) < u.pages.indexOf(y))) {
                newU.pages.set(u.pages.indexOf(x), y);
                newU.pages.set(u.pages.indexOf(y), x);
            }
        }
        return fixUpdate(newU);
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
                sum += u.middlePage;
            }
        }
        return sum;
    }

    private int fixAndSumInvalidUpdates() {
        int sum = 0;
        for (Update u : updates) {
            if (!u.isValid) {
                System.out.printf("The update %s is invalid.\n", u.pages.toString());
                Update f = fixUpdate(u);
                System.out.printf("\tFixing it returned the new update %s with middle value %d.\n", f.pages.toString(), f.middlePage);
                sum += f.middlePage;
                System.out.printf("The sum is now %d.\n", sum);
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        String fileName = "C:\\Users\\Lucas\\Documents\\My Repos\\Advent-of-code\\2024\\05\\input.txt";
        PrintQueue p = new PrintQueue(fileName);
        // System.out.println("Rules: " + (p.rules.toString()));
        System.out.println(p.fixAndSumInvalidUpdates());
    }
}
