package ru.shanalotte;

import java.util.BitSet;

public class BitSetUtil {

    public static void printBitSet(BitSet bitSet){
        String result = "";
        for (int i = 0; i < bitSet.length(); i++){
            if (bitSet.get(i))
                result = result + "1";
            else
                result = result + "0";
        }
        System.out.println(result);
    }
}
