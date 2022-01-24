package ru.shanalotte.objects;

import java.util.Objects;

public class B {
    C c = new C();
    int v =2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        B b = (B) o;
        return v == b.v && Objects.equals(c, b.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c, v);
    }

    @Override
    public String toString() {
        return "B{" +
                "c=" + c +
                ", v=" + v +
                '}';
    }
}
