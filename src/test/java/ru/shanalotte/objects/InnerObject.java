package ru.shanalotte.objects;

import java.util.Objects;

public class InnerObject {
    int innerValue = 5;

    @Override
    public String toString() {
        return "InnerObject{" +
                "innerValue=" + innerValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InnerObject that = (InnerObject) o;
        return innerValue == that.innerValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(innerValue);
    }
}
