package ru.shanalotte.caching;

import org.junit.jupiter.api.Test;
import ru.shanalotte.Chiisai;
import ru.shanalotte.iotest.TestClass;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CachingTest {

    @Test
    public void testInputOutput() throws IOException {
        ClassWithTheSameReferences testClass = new ClassWithTheSameReferences();
        ClassWithTheSameReferences unshrinked = (ClassWithTheSameReferences) new Chiisai().shrink(testClass).andUnshrink();
        System.out.println(testClass);
        System.out.println(unshrinked);
    }
}
