package ru.shanalotte.objects;

import java.util.Objects;

public class C {
    int v = 1;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        C c = (C) o;
        return v == c.v;
    }

    @Override
    public int hashCode() {
        return Objects.hash(v);
    }

    @Override
    public String toString() {
        return "C{" +
                "v=" + v +
                '}';
    }
}
