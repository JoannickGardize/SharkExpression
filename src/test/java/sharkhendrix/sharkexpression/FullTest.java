/*
 * Copyright 2024 Joannick Gardize
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package sharkhendrix.sharkexpression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sharkhendrix.sharkexpression.grammar.Variables;

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
        // infix: variable1 > 2.1 or 0 ? -3 : -2 * (-2+4+1)+variable2*3/2
        // postfix: v1 2.1 > 0 or -3 -2 -2 4 + 1 + * v2 3 * 2 / + ?:
        // simplified: v1 2.1 > 0 or -3 -6 v2 3 * BUG
        Expression exp = factory.parse("variable1 > 2.1 or 0 ? -3 : -2 * (-2+4+1)+variable2*3/2 ");
        Assertions.assertEquals(-1.5, exp.evaluate());
    }

    @Test
    void fullTest2() {
        Expression exp = factory.parse("2^2^4");
        Assertions.assertEquals(65536, exp.evaluate());
    }
}
