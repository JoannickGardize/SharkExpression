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

import sharkhendrix.sharkexpression.token.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PrimitiveIterator;

/**
 * <p>This class is responsible for "tokenizing" expression strings into a list of {@link Token}.
 * <p>Also supports a non-failing validation mode with character index tracking.
 */
public class Tokenizer {

    public static class TokenTrack {
        private final int index;
        private final String str;

        public TokenTrack(int index, String str) {
            this.index = index;
            this.str = str;
        }

        public int getIndex() {
            return index;
        }

        public String getStr() {
            return str;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TokenTrack that = (TokenTrack) o;
            return index == that.index && Objects.equals(str, that.str);
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, str);
        }

        @Override
        public String toString() {
            return "TokenTrack{" +
                    "index=" + index +
                    ", str='" + str + '\'' +
                    '}';
        }
    }

    public static final Token unknownToken = new Token() {
    };

    private enum TokenType {
        NUMBER,
        WORD,
        SYMBOL
    }

    private enum NumberDotState {
        MISSING,
        PRESENT,
        ERROR
    }

    protected final Grammar grammar;

    public Tokenizer(Grammar grammar) {
        this.grammar = grammar;
    }

    public List<Token> tokenize(String str) {
        return tokenize(str, null, null);
    }

    public List<Token> tokenize(String str, List<TokenTrack> tokenTracks, List<ValidationError> validationErrors) {
        PrimitiveIterator.OfInt codePointsIt = str.codePoints().iterator();
        StringBuilder stringBuilder = new StringBuilder();
        TokenType currentType = null;
        List<Token> tokens = new ArrayList<>();
        NumberDotState numberDotState = NumberDotState.MISSING;
        int charCount = 0;
        while (codePointsIt.hasNext()) {
            int c = codePointsIt.next();
            if (c == ' ') {
                addToken(currentType, stringBuilder, tokens, charCount, tokenTracks, validationErrors, numberDotState);
                numberDotState = NumberDotState.MISSING;
                currentType = null;
            } else if (Character.isDigit(c)) {
                if (currentType != TokenType.NUMBER && currentType != TokenType.WORD) {
                    addToken(currentType, stringBuilder, tokens, charCount, tokenTracks, validationErrors, numberDotState);
                    numberDotState = NumberDotState.MISSING;
                    currentType = TokenType.NUMBER;
                }
                stringBuilder.appendCodePoint(c);
            } else if (c == grammar.decimalSeparator()) {
                if (numberDotState == NumberDotState.PRESENT) {
                    if (validationErrors != null) {
                        numberDotState = NumberDotState.ERROR;
                    } else {
                        throw new InvalidExpressionSyntaxException("Invalid number format", charCount);
                    }
                }
                if (currentType != TokenType.NUMBER) {
                    addToken(currentType, stringBuilder, tokens, charCount, tokenTracks, validationErrors, numberDotState);
                    currentType = TokenType.NUMBER;
                }
                if (numberDotState == NumberDotState.MISSING) {
                    numberDotState = NumberDotState.PRESENT;
                }
                stringBuilder.appendCodePoint(c);
            } else if (Character.isAlphabetic(c) || grammar.isSpecialCharAllowedInWords(c)) {
                if (currentType != TokenType.WORD) {
                    addToken(currentType, stringBuilder, tokens, charCount, tokenTracks, validationErrors, numberDotState);
                    numberDotState = NumberDotState.MISSING;
                    currentType = TokenType.WORD;
                }
                stringBuilder.appendCodePoint(c);
            } else if (grammar.isLeftParenthesis(c)) {
                addToken(currentType, stringBuilder, tokens, charCount, tokenTracks, validationErrors, numberDotState);
                addCharacterToken(tokens, c, LeftParenthesis.getInstance(), charCount, tokenTracks);
                numberDotState = NumberDotState.MISSING;
                currentType = null;
            } else if (grammar.isRightParenthesis(c)) {
                addToken(currentType, stringBuilder, tokens, charCount, tokenTracks, validationErrors, numberDotState);
                addCharacterToken(tokens, c, RightParenthesis.getInstance(), charCount, tokenTracks);
                numberDotState = NumberDotState.MISSING;
                currentType = null;
            } else if (c == grammar.functionArgsSeparator()) {
                addToken(currentType, stringBuilder, tokens, charCount, tokenTracks, validationErrors, numberDotState);
                addCharacterToken(tokens, c, ArgSeparator.getInstance(), charCount, tokenTracks);
                numberDotState = NumberDotState.MISSING;
                currentType = null;
            } else {
                if (currentType != TokenType.SYMBOL) {
                    addToken(currentType, stringBuilder, tokens, charCount, tokenTracks, validationErrors, numberDotState);
                    numberDotState = NumberDotState.MISSING;
                    currentType = TokenType.SYMBOL;
                }
                stringBuilder.appendCodePoint(c);
            }
            charCount++;
        }
        addToken(currentType, stringBuilder, tokens, charCount, tokenTracks, validationErrors, numberDotState);
        return tokens;
    }

    private void addCharacterToken(List<Token> tokens, int c, Token token, int charCount, List<TokenTrack> tokenTracks) {
        tokens.add(token);
        if (tokenTracks != null) {
            tokenTracks.add(new TokenTrack(charCount, new String(new int[]{c}, 0, 1)));
        }
    }

    private void addToken(TokenType currentType, StringBuilder stringBuilder, List<Token> tokens,
                          int charCount, List<TokenTrack> tokenTracks, List<ValidationError> validationErrors, NumberDotState numberDotState) {
        if (currentType == null) {
            return;
        }
        String str = stringBuilder.toString();
        stringBuilder.setLength(0);
        switch (currentType) {
            case NUMBER:
                if (numberDotState == NumberDotState.ERROR) {
                    validationErrors.add(new ValidationError(ValidationError.Type.NUMBER_FORMAT, charCount - str.length(), str));
                    tokens.add(new ConstantNumber(0));
                } else {
                    tokens.add(new ConstantNumber(Float.parseFloat(str)));
                }
                break;
            case WORD:
                Token token = grammar.getVariable(str);
                if (token == null) {
                    token = grammar.getFunction(str);
                }
                if (token == null) {
                    addOperator(tokens, str, charCount, validationErrors);
                } else {
                    tokens.add(token);
                }
                break;
            case SYMBOL:
                addOperator(tokens, str, charCount, validationErrors);
                break;
        }
        if (tokenTracks != null) {
            tokenTracks.add(new TokenTrack(charCount - str.length(), str));
        }
    }

    private void addOperator(List<Token> tokens, String str, int charCount, List<ValidationError> validationErrors) {
        Token operator = shouldBeUnaryOperator(tokens) ?
                grammar.getUnaryOperator(str) : grammar.getBinaryOperator(str);
        if (operator == null) {
            if (validationErrors != null) {
                validationErrors.add(new ValidationError(ValidationError.Type.UNKNOWN_SYMBOL, charCount - str.length(), str));
                tokens.add(unknownToken);
            } else {
                throw new InvalidExpressionSyntaxException("Unknown symbol: " + str, charCount);
            }
        } else {
            tokens.add(operator);
        }
    }

    private boolean shouldBeUnaryOperator(List<Token> tokens) {
        if (tokens.isEmpty()) {
            return true;
        }
        Token previousToken = tokens.get(tokens.size() - 1);
        return previousToken instanceof BinaryOperator || previousToken instanceof LeftParenthesis;
    }
}
