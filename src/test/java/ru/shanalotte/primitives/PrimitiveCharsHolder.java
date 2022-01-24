package ru.shanalotte.primitives;

public class PrimitiveCharsHolder {
    char b1 = 'a';
    char b2 = '\u0000';
    char b3 = '1';
    char b4 = '\uFFFF';
    char b5 = '\n';

    @Override
    public String toString() {
        return "PrimitiveCharsHolder{" +
                "b1=" + b1 +
                ", b2=" + b2 +
                ", b3=" + b3 +
                ", b4=" + b4 +
                ", b5=" + b5 +
                '}';
    }
}
