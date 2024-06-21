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

class TernaryOperatorMergerTest {

    @Test
    void apply() {
        List<Token> tokens = Arrays.asList(
                new ConstantNumber(0),
                new ConstantNumber(1),
                new ConstantNumber(2),
                temporaryTernaryRightPart,
                temporaryTernaryLeftPart
        );

        List<Token> actual = new TernaryOperatorMerger().apply(tokens);

        List<Token> expected = Arrays.asList(
                new ConstantNumber(0),
                new ConstantNumber(1),
                new ConstantNumber(2),
                ternary
        );

        Assertions.assertEquals(expected, actual);
    }
}