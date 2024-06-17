package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.*;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

/**
 * <p>This class is responsible for "tokenizing" expression strings into a list of {@link Token}.
 */
public class Tokenizer {

    protected final Grammar grammar;

    public Tokenizer(Grammar grammar) {
        this.grammar = grammar;
    }

    private enum TokenType {
        NUMBER,
        WORD,
        SYMBOL
    }

    public List<Token> tokenize(String str) throws InvalidExpressionSyntaxException {
        PrimitiveIterator.OfInt codePointsIt = str.codePoints().iterator();
        StringBuilder stringBuilder = new StringBuilder();
        TokenType currentType = null;
        List<Token> tokens = new ArrayList<>();
        boolean gotTheDot = false;
        int charCount = 0;
        while (codePointsIt.hasNext()) {
            int c = codePointsIt.next();
            if (c == ' ') {
                addToken(currentType, stringBuilder, tokens);
                gotTheDot = false;
                currentType = null;
            } else if (Character.isDigit(c)) {
                if (currentType != TokenType.NUMBER && currentType != TokenType.WORD) {
                    addToken(currentType, stringBuilder, tokens);
                    gotTheDot = false;
                    currentType = TokenType.NUMBER;
                }
                stringBuilder.appendCodePoint(c);
            } else if (c == grammar.decimalSeparator()) {
                if (gotTheDot) {
                    throw new InvalidExpressionSyntaxException("Invalid number format at character " + charCount);
                }
                if (currentType != TokenType.NUMBER) {
                    addToken(currentType, stringBuilder, tokens);
                    currentType = TokenType.NUMBER;
                }
                gotTheDot = true;
                stringBuilder.appendCodePoint(c);
            } else if (Character.isAlphabetic(c) || grammar.isSpecialCharAuthorizedInWords(c)) {
                if (currentType != TokenType.WORD) {
                    addToken(currentType, stringBuilder, tokens);
                    gotTheDot = false;
                    currentType = TokenType.WORD;
                }
                stringBuilder.appendCodePoint(c);
            } else if (grammar.isLeftParenthesis(c)) {
                addToken(currentType, stringBuilder, tokens);
                tokens.add(LeftParenthesis.getInstance());
                gotTheDot = false;
                currentType = null;
            } else if (grammar.isRightParenthesis(c)) {
                addToken(currentType, stringBuilder, tokens);
                tokens.add(RightParenthesis.getInstance());
                gotTheDot = false;
                currentType = null;
            } else if (c == grammar.functionArgsSeparator()) {
                addToken(currentType, stringBuilder, tokens);
                tokens.add(ArgSeparator.getInstance());
                gotTheDot = false;
                currentType = null;
            } else {
                if (currentType != TokenType.SYMBOL) {
                    addToken(currentType, stringBuilder, tokens);
                    gotTheDot = false;
                    currentType = TokenType.SYMBOL;
                }
                stringBuilder.appendCodePoint(c);
            }
            charCount++;
        }
        addToken(currentType, stringBuilder, tokens);
        return tokens;
    }

    private void addToken(TokenType currentType, StringBuilder stringBuilder, List<Token> tokens) throws InvalidExpressionSyntaxException {
        if (currentType == null) {
            return;
        }
        String str = stringBuilder.toString();
        stringBuilder.setLength(0);
        switch (currentType) {
            case NUMBER:
                tokens.add(new ConstantNumber(Float.parseFloat(str)));
                break;
            case WORD:
                Token token = grammar.getVariable(str);
                if (token == null) {
                    token = grammar.getFunction(str);
                }
                if (token == null) {
                    addOperator(tokens, str);
                } else {
                    tokens.add(token);
                }
                break;
            case SYMBOL:
                addOperator(tokens, str);
                break;
        }
    }

    private void addOperator(List<Token> tokens, String str) throws InvalidExpressionSyntaxException {
        Token operator = shouldBeUnaryOperator(tokens) ?
                grammar.getUnaryOperator(str) : grammar.getBinaryOperator(str);
        if (operator == null) {
            throw new InvalidExpressionSyntaxException("Unknown symbol: " + str);
        }
        tokens.add(operator);
    }

    private boolean shouldBeUnaryOperator(List<Token> tokens) {
        if (tokens.isEmpty()) {
            return true;
        }
        Token previousToken = tokens.get(tokens.size() - 1);
        return previousToken instanceof BinaryOperator || previousToken instanceof LeftParenthesis;
    }
}
