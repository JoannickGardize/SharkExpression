package sharkhendrix.sharkexpression.token;

import sharkhendrix.sharkexpression.util.FloatStack;

public interface UnaryOperator extends Operator {

    float compute(float rightSide);

    @Override
    default float execute(FloatStack output) {
        float value = output.pop();
        return compute(value);
    }
}
