package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.ExpressionToken;

import java.util.List;

public interface TokenSequenceFunction {
    List<ExpressionToken> apply(List<ExpressionToken> expressionTokens) throws InvalidExpressionSyntaxException;
}
