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

package sharkhendrix.sharkexpression.util;


import java.util.Arrays;

/**
 * Simple implementation of a primitive float stack
 */
public class FloatStack {

    private float[] elements;
    private int size;

    /**
     * Create a new Float stack with an initial capacity of 10.
     */
    public FloatStack() {
        this(10);
    }

    /**
     * Create a new Float stack with the given initial capacity
     *
     * @param initialCapacity the initial capacity of the backing array
     */
    public FloatStack(int initialCapacity) {
        elements = new float[initialCapacity];
        size = 0;
    }

    /**
     * Push the given float to the head of this stack.
     *
     * @param e the float value to push
     */
    public void push(float e) {
        ensureCapacity();
        elements[size] = e;
        ++size;
    }

    /**
     * Retrieve and remove the head of this stack
     *
     * @return the value removed from the head of this stack
     */
    public float pop() {
        if (size == 0) {
            return Float.NaN;
        } else {
            --size;
            return elements[size];
        }
    }

    public int size() {
        return size;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, elements.length + 10);
        }
    }
}
