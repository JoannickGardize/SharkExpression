# SharkExpression

SharkExpression is a lightweight and flexible mathematical expression evaluator for Java.

It has been designed to provide reusable and performance-focused Expressions once parsed,
with varying variable values, externally supplied by the user application.

The design allows to produce multiple Expressions sharing the same variable context, with the minimal memory print.

## Features

* Reusable, pre-parsed, pre-simplified, and performance-focused Expression objects, with externally supplied variable values.
* Unary, binary, and ternary operators.
* Functions.
* Constant values.
* Pre-computing of eventual full-of-constants expression parts.
* Expression syntax validation, with precise error list for string highlighting.
* Parameterizable grammar rules.
* Default grammar close to the Java / C one.
* Use of code point characters, because there is more than the english alphabet in this world ;-) .

## Limitations

* Use of float primitives, not double (I designed it for the context of a video game).
* Unnecessary branches of the expression are always computed (i.e. no logical operator smartness).
* Immature framework, may contain bugs.

## Sample

```java
ExpressionFactory factory = new ExpressionFactory(new Variables()
        .add("x", 5) // Constant variable
        .add("random", () -> (float) Math.random()) // Supplied at evaluation
);

Expression exp = factory.parse("2*x + signum(random - 0.5)");

// print 9 or 11 half of the time
System.out.println(exp.evaluate());
```

## Default grammar

The default grammar is basically the Java grammar. With the given difference: parenthesis can be either `( )` or `[ ]`.

### Operators

It's basically the Java operators with additionally the `^` for power,
and some additional synonyms shown bellow between parenthesis.

* Arithmetic binary operators: `+`, `-`, `*`, `/`, `%`, `^`
* Arithmetic unary operator: `-`
* Ternary operator: {condition} `?` {if true} `:` {if false} (or {condition} `then` {if true} `else` {if false})
* Comparison operators: `==` (or `=`), `!=` (or `<>`), `>`, `<`, `>=`, `<=`
* Logical operators:  `||` (or `|` or `or`), `&&` (or `&` or `and`), `!` (or `not`)

*Note that logical operators use the C style boolean logic : 0 = false,
and any other values are true, but 1 is used by convention.*

### Functions

Most of the usual mathematical functions of java.lang.Math : `cos(x)`, `max(a, b)`, `round(x)`, etc...

### Constants 

`e` and `pi`.

## Customize Grammar

The expression grammar is fully customizable. you can start from scratch with a `new Grammar()`,
or start with `Grammar.withDefault()` to get the content above, or with `Grammar.withBasicDefault()`
to get the default content but without the bracket `[ ]` parenthesis, and none of the synonyms presented above.
Bellow an example:

```java
Grammar grammar = Grammar.withDefault()
        .setParenthesisPairs('(', ')', '{', '}')
        .setAllowedSpecialCharsInWords('_', '@');

grammar.variables()
        .add("tau", (float) (2 * Math.PI))
        .add("y", () -> getY());

grammar.functions()
        .add("my@function", (a, b) -> myFunction());

grammar.operators()
        .remove("^")
        .add("⋅", DefaultOperators.MULTIPLY) // Add synonym to '*'
        .add("#", new BinaryOperator() {
            @Override
            public float compute(float leftSide, float rightSide) {
                return myStrangeOperation(leftSide, rightSide);
            }

            @Override
            public int precedence() {
                return DefaultOperators.ADDITION_PRECEDENCE;
            }
        });

ExpressionFactory factory = new ExpressionFactory(grammar);
```
