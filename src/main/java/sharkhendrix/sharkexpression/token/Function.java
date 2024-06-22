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

public interface Function extends Token {

    interface NoArgs extends Function {

        /**
         * No-args functions probably does crazy things like using random values
         * and thus doesn't want to be simplified.
         *
         * @return false
         */
        @Override
        default boolean allowsSimplification() {
            return false;
        }

        float compute();

        @Override
        default float execute(FloatStack output) {
            return compute();
        }
    }

    interface OneArg extends Function {

        @Override
        default int numArgs() {
            return 1;
        }

        float compute(float arg);

        @Override
        default float execute(FloatStack output) {
            return compute(output.pop());
        }
    }

    interface TwoArgs extends Function {

        @Override
        default int numArgs() {
            return 2;
        }

        float compute(float arg1, float arg2);

        @Override
        default float execute(FloatStack output) {
            float arg2 = output.pop();
            float arg1 = output.pop();
            return compute(arg1, arg2);
        }
    }

    interface ThreeArgs extends Function {

        @Override
        default int numArgs() {
            return 3;
        }

        float compute(float arg1, float arg2, float arg3);

        @Override
        default float execute(FloatStack output) {
            float arg3 = output.pop();
            float arg2 = output.pop();
            float arg1 = output.pop();
            return compute(arg1, arg2, arg3);
        }
    }

    interface FourArgs extends Function {

        @Override
        default int numArgs() {
            return 4;
        }

        float compute(float arg1, float arg2, float arg3, float arg4);

        @Override
        default float execute(FloatStack output) {
            float arg4 = output.pop();
            float arg3 = output.pop();
            float arg2 = output.pop();
            float arg1 = output.pop();
            return compute(arg1, arg2, arg3, arg4);
        }
    }

    interface NArgs extends Function {

        float compute(float... args);

        @Override
        default float execute(FloatStack output) {
            int numArgs = numArgs();
            float[] args = new float[numArgs];
            for (int i = numArgs - 1; i >= 0; i--) {
                args[i] = output.pop();
            }
            return compute(args);
        }
    }
}
