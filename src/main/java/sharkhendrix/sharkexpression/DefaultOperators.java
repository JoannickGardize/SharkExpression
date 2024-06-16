package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.BinaryOperator;
import sharkhendrix.sharkexpression.token.TernaryOperator;

public class DefaultOperators {

    public static void apply(Operators operators) {

        // Mathematical operators

        operators.add("-", f -> -f);

        operators.add("+", new BinaryOperator() {
            @Override
            public float compute(float leftSide, float rightSide) {
                return leftSide + rightSide;
            }

            @Override
            public int precedence() {
                return 0;
            }
        });

        operators.add("-", new BinaryOperator() {
            @Override
            public float compute(float leftSide, float rightSide) {
                return leftSide - rightSide;
            }

            @Override
            public int precedence() {
                return 0;
            }
        });

        operators.add("*", new BinaryOperator() {
            @Override
            public float compute(float leftSide, float rightSide) {
                return leftSide * rightSide;
            }

            @Override
            public int precedence() {
                return 10;
            }
        });

        operators.add("/", new BinaryOperator() {
            @Override
            public float compute(float leftSide, float rightSide) {
                return leftSide / rightSide;
            }

            @Override
            public int precedence() {
                return 10;
            }
        });

        operators.add("%", new BinaryOperator() {
            @Override
            public float compute(float leftSide, float rightSide) {
                return leftSide % rightSide;
            }

            @Override
            public int precedence() {
                return 10;
            }
        });

        operators.add("^", new BinaryOperator() {
            @Override
            public float compute(float leftSide, float rightSide) {
                return (float) Math.pow(leftSide, rightSide);
            }

            @Override
            public int precedence() {
                return 20;
            }

            @Override
            public boolean isLeftAssociative() {
                return false;
            }
        });

        // Comparison operators

        operators.add("<", new BinaryOperator() {
            @Override
            public float compute(float leftSide, float rightSide) {
                return leftSide < rightSide ? 1 : 0;
            }

            @Override
            public int precedence() {
                return -10;
            }
        });

        operators.add("<=", new BinaryOperator() {
            @Override
            public float compute(float leftSide, float rightSide) {
                return leftSide <= rightSide ? 1 : 0;
            }

            @Override
            public int precedence() {
                return -10;
            }
        });

        operators.add(">", new BinaryOperator() {
            @Override
            public float compute(float leftSide, float rightSide) {
                return leftSide > rightSide ? 1 : 0;
            }

            @Override
            public int precedence() {
                return -10;
            }
        });

        operators.add(">=", new BinaryOperator() {
            @Override
            public float compute(float leftSide, float rightSide) {
                return leftSide >= rightSide ? 1 : 0;
            }

            @Override
            public int precedence() {
                return -10;
            }
        });

        addSynonyms(operators, new BinaryOperator() {
            @Override
            public float compute(float leftSide, float rightSide) {
                return leftSide == rightSide ? 1 : 0;
            }

            @Override
            public int precedence() {
                return -10;
            }
        }, "=", "==");

        addSynonyms(operators, new BinaryOperator() {
            @Override
            public float compute(float leftSide, float rightSide) {
                return leftSide != rightSide ? 1 : 0;
            }

            @Override
            public int precedence() {
                return -10;
            }
        }, "<>", "!=");

        // Logical operators

        TernaryOperator ternaryOperator = (l, m, r) -> l != 0 ? m : r;
        operators.add("?", ":", ternaryOperator);
        operators.add("then", "else", ternaryOperator);

        addSynonyms(operators, new BinaryOperator() {
            @Override
            public float compute(float leftSide, float rightSide) {
                return leftSide != 0 && rightSide != 0 ? 1 : 0;
            }

            @Override
            public int precedence() {
                return -20;
            }
        }, "&", "&&", "and");

        addSynonyms(operators, new BinaryOperator() {
            @Override
            public float compute(float leftSide, float rightSide) {
                return leftSide != 0 || rightSide != 0 ? 1 : 0;
            }

            @Override
            public int precedence() {
                return -20;
            }
        }, "|", "||", "or");
    }

    private static void addSynonyms(Operators operators, BinaryOperator operator, String... synonyms) {
        for (String synonym : synonyms) {
            operators.add(synonym, operator);
        }
    }
}
