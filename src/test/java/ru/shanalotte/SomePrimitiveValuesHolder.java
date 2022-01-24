package ru.shanalotte;

public class SomePrimitiveValuesHolder {
    byte byteValue = 111;
    short shortValue = 111;
    int intValue = 111;
    long logValue = 111;
    char c = 'Я';
    float f = -3444.120f;
    boolean booleanValue = true;
    char charValue = '\u2312';
    double doubleValue = 2.0d;
    float floatValue = 2.1f;

    @Override
    public String toString() {
        return "SomePrimitiveValuesHolder{" +
                "byteValue=" + byteValue +
                ", shortValue=" + shortValue +
                ", intValue=" + intValue +
                ", logValue=" + logValue +
                ", c=" + c +
                ", f=" + f +
                ", booleanValue=" + booleanValue +
                ", charValue=" + charValue +
                ", doubleValue=" + doubleValue +
                ", floatValue=" + floatValue +
                '}';
    }
}
