package ru.shanalotte;

import java.util.BitSet;

public interface ChiisaiSerializer {
    BitSet serialize(Object target) throws IllegalAccessException;

}
