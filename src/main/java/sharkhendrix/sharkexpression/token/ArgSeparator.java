package sharkhendrix.sharkexpression.token;

public class ArgSeparator implements Token {

    private static final ArgSeparator INSTANCE = new ArgSeparator();

    private ArgSeparator() {

    }

    public static ArgSeparator getInstance() {
        return INSTANCE;
    }
}
