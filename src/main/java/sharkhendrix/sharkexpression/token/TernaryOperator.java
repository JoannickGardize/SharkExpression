package sharkhendrix.sharkexpression.token;

import sharkhendrix.sharkexpression.FloatStack;

public interface TernaryOperator extends Operator {

    float compute(float leftSide, float middleSide, float rightSide);

    @Override
    default float execute(FloatStack output) {
        float right = output.pop();
        float middle = output.pop();
        float left = output.pop();
        return compute(left, middle, right);
    }
}
