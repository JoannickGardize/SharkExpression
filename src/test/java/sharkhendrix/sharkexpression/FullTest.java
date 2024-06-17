package sharkhendrix.sharkexpression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FullTest {

    private static ExpressionFactory factory;

    @BeforeAll
    static void initialize() {
        MapVariablePool variablePool = new MapVariablePool();
        variablePool.set("variable1", 2);
        variablePool.set("variable2", 3);
        factory = new ExpressionFactory(variablePool);
    }

    @Test
    void fullTest() {
        Expression exp = factory.parse("variable1 > 2.1 ? -3 : -2 * (-2+4+1)+variable2*3/2 ");
        Assertions.assertEquals(-1.5, exp.evaluate());
    }
}
