package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.BinaryOperator;
import sharkhendrix.sharkexpression.token.Function;
import sharkhendrix.sharkexpression.token.UnaryOperator;
import sharkhendrix.sharkexpression.token.VariableNumber;

import java.util.HashMap;
import java.util.Map;

public class DefaultGrammar implements Grammar {

    private final Operators operators;

    private final Functions functions;

    private final Map<String, VariableNumber> variables = new HashMap<>();

    public DefaultGrammar(Operators operators, Functions functions, VariablePool variablePool) {
        this.operators = operators;
        this.functions = functions;
        variablePool.forEach((name, index) -> variables.put(name, new VariableNumber(variablePool, index)));
    }

    public DefaultGrammar(VariablePool variablePool) {
        operators = new Operators();
        DefaultOperators.apply(operators);
        functions = new Functions();
        DefaultFunctions.apply(functions);
        variablePool.forEach((name, index) -> variables.put(name, new VariableNumber(variablePool, index)));
    }

    public Operators getOperators() {
        return operators;
    }

    public Functions getFunctions() {
        return functions;
    }

    @Override
    public UnaryOperator getUnaryOperator(String symbol) {
        return operators.getUnary(symbol);
    }

    @Override
    public BinaryOperator getBinaryOperator(String symbol) {
        return operators.getBinary(symbol);
    }

    @Override
    public VariableNumber getVariable(String name) {
        return variables.get(name);
    }

    @Override
    public Function getFunction(String name) {
        return functions.get(name);
    }
}
