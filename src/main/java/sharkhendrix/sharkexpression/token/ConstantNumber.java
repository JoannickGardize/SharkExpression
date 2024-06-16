package sharkhendrix.sharkexpression.token;

import java.util.Objects;

public class ConstantNumber implements Number {

    private final float value;

    public ConstantNumber(float value) {
        this.value = value;
    }

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstantNumber that = (ConstantNumber) o;
        return Float.compare(value, that.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
