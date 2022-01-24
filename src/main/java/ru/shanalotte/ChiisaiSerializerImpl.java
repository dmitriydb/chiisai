package ru.shanalotte;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

import static ru.shanalotte.Chiisai.*;

public class ChiisaiSerializerImpl implements ChiisaiSerializer {

    private BitSet bits = new BitSet();
    private int nextPosition;
    private static final Logger logger = LoggerFactory.getLogger(ChiisaiSerializerImpl.class);

    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    public static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        return ret;
    }

    @Override
    public BitSet serialize(Object target) throws IllegalAccessException {
        this.nextPosition = 0;
        bits = new BitSet();
        serializeObject(target);
        return bits;
    }

    public void serializeObject(Object target) throws IllegalAccessException {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("__$lineHits$__")) continue;
            field.setAccessible(true);
            writeObject(field.get(target));
        }
    }

    private void writeTypeDescriptor(int descriptor) {
        logger.debug("Writing descriptor {}", descriptor);

        for (int i = DESCRIPTOR_LENGTH - 1; i >= 0; i--) {
            if ((descriptor % 2) == 1) {
                bits.set(nextPosition + i);
            }
            descriptor = descriptor / 2;
        }
        nextPosition += DESCRIPTOR_LENGTH;
    }

    private void writeLongValue(long value) {
        logger.debug("Writing long value {}", value);

        boolean isNegative = false;
        if (value < 0) {
            isNegative = true;
            value = -value;
        }
        int len = 0;
        List<Integer> positions = new ArrayList<>();
        do {
            long bit = (value % 2);
            if (bit == 1) {
                positions.add(len);
            }
            value /= 2;
            len++;
        }
        while (value != 0);
        writeLength(len + 1);
        if (isNegative) {
            bits.set(nextPosition);
        }
        nextPosition++;
        for (int position : positions) {
            int pos = nextPosition + len - 1 - position;
            bits.set(pos);
        }
        nextPosition += len;
    }

    private void writeLength(int length) {
        logger.debug("Writing length {}", length);
        for (int i = VALUE_LENGTH; i >= 0; i--) {
            if (length % 2 == 1) {
                bits.set(nextPosition + i - 1);
            }
            length /= 2;
        }
        nextPosition += VALUE_LENGTH;
    }

    private void writeObjectLength(int length) {
        logger.debug("Writing object length {}", length);
        for (int i = OBJECT_VALUE_LENGTH; i >= 0; i--) {
            if (length % 2 == 1) {
                bits.set(nextPosition + i - 1);
            }
            length /= 2;
        }
        nextPosition += OBJECT_VALUE_LENGTH;
    }

    private void writePrimitiveValue(Object value) {
        if (value instanceof Number) {
            Number number = (Number) value;
            long longValue = 0L;
            if (value instanceof Integer) {
                longValue = number.intValue();
                writeLongValue(longValue);
            }
            if (value instanceof Byte) {
                longValue = number.byteValue();
                writeLongValue(longValue);
            }
            if (value instanceof Long) {
                longValue = number.longValue();
                writeLongValue(longValue);
            }
            if (value instanceof Short) {
                longValue = number.shortValue();
                writeLongValue(longValue);
            }
            if (value instanceof Double || value instanceof Float) {
                Double doubleValue = number.doubleValue();
                writeDoubleValue(doubleValue);
            }
        }
        if (value instanceof Character) {
            long longValue = (Character) value;
            writeLongValue(longValue);
        }
        if (value instanceof Boolean) {
            Boolean booleanValue = (Boolean) value;
            if (booleanValue)
                bits.set(nextPosition);
            nextPosition++;
        }

    }

    private void writeDoubleValue(Double value) {
        logger.debug("Writing double value {}", value);
        String valueString = Long.toBinaryString(Double.doubleToRawLongBits(value));
        logger.debug("Записываем {}", valueString);
        writeLength(valueString.length());
        for (char c : valueString.toCharArray()) {
            if (c == '1')
                bits.set(nextPosition);
            nextPosition++;
        }
    }

    public void writeObject(Object value) {
        int descriptor = 0;
        if (value == null){
            writeTypeDescriptor(DescriptorType.NULL.ordinal());
            return;
        }
        if (isWrapperType(value.getClass())) {
            descriptor = Arrays.stream(DescriptorType.values()).filter(v -> v.getWrapperTypeName().equals(value.getClass().getName())).findFirst().orElse(null).ordinal();
        } else if (value instanceof String) {
            descriptor = DescriptorType.STRING.ordinal();
        } else if (value.getClass().isArray()) {
            descriptor = DescriptorType.ARRAY.ordinal();
        }
        else
        {
            try {
                descriptor = DescriptorType.OBJECT.ordinal();
                writeTypeDescriptor(descriptor);

                int oldPosition = nextPosition;
                BitSet oldBitSet = bits;
                bits = new BitSet();
                serializeObject(value);
                int newPosition = nextPosition;
                logger.debug("Object bits writed = {}", (newPosition - oldPosition));
                int objectLen = newPosition - oldPosition;

                BitSet writedBits = bits;
                bits = oldBitSet;
                nextPosition = oldPosition;
                writeObjectLength(objectLen);
                for (int i = oldPosition; i < newPosition; i++){
                    if (writedBits.get(i))
                        bits.set(nextPosition);
                    nextPosition++;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return;
        }
        logger.debug("Descriptor {}", descriptor);
        writeTypeDescriptor(descriptor);
        //записать длину значения
        //записать значение
        if (descriptor == DescriptorType.ARRAY.ordinal()){
            writeArray(value);
        }
        else
        if (descriptor == DescriptorType.STRING.ordinal()) {
            writeStringValue(value);
        } else
            writePrimitiveValue(value);
    }

    private void writeArray(Object value) {
        Object[] array = unpackArray(value);
        writeArrayLength(array.length);
        for (Object v : array){
            writeObject(v);
        }
    }

    private void writeArrayLength(int length) {
        logger.debug("Writing length {}", length);
        for (int i = ARRAY_VALUE_LENGTH; i >= 0; i--) {
            if (length % 2 == 1) {
                bits.set(nextPosition + i - 1);
            }
            length /= 2;
        }
        nextPosition += ARRAY_VALUE_LENGTH;
    }

    private void writeStringValue(Object stringValue) {
        String bitsString = BinaryUtil.convertStringToBinary((String) stringValue);
        writeLength(bitsString.length());
        writeBitsString(bitsString);
    }

    private void writeBitsString(String bitsString) {
        for (char c : bitsString.toCharArray()) {
            if (c == '1')
                bits.set(nextPosition);
            nextPosition++;
        }
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
