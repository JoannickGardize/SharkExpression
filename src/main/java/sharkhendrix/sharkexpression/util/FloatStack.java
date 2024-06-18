package sharkhendrix.sharkexpression.util;


import java.util.Arrays;

public class FloatStack {

    private float[] elements;
    private int size;

    public FloatStack() {
        elements = new float[10];
        size = 0;
    }

    public void push(float e) {
        ensureCapacity();
        elements[size] = e;
        ++size;
    }

    public float pop() {
        if (size == 0) {
            return Float.NaN;
        } else {
            --size;
            return elements[size];
        }
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, elements.length + 10);
        }
    }
}
