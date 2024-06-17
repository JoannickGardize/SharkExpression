package sharkhendrix.sharkexpression;

import sharkhendrix.sharkexpression.token.Function;

import java.util.Random;

public class DefaultFunctions {

    private DefaultFunctions() {
    }

    public static void apply(Functions functions) {

        // Mathematical functions

        functions.add(((Function.OneArg) Math::abs), "abs");
        functions.add(((Function.OneArg) f -> (float) Math.acos(f)), "acos");
        functions.add(((Function.OneArg) f -> (float) Math.asin(f)), "asin");
        functions.add(((Function.OneArg) f -> (float) Math.atan(f)), "atan");
        functions.add(((Function.TwoArgs) (y, x) -> (float) Math.atan2(y, x)), "atan2");
        functions.add(((Function.OneArg) f -> (float) Math.cbrt(f)), "cbrt");
        functions.add(((Function.OneArg) f -> (float) Math.ceil(f)), "ceil");
        functions.add(((Function.OneArg) f -> (float) Math.cos(f)), "cos");
        functions.add(((Function.OneArg) f -> (float) Math.cosh(f)), "cosh");
        functions.add(((Function.OneArg) f -> (float) Math.exp(f)), "exp");
        functions.add(((Function.OneArg) f -> (float) Math.floor(f)), "floor");
        functions.add(((Function.OneArg) f -> (float) Math.log(f)), "log");
        functions.add(((Function.OneArg) f -> (float) Math.log10(f)), "log10");
        functions.add(((Function.TwoArgs) Math::max), "max");
        functions.add(((Function.TwoArgs) Math::min), "min");
        functions.add(((Function.TwoArgs) (f, e) -> (float) Math.pow(f, e)), "pow");
        functions.add(((Function.OneArg) f -> (float) Math.round(f)), "round");
        functions.add(((Function.OneArg) Math::signum), "signum");
        functions.add(((Function.OneArg) f -> (float) Math.sin(f)), "sin");
        functions.add(((Function.OneArg) f -> (float) Math.sinh(f)), "sinh");
        functions.add(((Function.OneArg) f -> (float) Math.sqrt(f)), "sqrt");
        functions.add(((Function.OneArg) f -> (float) Math.tan(f)), "tan");
        functions.add(((Function.OneArg) f -> (float) Math.tanh(f)), "tanh");

        // Misc

        Random random = new Random();
        functions.add(((Function.NoArgs) random::nextFloat), "random", "rand");
        functions.add(((Function.ThreeArgs) (c, t, f) -> c != 0 ? t : f), "choose");
    }
}
