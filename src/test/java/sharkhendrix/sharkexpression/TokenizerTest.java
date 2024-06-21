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
import sharkhendrix.sharkexpression.Tokenizer.TokenTrack;
import sharkhendrix.sharkexpression.token.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sharkhendrix.sharkexpression.GrammarTestKit.*;

class TokenizerTest {

    @Test
    void tokenizeTest() {
        String str = " _abc34>=  - (.5+1.6) + -3 plus max(2, 3)+ vâr  ";
        List<Token> actual = new Tokenizer(grammar).tokenize(str);
        List<Token> expected = Arrays.asList(
                abc, gte, negative, LeftParenthesis.getInstance(), new ConstantNumber(.5f), plus,
                new ConstantNumber(1.6f), RightParenthesis.getInstance(), plus, negative,
                new ConstantNumber(3), plus, max, LeftParenthesis.getInstance(), new ConstantNumber(2),
                ArgSeparator.getInstance(), new ConstantNumber(3), RightParenthesis.getInstance(), plus, vaar
        );
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void tokenizeWithTrackTest() {
        String str = " _abc34>=  - (.5+1.6)";
        List<TokenTrack> tokenTracks = new ArrayList<>();
        List<ValidationError> errors = new ArrayList<>();
        new Tokenizer(grammar).tokenize(str, tokenTracks, errors);

        List<TokenTrack> expected = Arrays.asList(
                new TokenTrack(1, "_abc34"), new TokenTrack(7, ">="), new TokenTrack(11, "-"),
                new TokenTrack(13, "("), new TokenTrack(14, ".5"), new TokenTrack(16, "+"),
                new TokenTrack(17, "1.6"), new TokenTrack(20, ")")
        );
        Assertions.assertEquals(expected, tokenTracks);
        Assertions.assertTrue(errors.isEmpty());

    }

    @Test
    void invalidNumberFormatTest() {
        Assertions.assertThrows(InvalidExpressionSyntaxException.class, () ->
                new Tokenizer(grammar).tokenize("3 + 4 - 6..4")
        );
    }

    @Test
    void tokenizeWithTrackErrorsTest() {
        String str = "3 + 4 + ..4 + #6";
        List<TokenTrack> tokenTracks = new ArrayList<>();
        List<ValidationError> errors = new ArrayList<>();

        List<Token> tokens = new Tokenizer(grammar).tokenize(str, tokenTracks, errors);

        List<ValidationError> expectedErrors = Arrays.asList(
                new ValidationError(ValidationError.Type.NUMBER_FORMAT, 8, "..4"),
                new ValidationError(ValidationError.Type.UNKNOWN_SYMBOL, 14, "#")
        );

        Assertions.assertEquals(expectedErrors, errors);
        Assertions.assertEquals(new ConstantNumber(0), tokens.get(4));
        Assertions.assertSame(Tokenizer.unknownToken, tokens.get(6));
    }

    @Test
    void unknownSymbolTest() {
        Assertions.assertThrows(InvalidExpressionSyntaxException.class, () ->
                new Tokenizer(grammar).tokenize("3 + 4 # 6")
        );
    }

    @Test
    void unknownVariableTest() {
        Assertions.assertThrows(InvalidExpressionSyntaxException.class, () ->
                new Tokenizer(grammar).tokenize("3 + 4 - sus")
        );
    }
}