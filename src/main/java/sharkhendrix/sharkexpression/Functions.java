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

import sharkhendrix.sharkexpression.token.Function;

import java.util.HashMap;
import java.util.Map;

public class Functions {

    private final Map<String, Function> functions = new HashMap<>();

    public Functions add(String name, Function function) {
        if (functions.get(name) != null) {
            throw new IllegalArgumentException("The function " + name + " already exists.");
        }
        functions.put(name, function);
        return this;
    }

    public Functions add(String name, Function.NoArgs function) {
        return add(name, (Function) function);
    }

    public Functions add(String name, Function.OneArg function) {
        return add(name, (Function) function);
    }

    public Functions add(String name, Function.TwoArgs function) {
        return add(name, (Function) function);
    }

    public Functions add(String name, Function.ThreeArgs function) {
        return add(name, (Function) function);
    }

    public Functions add(String name, Function.FourArgs function) {
        return add(name, (Function) function);
    }


    public Functions remove(String name) {
        functions.remove(name);
        return this;
    }

    public Function get(String name) {
        return functions.get(name);
    }
}
