package ru.shanalotte;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmptyClass{

}

public class SerializationTest {
    private Chiisai chiisai = new Chiisai();
    private List<Byte> result = new ArrayList<>();

    @Before
    public void setUp(){
        chiisai = new Chiisai();
    }



    @Test
    public void emptyClassShouldSerializeToNothing(){


        assertTrue( chiisai.shrink(new EmptyClass()).toBytes().length == 0);
    }

    @Test
    public void singleIntHolder(){
        SingleIntHolder target = new SingleIntHolder();
        SingleIntHolder unshrinked = (SingleIntHolder) chiisai.shrink(target).andUnshrink();
        assertEquals(unshrinked.value, 4);
    }

    @Test
    public void singleIntHolderSequel(){
        SingleIntHolder2 target = new SingleIntHolder2();
        target.value = 333;
        SingleIntHolder2 unshrinked = (SingleIntHolder2) chiisai.shrink(target).andUnshrink();
        assertEquals(unshrinked.value, 333);
    }

    @Test
    public void allPrimitivesAreSavedCorrectly(){
        SomePrimitiveValuesHolder target = new SomePrimitiveValuesHolder();
        chiisai.shrink(target).toBytes();

    }

    @Test
    public void tryingToUnshrinkTheClassWithAllPrimitivesSet(){
        SomePrimitiveValuesHolder target = new SomePrimitiveValuesHolder();
        chiisai.shrink(target).toBytes();
        SomePrimitiveValuesHolder unshrinked = (SomePrimitiveValuesHolder) chiisai.shrink(target).andUnshrink();
    }

    @Test
    public void allTogetherTest(){
        AllTogetherHolder holder = new AllTogetherHolder();
        AllTogetherHolder unshrinked = (AllTogetherHolder) chiisai.shrink(holder).andUnshrink();
        assertTrue(holder.equals(unshrinked));
    }
}
