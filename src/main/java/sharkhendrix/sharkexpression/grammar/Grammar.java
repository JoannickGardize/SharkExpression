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

package sharkhendrix.sharkexpression.grammar;

import sharkhendrix.sharkexpression.grammar.defaults.DefaultConstants;
import sharkhendrix.sharkexpression.grammar.defaults.DefaultFunctions;
import sharkhendrix.sharkexpression.grammar.defaults.DefaultOperators;
import sharkhendrix.sharkexpression.token.BinaryOperator;
import sharkhendrix.sharkexpression.token.Function;
import sharkhendrix.sharkexpression.token.Number;
import sharkhendrix.sharkexpression.token.UnaryOperator;

import java.util.Arrays;

/**
 * Definition of all the grammar for Expressions :
 * basic characters, operators, functions and variables.
 */
public class Grammar {

    private static class ParenthesisPair {
        final int left;
        final int right;

        public ParenthesisPair(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }

    private static final ParenthesisPair[] defaultParenthesisPairs
            = createParenthesisPairs('(', ')', '[', ']');

    private static final ParenthesisPair[] basicDefaultParenthesisPairs
            = createParenthesisPairs('(', ')');

    private static final int[] defaultAllowedSpecialCharsInWords = new int[]{'_'};

    private final Operators operators;
    private final Functions functions;
    private final Variables variables;

    private ParenthesisPair[] parenthesisPairs = basicDefaultParenthesisPairs;
    private int functionArgsSeparator = ',';
    public int decimalSeparator = '.';
    private int[] allowedSpecialCharsInWords = defaultAllowedSpecialCharsInWords;

    /**
     * Creates a Grammar empty of operators, functions, variables,
     * and with default parenthesis pair '( )'.
     */
    public Grammar() {
        operators = new Operators();
        functions = new Functions();
        this.variables = new Variables();
    }

    /**
     * Creates a Grammar with the given variables, empty of operators, functions,
     * and with default parenthesis pair '( )'.
     *
     * @param variables the variables of the grammar
     */
    public Grammar(Variables variables) {
        operators = new Operators();
        functions = new Functions();
        this.variables = variables;
    }

    /**
     * Creates a Grammar with the given variables, operators, functions,
     * and with default parenthesis pair '( )'.
     *
     * @param operators the operators of the grammar
     * @param functions the functions of the grammar
     * @param variables the variables of the grammar
     */
    public Grammar(Operators operators, Functions functions, Variables variables) {
        this.operators = operators;
        this.functions = functions;
        this.variables = variables;
    }

    /**
     * Creates a Grammar with default operators, functions, constants,
     * provided by {@link DefaultOperators}, {@link  DefaultFunctions} and {@link DefaultConstants},
     * and parenthesis pairs are '( )' and '[ ]'.
     *
     * @return a new Grammar with default content
     */
    public static Grammar withDefault() {
        return withDefault(new Variables());
    }

    /**
     * Creates a Grammar with default operators, functions, constants,
     * provided by {@link DefaultOperators}, {@link  DefaultFunctions} and {@link DefaultConstants},
     * and parenthesis pairs are '( )' and '[ ]'.
     *
     * @param variables the Variables to use
     * @return a new Grammar with default content
     */
    public static Grammar withDefault(Variables variables) {
        Grammar grammar = new Grammar(variables);
        grammar.parenthesisPairs = defaultParenthesisPairs;
        DefaultOperators.apply(grammar.operators);
        DefaultFunctions.apply(grammar.functions);
        DefaultConstants.apply(variables);
        return grammar;
    }

    /**
     * Creates a Grammar with default operators (but without synonyms), functions, constants,
     * provided by {@link DefaultOperators}, {@link  DefaultFunctions} and {@link DefaultConstants},
     * and parenthesis pair '( )'.
     *
     * @return a new Grammar with basic default content
     */
    public static Grammar withBasicDefault() {
        return withBasicDefault(new Variables());
    }

    /**
     * Creates a Grammar with default operators (but without synonyms), functions, constants,
     * provided by {@link DefaultOperators}, {@link  DefaultFunctions} and {@link DefaultConstants},
     * and parenthesis pair '( )'.
     *
     * @param variables the Variables to use
     * @return a new Grammar with basic default content
     */
    public static Grammar withBasicDefault(Variables variables) {
        Grammar grammar = new Grammar(variables);
        DefaultOperators.apply(grammar.operators, false);
        DefaultFunctions.apply(grammar.functions);
        DefaultConstants.apply(variables);
        return grammar;
    }

    /**
     * Get the Operators of this grammar, allowing to add and remove operators.
     *
     * @return the Operators which is part of this Grammar
     */
    public Operators operators() {
        return operators;
    }

    /**
     * Get the Functions of this grammar, allowing to add and remove functions.
     *
     * @return the Functions which is part of this Grammar
     */
    public Functions functions() {
        return functions;
    }

    /**
     * Get the Functions of this grammar, allowing to add and remove variables and constants.
     *
     * @return the Variables which is part of this Grammar
     */
    public Variables variables() {
        return variables;
    }

    /**
     * Set the allowed parenthesis pairs for this grammar.
     *
     * @param chars the succession of parenthesis pairs: left1, right1, left2... and so on
     * @return this for method chaining style
     */
    public Grammar setParenthesisPairs(int... chars) {
        parenthesisPairs = createParenthesisPairs(chars);
        return this;
    }

    /**
     * Set the function argument separator character. The default value is ','.
     *
     * @param functionArgsSeparator the function argument separator character.
     * @return this for method chaining style
     */
    public Grammar setFunctionArgsSeparator(int functionArgsSeparator) {
        this.functionArgsSeparator = functionArgsSeparator;
        return this;
    }

    /**
     * Set the decimal separator for numbers. The default value is '.'.
     *
     * @param decimalSeparator the decimal separator used for numbers
     * @return this for method chaining style
     */
    public Grammar setDecimalSeparator(int decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
        return this;
    }

    /**
     * Set the non-alphabetic characters allowed in words (in variables or functions).
     * The default value is '_'.
     *
     * @param chars the allowed non-alphabetic characters for words
     * @return this for method chaining style
     */
    public Grammar setAllowedSpecialCharsInWords(int... chars) {
        this.allowedSpecialCharsInWords = chars;
        return this;
    }

    public UnaryOperator getUnaryOperator(String symbol) {
        return operators.getUnary(symbol);
    }

    public BinaryOperator getBinaryOperator(String symbol) {
        return operators.getBinary(symbol);
    }

    public Number getVariable(String name) {
        return variables.get(name);
    }

    public Function getFunction(String name) {
        return functions.get(name);
    }

    public boolean isLeftParenthesis(int c) {
        return Arrays.stream(parenthesisPairs).anyMatch(p -> p.left == c);
    }

    public boolean isRightParenthesis(int c) {
        return Arrays.stream(parenthesisPairs).anyMatch(p -> p.right == c);
    }

    public boolean matchParenthesis(int left, int right) {
        return Arrays.stream(parenthesisPairs).anyMatch(p -> p.left == left && p.right == right);
    }

    public int functionArgsSeparator() {
        return functionArgsSeparator;
    }

    public int decimalSeparator() {
        return decimalSeparator;
    }

    public boolean isSpecialCharAllowedInWords(int c) {
        return Arrays.stream(allowedSpecialCharsInWords).anyMatch(a -> a == c);
    }

    private static ParenthesisPair[] createParenthesisPairs(int... chars) {
        if (chars.length % 2 != 0) {
            throw new IllegalArgumentException("Even number of argument required to produce parenthesis pairs");
        }
        ParenthesisPair[] parenthesisPairs = new ParenthesisPair[chars.length / 2];
        for (int i = 0; i < chars.length; i += 2) {
            parenthesisPairs[i / 2] = new ParenthesisPair(chars[i], chars[i + 1]);
        }
        return parenthesisPairs;
    }
}
