package sharkhendrix.sharkexpression;

import java.util.function.BiConsumer;

public interface VariablePool {

    void forEach(BiConsumer<String, Integer> variableConsumer);

    float getValue(int variableIndex);
}
