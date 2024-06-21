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
import sharkhendrix.sharkexpression.token.Token;

import java.util.Arrays;
import java.util.List;

import static sharkhendrix.sharkexpression.GrammarTestKit.*;

class ExpressionSimplifierTest {

    @Test
    void applyTest() {
        // infix: abc >= 3 ? 0 ? 1 : 2 * 3 + 3 : 3
        // postfix: abc 3 >= 0 1 2 3 * 3 + ?: 3 ?:
        // simplified: abc 3 >= 9 3 ?:
        List<Token> tokens = Arrays.asList(
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

        List<Token> expected = Arrays.asList(
                abc,
                new ConstantNumber(3),
                gte,
                new ConstantNumber(9),
                new ConstantNumber(3),
                ternary
        );
        List<Token> actual = new ExpressionSimplifier().apply(tokens);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void applyTest2() {
        // infix:  0 + abc + 4 - abc + (max(1, 24) / abc / 2) + 2
        // postfix: 0 abc + 4 + abc - 1 24 max abc / 2 / + 2 +
        // infix simplified step 1: 4 + abc - abc + (12 / abc) + 2
        // infix simplified step 2: 6 + abc - abc + (12 / abc)
        // postfix simplified:  6 abc + abc - 12 abc / +
        List<Token> tokens = Arrays.asList(
                new ConstantNumber(0),
                abc,
                plus,
                new ConstantNumber(4),
                plus,
                abc,
                minus,
                new ConstantNumber(1),
                new ConstantNumber(24),
                max,
                abc,
                divide,
                new ConstantNumber(2),
                divide,
                plus,
                new ConstantNumber(2),
                plus
        );

        List<Token> expected = Arrays.asList(
                new ConstantNumber(6),
                abc,
                plus,
                abc,
                minus,
                new ConstantNumber(12),
                abc,
                divide,
                plus
        );
        List<Token> actual = new ExpressionSimplifier().apply(tokens);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void apply3Test() {
        // infix: 1 ? abc * 2 : 0
        // postfix: 1 abc 2 * ?:
        List<Token> tokens = Arrays.asList(
                new ConstantNumber(1),
                abc,
                new ConstantNumber(2),
                multiply,
                ternary
        );


        List<Token> actual = new ExpressionSimplifier().apply(tokens);
        Assertions.assertEquals(tokens, actual);
    }

    @Test
    void applyTest4() {
        // infix:  abc + 3 + 4
        // postfix: abc 3 + 4 +
        // postfix simplified: abc 7 +
        List<Token> tokens = Arrays.asList(
                abc,
                new ConstantNumber(3),
                plus,
                new ConstantNumber(4),
                plus
        );

        List<Token> expected = Arrays.asList(
                abc,
                new ConstantNumber(7),
                plus
        );
        List<Token> actual = new ExpressionSimplifier().apply(tokens);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void applyTest5() {
        // infix:  2^abc^2
        // postfix: 2 abc 2 ^ ^
        List<Token> tokens = Arrays.asList(
                new ConstantNumber(2),
                abc,
                new ConstantNumber(2),
                power
        );

        List<Token> actual = new ExpressionSimplifier().apply(tokens);
        Assertions.assertEquals(tokens, actual);
    }
}