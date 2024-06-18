package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.VariableNumber;

import java.util.HashMap;
import java.util.Map;

public class Variables {

    private final Map<String, VariableNumber> variables = new HashMap<>();

    public void add(String name, VariableNumber valueSupplier) {
        if (variables.get(name) != null) {
            throw new IllegalArgumentException("The variable " + name + " already exists.");
        }
        variables.put(name, valueSupplier);
    }

    public VariableNumber get(String name) {
        return variables.get(name);
    }
}
