package ru.shanalotte.nulltest;

import org.junit.jupiter.api.Test;
import ru.shanalotte.Chiisai;
import ru.shanalotte.wrappers.BoxedValuesHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NullTest {
    private Chiisai chiisai = new Chiisai();
    private List<Byte> result = new ArrayList<>();
    @Test
    public void nullTest(){
        NullHolder target = new NullHolder();
        NullHolder unshrinked = (NullHolder) chiisai.shrink(target).andUnshrink();

        assertEquals(unshrinked.value, null);
        assertEquals(unshrinked.intvalue, target.intvalue);
        System.out.println(target);
        System.out.println(unshrinked);
    }
}