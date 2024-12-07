
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BridgeRepair {

    private final List<Long> testValues;
    private final List<List<Long>> operands;
    private final char[] OP = {'*', '+'};  // Available operators

    public BridgeRepair(String fileName) {
        testValues = new ArrayList<>();
        operands = new ArrayList<>();
        readInput(fileName);
    }

    private void readInput(String fileName) {
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line = null;
            while ((line = in.readLine()) != null) {
                String[] split1 = line.split(":");              // "190: 10 19" -> {"190", " 10 19"}
                String[] split2 = split1[1].strip().split(" "); // " 10 19" -> "10 19" -> {"10", "19"}
                List<Long> operands = new ArrayList<>();           // {"10", "19"} -> {10, 19}
                for (String nStr : split2) {
                    operands.add(Long.valueOf(nStr));
                }
                this.operands.add(operands);
                this.testValues.add(Long.valueOf(split1[0]));
            }
            // System.out.println("Read input successfully: " + this.operands + ", " + this.testValues);
        } catch (IOException e) {
            // System.out.println("Invalid fileName: " + fileName);
        }
    }

    private long calibrateResult() {
        long sum = 0;
        for (int eq = 0; eq < testValues.size(); eq++) {
            // System.out.println(" - ".repeat(6) + "Testing equation " + eq + " - ".repeat(6));
            if (isValid(operands.get(eq), testValues.get(eq), 0)) {
                sum += testValues.get(eq);
                // System.out.println("Equation " + eq + " is valid. Current sum: " + sum);
            } else {
                // System.out.println("Equation " + eq + " is invalid.");
            }
        }
        return sum;
    }

    private boolean isValid(List<Long> operands, long testValue, long currentValue) {
        boolean testSum = testEquation(operands, '+', testValue, currentValue);
        boolean testProduct = testEquation(operands, '*', testValue, currentValue);
        return (testSum || testProduct);
    }

    private boolean testEquation(List<Long> operands, char operator, long testValue, long currentValue) {
        // System.out.println("Testing with operator " + operator + ", operands " + operands + ", test value " + testValue);
        if (currentValue == testValue && operands.isEmpty()) {
            // System.out.println("Current value equals test value: " + currentValue);
            return true;
        }

        if (currentValue > testValue || operands.isEmpty()) {
            // System.out.println("Current value exceeds test value or operands are empty: " + currentValue);
            return false;
        }

        currentValue = switch (operator) {
            case '+' ->
                currentValue + operands.getFirst();
            case '*' ->
                currentValue * operands.getFirst();
            default ->
                throw new IllegalArgumentException("Unexpected value: " + operator);
        };

        // System.out.println("Updated current value: " + currentValue);
        List<Long> newOperands = new ArrayList<>(operands.subList(1, operands.size()));
        return (isValid(newOperands, testValue, currentValue));

    }

    public static void main(String[] args) {
        String fileName = "C:\\Users\\Lucas\\Documents\\My Repos\\Advent-of-code\\2024\\07\\input.txt";
        BridgeRepair b = new BridgeRepair(fileName);
        System.out.println("Calibrated result: " + b.calibrateResult());
    }

}
