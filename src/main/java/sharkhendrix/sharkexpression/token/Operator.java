package sharkhendrix.sharkexpression.token;

import sharkhendrix.sharkexpression.FloatStack;

public interface Operator extends ExpressionToken {
    float execute(FloatStack output);
}
