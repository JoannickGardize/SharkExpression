package sharkhendrix.sharkexpression.token;

import sharkhendrix.sharkexpression.util.FloatStack;

public interface Function extends Operator {

    int numArgs();

    /**
     * Allows simplification by the {@link sharkhendrix.sharkexpression.ExpressionSimplifier}
     * when the input parameters are all constants.
     *
     * @return true if the simplification is allowed for this function
     */
    default boolean allowsSimplification() {
        return true;
    }

    interface NoArgs extends Function {

        @Override
        default int numArgs() {
            return 0;
        }

        /**
         * No-args functions probably does crazy things like using random values and thus doesn't want to be simplified.
         *
         * @return false
         */
        @Override
        default boolean allowsSimplification() {
            return false;
        }

        float compute();

        @Override
        default float execute(FloatStack output) {
            return compute();
        }
    }

    interface OneArg extends Function {

        @Override
        default int numArgs() {
            return 1;
        }

        float compute(float arg);

        @Override
        default float execute(FloatStack output) {
            return compute(output.pop());
        }
    }

    interface TwoArgs extends Function {

        @Override
        default int numArgs() {
            return 2;
        }

        float compute(float arg1, float arg2);

        @Override
        default float execute(FloatStack output) {
            float arg2 = output.pop();
            float arg1 = output.pop();
            return compute(arg1, arg2);
        }
    }

    interface ThreeArgs extends Function {

        @Override
        default int numArgs() {
            return 3;
        }

        float compute(float arg1, float arg2, float arg3);

        @Override
        default float execute(FloatStack output) {
            float arg3 = output.pop();
            float arg2 = output.pop();
            float arg1 = output.pop();
            return compute(arg1, arg2, arg3);
        }
    }

    interface FourArgs extends Function {

        @Override
        default int numArgs() {
            return 4;
        }

        float compute(float arg1, float arg2, float arg3, float arg4);

        @Override
        default float execute(FloatStack output) {
            float arg4 = output.pop();
            float arg3 = output.pop();
            float arg2 = output.pop();
            float arg1 = output.pop();
            return compute(arg1, arg2, arg3, arg4);
        }
    }

    interface NArgs extends Function {

        float compute(float... args);

        @Override
        default float execute(FloatStack output) {
            int numArgs = numArgs();
            float[] args = new float[numArgs];
            for (int i = numArgs - 1; i >= 0; i--) {
                args[i] = output.pop();
            }
            return compute(args);
        }
    }
}
