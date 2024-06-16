package sharkhendrix.sharkexpression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sharkhendrix.sharkexpression.token.ConstantNumber;
import sharkhendrix.sharkexpression.token.ExpressionToken;

import java.util.Arrays;
import java.util.List;

import static sharkhendrix.sharkexpression.GrammarTestKit.*;

class ExpressionTest {

    @Test
    void evaluateTest() {
        // 3 5 x - * 7 9 + * + 11 +
        List<ExpressionToken> tokens = Arrays.asList(
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
        Expression expression = new Expression(tokens.toArray(new ExpressionToken[tokens.size()]));

        Assertions.assertEquals(-626, expression.evaluate());
    }

    @Test
    void evaluateWithTernary() {
        // infix: abc >= 3 ? 0 ? 1 : 2 * 3 + 3 : 3
        // postfix: abc 3 >= 0 1 2 3 * 3 + ?: 3 ?:
        List<ExpressionToken> tokens = Arrays.asList(
                abc,
                new ConstantNumber(3),
                gte,
                new ConstantNumber(0),
                new ConstantNumber(1),
                new ConstantNumber(2),
                new ConstantNumber(3),
                multiply,
                new ConstantNumber(3),
                plus,
                ternary,
                new ConstantNumber(3),
                ternary
        );
        Expression expression = new Expression(tokens.toArray(new ExpressionToken[tokens.size()]));

        Assertions.assertEquals(9, expression.evaluate());
    }
}