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
import org.junit.jupiter.api.Test;
import sharkhendrix.sharkexpression.token.ConstantNumber;
import sharkhendrix.sharkexpression.token.Function;
import sharkhendrix.sharkexpression.token.Token;

import java.util.Arrays;
import java.util.List;

import static sharkhendrix.sharkexpression.GrammarTestKit.*;

class ExpressionTest {

    @Test
    void evaluateTest() {
        // 3 5 x - * 7 9 + * + 11 +
        List<Token> tokens = Arrays.asList(
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
        Expression expression = new StandaloneExpression(tokens.toArray(new Token[0]));

        Assertions.assertEquals(-626, expression.evaluate());
    }

    @Test
    void evaluateWithTernary() {
        // infix: abc >= 3 ? 0 ? 1 : 2 * 3 + 3 : 3
        // postfix: abc 3 >= 0 1 2 3 * 3 + ?: 3 ?:
        Token[] tokens = new Token[]{
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
        };
        Expression expression = new StandaloneExpression(tokens);

        Assertions.assertEquals(9, expression.evaluate());
    }

    @Test
    void evaluateWithFunction() {
        // 2 + max(3, 3 + 1)
        // 2 3 3 1 + max +
        Token[] tokens = new Token[]{
                new ConstantNumber(2),
                new ConstantNumber(3),
                new ConstantNumber(3),
                new ConstantNumber(1),
                plus,
                max,
                plus
        };
        Expression expression = new StandaloneExpression(tokens);

        Assertions.assertEquals(6, expression.evaluate());
    }

    @Test
    void functionArgsOrderTest() {
        Function function = (Function.FourArgs) (a, b, c, d) -> {
            if (a != 1) {
                Assertions.fail("First arg is not 1");
            }
            if (b != 2) {
                Assertions.fail("Second arg is not 2");
            }
            if (c != 3) {
                Assertions.fail("Third arg is not 3");
            }
            if (d != 4) {
                Assertions.fail("Fourth arg is not 4");
            }
            return 1;
        };
        new StandaloneExpression(new Token[]{
                new ConstantNumber(1),
                new ConstantNumber(2),
                new ConstantNumber(3),
                new ConstantNumber(4),
                function}).evaluate();
    }
}