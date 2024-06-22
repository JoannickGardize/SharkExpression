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

import sharkhendrix.sharkexpression.token.BinaryOperator;
import sharkhendrix.sharkexpression.token.ConstantNumber;
import sharkhendrix.sharkexpression.token.Number;
import sharkhendrix.sharkexpression.token.Token;
import sharkhendrix.sharkexpression.util.FloatStack;

import java.util.List;

/**
 * Simplify an expression to its maximum,
 * by merging binary operation's constant values with the same precedence,
 * or by merging any other type of operator and functions when all arguments are constants.
 * <p>The input tokens should be in postfix notation.
 * <p>In the worst-case unusual scenario, this algorithm has a O(n²) complexity
 */
public class ExpressionSimplifier implements TokenPipeline {

    private static class TokenListFloatStackProxy extends FloatStack {

        private final List<Token> tokens;

        public TokenListFloatStackProxy(List<Token> tokens) {
            super(0);
            this.tokens = tokens;
        }

        @Override
        public void push(float e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public float pop() {
            return ((ConstantNumber) tokens.remove(tokens.size() - 1)).getValue();
        }
    }

    @Override
    public void apply(List<Token> input, List<Token> output) {
        FloatStack stack = new TokenListFloatStackProxy(output);
        for (Token token : input) {
            if (token instanceof Number) {
                output.add(token);
            } else if (token instanceof BinaryOperator) {
                if (output.get(output.size() - 1) instanceof ConstantNumber
                        && token.allowsSimplification()) {
                    BinaryOperator currentOperator = (BinaryOperator) token;
                    int leftConstantIndex = findLeftCompatibleConstant(output.size() - 2, currentOperator, output);
                    if (leftConstantIndex != -1) {
                        Token right = output.remove(output.size() - 1);
                        output.set(leftConstantIndex, new ConstantNumber(
                                currentOperator.compute(
                                        ((ConstantNumber) output.get(leftConstantIndex)).getValue(),
                                        ((ConstantNumber) right).getValue())));
                    } else {
                        output.add(token);
                    }
                } else {
                    output.add(token);
                }
            } else {
                if (token.allowsSimplification() && allArgsAreConstant(token.numArgs(), output)) {
                    output.add(new ConstantNumber(token.execute(stack)));
                } else {
                    output.add(token);
                }
            }
        }
    }

    private int findLeftCompatibleConstant(int startIndex, BinaryOperator operator, List<Token> output) {
        int numberCountdown = 1;
        int currentPrecedence = operator.precedence();
        for (int i = startIndex; i >= 0; i--) {
            Token token = output.get(i);
            numberCountdown += token.numArgs() - 1;
            if (token instanceof Number) {
                if (numberCountdown == 0
                        || currentPrecedence == operator.precedence()
                        && numberCountdown == 1) {
                    if (token instanceof ConstantNumber) {
                        return i;
                    }
                } else if (numberCountdown < 0) {
                    return -1;
                }
            } else if (token instanceof BinaryOperator) {
                BinaryOperator leftOp = (BinaryOperator) token;
                currentPrecedence = leftOp.precedence();
                if (currentPrecedence < operator.precedence()) {
                    return -1;
                }
            }
        }
        return -1;
    }

    private boolean allArgsAreConstant(int argsCount, List<Token> output) {
        for (int i = 0; i < argsCount; i++) {
            if (!(output.get(output.size() - 1 - i) instanceof ConstantNumber)) {
                return false;
            }
        }
        return true;
    }
}
