package ru.shanalotte.primitives;

import org.junit.jupiter.api.Test;
import ru.shanalotte.Chiisai;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrimitivesTest {
    private Chiisai chiisai = new Chiisai();
    private List<Byte> result = new ArrayList<>();

    @Test
    public void byteTest(){
        PrimitiveBytesHolder target = new PrimitiveBytesHolder();

        chiisai.shrink(target).andStoreIn(result);
        PrimitiveBytesHolder unshrinked = (PrimitiveBytesHolder) chiisai
                .from(result)
                .asClass(PrimitiveBytesHolder.class)
                .unshrink();
        System.out.println(target);
        System.out.println(unshrinked);
        assertEquals(target.b1, unshrinked.b1);
        assertEquals(target.b2, unshrinked.b2);
        assertEquals(target.b3, unshrinked.b3);
        assertEquals(target.b4, unshrinked.b4);
        assertEquals(target.b5, unshrinked.b5);
    }

    @Test
    public void shortTest(){
        PrimitiveShortsHolder target = new PrimitiveShortsHolder();
        PrimitiveShortsHolder unshrinked = (PrimitiveShortsHolder) chiisai.shrink(target).andUnshrink();

        System.out.println(target);
        System.out.println(unshrinked);
        assertEquals(target.b1, unshrinked.b1);
        assertEquals(target.b2, unshrinked.b2);
        assertEquals(target.b3, unshrinked.b3);
        assertEquals(target.b4, unshrinked.b4);
        assertEquals(target.b5, unshrinked.b5);
    }

    @Test
    public void intTest(){
        PrimitiveIntHolder target = new PrimitiveIntHolder();
        PrimitiveIntHolder unshrinked = (PrimitiveIntHolder) chiisai.shrink(target).andUnshrink();
        System.out.println(target);
        System.out.println(unshrinked);
        assertEquals(target.b1, unshrinked.b1);
        assertEquals(target.b2, unshrinked.b2);
        assertEquals(target.b3, unshrinked.b3);
        assertEquals(target.b4, unshrinked.b4);
        assertEquals(target.b5, unshrinked.b5);
    }

    @Test
    public void longTest(){
        PrimitiveLongHolder target = new PrimitiveLongHolder();
        PrimitiveLongHolder unshrinked = (PrimitiveLongHolder) chiisai.shrink(target).andUnshrink();
        System.out.println(target);
        System.out.println(unshrinked);
        assertEquals(target.b1, unshrinked.b1);
        assertEquals(target.b2, unshrinked.b2);
        assertEquals(target.b3, unshrinked.b3);
        assertEquals(target.b4, unshrinked.b4);
        assertEquals(target.b5, unshrinked.b5);
    }

    @Test
    public void charTest(){
        PrimitiveCharsHolder target = new PrimitiveCharsHolder();
        PrimitiveCharsHolder unshrinked = (PrimitiveCharsHolder) chiisai.shrink(target).andUnshrink();
        System.out.println(target);
        System.out.println(unshrinked);
        assertEquals(target.b1, unshrinked.b1);
        assertEquals(target.b2, unshrinked.b2);
        assertEquals(target.b3, unshrinked.b3);
        assertEquals(target.b4, unshrinked.b4);
        assertEquals(target.b5, unshrinked.b5);
    }

    @Test
    public void booleanTest(){
        PrimitiveBooleansHolder target = new PrimitiveBooleansHolder();
        PrimitiveBooleansHolder unshrinked = (PrimitiveBooleansHolder) chiisai.shrink(target).andUnshrink();
        System.out.println(target);
        System.out.println(unshrinked);
        assertEquals(target.b1, unshrinked.b1);
        assertEquals(target.b2, unshrinked.b2);
        assertEquals(target.b3, unshrinked.b3);
        assertEquals(target.b4, unshrinked.b4);
        assertEquals(target.b5, unshrinked.b5);
    }

    @Test
    public void floatTest(){
        PrimitiveFloatsHolder target = new PrimitiveFloatsHolder();
        PrimitiveFloatsHolder unshrinked = (PrimitiveFloatsHolder) chiisai.shrink(target).andUnshrink();
        System.out.println(target);
        System.out.println(unshrinked);
        assertEquals(target.b1, unshrinked.b1);
        assertEquals(target.b2, unshrinked.b2);
        assertEquals(target.b3, unshrinked.b3);
        assertEquals(target.b4, unshrinked.b4);
        assertEquals(target.b5, unshrinked.b5);
    }

    @Test
    public void doubleTest(){
        PrimitiveDoublesHolder target = new PrimitiveDoublesHolder();
        PrimitiveDoublesHolder unshrinked = (PrimitiveDoublesHolder) chiisai.shrink(target).andUnshrink();
        System.out.println(target);
        System.out.println(unshrinked);
        assertEquals(target.b1, unshrinked.b1);
        assertEquals(target.b2, unshrinked.b2);
        assertEquals(target.b3, unshrinked.b3);
        assertEquals(target.b4, unshrinked.b4);
        assertEquals(target.b5, unshrinked.b5);
        assertEquals(target.b6, unshrinked.b6);
        assertEquals(target.b7, unshrinked.b7);
    }
}
