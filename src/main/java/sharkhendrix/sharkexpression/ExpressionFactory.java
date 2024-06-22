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
import sharkhendrix.sharkexpression.grammar.Variables;
import sharkhendrix.sharkexpression.token.Token;
import sharkhendrix.sharkexpression.util.FloatStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Builder of {@link Expression} Object from expression sentence.
 * Also provide an expression validation method.
 * <p>
 * Expression build steps:
 * <ol>
 *     <li>Tokenize input string.
 *     <li>Shunting yard algorithm to produce postfix ordered tokens.
 *     <li>Merge ternary operators into their final forms.
 *     <li>Simplify constant branches of the expression.
 * </ol>
 */
public class ExpressionFactory {

    private final Tokenizer tokenizer;
    private TokenPipeline[] tokenPipelines;
    private final ExpressionValidator validator;
    private Function<Token[], Expression> expressionConstructor = StandaloneExpression::new;

    /**
     * Creates an ExpressionFactory with a default Grammar and the given Variables.
     *
     * @param variables the Variables to use
     */
    public ExpressionFactory(Variables variables) {
        Grammar grammar = Grammar.withDefault(variables);
        tokenizer = new Tokenizer(grammar);
        createDefaultTokenSequenceFunctions();
        validator = new ExpressionValidator(tokenizer, grammar);
    }

    /**
     * Creates an ExpressionFactory with the given Grammar.
     *
     * @param grammar the Grammar to use
     */
    public ExpressionFactory(Grammar grammar) {
        tokenizer = new Tokenizer(grammar);
        createDefaultTokenSequenceFunctions();
        validator = new ExpressionValidator(tokenizer, grammar);
    }

    /**
     * Create an ExpressionFactory made with the given parameters.
     *
     * @param tokenizer      the tokenizer to use to parse expressions
     * @param validator      the validator to use to validate the expressions
     * @param tokenPipelines the sequence of TokenPipeline to execute to build the expressions
     */
    public ExpressionFactory(ExpressionValidator validator, Tokenizer tokenizer, TokenPipeline... tokenPipelines) {
        this.validator = validator;
        this.tokenizer = tokenizer;
        this.tokenPipelines = Arrays.copyOf(tokenPipelines, tokenPipelines.length);
    }

    /**
     * <i>Performance optimization topic.</i>
     *
     * <p>Set the Expression type built by this factory. {@link StandaloneExpression} is built by default.
     *
     * <p>StandaloneExpression holds a working FloatStack, so {@link Expression#evaluate()} should be called.
     * {@link Expression} does not hold such a FloatStack, so {@link Expression#evaluate(FloatStack)} must be called.
     *
     * @param buildStandAloneExpression true to build StandaloneExpressions, false to build Expressions,
     *                                  that requires an external FloatStack to work.
     */
    public void buildStandAloneExpression(boolean buildStandAloneExpression) {
        if (buildStandAloneExpression) {
            expressionConstructor = StandaloneExpression::new;
        } else {
            expressionConstructor = Expression::new;
        }
    }

    /**
     * Validate the given expression string.
     *
     * @param expressionStr the expression string to validate
     * @return the list of error, the expression is valid if the list is empty
     */
    public List<ValidationError> validate(String expressionStr) {
        return validator.validate(expressionStr);
    }

    /**
     * <p>Parse the given expression string into an Expression.
     * Note that this method could end successfully with an invalid expression,
     * such an Expression has an unexpected behavior.
     * <p> Note that once created, any change to the Grammar,
     * including variables, won't affect the Expression.
     *
     * @param expressionStr the expression string to parse
     * @return the Expression representing the expression string
     * @throws InvalidExpressionSyntaxException if this method detected an error in the expression string
     */
    public Expression parse(String expressionStr) {
        List<Token> tokens = tokenizer.tokenize(expressionStr);
        return buildExpression(tokens);
    }

    /**
     * <p>Validate and parse the given expression.
     * <p>Calling this method is equivalent to calling {@link #validate(String)} followed by {@link #parse(String)}.
     * But this method is preferred in such situation, because the tokenization is done once instead of twice.
     *
     * @param expressionStr the expression string to parse
     * @param errors        an empty list that could be populated with the expression validation errors.
     * @return the Expression representing the expression string, or null if there are validation errors.
     */
    public Expression validateAndParse(String expressionStr, List<ValidationError> errors) {
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("the 'errors' argument must be empty");
        }
        List<Token> tokens = validator.validate(expressionStr, errors);
        if (errors.isEmpty()) {
            return buildExpression(tokens);
        } else {
            return null;
        }
    }

    private void createDefaultTokenSequenceFunctions() {
        tokenPipelines = new TokenPipeline[]{
                new ShuntingYardAlgorithm(),
                new TernaryOperatorMerger(),
                new ExpressionSimplifier()
        };
    }

    private Expression buildExpression(List<Token> tokens) {
        List<Token> output = new ArrayList<>(tokens.size());
        for (TokenPipeline function : tokenPipelines) {
            output.clear();
            function.apply(tokens, output);
            List<Token> tmp = tokens;
            tokens = output;
            output = tmp;
        }
        return expressionConstructor.apply(tokens.toArray(new Token[0]));
    }
}
