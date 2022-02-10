package ru.shanalotte.objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shanalotte.Chiisai;

import java.util.ArrayList;
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
    public void nestedObjectsTest(){
        OuterObject target = new OuterObject();
        OuterObject unshrinked = (OuterObject) chiisai.shrink(target).andUnshrink();
        assertEquals(target, unshrinked);
    }

    @Test
    public void complexListTest(){
        ListHolder2 listHolder2 = new ListHolder2();
        listHolder2.initList();
        ListHolder2 unshrinked = (ListHolder2) chiisai.shrink(listHolder2).andUnshrink();
        assertEquals(listHolder2, unshrinked);
        System.out.println(listHolder2);
        System.out.println(unshrinked);
    }

    @Test
    public void complexArrayHolderTest(){
        ComplexArrayHolder complexArrayHolder = new ComplexArrayHolder();
        ComplexArrayHolder unshrinked = (ComplexArrayHolder) chiisai.shrink(complexArrayHolder).andUnshrink();

        System.out.println(complexArrayHolder);
        System.out.println(unshrinked);
    }

    @Test
    public void objectArrayTest(){
        ObjectArrayHolder objectArrayHolder = new ObjectArrayHolder();
        ObjectArrayHolder unshrinked = (ObjectArrayHolder) chiisai.shrink(objectArrayHolder).andUnshrink();
        System.out.println(unshrinked);
        System.out.println(objectArrayHolder);
    }

    @Test
    public void timeHolderTest(){
        LocalDateTimeHolder localDateTimeHolder = new LocalDateTimeHolder();
        LocalDateTimeHolder unshrinked = (LocalDateTimeHolder) chiisai.shrink(localDateTimeHolder).andUnshrink();
        System.out.println(unshrinked);
        System.out.println(localDateTimeHolder);
    }

    @Test
    public void inheritedFieldsAreSerialized(){
        Child c = new Child();
        c.setX(3);
        Child unshrinked = (Child) chiisai.shrink(c).andUnshrink();
        System.out.println(unshrinked.getX() + " : " + unshrinked.getY());
    }

    @Test
    public void someObjectHierarchyTest(){
       Player p = new Player();
       p.setKilled(1000);
       p.dmg = 1;
        p.hp = 1;
       p.name = "player";
        Player unshrinked = (Player) chiisai.shrink(p).andUnshrink();
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