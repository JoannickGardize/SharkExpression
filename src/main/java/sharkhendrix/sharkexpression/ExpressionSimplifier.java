package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.Number;
import sharkhendrix.sharkexpression.token.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Transforms ternary operators from their temporary binary forms to their ternary forms, and simply constant operations.
 */
public class ExpressionSimplifier implements TokenSequenceFunction {
    @Override
    public List<ExpressionToken> apply(List<ExpressionToken> expressionTokens) throws InvalidExpressionSyntaxException {
        List<ExpressionToken> result = new ArrayList<>(expressionTokens.size());
        Operators.TemporaryTernaryRightPart currentTernaryRight = null;
        for (ExpressionToken token : expressionTokens) {
            if (currentTernaryRight != null && !(token instanceof Operators.TemporaryTernaryLeftPart)) {
                throw new InvalidExpressionSyntaxException("Missing ternary second symbol");
            }
            if (token instanceof Number) {
                result.add(token);
            } else if (token instanceof Operators.TemporaryTernaryRightPart) {
                if (currentTernaryRight != null) {
                    throw new InvalidExpressionSyntaxException("Invalid ternary operator");
                }
                currentTernaryRight = (Operators.TemporaryTernaryRightPart) token;
            } else if (token instanceof Operators.TemporaryTernaryLeftPart) {
                if (currentTernaryRight == null) {
                    throw new InvalidExpressionSyntaxException("Unexpected ternary second symbol");
                } else {
                    Operators.TemporaryTernaryLeftPart leftPart = (Operators.TemporaryTernaryLeftPart) token;
                    if (currentTernaryRight.getOperator() != leftPart.operator()) {
                        throw new InvalidExpressionSyntaxException("Ternary symbols mismatch");
                    }
                    if (result.get(result.size() - 1) instanceof ConstantNumber
                            && result.get(result.size() - 2) instanceof ConstantNumber
                            && result.get(result.size() - 3) instanceof ConstantNumber) {
                        ExpressionToken right = result.remove(result.size() - 1);
                        ExpressionToken middle = result.remove(result.size() - 1);
                        ExpressionToken left = result.remove(result.size() - 1);
                        result.add(new ConstantNumber(currentTernaryRight.getOperator()
                                .compute(((ConstantNumber) left).getValue(), ((ConstantNumber) middle).getValue(),
                                        ((ConstantNumber) right).getValue())));
                    } else {
                        result.add(currentTernaryRight.getOperator());
                    }
                    currentTernaryRight = null;
                }
            } else if (token instanceof UnaryOperator) {
                if (result.get(result.size() - 1) instanceof ConstantNumber) {
                    result.add(new ConstantNumber(((UnaryOperator) token)
                            .compute(((ConstantNumber) result.remove(result.size() - 1)).getValue())));
                } else {
                    result.add(token);
                }
            } else if (token instanceof BinaryOperator) {
                if (result.get(result.size() - 1) instanceof ConstantNumber
                        && result.get(result.size() - 2) instanceof ConstantNumber) {
                    ExpressionToken right = result.remove(result.size() - 1);
                    ExpressionToken left = result.remove(result.size() - 1);
                    result.add(new ConstantNumber(((BinaryOperator) token)
                            .compute(((ConstantNumber) left).getValue(), ((ConstantNumber) right).getValue())));
                } else {
                    result.add(token);
                }
            }
        }
        return result;
    }
}
