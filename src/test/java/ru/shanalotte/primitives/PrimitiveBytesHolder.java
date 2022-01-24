package ru.shanalotte.primitives;

public class PrimitiveBytesHolder {
    byte b1 = Byte.MIN_VALUE;
    byte b2 = Byte.MAX_VALUE;
    byte b3 = 0;
    byte b4 = -55;
    byte b5 = 101;

    @Override
    public String toString() {
        return "PrimitiveBytesHolder{" +
                "b1=" + b1 +
                ", b2=" + b2 +
                ", b3=" + b3 +
                ", b4=" + b4 +
                ", b5=" + b5 +
                '}';
    }
}
