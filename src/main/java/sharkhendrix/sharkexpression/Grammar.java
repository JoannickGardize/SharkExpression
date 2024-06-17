package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.BinaryOperator;
import sharkhendrix.sharkexpression.token.Function;
import sharkhendrix.sharkexpression.token.UnaryOperator;
import sharkhendrix.sharkexpression.token.VariableNumber;

public interface Grammar {

    UnaryOperator getUnaryOperator(String symbol);

    BinaryOperator getBinaryOperator(String symbol);

    VariableNumber getVariable(String name);

    Function getFunction(String name);

    default boolean isLeftParenthesis(int c) {
        return c == '(' || c == '[';
    }

    default boolean isRightParenthesis(int c) {
        return c == ')' || c == ']';
    }

    default boolean matchParenthesis(int left, int right) {
        return left == '(' && right == ')' || left == '[' && right == ']';
    }

    default int functionArgsSeparator() {
        return ',';
    }

    default int decimalSeparator() {
        return '.';
    }

    default boolean isSpecialCharAuthorizedInWords(int c) {
        return c == '_';
    }

    default boolean allowMissingParenthesisForNoArgsFunction() {
        return true;
    }
}
