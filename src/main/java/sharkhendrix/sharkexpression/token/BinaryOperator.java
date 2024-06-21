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

package sharkhendrix.sharkexpression.token;

import sharkhendrix.sharkexpression.util.FloatStack;

public interface BinaryOperator extends Token {

    float compute(float leftSide, float rightSide);

    /**
     * The priority order of this operator regarding other operators at  the same expression depth.
     * Higher values have the priority to smaller values.
     *
     * @return the precedence of this operator
     */
    int precedence();

    default boolean isLeftAssociative() {
        return true;
    }

    @Override
    default int numArgs() {
        return 2;
    }

    @Override
    default float execute(FloatStack output) {
        float right = output.pop();
        float left = output.pop();
        return compute(left, right);
    }
}
