package ru.shanalotte;

import java.util.Objects;

public class AllTogetherHolder {
    private byte byteValue = 123;
    private short shortValue = 3333;
    private int intValue = -34424234;
    private long longValue = 23471623232L;
    private float floatValue = 245.2342f;
    private double doubleValue = 7123.13112834d;
    private char charValue = 'd';
    private boolean booleanValue = true;

    private Byte b = 12;
    private Short s = null;
    private Integer i = 234;
    private Long l = 128314L;
    private Float f = 24.23423f;
    private Double d = 2345.1823d;
    private Character c = '\u8123';
    private Boolean bw = true;

    private String str = "Hello world";
    private String str2 = "";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllTogetherHolder that = (AllTogetherHolder) o;
        return byteValue == that.byteValue && shortValue == that.shortValue && intValue == that.intValue && longValue == that.longValue && Float.compare(that.floatValue, floatValue) == 0 && Double.compare(that.doubleValue, doubleValue) == 0 && charValue == that.charValue && booleanValue == that.booleanValue && Objects.equals(b, that.b) && Objects.equals(s, that.s) && Objects.equals(i, that.i) && Objects.equals(l, that.l) && Objects.equals(f, that.f) && Objects.equals(d, that.d) && Objects.equals(c, that.c) && Objects.equals(bw, that.bw) && Objects.equals(str, that.str) && Objects.equals(str2, that.str2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(byteValue, shortValue, intValue, longValue, floatValue, doubleValue, charValue, booleanValue, b, s, i, l, f, d, c, bw, str, str2);
    }
}
