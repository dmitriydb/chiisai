package ru.shanalotte.objects;

import java.util.Objects;

public class OuterObject {
    int outerValue = 10;
    InnerObject inner = new InnerObject();

    @Override
    public String toString() {
        return "OuterObject{" +
                "outerValue=" + outerValue +
                ", inner=" + inner +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OuterObject that = (OuterObject) o;
        return outerValue == that.outerValue && Objects.equals(inner, that.inner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outerValue, inner);
    }
}
