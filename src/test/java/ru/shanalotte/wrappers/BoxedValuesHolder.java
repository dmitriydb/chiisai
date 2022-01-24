package ru.shanalotte.wrappers;

import java.util.Objects;

public class BoxedValuesHolder {
    Byte b1 = -128;
    Short b2 = Short.MIN_VALUE;
    Integer b3 = Integer.MIN_VALUE;
    Long b4 = Long.MIN_VALUE;
    Character b5 = '3';
    Boolean b6 = true;
    Float b7 = 254.25425f;
    Double b8 = 2542542.234234234d;

    @Override
    public String toString() {
        return "BoxedValuesHolder{" +
                "b1=" + b1 +
                ", b2=" + b2 +
                ", b3=" + b3 +
                ", b4=" + b4 +
                ", b5=" + b5 +
                ", b6=" + b6 +
                ", b7=" + b7 +
                ", b8=" + b8 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoxedValuesHolder that = (BoxedValuesHolder) o;
        return Objects.equals(b1, that.b1) && Objects.equals(b2, that.b2) && Objects.equals(b3, that.b3) && Objects.equals(b4, that.b4) && Objects.equals(b5, that.b5) && Objects.equals(b6, that.b6) && Objects.equals(b7, that.b7) && Objects.equals(b8, that.b8);
    }

    @Override
    public int hashCode() {
        return Objects.hash(b1, b2, b3, b4, b5, b6, b7, b8);
    }
}
