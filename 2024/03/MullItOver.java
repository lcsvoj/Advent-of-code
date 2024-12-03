
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/* The computer appears to be trying to run a program, but its memory (your puzzle input) is corrupted.
 * It seems like the goal of the program is just to multiply some numbers. 
 * It does that with instructions like mul(X,Y), where X and Y are each 1-3 digit numbers.
 * However, because the program's memory has been corrupted, there are also many invalid characters that 
 * should be ignored, even if they look like part of a mul instruction.
 * What do you get if you add up all of the results of the multiplications?
 */
public class MullItOver {

    public static long getSum(String fileName) {
        long sum = 0;
        String str = null;
        boolean active = true;
        try (Scanner in = new Scanner(new FileInputStream(fileName))) {
            while ((str = in.findWithinHorizon("mul\\([0-9]+,[0-9]+\\)|don't\\(\\)|do\\(\\)", 0)) != null) {
                switch (str) {
                    case "do()":
                        active = true;
                        break;
                    case "don't()":
                        active = false;
                        break;
                    default:
                        if (active) {
                            sum += compute(str);
                        }
                }
            }
        } catch (IOException e) {
            System.out.println("Invalid fileName: " + fileName);
        }
        return sum;
    }

    private static long compute(String mul) {
        int firstParenthesis = mul.indexOf('(');
        int comma = mul.indexOf(',');
        int secondParenthesis = mul.indexOf(')');
        long x = Long.parseLong(mul.substring(firstParenthesis + 1, comma));
        long y = Long.parseLong(mul.substring(comma + 1, secondParenthesis));
        // System.out.printf(" = %d * %d = %d\n", x, y, Math.multiplyExact(x, y));
        return Math.multiplyExact(x, y);
    }

    public static void main(String[] args) {
        System.out.println("Sum = " + MullItOver.getSum("C:\\Users\\Lucas\\Documents\\My Repos\\Advent-of-code\\2024\\03\\input.txt"));
    }

}
