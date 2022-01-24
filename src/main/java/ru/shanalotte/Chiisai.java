package ru.shanalotte;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

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

    private void writeLongValueIntoBits(BitSet bits, long number){
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
        for (int i = 8; i >= 0; i--) {
            if (len % 2 == 1){
                bits.set(nextPosition + i - 1 );
                System.out.println("Setting pos #" + (nextPosition + i - 1));
            }
            len /= 2;
        }
        nextPosition += 8;
        System.out.println("==LEN DONE");
    }

    private void writePrimitiveValueIntoBits(BitSet bits, Object value, String type){
        System.out.println(type);
        if (value instanceof Number){
            Number number = (Number)value;
            long longValue = 0L;
            if (value instanceof Integer){
                longValue = number.intValue();
                writeLongValueIntoBits(bits, longValue);
            }
            if (value instanceof Byte){
                longValue = number.byteValue();
                writeLongValueIntoBits(bits, longValue);
            }
            if (value instanceof Long){
                longValue = number.longValue();
                writeLongValueIntoBits(bits, longValue);
            }
            if (value instanceof Short){
                longValue = number.shortValue();
                writeLongValueIntoBits(bits, longValue);
            }
            if (value instanceof Double || value instanceof Float){
                Double doubleValue = number.doubleValue();
                writeDoubleValueIntoBits(bits, doubleValue);
            }
        }
        if (value instanceof Character){
            Character ch = (Character) value;
            long longValue = ch.charValue();
            writeLongValueIntoBits(bits, longValue);
        }
        if (value instanceof Boolean){
            Boolean booleanValue = (Boolean) value;
            if (booleanValue.booleanValue())
                bits.set(nextPosition);
        }

    }

    private void writeDoubleValueIntoBits(BitSet bits, Double doubleValue) {
        String value = Long.toBinaryString(Double.doubleToRawLongBits(doubleValue));
        int length = value.length();
        value = value.replace("0", " ");
        value = value.trim();
        value = value.replace(" ", "0");

        System.out.println("Writing " + value + " into bits");
        writePrimitiveTypeLengthIntoBits(bits, length);
        for (char c : value.toCharArray()){
            if (c == '1')
                bits.set(nextPosition);
            nextPosition++;
        }
        System.out.println(binStrToDbl(value));
    }

    static double binStrToDbl(String myBinStr)
    {
        double myDbl = 0.0;
        String myRegex = "[01]*";
        if(myBinStr == null || myBinStr == "" ||
                myBinStr.length() > 64 || !myBinStr.matches(myRegex))
        {
            // throw an error
        }
        if (myBinStr.length() == 64)
        {
            if (myBinStr.charAt(0) == '1')
            {
                String negBinStr = myBinStr.substring(1);
                myDbl = -1 * Double.longBitsToDouble(Long.parseLong(negBinStr, 2));
            }
            else if (myBinStr.charAt(0) == '0')
            {
                myDbl = Double.longBitsToDouble(Long.parseLong(myBinStr, 2));
            }
        }
        else if(myBinStr.length() < 64)
        {
            myDbl = Double.longBitsToDouble(Long.parseLong(myBinStr, 2));
        }

        return myDbl;
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
        System.out.println(bits);
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
