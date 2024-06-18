package sharkhendrix.sharkexpression.token;

import sharkhendrix.sharkexpression.util.FloatStack;

public interface Operator extends Token {
    float execute(FloatStack output);
}
