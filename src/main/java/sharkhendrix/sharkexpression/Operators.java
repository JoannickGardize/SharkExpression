package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.BinaryOperator;
import sharkhendrix.sharkexpression.token.TernaryOperator;
import sharkhendrix.sharkexpression.token.UnaryOperator;

import java.util.HashMap;
import java.util.Map;

public class Operators {

    public static final class TemporaryTernaryLeftPart implements BinaryOperator {
        private final TernaryOperator operator;

        public TemporaryTernaryLeftPart(TernaryOperator operator) {
            this.operator = operator;
        }

        public TernaryOperator operator() {
            return operator;
        }

        public TernaryOperator getOperator() {
            return operator;
        }

        @Override
        public float compute(float leftSide, float rightSide) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int precedence() {
            return Integer.MIN_VALUE;
        }
    }

    public static final class TemporaryTernaryRightPart implements BinaryOperator {
        private final TernaryOperator operator;

        public TemporaryTernaryRightPart(TernaryOperator operator) {
            this.operator = operator;
        }

        public TernaryOperator operator() {
            return operator;
        }

        public TernaryOperator getOperator() {
            return operator;
        }

        @Override
        public float compute(float leftSide, float rightSide) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int precedence() {
            return Integer.MIN_VALUE + 1;
        }
    }

    private final Map<String, BinaryOperator> binaryOperators = new HashMap<>();
    private final Map<String, UnaryOperator> unaryOperators = new HashMap<>();

    public void add(String symbol, UnaryOperator unaryOperator) {
        if (unaryOperators.get(symbol) != null) {
            throw new IllegalArgumentException("The unary symbol " + symbol + " already exists.");
        }
        unaryOperators.put(symbol, unaryOperator);
    }

    public void add(String symbol, BinaryOperator operator) {
        if (binaryOperators.get(symbol) != null) {
            throw new IllegalArgumentException("The binary or ternary symbol " + symbol + " already exists.");
        }
        binaryOperators.put(symbol, operator);
    }

    public void add(String firstSymbol, String secondSymbol, TernaryOperator operator) {
        add(firstSymbol, new TemporaryTernaryLeftPart(operator));
        add(secondSymbol, new TemporaryTernaryRightPart(operator));
    }

    public UnaryOperator getUnary(String symbol) {
        return unaryOperators.get(symbol);
    }

    public BinaryOperator getBinary(String symbol) {
        return binaryOperators.get(symbol);
    }
}
