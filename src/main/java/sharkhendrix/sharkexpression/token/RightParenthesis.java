package sharkhendrix.sharkexpression.token;


public class RightParenthesis implements Token {

    private static final RightParenthesis INSTANCE = new RightParenthesis();

    private RightParenthesis() {
    }

    public static RightParenthesis getInstance() {
        return INSTANCE;
    }
}
