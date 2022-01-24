package ru.shanalotte.iotest;

import java.io.Serializable;
import java.util.Objects;

public class TestClass implements Serializable {
    int x = 5;
    String s = "hello";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestClass testClass = (TestClass) o;
        return x == testClass.x && Objects.equals(s, testClass.s);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, s);
    }

    @Override
    public String toString() {
        return "TestClass{" +
                "x=" + x +
                ", s='" + s + '\'' +
                '}';
    }
}
