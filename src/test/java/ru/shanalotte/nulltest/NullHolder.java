package ru.shanalotte.nulltest;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.mockito.internal.matchers.Null;

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
