package ru.shanalotte;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;
import java.util.List;

/**
 * Класс-фасад, предоставляющий удобный API сериализации/десериализации
 */
public class Chiisai {

    private static final Logger logger = LoggerFactory.getLogger(Chiisai.class);

    /**
     * Длина дескриптора типа поля в битах
     */
    static final int DESCRIPTOR_LENGTH = 4;

    /**
     * Длина значения поля в битах
     */
    static final int VALUE_LENGTH = 8;

    /**
     * Длина массива
     */
    static final int ARRAY_SIZE_LENGTH = 16;

    /**
     * Длина содержимого объекта в битах
     */
    static final int OBJECT_VALUE_LENGTH = 16;

    /**
     * Размер ссылки на уже сериализованный объект
     */
    static final int CACHED_OBJECT_REFERENCE_LENGTH = 8;

    private Class targetClass;
    private ChiisaiSerializer serializer;
    private ChiisaiDeserializer deserializer;
    private BitSet bits = new BitSet();

    /**
     * Внутренний класс для предоставления API
     */
    public class ChiisaiBox{

        /**
         * Этот класс не нужно создавать вне данного пакета
         */
        ChiisaiBox(){

        }

        public byte[] toBytes(){
           return bits.toByteArray();
        }

        public Object andUnshrink() {
            return unshrink();
        }

        private Object unshrink() {
            return deserializer.deserialize(bits, targetClass);
        }

        public void andWriteTo(OutputStream out) throws IOException {
                out.write(bits.toByteArray());
           }

    }

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

    public ChiisaiBox shrink(Object target){
        this.targetClass = target.getClass();
        try {
            bits = serializer.serialize(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ChiisaiBox();
    }

    public Object readAndUnshrink(InputStream in, Class clazz) throws IOException {
        this.targetClass = clazz;
        bits = BitSet.valueOf(BinaryUtil.readBytesFromInputStream(in));
        return deserializer.deserialize(bits, targetClass);
        }


    }




