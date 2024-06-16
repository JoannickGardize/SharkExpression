package sharkhendrix.sharkexpression.token;

public class LeftParenthesis implements ExpressionToken {
    private static final LeftParenthesis INSTANCE = new LeftParenthesis();

    public static LeftParenthesis getInstance() {
        return INSTANCE;
    }
}
