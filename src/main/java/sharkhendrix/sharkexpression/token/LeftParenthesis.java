package sharkhendrix.sharkexpression.token;

public class LeftParenthesis implements Token {

    private static final LeftParenthesis INSTANCE = new LeftParenthesis();

    private LeftParenthesis() {
    }

    public static LeftParenthesis getInstance() {
        return INSTANCE;
    }
}
