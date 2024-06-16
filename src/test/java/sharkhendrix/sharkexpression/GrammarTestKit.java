package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.BinaryOperator;
import sharkhendrix.sharkexpression.token.TernaryOperator;
import sharkhendrix.sharkexpression.token.UnaryOperator;
import sharkhendrix.sharkexpression.token.VariableNumber;

public class GrammarTestKit {

    public static final BinaryOperator gte = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide >= rightSide ? 1 : 0;
        }

        @Override
        public int precedence() {
            return -10;
        }
    };
    public static final BinaryOperator plus = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide + rightSide;
        }

        @Override
        public int precedence() {
            return 0;
        }
    };
    public static final BinaryOperator multiply = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide * rightSide;
        }

        @Override
        public int precedence() {
            return 10;
        }
    };
    public static final UnaryOperator negative = f -> -f;
    public static final VariableNumber abc = new VariableNumber(null, 0) {
        @Override
        public float getValue() {
            return 6;
        }
    };
    public static final VariableNumber vaar = new VariableNumber(null, 0) {
        @Override
        public float getValue() {
            return 8;
        }
    };
    public static final TernaryOperator ternary = (v1, v2, v3) -> v1 != 0 ? v2 : v3;
    public static final Operators.TemporaryTernaryRightPart temporaryTernaryRightPart = new Operators.TemporaryTernaryRightPart(ternary);
    public static final Operators.TemporaryTernaryLeftPart temporaryTernaryLeftPart = new Operators.TemporaryTernaryLeftPart(ternary);

    public static final ExpressionGrammar grammar = new ExpressionGrammar() {
        @Override
        public UnaryOperator getUnaryOperator(String operator) {
            return operator.equals("-") ? negative : null;
        }

        @Override
        public BinaryOperator getBinaryOperator(String operator) {
            switch (operator) {
                case ">=":
                    return gte;
                case "+":
                    return plus;
                case "*":
                    return multiply;
                case "?":
                    return temporaryTernaryLeftPart;
                case ":":
                    return temporaryTernaryRightPart;
                default:
                    return null;
            }
        }

        @Override
        public VariableNumber getVariable(String name) {
            if (name.equals("_abc34")) {
                return abc;
            } else if (name.equals("vâr")) {
                return vaar;
            } else {
                return null;
            }
        }
    };
}