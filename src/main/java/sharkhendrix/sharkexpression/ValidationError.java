package sharkhendrix.sharkexpression;

import java.util.Objects;

public class ValidationError {

    public enum Type {
        UNKNOWN_SYMBOL,
        NUMBER_FORMAT,
        UNEXPECTED_TOKEN,
        MISSING_LEFT_PARENTHESIS,
        PARENTHESIS_MISMATCH,
        MISSING_RIGHT_PARENTHESIS,
        WRONG_NUMBER_OF_ARGUMENTS,
        EMPTY_EXPRESSION
    }

    private final Type type;
    private final int index;
    private final String tokenString;

    public ValidationError(Type type, int index, String tokenString) {
        this.type = type;
        this.index = index;
        this.tokenString = tokenString;
    }

    public ValidationError(Type type, Tokenizer.TokenTrack tokenTrack) {
        this.type = type;
        this.index = tokenTrack.getIndex();
        this.tokenString = tokenTrack.getStr();
    }

    public Type getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public String getTokenString() {
        return tokenString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationError that = (ValidationError) o;
        return index == that.index && type == that.type && Objects.equals(tokenString, that.tokenString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, index, tokenString);
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "type=" + type +
                ", index=" + index +
                ", tokenString='" + tokenString + '\'' +
                '}';
    }
}
