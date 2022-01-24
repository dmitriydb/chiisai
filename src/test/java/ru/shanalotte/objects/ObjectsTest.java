package ru.shanalotte.objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shanalotte.Chiisai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectsTest {
    private Chiisai chiisai = new Chiisai();
    private List<Byte> result = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        chiisai = new Chiisai();
    }

    @Test
    public void arraysTest(){
        ArrayHolder target = new ArrayHolder();
        ArrayHolder unshrinked = (ArrayHolder) chiisai.shrink(target).andUnshrink();
        assertEquals(target, unshrinked);
    }

    @Test
    public void nesterObjectsTest(){
        OuterObject target = new OuterObject();
        OuterObject unshrinked = (OuterObject) chiisai.shrink(target).andUnshrink();
        assertEquals(target, unshrinked);
    }

    @Test
    public void threeNestedObjects(){
        A target = new A();
        A unshrinked = (A) chiisai.shrink(target).andUnshrink();
        assertEquals(target, unshrinked);
        System.out.println(target);
        System.out.println(unshrinked);
    }

    @Test
    public void listSerialization(){
        ListHolder listHolder = new ListHolder();
        ListHolder unshrinked = (ListHolder) chiisai.shrink(listHolder).andUnshrink();
        System.out.println(listHolder);
        System.out.println(unshrinked);
    }
}