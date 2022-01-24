package ru.shanalotte.stringtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shanalotte.Chiisai;
import ru.shanalotte.nulltest.NullHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringTest {
    private Chiisai chiisai = new Chiisai();
    private List<Byte> result = new ArrayList<>();



    @BeforeEach
    public void setUp(){
        chiisai = new Chiisai();
    }

    @Test
    public void stringsTest(){
        StringHolder target = new StringHolder();
        StringHolder unshrinked = (StringHolder) chiisai.shrink(target).andUnshrink();

        System.out.println(target);
        System.out.println(unshrinked);
        assertEquals(target, unshrinked);
    }
}