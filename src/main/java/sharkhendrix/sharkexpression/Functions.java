package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.Function;

import java.util.HashMap;
import java.util.Map;

public class Functions {

    private final Map<String, Function> functions = new HashMap<>();

    public void add(String name, Function function) {
        if (functions.get(name) != null) {
            throw new IllegalArgumentException("The function " + name + " already exists.");
        }
        functions.put(name, function);
    }

    public Function get(String name) {
        return functions.get(name);
    }
}
