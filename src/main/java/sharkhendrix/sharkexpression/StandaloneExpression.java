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
 * A parsed Expression.
 * This implementation holds a working FloatStack and is not thread-safe.
 */
public class StandaloneExpression extends Expression {

    private final FloatStack outputStack;

    public StandaloneExpression(Token[] tokens) {
        super(tokens);
        outputStack = new FloatStack(Math.min(tokens.length / 2 + 1, 10));
    }

    public float evaluate() {
        return evaluate(outputStack);
    }
}
