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
import sharkhendrix.sharkexpression.grammar.Grammar;
import sharkhendrix.sharkexpression.grammar.Variables;
import sharkhendrix.sharkexpression.token.Token;

import java.util.ArrayList;
import java.util.List;

class ExpressionFactoryTest {

    @Test
    void buildStandAloneExpression() {
        ExpressionFactory factory = new ExpressionFactory(new Variables());
        Expression exp = factory.parse("3");
        Assertions.assertEquals(StandaloneExpression.class, exp.getClass());
        factory.buildStandAloneExpression(true);
        exp = factory.parse("3");
        Assertions.assertEquals(StandaloneExpression.class, exp.getClass());
        factory.buildStandAloneExpression(false);
        exp = factory.parse("3");
        Assertions.assertEquals(Expression.class, exp.getClass());
    }

    private int callCount;

    @Test
    void validate() {
        Grammar grammar = Grammar.withDefault(new Variables());
        Tokenizer tokenizer = new Tokenizer(grammar);
        callCount = 0;
        ExpressionValidator validator = new ExpressionValidator(tokenizer, grammar) {
            @Override
            public List<Token> validate(String expressionStr, List<ValidationError> errors) {
                Assertions.assertEquals("3+4", expressionStr);
                Assertions.assertEquals(0, errors.size());
                callCount++;
                return null;
            }
        };

        ExpressionFactory factory = new ExpressionFactory(validator, tokenizer);
        List<ValidationError> returnValue = factory.validate("3+4");

        Assertions.assertEquals(1, callCount);
        Assertions.assertEquals(0, returnValue.size());
    }

    @Test
    void parse() {
        ExpressionFactory factory = new ExpressionFactory(new Variables());
        Expression exp = factory.parse("3+4");
        Assertions.assertEquals(1, exp.tokenLength());
    }

    @Test
    void validateAndParse() {
        ExpressionFactory factory = new ExpressionFactory(new Variables());
        List<ValidationError> errors = new ArrayList<>();
        Expression exp = factory.validateAndParse("3+4", errors);
        Assertions.assertEquals(0, errors.size());
        Assertions.assertEquals(1, exp.tokenLength());
        exp = factory.validateAndParse("3+", errors);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertNull(exp);
    }
}