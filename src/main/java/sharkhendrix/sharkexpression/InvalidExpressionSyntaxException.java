package sharkhendrix.sharkexpression;

public class InvalidExpressionSyntaxException extends RuntimeException {

    private final int charIndex;

    public InvalidExpressionSyntaxException(String message) {
        this(message, -1);
    }

    public InvalidExpressionSyntaxException(String message, int charIndex) {
        super(charIndex != -1 ? message + " at character " + charIndex : message);
        this.charIndex = charIndex;
    }

    public int getCharIndex() {
        return charIndex;
    }
}
