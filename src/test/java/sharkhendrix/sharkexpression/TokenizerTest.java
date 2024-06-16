package sharkhendrix.sharkexpression;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sharkhendrix.sharkexpression.token.ConstantNumber;
import sharkhendrix.sharkexpression.token.ExpressionToken;
import sharkhendrix.sharkexpression.token.LeftParenthesis;
import sharkhendrix.sharkexpression.token.RightParenthesis;

import java.util.Arrays;
import java.util.List;

import static sharkhendrix.sharkexpression.GrammarTestKit.*;

class TokenizerTest {
    
    @Test
    void tokenizeTest() {
        String str = " _abc34>=  - (.5+1.6) + -3 + vâr ";
        List<ExpressionToken> actual = null;
        try {
            actual = new Tokenizer(GrammarTestKit.grammar).tokenize(str);
        } catch (InvalidExpressionSyntaxException e) {
            Assertions.fail(e);
        }
        List<ExpressionToken> expected = Arrays.asList(
                abc, gte, negative, LeftParenthesis.getInstance(), new ConstantNumber(.5f), plus,
                new ConstantNumber(1.6f), RightParenthesis.getInstance(), plus, negative, new ConstantNumber(3), plus, vaar
        );
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void invalidNumberFormatTest() {
        Assertions.assertThrows(InvalidExpressionSyntaxException.class, () ->
                new Tokenizer(grammar).tokenize("3 + 4 - 6..4")
        );
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