package sharkhendrix.sharkexpression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sharkhendrix.sharkexpression.token.*;

import java.util.Arrays;
import java.util.List;

import static sharkhendrix.sharkexpression.GrammarTestKit.*;

class ShuntingYardAlgorithmTest {

    @Test
    void applyTest() {
        // 3 + 5 * -vaar * (7 + 9) + 11
        List<Token> tokens = Arrays.asList(
                new ConstantNumber(3),
                plus,
                new ConstantNumber(5),
                multiply,
                negative,
                vaar,
                multiply,
                LeftParenthesis.getInstance(),
                new ConstantNumber(7),
                plus,
                new ConstantNumber(9),
                RightParenthesis.getInstance(),
                plus,
                new ConstantNumber(11)
        );
        List<Token> actual = new ShuntingYardAlgorithm().apply(tokens);
        // 3 5 x - * 7 9 + * + 11 +
        List<Token> expected = Arrays.asList(
                new ConstantNumber(3),
                new ConstantNumber(5),
                vaar,
                negative,
                multiply,
                new ConstantNumber(7),
                new ConstantNumber(9),
                plus,
                multiply,
                plus,
                new ConstantNumber(11),
                plus
        );

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void applyWithFunctionTest() {
        // 2 + max(3, 3 + 1)
        List<Token> tokens = Arrays.asList(
                new ConstantNumber(2),
                plus,
                max,
                LeftParenthesis.getInstance(),
                new ConstantNumber(3),
                ArgSeparator.getInstance(),
                new ConstantNumber(3),
                plus,
                new ConstantNumber(1),
                RightParenthesis.getInstance()
        );
        List<Token> actual = new ShuntingYardAlgorithm().apply(tokens);
        // 2 3 3 1 + max +
        List<Token> expected = Arrays.asList(
                new ConstantNumber(2),
                new ConstantNumber(3),
                new ConstantNumber(3),
                new ConstantNumber(1),
                plus,
                max,
                plus
        );

        Assertions.assertEquals(expected, actual);
    }
}