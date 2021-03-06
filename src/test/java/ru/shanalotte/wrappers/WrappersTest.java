package ru.shanalotte.wrappers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shanalotte.Chiisai;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WrappersTest {
    private Chiisai chiisai = new Chiisai();
    private List<Byte> result = new ArrayList<>();



    @BeforeEach
    public void setUp(){
        chiisai = new Chiisai();
    }


    @Test
    public void boxedValuesTest(){
        BoxedValuesHolder target = new BoxedValuesHolder();
        BoxedValuesHolder unshrinked = (BoxedValuesHolder) chiisai.shrink(target).andUnshrink();
        assertEquals(target, unshrinked);
    }

}