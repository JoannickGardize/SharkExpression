package sharkhendrix.sharkexpression.token;

import sharkhendrix.sharkexpression.FloatStack;

public interface Operator extends Token {
    float execute(FloatStack output);
}
