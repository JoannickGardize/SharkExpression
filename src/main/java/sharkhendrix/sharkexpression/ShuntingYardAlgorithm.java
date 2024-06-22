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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class ShuntingYardAlgorithm implements TokenPipeline {


    @Override
    public void apply(List<Token> input, List<Token> output) {
        Deque<Token> operatorStack = new ArrayDeque<>();
        for (Token token : input) {
            if (token instanceof Number) {
                output.add(token);
            } else if (token instanceof Function) {
                operatorStack.push(token);
            } else if (token instanceof BinaryOperator || token instanceof UnaryOperator) {
                while (!operatorStack.isEmpty() && comparePrecedence(operatorStack.peek(), token)) {
                    output.add(operatorStack.pop());
                }
                operatorStack.push(token);
            } else if (token instanceof LeftParenthesis) {
                operatorStack.push(token);
            } else if (token instanceof RightParenthesis) {
                unstackUntilLeftParenthesis(operatorStack, output);
                operatorStack.pop();
                if (operatorStack.peek() instanceof Function) {
                    output.add(operatorStack.pop());
                }
            } else if (token instanceof ArgSeparator) {
                unstackUntilLeftParenthesis(operatorStack, output);
            }
        }
        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek() instanceof LeftParenthesis) {
                throw new InvalidExpressionSyntaxException("Missing left parenthesis");
            }
            output.add(operatorStack.pop());
        }
    }

    private void unstackUntilLeftParenthesis(Deque<Token> operatorStack, List<Token> output) {
        while (operatorStack.peek() != LeftParenthesis.getInstance()) {
            if (operatorStack.isEmpty()) {
                throw new InvalidExpressionSyntaxException("Missing left parenthesis");
            }
            output.add(operatorStack.pop());
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
