package ru.shanalotte.objects;

import java.time.LocalDateTime;
import java.util.Arrays;

public class ComplexArrayHolder {

    private Object[][] arr = {{1,2,3},{6,5,4}};

    @Override
    public String toString() {
        return "ComplexArrayHolder{" +
                "arr=" + Arrays.deepToString(arr) +
                '}';
    }
}
