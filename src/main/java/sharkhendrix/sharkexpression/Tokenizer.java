package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.*;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

/**
 * <p>This class is responsible for "tokenizing" expression strings into a list of {@link ExpressionToken}.
 */
public class Tokenizer {

    protected final ExpressionGrammar grammar;

    public Tokenizer(ExpressionGrammar grammar) {
        this.grammar = grammar;
    }

    private enum TokenType {
        NUMBER,
        WORD,
        SYMBOL
    }

    public List<ExpressionToken> tokenize(String str) throws InvalidExpressionSyntaxException {
        PrimitiveIterator.OfInt codePointsIt = str.codePoints().iterator();
        StringBuilder stringBuilder = new StringBuilder();
        TokenType currentType = null;
        List<ExpressionToken> tokens = new ArrayList<>();
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
            } else if (c == grammar.leftParenthesis()) {
                addToken(currentType, stringBuilder, tokens);
                tokens.add(LeftParenthesis.getInstance());
                gotTheDot = false;
                currentType = null;
            } else if (c == grammar.rightParenthesis()) {
                addToken(currentType, stringBuilder, tokens);
                tokens.add(RightParenthesis.getInstance());
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

    private void addToken(TokenType currentType, StringBuilder stringBuilder, List<ExpressionToken> tokens) throws InvalidExpressionSyntaxException {
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
                VariableNumber variableNumber = grammar.getVariable(str);
                if (variableNumber == null) {
                    addOperator(tokens, str);
                }
                tokens.add(variableNumber);
                break;
            case SYMBOL:
                addOperator(tokens, str);
                break;
        }
    }

    private void addOperator(List<ExpressionToken> tokens, String str) throws InvalidExpressionSyntaxException {
        ExpressionToken operator = shouldBeUnaryOperator(tokens) ?
                grammar.getUnaryOperator(str) : grammar.getBinaryOperator(str);
        if (operator == null) {
            throw new InvalidExpressionSyntaxException("Unknown symbol: " + str);
        }
        tokens.add(operator);
    }

    private boolean shouldBeUnaryOperator(List<ExpressionToken> tokens) {
        if (tokens.isEmpty()) {
            return true;
        }
        ExpressionToken previousToken = tokens.get(tokens.size() - 1);
        return previousToken instanceof BinaryOperator || previousToken instanceof LeftParenthesis;
    }
}
