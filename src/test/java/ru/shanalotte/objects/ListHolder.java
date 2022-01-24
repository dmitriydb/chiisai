package ru.shanalotte.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListHolder implements Serializable {
    ArrayList<String> stringList = new ArrayList<>();
    {
        stringList.add("1");
        stringList.add("2");
        stringList.add("3");
    }

    @Override
    public String toString() {
        return "ListHolder{" +
                "stringList=" + stringList +
                '}';
    }
}
