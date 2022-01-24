package ru.shanalotte;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class Chiisai {

     private static final Logger logger = LoggerFactory.getLogger(Chiisai.class);

    public static final int DESCRIPTOR_LENGTH = 4;
    public static final int VALUE_LENGTH = 8;
    public static final int ARRAY_VALUE_LENGTH = 16;
    public static final int OBJECT_VALUE_LENGTH = 16;
    public static final int CACHED_OBJECT_REFERENCE_LENGTH = 8;

    private Object target;
    private List<Byte> listByteHolder;
    private Class targetClass;
    private ChiisaiSerializer serializer;
    private ChiisaiDeserializer deserializer;
    private BitSet bits = new BitSet();

    public Chiisai() {
        this.serializer = new ChiisaiSerializerImpl();
        this.deserializer = new ChiisaiDeserializerImpl();
    }

    public void setSerializer(ChiisaiSerializer serializer) {
        this.serializer = serializer;
    }

    public void setDeserializer(ChiisaiDeserializer deserializer) {
        this.deserializer = deserializer;
    }

    public Chiisai shrink(Object target){
        this.targetClass = target.getClass();
        try {
            bits = serializer.serialize(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void andStoreIn(List<Byte> result){
        for (byte b : bits.toByteArray()){
            result.add(b);
        }
    }


    public Chiisai from(List<Byte> bytes){
        this.listByteHolder = bytes;
        return this;
    }

    public Chiisai asClass(Class clazz){
        this.targetClass = clazz;
        return this;
    }

    public Object unshrink() {
        if (listByteHolder != null){
            byte[] bytes = new byte[listByteHolder.size()];
            for (int i = 0; i < listByteHolder.size(); i++)
                bytes[i] = listByteHolder.get(i);
            bits = BitSet.valueOf(bytes);
        }
        BitSetUtil.printBitSet(bits);
        //TODO записать все поля из битсета
        return deserializer.deserialize(bits, targetClass);
    }

    public Object andUnshrink() {
        return unshrink();
    }


}
