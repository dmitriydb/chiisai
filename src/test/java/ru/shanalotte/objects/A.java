package ru.shanalotte.objects;

import java.util.Objects;

public class A {
    B b = new B();
    int v =3;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        A a = (A) o;
        return v == a.v && Objects.equals(b, a.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(b, v);
    }

    @Override
    public String toString() {
        return "A{" +
                "b=" + b +
                ", v=" + v +
                '}';
    }
}
