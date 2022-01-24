package ru.shanalotte;

import java.util.BitSet;

/**
 * Вспомогательный класс для работы с java.util.BitSet
 */
public class BitSetUtil {

    /**
     * Возвращает BitSet в виде символьной строки
     * @param bitSet
     */
    public static String printBitSet(BitSet bitSet){
        String result = "";
        for (int i = 0; i < bitSet.length(); i++){
            if (bitSet.get(i))
                result = result + "1";
            else
                result = result + "0";
        }
        return result;
    }


}
