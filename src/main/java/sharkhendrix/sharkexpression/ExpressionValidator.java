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

import sharkhendrix.sharkexpression.grammar.Grammar;
import sharkhendrix.sharkexpression.token.Number;
import sharkhendrix.sharkexpression.token.*;

import java.util.*;

public class ExpressionValidator {

    private interface Begin extends Token {
    }

    private interface End extends Token {
    }

    private static final Map<Class<? extends Token>, List<Class<? extends Token>>> sequenceExpectation = new IdentityHashMap<>();

    static {
        List<Class<? extends Token>> afterOperand = Arrays.asList(
                BinaryOperator.class, ArgSeparator.class, RightParenthesis.class, End.class);
        List<Class<? extends Token>> afterOperator = Arrays.asList(
                UnaryOperator.class, Number.class, LeftParenthesis.class, Function.class);
        sequenceExpectation.put(Number.class, afterOperand);
        sequenceExpectation.put(RightParenthesis.class, afterOperand);
        sequenceExpectation.put(Function.class, Collections.singletonList(LeftParenthesis.class));
        sequenceExpectation.put(BinaryOperator.class, afterOperator);
        sequenceExpectation.put(UnaryOperator.class, afterOperator);
        sequenceExpectation.put(ArgSeparator.class, afterOperator);
        sequenceExpectation.put(Begin.class, afterOperator);
        sequenceExpectation.put(LeftParenthesis.class, afterOperator);
    }

    private final Tokenizer tokenizer;
    private final Grammar grammar;

    public ExpressionValidator(Tokenizer tokenizer, Grammar grammar) {
        this.tokenizer = tokenizer;
        this.grammar = grammar;
    }

    public List<ValidationError> validate(String expressionStr) {
        List<ValidationError> errors = new ArrayList<>();
        validate(expressionStr, errors);
        return errors;
    }

    public List<Token> validate(String expressionStr, List<ValidationError> errors) {
        List<Tokenizer.TokenTrack> tokenTracks = new ArrayList<>();
        List<Token> tokens = tokenizer.tokenize(expressionStr, tokenTracks, errors);
        if (tokens.isEmpty()) {
            errors.add(new ValidationError(ValidationError.Type.EMPTY_EXPRESSION, -1, null));
            return Collections.emptyList();
        }
        Deque<Tokenizer.TokenTrack> leftParenthesisStack = new ArrayDeque<>();
        Class<? extends Token> previousTokenClass = Begin.class;
        Token previousToken = null;
        Function currentFunction = null;
        int currentFunctionParenthesisDepth = 0;
        int argCount = 0;
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            Tokenizer.TokenTrack tokenTrack = tokenTracks.get(i);
            Class<? extends Token> tokenClass = getRelevantClass(token);
            // Ignore sequence check after an unknown token
            if (previousToken != Tokenizer.unknownToken && token != Tokenizer.unknownToken) {
                List<Class<? extends Token>> expectedList = sequenceExpectation.get(previousTokenClass);
                if (!expectedList.contains(tokenClass)) {
                    errors.add(new ValidationError(ValidationError.Type.UNEXPECTED_TOKEN, tokenTrack));
                }
            }
            if (tokenClass != LeftParenthesis.class && previousTokenClass == Function.class) {
                // Discard function args check if there is missing left parenthesis for that function
                currentFunction = null;
            }
            if (tokenClass == LeftParenthesis.class) {
                leftParenthesisStack.push(tokenTrack);
                currentFunctionParenthesisDepth++;
            } else if (tokenClass == RightParenthesis.class) {
                currentFunctionParenthesisDepth--;
                if (leftParenthesisStack.isEmpty()) {
                    errors.add(new ValidationError(ValidationError.Type.MISSING_LEFT_PARENTHESIS, tokenTrack));
                } else {
                    checkParenthesisPair(leftParenthesisStack, tokenTrack, errors);
                    if (currentFunction != null && currentFunctionParenthesisDepth == 0) {
                        checkFunction(previousTokenClass, argCount, currentFunction, errors, tokenTrack);
                        currentFunction = null;
                    }
                }
            } else if (tokenClass == Function.class) {
                currentFunction = (Function) token;
                argCount = 0;
                currentFunctionParenthesisDepth = 0;
            } else if (tokenClass == ArgSeparator.class) {
                argCount++;
            }
            previousTokenClass = tokenClass;
            previousToken = token;
        }
        // Ignore sequence check after an unknown token
        if (previousToken != Tokenizer.unknownToken) {
            List<Class<? extends Token>> expectedList = sequenceExpectation.get(previousTokenClass);
            if (!expectedList.contains(End.class)) {
                errors.add(new ValidationError(ValidationError.Type.UNEXPECTED_TOKEN, tokenTracks.get(tokenTracks.size() - 1)));
            }
        }
        for (Tokenizer.TokenTrack leftParenthesis : leftParenthesisStack) {
            errors.add(new ValidationError(ValidationError.Type.MISSING_RIGHT_PARENTHESIS, leftParenthesis));
        }
        return tokens;
    }

    private void checkFunction(Class<? extends Token> previousTokenClass, int argCount, Function currentFunction, List<ValidationError> errors, Tokenizer.TokenTrack tokenTrack) {
        if (currentFunction.numArgs() != ((previousTokenClass != LeftParenthesis.class) ? argCount + 1 : argCount)) {
            errors.add(new ValidationError(ValidationError.Type.WRONG_NUMBER_OF_ARGUMENTS, tokenTrack));
        }
    }

    private void checkParenthesisPair(Deque<Tokenizer.TokenTrack> leftParenthesisStack,
                                      Tokenizer.TokenTrack RightParenthesisTrack, List<ValidationError> errors) {
        Tokenizer.TokenTrack leftTokenTrack = leftParenthesisStack.pop();
        if (!grammar.matchParenthesis(leftTokenTrack.getStr().codePointAt(0),
                RightParenthesisTrack.getStr().codePointAt(0))) {
            errors.add(new ValidationError(ValidationError.Type.PARENTHESIS_MISMATCH, RightParenthesisTrack));
        }
    }

    private Class<? extends Token> getRelevantClass(Token token) {
        if (token instanceof Number) {
            return Number.class;
        } else if (token instanceof UnaryOperator) {
            return UnaryOperator.class;
        } else if (token instanceof BinaryOperator) {
            return BinaryOperator.class;
        } else if (token instanceof Function) {
            return Function.class;
        } else {
            return token.getClass();
        }
    }
}
