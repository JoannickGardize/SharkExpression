package sharkhendrix.sharkexpression.token;

import sharkhendrix.sharkexpression.util.FloatSupplier;

public interface VariableNumber extends Number, FloatSupplier {

    @Override
    default float getValue() {
        return getAsFloat();
    }
}
