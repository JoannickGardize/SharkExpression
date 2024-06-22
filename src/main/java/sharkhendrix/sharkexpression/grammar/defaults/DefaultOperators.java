/*
 * Copyright 2024 Joannick Gardize
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package sharkhendrix.sharkexpression.grammar.defaults;

import sharkhendrix.sharkexpression.grammar.Operators;
import sharkhendrix.sharkexpression.token.BinaryOperator;
import sharkhendrix.sharkexpression.token.TernaryOperator;
import sharkhendrix.sharkexpression.token.UnaryOperator;

public class DefaultOperators {

    public static final int ADDITION_PRECEDENCE = 0;
    public static final int MULTIPLICATION_PRECEDENCE = 10;
    public static final int POWER_PRECEDENCE = 20;
    public static final int COMPARISON_PRECEDENCE = -10;
    public static final int AND_PRECEDENCE = -20;
    public static final int OR_PRECEDENCE = -30;

    public static final UnaryOperator NEGATE = f -> -f;

    public static final BinaryOperator PLUS = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide + rightSide;
        }

        @Override
        public int precedence() {
            return ADDITION_PRECEDENCE;
        }
    };

    public static final BinaryOperator MINUS = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide - rightSide;
        }

        @Override
        public int precedence() {
            return ADDITION_PRECEDENCE;
        }
    };

    public static final BinaryOperator MULTIPLY = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide * rightSide;
        }

        @Override
        public int precedence() {
            return MULTIPLICATION_PRECEDENCE;
        }
    };

    public static final BinaryOperator DIVIDE = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide / rightSide;
        }

        @Override
        public int precedence() {
            return MULTIPLICATION_PRECEDENCE;
        }
    };

    public static final BinaryOperator MODULO = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide % rightSide;
        }

        @Override
        public int precedence() {
            return MULTIPLICATION_PRECEDENCE;
        }
    };

    public static final BinaryOperator POW = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return (float) Math.pow(leftSide, rightSide);
        }

        @Override
        public int precedence() {
            return POWER_PRECEDENCE;
        }

        @Override
        public boolean isLeftAssociative() {
            return false;
        }
    };

    public static final BinaryOperator LT = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide < rightSide ? 1 : 0;
        }

        @Override
        public int precedence() {
            return COMPARISON_PRECEDENCE;
        }
    };

    public static final BinaryOperator LTE = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide <= rightSide ? 1 : 0;
        }

        @Override
        public int precedence() {
            return COMPARISON_PRECEDENCE;
        }
    };

    public static final BinaryOperator GT = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide > rightSide ? 1 : 0;
        }

        @Override
        public int precedence() {
            return COMPARISON_PRECEDENCE;
        }
    };

    public static final BinaryOperator GTE = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide >= rightSide ? 1 : 0;
        }

        @Override
        public int precedence() {
            return COMPARISON_PRECEDENCE;
        }
    };

    public static final BinaryOperator EQUAL_TO = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide == rightSide ? 1 : 0;
        }

        @Override
        public int precedence() {
            return COMPARISON_PRECEDENCE;
        }
    };

    public static final BinaryOperator NOT_EQUAL_TO = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide != rightSide ? 1 : 0;
        }

        @Override
        public int precedence() {
            return COMPARISON_PRECEDENCE;
        }
    };

    public static final UnaryOperator NOT = f -> f == 0 ? 1 : 0;

    public static final TernaryOperator TERNARY_CONDITION = (l, m, r) -> l != 0 ? m : r;

    public static final BinaryOperator AND = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide != 0 && rightSide != 0 ? 1 : 0;
        }

        @Override
        public int precedence() {
            return AND_PRECEDENCE;
        }
    };

    public static final BinaryOperator OR = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide != 0 || rightSide != 0 ? 1 : 0;
        }

        @Override
        public int precedence() {
            return OR_PRECEDENCE;
        }
    };

    private DefaultOperators() {
    }

    public static void apply(Operators operators) {
        apply(operators, true);
    }

    public static void apply(Operators operators, boolean extendedSynonyms) {

        // Mathematical operators

        operators.add("-", NEGATE);
        operators.add("+", PLUS);
        operators.add("-", MINUS);
        operators.add("*", MULTIPLY);
        operators.add("/", DIVIDE);
        operators.add("%", MODULO);
        operators.add("^", POW);

        // Comparison operators

        operators.add("<", LT);
        operators.add("<=", LTE);
        operators.add(">", GT);
        operators.add(">=", GTE);
        operators.add("==", EQUAL_TO);
        operators.add("!=", NOT_EQUAL_TO);

        // Logical operators

        operators.add("!", NOT);
        operators.add("?", ":", TERNARY_CONDITION);
        operators.add("&&", AND);
        operators.add("||", OR);

        if (extendedSynonyms) {
            operators.add("then", "else", TERNARY_CONDITION);
            operators.add("=", EQUAL_TO);
            operators.add("<>", NOT_EQUAL_TO);
            operators.add("&", AND);
            operators.add("and", AND);
            operators.add("|", OR);
            operators.add("or", OR);
            operators.add("not", NOT);
        }
    }
}
