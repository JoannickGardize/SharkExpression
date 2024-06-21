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

import sharkhendrix.sharkexpression.token.Number;
import sharkhendrix.sharkexpression.token.*;
import sharkhendrix.sharkexpression.util.FloatStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplify an expression, by merging constant values with the same precedence.
 * The input tokens should be in postfix notation.
 * <p>In the worst but unusual case, this algorithm has a O(n²) complexity
 */
public class ExpressionSimplifier implements TokenSequenceFunction {

    @Override
    public List<Token> apply(List<Token> tokens) {
        List<Token> result = new ArrayList<>(tokens.size());
        for (Token token : tokens) {
            if (token instanceof Number) {
                result.add(token);
            } else if (token instanceof TernaryOperator) {
                if (allArgsAreConstant(3, result)) {
                    Token right = result.remove(result.size() - 1);
                    Token middle = result.remove(result.size() - 1);
                    Token left = result.remove(result.size() - 1);
                    result.add(new ConstantNumber(((TernaryOperator) token)
                            .compute(((ConstantNumber) left).getValue(), ((ConstantNumber) middle).getValue(),
                                    ((ConstantNumber) right).getValue())));
                } else {
                    result.add(token);
                }
            } else if (token instanceof UnaryOperator) {
                if (result.get(result.size() - 1) instanceof ConstantNumber) {
                    result.add(new ConstantNumber(((UnaryOperator) token)
                            .compute(((ConstantNumber) result.remove(result.size() - 1)).getValue())));
                } else {
                    result.add(token);
                }
            } else if (token instanceof BinaryOperator) {
                if (result.get(result.size() - 1) instanceof ConstantNumber) {
                    BinaryOperator currentOperator = (BinaryOperator) token;
                    int leftConstantIndex = findLeftCompatibleConstant(result.size() - 2, currentOperator, result);
                    if (leftConstantIndex != -1) {
                        Token right = result.remove(result.size() - 1);
                        result.set(leftConstantIndex, new ConstantNumber(
                                currentOperator.compute(
                                        ((ConstantNumber) result.get(leftConstantIndex)).getValue(),
                                        ((ConstantNumber) right).getValue())));
                    } else {
                        result.add(token);
                    }
                } else {
                    result.add(token);
                }
            } else if (token instanceof Function) {
                Function function = (Function) token;
                if (function.allowsSimplification() && allArgsAreConstant(function.numArgs(), result)) {
                    FloatStack stack = new FloatStack();
                    for (int i = 0; i < function.numArgs(); i++) {
                        stack.push(((ConstantNumber) result.remove(result.size() - 1)).getValue());
                    }
                    result.add(new ConstantNumber(function.execute(stack)));
                } else {
                    result.add(function);
                }
            }
        }
        return result;
    }

    private int findLeftCompatibleConstant(int startIndex, BinaryOperator operator, List<Token> result) {
        int numberCountdown = 1;
        int currentPrecedence = operator.precedence();
        for (int i = startIndex; i >= 0; i--) {
            Token token = result.get(i);
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

    private boolean allArgsAreConstant(int argsCount, List<Token> result) {
        for (int i = 0; i < argsCount; i++) {
            if (!(result.get(result.size() - 1 - i) instanceof ConstantNumber)) {
                return false;
            }
        }
        return true;
    }
}
