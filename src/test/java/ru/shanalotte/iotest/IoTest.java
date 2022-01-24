package ru.shanalotte.iotest;

import org.junit.jupiter.api.Test;
import ru.shanalotte.BinaryUtil;
import ru.shanalotte.Chiisai;
import ru.shanalotte.objects.ListHolder;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IoTest {

    @Test
    public void testInputOutput() throws IOException {
        TestClass testClass = new TestClass();
        FileOutputStream outputStream = new FileOutputStream(new File("D:\\test.txt"));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("D:\\javatest.txt")));
        objectOutputStream.writeObject(testClass);
        new Chiisai().shrink(testClass).andWriteTo(outputStream);
        outputStream.close();
        objectOutputStream.close();
        FileInputStream inputStream = new FileInputStream(new File("D:\\test.txt"));
        TestClass unshrinked = (TestClass) new Chiisai().readAndUnshrink(inputStream, TestClass.class);
        assertEquals(testClass, unshrinked);
    }

    @Test
    public void testSavingList() throws IOException {
        ListHolder listHolder = new ListHolder();
        FileOutputStream outputStream = new FileOutputStream(new File("D:\\listtest.txt"));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("D:\\javalisttest.txt")));
        objectOutputStream.writeObject(listHolder);
        new Chiisai().shrink(listHolder).andWriteTo(outputStream);
        outputStream.close();
        objectOutputStream.close();
    }
}
