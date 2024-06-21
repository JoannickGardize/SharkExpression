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
 * A parsed expression. this implement is not thread-safe.
 */
public class Expression {

    private final Token[] tokens;

    private final FloatStack outputStack;

    public Expression(Token[] tokens) {
        outputStack = new FloatStack(Math.min(tokens.length / 2 + 1, 10));
        this.tokens = tokens;
    }

    public float evaluate() {
        for (Token token : tokens) {
            outputStack.push(token.execute(outputStack));
        }
        return outputStack.pop();
    }
}
