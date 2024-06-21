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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static sharkhendrix.sharkexpression.GrammarTestKit.grammar;

class ExpressionValidatorTest {

    private static final ExpressionValidator validator = new ExpressionValidator(new Tokenizer(grammar), grammar);

    @ParameterizedTest(name = "{0}")
    @MethodSource("testParameters")
    void validateTest(String testName, String expressionStr, List<ValidationError> expectedErrors) {
        List<ValidationError> actual = validator.validate(expressionStr);
        Assertions.assertEquals(expectedErrors, actual);
    }

    private static Stream<Arguments> testParameters() {
        return Stream.of(
                Arguments.of("no error", "vâr + max(3, (3) + 1)", Collections.emptyList()
                ),
                Arguments.of("unknown symbol", "vaaar + maax(3, (3) + 1)", Arrays.asList(
                        new ValidationError(ValidationError.Type.UNKNOWN_SYMBOL, 0, "vaaar"),
                        new ValidationError(ValidationError.Type.UNKNOWN_SYMBOL, 8, "maax"))
                ),
                Arguments.of("number format", "4+(5+5.6.5) * 2", Collections.singletonList(
                        new ValidationError(ValidationError.Type.NUMBER_FORMAT, 5, "5.6.5"))
                ),
                Arguments.of("unknown unary", " + 5", Collections.singletonList(
                        new ValidationError(ValidationError.Type.UNKNOWN_SYMBOL, 1, "+"))
                ),
                Arguments.of("unexpected number", "3 5", Collections.singletonList(
                        new ValidationError(ValidationError.Type.UNEXPECTED_TOKEN, 2, "5"))
                ),
                Arguments.of("unexpected left parenthesis", " 5 [3]", Collections.singletonList(
                        new ValidationError(ValidationError.Type.UNEXPECTED_TOKEN, 3, "["))
                ),
                Arguments.of("unexpected right parenthesis", " (5 + ) + 3", Collections.singletonList(
                        new ValidationError(ValidationError.Type.UNEXPECTED_TOKEN, 6, ")"))
                ),
                Arguments.of("unexpected empty parenthesis", " () + 3 ", Collections.singletonList(
                        new ValidationError(ValidationError.Type.UNEXPECTED_TOKEN, 2, ")"))
                ),
                Arguments.of("missing left parenthesis", "4 + 3 + [ 5 + 2 ] ) + 1", Collections.singletonList(
                        new ValidationError(ValidationError.Type.MISSING_LEFT_PARENTHESIS, 18, ")"))
                ),
                Arguments.of("parenthesis mismatch", " (5 + 3 + ( 4 ] + 3 )", Collections.singletonList(
                        new ValidationError(ValidationError.Type.PARENTHESIS_MISMATCH, 14, "]"))
                ),
                Arguments.of("missing right parenthesis", " (5 + 3 + ( 4 + 3 )", Collections.singletonList(
                        new ValidationError(ValidationError.Type.MISSING_RIGHT_PARENTHESIS, 1, "("))
                ),
                Arguments.of("wrong number of argument", " (5 + max(2, 3, 4))", Collections.singletonList(
                        new ValidationError(ValidationError.Type.WRONG_NUMBER_OF_ARGUMENTS, 17, ")"))
                ),
                Arguments.of("empty expression", " ", Collections.singletonList(
                        new ValidationError(ValidationError.Type.EMPTY_EXPRESSION, -1, null))
                ),
                Arguments.of("missing function parenthesis", "3 + max + 4", Collections.singletonList(
                        new ValidationError(ValidationError.Type.UNEXPECTED_TOKEN, 8, "+"))
                )
        );
    }
}