package ru.shanalotte;

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

    @Test
    public void emptyClassShouldSerializeToNothing(){
        chiisai.shrink(new EmptyClass())
                .andStoreIn(result);
        assertTrue(result.size() == 0);
    }

    @Test
    public void singleIntHolder(){
        SingleIntHolder target = new SingleIntHolder();
        target.value = 5;
        chiisai.shrink(target).andStoreIn(result);

        SingleIntHolder unshrinked = (SingleIntHolder) chiisai
                .from(result)
                .asClass(SingleIntHolder.class)
                .unshrink();
        assertEquals(unshrinked.value, 5);
    }

    @Test
    public void singleIntHolderSequel(){
        SingleIntHolder2 target = new SingleIntHolder2();
        target.value = 10;
        chiisai.shrink(target).andStoreIn(result);

        SingleIntHolder2 unshrinked = (SingleIntHolder2) chiisai
                .from(result)
                .asClass(SingleIntHolder2.class)
                 .unshrink();
        assertEquals(unshrinked.value, 10);
    }

    @Test
    public void allPrimitivesAreSavedCorrectly(){
        SomePrimitiveValuesHolder target = new SomePrimitiveValuesHolder();
        chiisai.shrink(target).andStoreIn(result);

    }

    @Test
    public void tryingToUnshrinkTheClassWithAllPrimitivesSet(){
        SomePrimitiveValuesHolder target = new SomePrimitiveValuesHolder();
        chiisai.shrink(target).andStoreIn(result);
        SomePrimitiveValuesHolder unshrinked = (SomePrimitiveValuesHolder) chiisai
                .from(result)
                .asClass(SomePrimitiveValuesHolder.class)
                .unshrink();

    }

}
