/*
 * Copyright 2024 Joannick Gardize
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Merge ternary operators into their final forms.
 * The input tokens should be in postfix notation.
 */
public class TernaryOperatorMerger implements TokenSequenceFunction {

    @Override
    public List<Token> apply(List<Token> tokens) {
        List<Token> result = new ArrayList<>(tokens.size());
        Operators.TemporaryTernaryRightPart currentTernaryRight = null;
        for (Token token : tokens) {
            if (currentTernaryRight != null && !(token instanceof Operators.TemporaryTernaryLeftPart)) {
                throw new InvalidExpressionSyntaxException("Missing ternary second symbol");
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
                    if (currentTernaryRight.getOperator() != leftPart.getOperator()) {
                        throw new InvalidExpressionSyntaxException("Ternary symbols mismatch");
                    }
                    result.add(currentTernaryRight.getOperator());
                    currentTernaryRight = null;
                }
            } else {
                result.add(token);
            }
        }
        return result;
    }
}
