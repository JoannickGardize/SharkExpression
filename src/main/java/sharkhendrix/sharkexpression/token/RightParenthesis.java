package sharkhendrix.sharkexpression.token;


public class RightParenthesis implements ExpressionToken {
    private static final RightParenthesis INSTANCE = new RightParenthesis();

    public static RightParenthesis getInstance() {
        return INSTANCE;
    }
}
