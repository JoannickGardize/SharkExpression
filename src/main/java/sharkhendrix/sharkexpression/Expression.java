package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.ExpressionToken;
import sharkhendrix.sharkexpression.token.Number;
import sharkhendrix.sharkexpression.token.Operator;

/**
 * Not thread-safe.
 */
public class Expression {

    private final ExpressionToken[] tokens;

    private final FloatStack outputStack = new FloatStack();

    public Expression(ExpressionToken[] tokens) {
        this.tokens = tokens;
    }

    public float evaluate() {
        for (ExpressionToken token : tokens) {
            if (token instanceof Number) {
                outputStack.push(((Number) token).getValue());
            } else {
                outputStack.push(((Operator) token).execute(outputStack));
            }
        }
        return outputStack.pop();
    }
}
