package ru.shanalotte;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.BitSet;
import static ru.shanalotte.Chiisai.*;
import static ru.shanalotte.DescriptorType.*;

public class ChiisaiDeserializerImpl implements ChiisaiDeserializer{
    private int nextPosition;
    private BitSet bits;
     private static final Logger logger = LoggerFactory.getLogger(ChiisaiDeserializerImpl.class);

    @Override
    public Object deserialize(BitSet bits, Class targetClass) {
        logger.debug("Десериализуем {}", targetClass);
        this.nextPosition = 0;
        try {
            this.bits = bits;
            Object result = targetClass.newInstance();
            for (Field field : targetClass.getDeclaredFields()){
                logger.debug(field.getName());
                if (field.getName().contains("__$lineHits$__")) continue;
                logger.debug("Field name = {}", field.getName());
                field.setAccessible(true);
                Object fieldValue = readNextField(field);
                if (fieldValue != null && fieldValue.getClass().isArray()){
                    Object arr = Array.newInstance(field.getType().getComponentType(), Array.getLength(fieldValue));
                    for (int i = 0; i < Array.getLength(fieldValue); i++)
                        Array.set(arr, i, Array.get(fieldValue, i));
                    field.set(result, arr);
                }
                else
                field.set(result, fieldValue);
            }
            return result;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object readNextField(Field field) {
        //читаем тип поля
        DescriptorType descriptor = readDescriptor();
        logger.debug("Прочитали дескриптор {}", descriptor.toString());
        //читаем длину поля

        if (descriptor == NULL){
            return null;
        }
        if (descriptor == OBJECT){
            BitSet oldbits = bits;
            BitSet newbits = new BitSet();
            String bitsLine = readNextNBits(OBJECT_VALUE_LENGTH);
            int objectDataLength = (int)binaryStringToNumber(bitsLine);
            for (int i = 0; i < objectDataLength; i++){
                if (bits.get(nextPosition + i))
                    newbits.set(i);
            }
            nextPosition += objectDataLength;
            int oldPosition = nextPosition;
            nextPosition = 0;
            bits = newbits;
            Object result = null;
            try {
                result = deserialize(bits, Class.forName(field.getType().getCanonicalName()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            bits = oldbits;
            nextPosition = oldPosition;
            return result;
        }
        if (descriptor == ARRAY){
            String bitsLine = readNextNBits(ARRAY_VALUE_LENGTH);
            int arrayLength = (int)binaryStringToNumber(bitsLine);
            Object[] resultArray = new Object[arrayLength];
            for (int i = 0; i < arrayLength; i++){
                 resultArray[i] = readNextField(field);
            }
            return unpackArray(resultArray);
        }

        if (descriptor == STRING){
            logger.debug("Это строка");
            String bitsLine = readNextNBits(VALUE_LENGTH);
            int len = (int)binaryStringToNumber(bitsLine);
            logger.debug("Длина поля равна {} бит", len);
            String valueLine = readNextNBits(len);
            String result = BinaryUtil.binaryToText(valueLine);
            logger.debug("Значение строки [{}]", result);
            return result;
        }

        if (descriptor == BOOLEAN){
            logger.debug("Это boolean");
            String bitsLine = readNextNBits(1);
            return bitsLine.equals("1");
        }
        String bitsLine = readNextNBits(VALUE_LENGTH);
        int len = (int)binaryStringToNumber(bitsLine);
        logger.debug("Длина поля равна {} бит", len);
        String valueLine = readNextNBits(len);

        if (descriptor == FLOAT){
            return (float)BinaryUtil.binaryStringToDouble(valueLine);
        }
        if (descriptor == DOUBLE){
            return BinaryUtil.binaryStringToDouble(valueLine);
        }
        if (descriptor == INT){
            return (int)binaryStringToSignedNumber(valueLine);
        }
        if (descriptor == SHORT){
            return (short)binaryStringToSignedNumber(valueLine);
        }
        if (descriptor == BYTE){
            return (byte)binaryStringToSignedNumber(valueLine);
        }
        if (descriptor == CHAR){
            return (char)binaryStringToSignedNumber(valueLine);
        }
        if (descriptor == LONG){
            return binaryStringToSignedNumber(valueLine);
        }
        return null;
        //считываем значение поля
    }


    private DescriptorType readDescriptor() {
        String bitsLine = readNextNBits(DESCRIPTOR_LENGTH);
        int descriptor = (int)binaryStringToNumber(bitsLine);
        return DescriptorType.values()[descriptor];
    }

    private long binaryStringToNumber(String bitsLine){
        long result =  Integer.parseInt(bitsLine, 2);
        logger.debug("Значение [{}] ", result);
        return result;
    }

    private long binaryStringToSignedNumber(String bitsLine){
        boolean isNegative = bitsLine.charAt(0) == '1';
        long result =  Long.parseLong(bitsLine.substring(1, bitsLine.length()), 2);

        if (bitsLine.substring(1, bitsLine.length()).length() == 64){
            result = Long.MIN_VALUE;
        }
        else
        if (isNegative) result = -result;
        logger.debug("Значение [{}] ", result);
        return result;
    }

    private String readNextNBits(int n) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++){
            if (bits.get(nextPosition + i))
                result.append("1");
            else
                result.append("0");
        }
        nextPosition += n;
        logger.debug("Считали [{}]", result.toString());
        return result.toString();
    }

    public static Object[] unpackArray(final Object value)
    {
        if(value == null) return null;
        if(value.getClass().isArray())
        {
            if(value instanceof Object[])
            {
                return (Object[])value;
            }
            else // box primitive arrays
            {
                final Object[] boxedArray = new Object[Array.getLength(value)];
                for(int index=0;index<boxedArray.length;index++)
                {
                    boxedArray[index] = Array.get(value, index); // automatic boxing
                }
                return boxedArray;
            }
        }
        else throw new IllegalArgumentException("Not an array");
    }

}
