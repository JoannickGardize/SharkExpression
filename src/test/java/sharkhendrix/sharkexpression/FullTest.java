package sharkhendrix.sharkexpression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FullTest {

    private static ExpressionFactory factory;

    @BeforeAll
    static void initialize() {
        Variables variables = new Variables();
        variables.add("variable1", () -> 2);
        variables.add("variable2", () -> 3);
        factory = new ExpressionFactory(variables);
    }

    @Test
    void fullTest() {
        Expression exp = factory.parse("variable1 > 2.1 ? -3 : -2 * (-2+4+1)+variable2*3/2 ");
        Assertions.assertEquals(-1.5, exp.evaluate());
    }
}
