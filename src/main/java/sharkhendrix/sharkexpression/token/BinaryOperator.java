package sharkhendrix.sharkexpression.token;

import sharkhendrix.sharkexpression.FloatStack;


public interface BinaryOperator extends Operator {

    float compute(float leftSide, float rightSide);

    int precedence();

    default boolean isLeftAssociative() {
        return true;
    }

    @Override
    default float execute(FloatStack output) {
        float right = output.pop();
        float left = output.pop();
        return compute(left, right);
    }
}
