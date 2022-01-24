package ru.shanalotte.objects;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class ArrayHolder {

    int[] intArray = new int[5];
    Object[] objectArray = new Object[]{1,2,3,4,5};
    float[] emptyArray = new float[0];
    double[] nullArray = null;

    @Override
    public String toString() {
        return "ArrayHolder{" +
                "intArray=" + Arrays.toString(intArray) +
                ", objectArray=" + Arrays.deepToString(objectArray) +
                ", emptyArray=" + Arrays.toString(emptyArray) +
                ", nullArray=" + Arrays.toString(nullArray) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayHolder that = (ArrayHolder) o;
        return Arrays.equals(intArray, that.intArray) && Arrays.deepEquals(objectArray, that.objectArray) && Arrays.equals(emptyArray, that.emptyArray) && Arrays.equals(nullArray, that.nullArray);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(intArray);
        result = 31 * result + Arrays.hashCode(objectArray);
        result = 31 * result + Arrays.hashCode(emptyArray);
        result = 31 * result + Arrays.hashCode(nullArray);
        return result;
    }
}
