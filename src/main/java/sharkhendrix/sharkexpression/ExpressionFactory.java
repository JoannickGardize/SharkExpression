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
    private final ExpressionValidator validator;

    public ExpressionFactory(Variables variables) {
        Grammar grammar = new Grammar(variables);
        tokenizer = new Tokenizer(grammar);
        createDefaultTokenSequenceFunctions();
        validator = new ExpressionValidator(tokenizer, grammar);
    }

    public ExpressionFactory(Grammar grammar) {
        tokenizer = new Tokenizer(grammar);
        createDefaultTokenSequenceFunctions();
        validator = new ExpressionValidator(tokenizer, grammar);
    }

    public ExpressionFactory(Tokenizer tokenizer, ExpressionValidator validator, TokenSequenceFunction... tokenSequenceFunctions) {
        this.tokenizer = tokenizer;
        this.tokenSequenceFunctions = Arrays.copyOf(tokenSequenceFunctions, tokenSequenceFunctions.length);
        this.validator = validator;
    }

    private void createDefaultTokenSequenceFunctions() {
        tokenSequenceFunctions = new TokenSequenceFunction[]{
                new ShuntingYardAlgorithm(),
                new ExpressionSimplifier()
        };
    }

    public List<ValidationError> validate(String expressionStr) {
        return validator.validate(expressionStr);
    }

    public Expression parse(String expressionStr) {
        List<Token> tokens = tokenizer.tokenize(expressionStr);
        for (TokenSequenceFunction function : tokenSequenceFunctions) {
            tokens = function.apply(tokens);
        }
        return new Expression(tokens.toArray(new Token[tokens.size()]));
    }
}
