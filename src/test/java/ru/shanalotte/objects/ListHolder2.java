package ru.shanalotte.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListHolder2 {

    List<Integer> l = new ArrayList<>();

    public void initList(){
        l.add(1);
        l.add(2);
        l.add(3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListHolder2 that = (ListHolder2) o;
        return Objects.equals(l, that.l);
    }

    @Override
    public int hashCode() {
        return Objects.hash(l);
    }

    @Override
    public String toString() {
        return "ListHolder2{" +
                "l=" + l +
                '}';
    }
}
