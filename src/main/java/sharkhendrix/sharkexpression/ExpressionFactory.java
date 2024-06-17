package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.Token;

import java.util.Arrays;
import java.util.List;

/**
 * Expression build steps:
 * <ol>
 *     <li>Tokenize input string.</li>
 *     <li>Shunting yard algorithm to produce postfix ordered tokens.</li>
 *     <li>Merge ternary operators to their final forms and simplify constant branches.</li>
 * </ol>
 */
public class ExpressionFactory {

    private final Tokenizer tokenizer;
    private TokenSequenceFunction[] tokenSequenceFunctions;

    public ExpressionFactory(VariablePool variablePool) {
        tokenizer = new Tokenizer(new DefaultGrammar(variablePool));
        createDefaultTokenSequenceFunctions();
    }

    public ExpressionFactory(Grammar grammar) {
        tokenizer = new Tokenizer(grammar);
        createDefaultTokenSequenceFunctions();
    }

    public ExpressionFactory(Tokenizer tokenizer, TokenSequenceFunction... tokenSequenceFunctions) {
        this.tokenizer = tokenizer;
        this.tokenSequenceFunctions = Arrays.copyOf(tokenSequenceFunctions, tokenSequenceFunctions.length);
    }

    private void createDefaultTokenSequenceFunctions() {
        tokenSequenceFunctions = new TokenSequenceFunction[]{
                new ShuntingYardAlgorithm(),
                new ExpressionSimplifier()
        };
    }

    public Expression parse(String expressionStr) throws InvalidExpressionSyntaxException {
        List<Token> tokens = tokenizer.tokenize(expressionStr);
        for (TokenSequenceFunction function : tokenSequenceFunctions) {
            tokens = function.apply(tokens);
        }
        return new Expression(tokens.toArray(new Token[tokens.size()]));
    }
}
