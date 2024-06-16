package sharkhendrix.sharkexpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class MapVariablePool implements VariablePool {

    private final Map<String, Integer> variableIndexes = new HashMap<>();
    List<Float> values = new ArrayList<>();

    public void set(String name, float value) {
        int index = variableIndexes.computeIfAbsent(name, n -> {
            int newIndex = values.size();
            values.add(value);
            return newIndex;
        });
        values.set(index, value);
    }

    @Override
    public void forEach(BiConsumer<String, Integer> variableConsumer) {
        variableIndexes.forEach(variableConsumer);
    }

    @Override
    public float getValue(int variableIndex) {
        return values.get(variableIndex);
    }
}
