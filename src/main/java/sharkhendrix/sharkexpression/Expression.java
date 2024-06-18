package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.Number;
import sharkhendrix.sharkexpression.token.Operator;
import sharkhendrix.sharkexpression.token.Token;
import sharkhendrix.sharkexpression.util.FloatStack;

/**
 * Not thread-safe.
 */
public class Expression {

    private final Token[] tokens;

    private final FloatStack outputStack = new FloatStack();

    public Expression(Token[] tokens) {
        this.tokens = tokens;
    }

    public float evaluate() {
        for (Token token : tokens) {
            if (token instanceof Number) {
                outputStack.push(((Number) token).getValue());
            } else {
                outputStack.push(((Operator) token).execute(outputStack));
            }
        }
        return outputStack.pop();
    }
}
