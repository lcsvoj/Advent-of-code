
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* Each scratchcard has two lists of numbers separated by a vertical bar (|): a list of winning numbers and then a list of numbers you have.
 * You organize the information into a table (your puzzle input). You have to figure out which of the numbers you have appear in the list of winning numbers.
 * The first match makes the card worth one point and each match after the first doubles the point value of that card.
 * How many points are they worth in total?
 */
public class Scratchcards {

    private List<Card> cards = new ArrayList<>();

    public void addCard(String cardString) {
        Card newCard = new Card(cardString);
        cards.add(newCard);
    }

    private class Card {

        private final List<Integer> winningNumbers;
        private final List<Integer> cardNumbers;
        private double points;

        public Card(String cardString) {
            String[] groupsOfNumbers = cardString.split("\\|");

            List<String> winningNumbersStr = Arrays.asList(groupsOfNumbers[0].split("\\s+"));
            winningNumbers = new ArrayList<>();
            for (String n : winningNumbersStr) {
                if (n.matches("[0-9]+")) {
                    winningNumbers.add(Integer.valueOf(n));
                }
            }

            List<String> cardNumbersStr = Arrays.asList(groupsOfNumbers[1].split("\\s+"));
            cardNumbers = new ArrayList<>();
            for (String n : cardNumbersStr) {
                if (n.matches("[0-9]+")) {
                    cardNumbers.add(Integer.valueOf(n));
                }
            }

            points = computeCardPoints(winningNumbers, cardNumbers);
        }

    }

    private double computeCardPoints(List<Integer> winningNumbers, List<Integer> cardNumbers) {
        double matchingNumbers = cardNumbers.stream()
                .filter(n -> winningNumbers.contains(n))
                .count();
        return matchingNumbers > 0 ? Math.pow(2, matchingNumbers - 1) : 0;
    }

    private double computeSum() {
        return cards.stream()
                .mapToDouble(c -> c.points)
                .sum();
    }

    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\Lucas\\Documents\\My Repos\\Advent-of-code\\2023\\04\\input.txt"))) {
            String line = null;
            Scratchcards cards = new Scratchcards();
            while ((line = in.readLine()) != null) {
                cards.addCard(line);
            }
            System.out.println(cards.computeSum());
        } catch (IOException e) {
            System.out.println("Invalid fileName");
        }
    }

}
