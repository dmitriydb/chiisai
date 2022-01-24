package ru.shanalotte.stringtest;

import java.util.Objects;

public class StringHolder {
    String str = "Hello serialized string";
    String str2 = "Hello again";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringHolder that = (StringHolder) o;
        return Objects.equals(str, that.str) && Objects.equals(str2, that.str2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str, str2);
    }
}
