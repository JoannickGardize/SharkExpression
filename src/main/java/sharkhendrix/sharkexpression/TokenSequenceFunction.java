package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.Token;

import java.util.List;

public interface TokenSequenceFunction {
    List<Token> apply(List<Token> tokens) throws InvalidExpressionSyntaxException;
}
