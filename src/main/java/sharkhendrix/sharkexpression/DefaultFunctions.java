package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.Function;

public class DefaultFunctions {

    private DefaultFunctions() {
    }

    public static void apply(Functions functions) {
        functions.add("abs", ((Function.OneArg) Math::abs));
        functions.add("acos", ((Function.OneArg) f -> (float) Math.acos(f)));
        functions.add("asin", ((Function.OneArg) f -> (float) Math.asin(f)));
        functions.add("atan", ((Function.OneArg) f -> (float) Math.atan(f)));
        functions.add("atan2", ((Function.TwoArgs) (y, x) -> (float) Math.atan2(y, x)));
        functions.add("cbrt", ((Function.OneArg) f -> (float) Math.cbrt(f)));
        functions.add("ceil", ((Function.OneArg) f -> (float) Math.ceil(f)));
        functions.add("cos", ((Function.OneArg) f -> (float) Math.cos(f)));
        functions.add("cosh", ((Function.OneArg) f -> (float) Math.cosh(f)));
        functions.add("exp", ((Function.OneArg) f -> (float) Math.exp(f)));
        functions.add("floor", ((Function.OneArg) f -> (float) Math.floor(f)));
        functions.add("log", ((Function.OneArg) f -> (float) Math.log(f)));
        functions.add("log10", ((Function.OneArg) f -> (float) Math.log10(f)));
        functions.add("max", ((Function.TwoArgs) Math::max));
        functions.add("min", ((Function.TwoArgs) Math::min));
        functions.add("pow", ((Function.TwoArgs) (f, e) -> (float) Math.pow(f, e)));
        functions.add("round", ((Function.OneArg) f -> (float) Math.round(f)));
        functions.add("signum", ((Function.OneArg) Math::signum));
        functions.add("sin", ((Function.OneArg) f -> (float) Math.sin(f)));
        functions.add("sinh", ((Function.OneArg) f -> (float) Math.sinh(f)));
        functions.add("sqrt", ((Function.OneArg) f -> (float) Math.sqrt(f)));
        functions.add("tan", ((Function.OneArg) f -> (float) Math.tan(f)));
        functions.add("tanh", ((Function.OneArg) f -> (float) Math.tanh(f)));
    }
}
