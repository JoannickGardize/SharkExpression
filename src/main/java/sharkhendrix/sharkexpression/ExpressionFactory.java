package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.BinaryOperator;
import sharkhendrix.sharkexpression.token.ExpressionToken;
import sharkhendrix.sharkexpression.token.UnaryOperator;
import sharkhendrix.sharkexpression.token.VariableNumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Expression build steps:
 * <ol>
 *     <li>Tokenize input string.</li>
 *     <li>Shunting yard algorithm to produce postfix ordered tokens.</li>
 *     <li>Merge ternary operators to their final forms and simplify constant branches.</li>
 * </ol>
 */
public class ExpressionFactory implements ExpressionGrammar {

    private Tokenizer tokenizer;

    private Operators operators;


    private List<TokenSequenceFunction> tokenSequenceFunctions = new ArrayList<>();

    private final Map<String, VariableNumber> variables = new HashMap<>();

    public static ExpressionFactory createDefault(VariablePool variablePool) {
        ExpressionFactory factory = new ExpressionFactory();
        Operators operators = new Operators();
        DefaultOperators.apply(operators);
        factory.setOperators(operators);
        factory.setVariablePool(variablePool);
        factory.setTokenizer(new Tokenizer(factory));
        factory.getTokenSequenceFunctions().add(new ShuntingYardAlgorithm());
        factory.getTokenSequenceFunctions().add(new ExpressionSimplifier());
        return factory;
    }

    public Expression parse(String expressionStr) throws InvalidExpressionSyntaxException {
        List<ExpressionToken> tokens = tokenizer.tokenize(expressionStr);
        for (TokenSequenceFunction function : tokenSequenceFunctions) {
            tokens = function.apply(tokens);
        }
        return new Expression(tokens.toArray(new ExpressionToken[tokens.size()]));
    }

    public void setVariablePool(VariablePool variablePool) {
        variables.clear();
        variablePool.forEach((name, index) -> variables.put(name, new VariableNumber(variablePool, index)));
    }

    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    public void setTokenizer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public Operators getOperators() {
        return operators;
    }

    public void setOperators(Operators operators) {
        this.operators = operators;
    }

    public List<TokenSequenceFunction> getTokenSequenceFunctions() {
        return tokenSequenceFunctions;
    }

    public void setTokenSequenceFunctions(List<TokenSequenceFunction> tokenSequenceFunctions) {
        this.tokenSequenceFunctions = tokenSequenceFunctions;
    }

    @Override
    public UnaryOperator getUnaryOperator(String operator) {
        return operators.getUnary(operator);
    }

    @Override
    public BinaryOperator getBinaryOperator(String operator) {
        return operators.getBinary(operator);
    }

    @Override
    public VariableNumber getVariable(String name) {
        return variables.get(name);
    }
}
