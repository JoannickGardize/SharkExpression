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

package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.*;

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

        @Override
        public String toString() {
            return ">=";
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

        @Override
        public String toString() {
            return "+";
        }
    };
    public static final BinaryOperator minus = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide - rightSide;
        }

        @Override
        public int precedence() {
            return 0;
        }

        @Override
        public String toString() {
            return "-";
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

        @Override
        public String toString() {
            return "*";
        }
    };
    public static final BinaryOperator divide = new BinaryOperator() {
        @Override
        public float compute(float leftSide, float rightSide) {
            return leftSide / rightSide;
        }

        @Override
        public int precedence() {
            return 10;
        }

        @Override
        public String toString() {
            return "/";
        }
    };
    public static final BinaryOperator power = new BinaryOperator() {
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

        @Override
        public String toString() {
            return "^";
        }
    };
    public static final UnaryOperator negative = new UnaryOperator() {
        @Override
        public float compute(float rightSide) {
            return -rightSide;
        }

        @Override
        public String toString() {
            return "(-)";
        }
    };
    public static final VariableNumber abc = new VariableNumber() {
        @Override
        public float getValue() {
            return 6;
        }

        @Override
        public String toString() {
            return "_abc34";
        }
    };
    public static final VariableNumber vaar = new VariableNumber() {
        @Override
        public float getValue() {
            return 8;
        }

        @Override
        public String toString() {
            return "vâr";
        }
    };
    public static final TernaryOperator ternary = new TernaryOperator() {
        @Override
        public float compute(float v1, float v2, float v3) {
            return v1 != 0 ? v2 : v3;
        }

        @Override
        public String toString() {
            return "?:";
        }
    };
    public static final Operators.TemporaryTernaryRightPart temporaryTernaryRightPart = new Operators.TemporaryTernaryRightPart(ternary) {
        @Override
        public String toString() {
            return "(temporary):";
        }
    };
    public static final Operators.TemporaryTernaryLeftPart temporaryTernaryLeftPart = new Operators.TemporaryTernaryLeftPart(ternary) {
        @Override
        public String toString() {
            return "(temporary)?";
        }
    };
    public static final Function.TwoArgs max = new Function.TwoArgs() {
        @Override
        public float compute(float arg1, float arg2) {
            return Math.max(arg1, arg2);
        }

        @Override
        public String toString() {
            return "max";
        }
    };

    public static final Grammar grammar = new Grammar(null) {

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
                case "plus":
                    return plus;
                case "-":
                    return minus;
                case "*":
                    return multiply;
                case "/":
                    return divide;
                case "?":
                    return temporaryTernaryLeftPart;
                case ":":
                    return temporaryTernaryRightPart;
                case "^":
                    return power;
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

        @Override
        public Function getFunction(String name) {
            return name.equals("max") ? max : null;
        }
    };

    static {
        grammar.setParenthesisPairs('(', ')', '[', ']');
    }
}
