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

/**
 * A Token is a piece of an Expression.
 */
public interface Token {

    /**
     * The number of argument (or operand) this token requires.
     * May be zero for terminal tokens (i.e. numbers). Default return value is zero.
     *
     * @return the number of argument (or operand) this token needs
     */
    default int numArgs() {
        return 0;
    }

    /**
     * Execute this token, it must pop() as many times as the return value of numArgs().
     *
     * @param output the output stack of the expression execution,
     *               it is expected to pop() numArgs() times
     * @return the resulting value of this token
     */
    default float execute(FloatStack output) {
        throw new UnsupportedOperationException();
    }
}
