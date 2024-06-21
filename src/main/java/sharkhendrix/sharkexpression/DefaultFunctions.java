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

public class DefaultFunctions {

    private DefaultFunctions() {
    }

    public static void apply(Functions functions) {
        functions.add("abs", Math::abs);
        functions.add("acos", f -> (float) Math.acos(f));
        functions.add("asin", f -> (float) Math.asin(f));
        functions.add("atan", f -> (float) Math.atan(f));
        functions.add("atan2", (y, x) -> (float) Math.atan2(y, x));
        functions.add("cbrt", f -> (float) Math.cbrt(f));
        functions.add("ceil", f -> (float) Math.ceil(f));
        functions.add("cos", f -> (float) Math.cos(f));
        functions.add("cosh", f -> (float) Math.cosh(f));
        functions.add("exp", f -> (float) Math.exp(f));
        functions.add("floor", f -> (float) Math.floor(f));
        functions.add("log", f -> (float) Math.log(f));
        functions.add("log10", f -> (float) Math.log10(f));
        functions.add("max", Math::max);
        functions.add("min", Math::min);
        functions.add("pow", (f, e) -> (float) Math.pow(f, e));
        functions.add("round", f -> (float) Math.round(f));
        functions.add("signum", Math::signum);
        functions.add("sin", f -> (float) Math.sin(f));
        functions.add("sinh", f -> (float) Math.sinh(f));
        functions.add("sqrt", f -> (float) Math.sqrt(f));
        functions.add("tan", f -> (float) Math.tan(f));
        functions.add("tanh", f -> (float) Math.tanh(f));
    }
}
