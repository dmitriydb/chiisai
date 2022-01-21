package ru.shanalotte;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import static ru.shanalotte.PrimitiveTypeDescriptors.*;

public class Chiisai {

    private int nextPosition;
    private BitSet bits = new BitSet();
    private Object target;
    private List<Byte> listByteHolder;
    private Class targetClass;


    public Chiisai shrink(Object target){
        Chiisai result = new Chiisai();
        try {
            result.convertTargetToBits(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        BitSetUtil.printBitSet(result.bits);
        System.out.println(result.bits);
        return result;
    }

    private void convertTargetToBits(Object target) throws IllegalAccessException {
        Field[] fields = target.getClass().getDeclaredFields();
        BitSet result = new BitSet();
        for (Field field : fields){
            writeFieldIntoBits(field, bits, target);
        }


    }

    private void writeTypeDescriptorIntoBits(BitSet bits, int descriptor){
        System.out.println(descriptor);
        for (int i = 3; i >= 0; i--) {
            if ((descriptor % 2) == 1){
                bits.set(nextPosition + i);
                System.out.println("[D] Setting pos #" + (nextPosition + i));
            }
            descriptor = descriptor / 2;
        }
        nextPosition += 4;
    }

    private void writeDecimalIntoBits(BitSet bits, long number){
        System.out.println("Writing value " + Long.toBinaryString(number));
        int len = 0;
        List<Integer> positions = new ArrayList<>();
        do {
            long bit = (number % 2);
            if (bit == 1) {
                positions.add(len);
            }
            number /= 2;
            len++;
        }
        while (number != 0);
        writePrimitiveTypeLengthIntoBits(bits, len);
        for (int position : positions){
            int pos = nextPosition + len - 1 - position;
            bits.set(pos);
            System.out.println("Setting pos #" + (pos));
        }
        nextPosition += len;
    }

    private void writePrimitiveTypeLengthIntoBits(BitSet bits, int len) {
        System.out.println("Writing len" + Integer.toBinaryString(len));
        for (int i = 6; i >= 0; i--) {
            if (len % 2 == 1){
                bits.set(nextPosition + i - 1 );
                System.out.println("Setting pos #" + (nextPosition + i - 1));
            }
            len /= 2;
        }
        nextPosition += 6;
        System.out.println("==LEN DONE");
    }

    private void writePrimitiveValueIntoBits(BitSet bits, Object value, String type){
        System.out.println(type);
        if (value instanceof Number){
            Number number = (Number)value;
            long longValue = 0L;
            if (value instanceof Integer){
                longValue = number.intValue();
                writeDecimalIntoBits(bits, longValue);
            }
            if (value instanceof Byte){
                longValue = number.byteValue();
                writeDecimalIntoBits(bits, longValue);
            }
            if (value instanceof Long){
                longValue = number.longValue();
                writeDecimalIntoBits(bits, longValue);
            }
            if (value instanceof Short){
                longValue = number.shortValue();
                writeDecimalIntoBits(bits, longValue);
            }
        }


    }

    private void writeFieldIntoBits(Field field, BitSet bits, Object target) throws IllegalAccessException {
        if (field.getType().isPrimitive()){
            //записать тип
            int descriptor = Arrays.stream(PrimitiveTypeDescriptors.values()).filter(value -> value.getPrimitiveTypeName().equals(field.getType().getTypeName())).findFirst().orElse(null).ordinal();
            writeTypeDescriptorIntoBits(bits, descriptor);
            //записать длину значения
            writePrimitiveValueIntoBits(bits, field.get(target), field.getGenericType().getTypeName());
            //записать значение
        }

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

        byte[] bytes = new byte[listByteHolder.size()];

        for (int i = 0; i < listByteHolder.size(); i++)
            bytes[i] = listByteHolder.get(i);

        BitSet bits = BitSet.valueOf(bytes);

        //TODO записать все поля из битсета

        try {
            Object unshrinkedObject = targetClass.newInstance();
            Field[] fields = targetClass.getDeclaredFields();
            for (Field field : fields){
                //field.set(unshrinkedObject, value);
            }
            return unshrinkedObject;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
