
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.MatchResult;

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
        String mul = null;
        try (Scanner in = new Scanner(new FileInputStream(fileName))) {
            sum = in.findAll("mul\\([0-9]+,[0-9]+\\)")
                    .map(MatchResult::group)
                    .mapToLong(MullItOver::compute)
                    .sum();
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
        return Math.multiplyExact(x, y);
    }

    public static void main(String[] args) {
        System.out.println("Sum = " + MullItOver.getSum("C:\\Users\\Lucas\\Documents\\My Repos\\Advent-of-code\\2024\\03\\input.txt"));
    }

}
