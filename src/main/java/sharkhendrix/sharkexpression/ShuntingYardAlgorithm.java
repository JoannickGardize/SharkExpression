package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.Number;
import sharkhendrix.sharkexpression.token.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ShuntingYardAlgorithm implements TokenSequenceFunction {

    @Override
    public List<Token> apply(List<Token> tokens) throws InvalidExpressionSyntaxException {
        List<Token> result = new ArrayList<>(tokens.size());
        Deque<Token> operatorStack = new ArrayDeque<>();
        for (Token token : tokens) {
            if (token instanceof Number) {
                result.add(token);
            } else if (token instanceof Function) {
                operatorStack.push(token);
            } else if (token instanceof BinaryOperator || token instanceof UnaryOperator) {
                while (!operatorStack.isEmpty() && comparePrecedence(operatorStack.peek(), token)) {
                    result.add(operatorStack.pop());
                }
                operatorStack.push(token);
            } else if (token instanceof LeftParenthesis) {
                operatorStack.push(token);
            } else if (token instanceof RightParenthesis) {
                unstackUntilLeftParenthesis(operatorStack, result);
                operatorStack.pop();
                if (operatorStack.peek() instanceof Function) {
                    result.add(operatorStack.pop());
                }
            } else if (token instanceof ArgSeparator) {
                unstackUntilLeftParenthesis(operatorStack, result);
            }
        }
        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek() instanceof LeftParenthesis) {
                throw new InvalidExpressionSyntaxException("Missing left parenthesis");
            }
            result.add(operatorStack.pop());
        }
        return result;
    }

    private void unstackUntilLeftParenthesis(Deque<Token> operatorStack, List<Token> result) {
        while (operatorStack.peek() != LeftParenthesis.getInstance()) {
            if (operatorStack.isEmpty()) {
                throw new InvalidExpressionSyntaxException("Missing left parenthesis");
            }
            result.add(operatorStack.pop());
        }
    }

    private boolean comparePrecedence(Token token1, Token token2) {
        if (token1 instanceof LeftParenthesis) {
            return false;
        } else if (token1 instanceof BinaryOperator) {
            BinaryOperator op1 = (BinaryOperator) token1;
            if (token2 instanceof BinaryOperator) {
                BinaryOperator op2 = (BinaryOperator) token2;
                return op1.precedence() > op2.precedence()
                        || (op2.isLeftAssociative() && op1.precedence() == op2.precedence());
            } else {
                return false;
            }
        } else if (token1 instanceof UnaryOperator) {
            return !(token2 instanceof UnaryOperator);
        } else {
            throw new IllegalArgumentException();
        }

    }
}
