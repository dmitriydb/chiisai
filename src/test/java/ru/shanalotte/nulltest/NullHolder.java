package ru.shanalotte.nulltest;

public class NullHolder {

    Boolean value = null;
    int intvalue = 45;

    @Override
    public String toString() {
        return "NullHolder{" +
                "value=" + value +
                ", intvalue=" + intvalue +
                '}';
    }
}
