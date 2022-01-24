package ru.shanalotte;

import java.util.BitSet;

public interface ChiisaiDeserializer {
    Object deserialize(BitSet bits, Class targetClass);
}
