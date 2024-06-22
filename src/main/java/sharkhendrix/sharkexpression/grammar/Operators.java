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

package sharkhendrix.sharkexpression.grammar;

import sharkhendrix.sharkexpression.token.BinaryOperator;
import sharkhendrix.sharkexpression.token.TernaryOperator;
import sharkhendrix.sharkexpression.token.UnaryOperator;

import java.util.HashMap;
import java.util.Map;

/**
 * Container of operators.
 */
public class Operators {

    public static class TemporaryTernaryLeftPart implements BinaryOperator {
        private final TernaryOperator operator;

        public TemporaryTernaryLeftPart(TernaryOperator operator) {
            this.operator = operator;
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

    public static class TemporaryTernaryRightPart implements BinaryOperator {
        private final TernaryOperator operator;

        public TemporaryTernaryRightPart(TernaryOperator operator) {
            this.operator = operator;
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

    /**
     * Add the given unary operator.
     *
     * @param symbol        the symbol of the unary operator
     * @param unaryOperator the unary operator to add
     * @return this for method chaining
     * @throws IllegalArgumentException if a unary operator already exists with the given name
     */
    public Operators add(String symbol, UnaryOperator unaryOperator) {
        if (unaryOperators.get(symbol) != null) {
            throw new IllegalArgumentException("The unary symbol " + symbol + " already exists.");
        }
        unaryOperators.put(symbol, unaryOperator);
        return this;
    }

    /**
     * Add the given binary operator.
     *
     * @param symbol   the symbol of the binary operator
     * @param operator the binary operator to add
     * @return this for method chaining
     * @throws IllegalArgumentException if a binary operator already exists with the given name
     */
    public Operators add(String symbol, BinaryOperator operator) {
        if (binaryOperators.get(symbol) != null) {
            throw new IllegalArgumentException("The binary or ternary symbol " + symbol + " already exists.");
        }
        binaryOperators.put(symbol, operator);
        return this;
    }

    /**
     * Add the given ternary operator.
     *
     * @param firstSymbol  the first symbol of the ternary operator
     * @param secondSymbol the second symbol of the ternary operator
     * @param operator     the ternary operator
     * @return this for method chaining
     * @throws IllegalArgumentException if a binary operator or a ternary operator already exists with
     *                                  the first or the second symbol
     */
    public Operators add(String firstSymbol, String secondSymbol, TernaryOperator operator) {
        add(firstSymbol, new TemporaryTernaryLeftPart(operator));
        add(secondSymbol, new TemporaryTernaryRightPart(operator));
        return this;
    }

    /**
     * Remove the unary operator with the given symbol from this Operators, if exists.
     *
     * @param symbol the symbol of the unary operator to remove
     * @return this for method chaining
     */
    public Operators removeUnary(String symbol) {
        unaryOperators.remove(symbol);
        return this;
    }

    /**
     * Remove the binary operator with the given symbol from this Operators, if exists.
     *
     * @param symbol the symbol of the binary operator to remove
     * @return this for method chaining
     */
    public Operators removeBinary(String symbol) {
        binaryOperators.remove(symbol);
        return this;
    }

    /**
     * Remove the ternary operator with the given symbol pair from this Operators, if exists.
     *
     * @param firstSymbol  the first symbol of the ternary operator to remove
     * @param secondSymbol the second symbol of the ternary operator to remove
     * @return this for method chaining
     */
    public Operators removeTernary(String firstSymbol, String secondSymbol) {
        BinaryOperator leftPart = binaryOperators.get(firstSymbol);
        BinaryOperator rightPart = binaryOperators.get(secondSymbol);
        if (leftPart instanceof TemporaryTernaryLeftPart && rightPart instanceof TemporaryTernaryRightPart
                && ((TemporaryTernaryLeftPart) leftPart).getOperator() == ((TemporaryTernaryRightPart) rightPart).getOperator()) {
            binaryOperators.remove(firstSymbol);
            binaryOperators.remove(secondSymbol);
        }
        return this;
    }


    public UnaryOperator getUnary(String symbol) {
        return unaryOperators.get(symbol);
    }

    public BinaryOperator getBinary(String symbol) {
        return binaryOperators.get(symbol);
    }
}
