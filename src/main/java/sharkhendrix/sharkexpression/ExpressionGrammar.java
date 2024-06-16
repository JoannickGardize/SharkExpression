package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.BinaryOperator;
import sharkhendrix.sharkexpression.token.UnaryOperator;
import sharkhendrix.sharkexpression.token.VariableNumber;

public interface ExpressionGrammar {

    UnaryOperator getUnaryOperator(String operator);

    BinaryOperator getBinaryOperator(String operator);

    VariableNumber getVariable(String name);

    default int leftParenthesis() {
        return '(';
    }

    default int rightParenthesis() {
        return ')';
    }

    default int decimalSeparator() {
        return '.';
    }

    default boolean isSpecialCharAuthorizedInWords(int c) {
        return c == '_';
    }
}
