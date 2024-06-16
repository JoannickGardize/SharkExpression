package sharkhendrix.sharkexpression.token;

import sharkhendrix.sharkexpression.VariablePool;

public class VariableNumber implements Number {

    private final VariablePool variablePool;
    private final int index;

    public VariableNumber(VariablePool variablePool, int index) {
        this.variablePool = variablePool;
        this.index = index;
    }


    @Override
    public float getValue() {
        return variablePool.getValue(index);
    }
}
