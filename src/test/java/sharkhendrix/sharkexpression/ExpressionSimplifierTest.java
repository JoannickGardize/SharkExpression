package sharkhendrix.sharkexpression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sharkhendrix.sharkexpression.token.ConstantNumber;
import sharkhendrix.sharkexpression.token.ExpressionToken;

import java.util.Arrays;
import java.util.List;

import static sharkhendrix.sharkexpression.GrammarTestKit.*;

class ExpressionSimplifierTest {

    @Test
    void applyTest() throws InvalidExpressionSyntaxException {
        // infix: abc >= 3 ? 0 ? 1 : 2 * 3 + 3 : 3
        // postfix: abc 3 >= 0 1 2 3 * 3 + ?: 3 ?:
        // simplified: abc 3 >= 9 3 ?:
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
                temporaryTernaryRightPart,
                temporaryTernaryLeftPart,
                new ConstantNumber(3),
                temporaryTernaryRightPart,
                temporaryTernaryLeftPart
        );

        List<ExpressionToken> expected = Arrays.asList(
                abc,
                new ConstantNumber(3),
                gte,
                new ConstantNumber(9),
                new ConstantNumber(3),
                ternary
        );
        List<ExpressionToken> actual = new ExpressionSimplifier().apply(tokens);
        Assertions.assertEquals(expected, actual);
    }
}