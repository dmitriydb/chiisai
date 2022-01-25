package ru.shanalotte.caching;

import ru.shanalotte.objects.B;

import java.util.List;

public class ClassWithTheSameReferences {

    private Box b1;
    private Box b2;
    private Box b3;
    private Box b4;
    private Box b5;

    {
        b1 = new Box();
        b2 = b1;
        b3 = b2;
        b2 = b3;
    }

    @Override
    public String toString() {
        return "ClassWithTheSameReferences{" +
                "b1=" + b1 +
                ", b2=" + b2 +
                ", b3=" + b3 +
                ", b4=" + b4 +
                ", b5=" + b5 +
                '}';
    }
}
