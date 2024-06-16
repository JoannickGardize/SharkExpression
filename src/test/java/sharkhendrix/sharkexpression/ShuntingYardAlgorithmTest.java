package sharkhendrix.sharkexpression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sharkhendrix.sharkexpression.token.ConstantNumber;
import sharkhendrix.sharkexpression.token.ExpressionToken;
import sharkhendrix.sharkexpression.token.LeftParenthesis;
import sharkhendrix.sharkexpression.token.RightParenthesis;

import java.util.Arrays;
import java.util.List;

import static sharkhendrix.sharkexpression.GrammarTestKit.*;

class ShuntingYardAlgorithmTest {

    @Test
    void applyTest() throws InvalidExpressionSyntaxException {
        // 3 + 5 * -vaar * (7 + 9) + 11
        List<ExpressionToken> expressionTokens = Arrays.asList(
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
        List<ExpressionToken> actual = new ShuntingYardAlgorithm().apply(expressionTokens);
        // 3 5 x - * 7 9 + * + 11 +
        List<ExpressionToken> expected = Arrays.asList(
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
}