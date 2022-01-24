package ru.shanalotte;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.BitSet;
import static ru.shanalotte.Chiisai.*;
import static ru.shanalotte.PrimitiveTypeDescriptor.*;

public class ChiisaiDeserializerImpl implements ChiisaiDeserializer{
    private int nextPosition;
    private BitSet bits;
     private static final Logger logger = LoggerFactory.getLogger(ChiisaiDeserializerImpl.class);

    @Override
    public Object deserialize(BitSet bits, Class targetClass) {
        try {
            this.bits = bits;
            Object result = targetClass.newInstance();
            for (Field field : targetClass.getDeclaredFields()){
                field.setAccessible(true);
                Object fieldValue = readNextField();
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

    private Object readNextField() {
        //читаем тип поля
        PrimitiveTypeDescriptor descriptor = readDescriptor();
        logger.debug("Прочитали дескриптор {}", descriptor.toString());
        //читаем длину поля
        if (descriptor == PrimitiveTypeDescriptor.BOOLEAN){
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
        return binaryStringToSignedNumber(valueLine);
        //считываем значение поля
    }


    private PrimitiveTypeDescriptor readDescriptor() {
        String bitsLine = readNextNBits(4);
        int descriptor = (int)binaryStringToNumber(bitsLine);
        return PrimitiveTypeDescriptor.values()[descriptor];
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
}
