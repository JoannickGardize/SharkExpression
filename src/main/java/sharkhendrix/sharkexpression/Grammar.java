package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.BinaryOperator;
import sharkhendrix.sharkexpression.token.Function;
import sharkhendrix.sharkexpression.token.UnaryOperator;
import sharkhendrix.sharkexpression.token.VariableNumber;

public class Grammar {

    private final Operators operators;
    private final Functions functions;
    private final Variables variables;

    public Grammar(Operators operators, Functions functions, Variables variables) {
        this.operators = operators;
        this.functions = functions;
        this.variables = variables;
    }

    public Grammar(Variables variables) {
        operators = new Operators();
        DefaultOperators.apply(operators);
        functions = new Functions();
        DefaultFunctions.apply(functions);
        this.variables = variables;
    }

    public UnaryOperator getUnaryOperator(String symbol) {
        return operators.getUnary(symbol);
    }

    public BinaryOperator getBinaryOperator(String symbol) {
        return operators.getBinary(symbol);
    }

    public VariableNumber getVariable(String name) {
        return variables.get(name);
    }

    public Function getFunction(String name) {
        return functions.get(name);
    }

    public boolean isLeftParenthesis(int c) {
        return c == '(' || c == '[';
    }

    public boolean isRightParenthesis(int c) {
        return c == ')' || c == ']';
    }

    public boolean matchParenthesis(int left, int right) {
        return left == '(' && right == ')' || left == '[' && right == ']';
    }

    public int functionArgsSeparator() {
        return ',';
    }

    public int decimalSeparator() {
        return '.';
    }

    public boolean isSpecialCharAuthorizedInWords(int c) {
        return c == '_';
    }
}
