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

import sharkhendrix.sharkexpression.token.Token;
import sharkhendrix.sharkexpression.util.FloatStack;

/**
 * A parsed expression.
 * This implementation does not hold a working FloatStack,
 * so the method evaluate() is not supported,
 * call evaluate(FloatStack) instead.
 */
public class Expression {

    private final Token[] tokens;

    public Expression(Token[] tokens) {
        this.tokens = tokens;
    }

    /**
     * Evaluate this expression.
     *
     * @return the result of the expression
     */
    public float evaluate() {
        throw new UnsupportedOperationException("This is not a standalone expression," +
                "call instead evaluate(FloatStack) or setup the ExpressionFactory to build StandAloneExpression.");
    }

    /**
     * Evaluate this expression.
     *
     * @param outputStack the working FloatStack used to evaluate the expression,
     *                    it is intended to be empty,
     *                    but it will work with a non-empty outputStack.
     *                    After this method, the outputStack remains unchanged.
     * @return the result of the expression
     */
    public float evaluate(FloatStack outputStack) {
        for (Token token : tokens) {
            outputStack.push(token.execute(outputStack));
        }
        return outputStack.pop();
    }

    /**
     * @return the number of token this expression is made of
     */
    public int tokenLength() {
        return tokens.length;
    }
}
