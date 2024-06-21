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

import sharkhendrix.sharkexpression.token.ConstantNumber;
import sharkhendrix.sharkexpression.token.Number;
import sharkhendrix.sharkexpression.token.VariableNumber;

import java.util.HashMap;
import java.util.Map;

/**
 * Container of variables. Supports varying values and constant values.
 */
public class Variables {

    private final Map<String, Number> variables = new HashMap<>();

    /**
     * Add a variable with the given value supplier.
     *
     * @param name          the name of the variable
     * @param valueSupplier the value supplier of the variable
     * @return this for method chaining style
     * @throws IllegalArgumentException if a variable already exists with the given name
     */
    public Variables add(String name, VariableNumber valueSupplier) {
        checkExists(name);
        variables.put(name, valueSupplier);
        return this;
    }

    /**
     * Add a variable with a constant value, allowing expression simplification.
     *
     * @param name          the name of the variable
     * @param constantValue the constant value of the variable
     * @return this for method chaining style
     * @throws IllegalArgumentException if a variable already exists with the given name
     */
    public Variables add(String name, float constantValue) {
        checkExists(name);
        variables.put(name, new ConstantNumber(constantValue));
        return this;
    }

    /**
     * Remove the given variable or constant from this Variables if exists.
     *
     * @param name the name the variable to remove
     * @return this for method chaining style
     */
    public Variables remove(String name) {
        variables.remove(name);
        return this;
    }

    /**
     * Get the ready-to-use token corresponding to the variable with the given name.
     *
     * @param name the name of the variable
     * @return the ready-to-use token of the variable
     */
    public Number get(String name) {
        return variables.get(name);
    }

    private void checkExists(String name) {
        if (variables.get(name) != null) {
            throw new IllegalArgumentException("The variable " + name + " already exists.");
        }
    }
}
