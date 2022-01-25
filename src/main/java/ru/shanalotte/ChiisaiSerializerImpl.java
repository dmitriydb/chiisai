package ru.shanalotte;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static ru.shanalotte.Chiisai.*;

public class ChiisaiSerializerImpl implements ChiisaiSerializer {

    private BitSet bits = new BitSet();
    private int nextPosition;
    private int nextCachedObjectNumber = 0;
    private static final Logger logger = LoggerFactory.getLogger(ChiisaiSerializerImpl.class);
    private IdentityHashMap<Object, Integer> cachedObjects = new IdentityHashMap<>();
    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    private static boolean isWrapperType(Class<?> clazz) {
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
        cachedObjects = new IdentityHashMap<>();
        nextCachedObjectNumber = 0;
        this.nextPosition = 0;
        bits = new BitSet();
        serializeObject(target);
        return bits;
    }

    public void serializeObject(Object target) throws IllegalAccessException {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            //игнорируем поле, добавляемое в классы IDEA во время тестирования с покрытием
            if (field.getName().equals("__$lineHits$__")) continue;
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())){
                continue;
            }
            logger.debug("Сериализуется поле {}", field.getName());
            field.setAccessible(true);
            writeObject(field.get(target));
        }
    }

    private void writeTypeDescriptor(int descriptor) {
        logger.debug("Writing {} descriptor ({})", DescriptorType.values()[descriptor].name(), Integer.toBinaryString(descriptor));
        for (int i = DESCRIPTOR_LENGTH - 1; i >= 0; i--) {
            if ((descriptor % 2) == 1) {
                bits.set(nextPosition + i);
            }
            descriptor = descriptor / 2;
        }
        nextPosition += DESCRIPTOR_LENGTH;
    }

    /**
     * Сериализует число типа long
     * Перед битами значения числа записывает 1 дополнительный бит знака (1 - если число отрицательное)
     * Должно учитываться во время десериализации
     *
     * @param value
     */
    private void writeLongValue(long value) {
        long oldValue = value;
        logger.debug("value {}", value);
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
        logger.debug("Записано число {} ({})", oldValue, Long.toBinaryString(oldValue));
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
        logger.debug("Длина содержимого = {} бит ({})", length, Integer.toBinaryString(length));
        for (int i = VALUE_LENGTH; i >= 0; i--) {
            if (length % 2 == 1) {
                bits.set(nextPosition + i - 1);
            }
            length /= 2;
        }
        nextPosition += VALUE_LENGTH;
    }

    private void writeObjectLength(int length) {
        logger.debug("Длина объекта {} бит ({})", length, Integer.toBinaryString(length));
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
        String valueString = Long.toBinaryString(Double.doubleToRawLongBits(value));
        writeLength(valueString.length());
        logger.debug("Writing double value {} ({})", value, valueString);
        for (char c : valueString.toCharArray()) {
            if (c == '1')
                bits.set(nextPosition);
            nextPosition++;
        }
    }

    public void writeObject(Object value) {
        int descriptor = 0;
        if (value == null) {
            writeTypeDescriptor(DescriptorType.NULL.ordinal());
            return;
        }
        if (isWrapperType(value.getClass())) {
            descriptor = Arrays.stream(DescriptorType.values()).filter(v -> v.getWrapperTypeName().equals(value.getClass().getName())).findFirst().orElse(null).ordinal();
        } else if (value instanceof String) {
            descriptor = DescriptorType.STRING.ordinal();
        } else if (value.getClass().isArray()) {
            descriptor = DescriptorType.ARRAY.ordinal();
        } else {
            try {
                descriptor = DescriptorType.OBJECT.ordinal();
                if (!cachedObjects.containsKey(value)){
                    logger.debug("Такой объект еще не был сериализован, добавляем на него ссылку с номером {}", nextCachedObjectNumber);
                    cachedObjects.put(value, nextCachedObjectNumber++);
                }
                else {
                    logger.debug("Такой объект уже был сериализован, номер ссылки = {}", cachedObjects.get(value));
                    writeTypeDescriptor(DescriptorType.CACHED_OBJECT_REFENCE.ordinal());
                    writeCachedObjectReference(cachedObjects.get(value));
                    return;
                }
                writeTypeDescriptor(descriptor);
                int oldPosition = nextPosition;
                BitSet oldBitSet = bits;
                bits = new BitSet();
                serializeObject(value);
                int newPosition = nextPosition;
                logger.debug("Битов объекта записано {}", (newPosition - oldPosition));
                int objectLen = newPosition - oldPosition;
                BitSet writedBits = bits;
                bits = oldBitSet;
                nextPosition = oldPosition;
                writeObjectLength(objectLen);
                for (int i = oldPosition; i < newPosition; i++) {
                    if (writedBits.get(i))
                        bits.set(nextPosition);
                    nextPosition++;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return;
        }
        writeTypeDescriptor(descriptor);
        if (descriptor == DescriptorType.ARRAY.ordinal()) {
            writeArray(value);
        } else if (descriptor == DescriptorType.STRING.ordinal()) {
            writeStringValue(value);
        } else
            writePrimitiveValue(value);
    }

    private void writeArray(Object value) {
        Object[] array = unpackArray(value);
        writeArrayLength(array.length);
        for (Object v : array) {
            writeObject(v);
        }
    }

    private void writeArrayLength(int length) {
        logger.debug("Пишется массив длиной {} ({})", length, Integer.toBinaryString(length));
        for (int i = ARRAY_SIZE_LENGTH; i >= 0; i--) {
            if (length % 2 == 1) {
                bits.set(nextPosition + i - 1);
            }
            length /= 2;
        }
        nextPosition += ARRAY_SIZE_LENGTH;
    }

    private void writeCachedObjectReference(int ref) {
        logger.debug("Номер ссылки на объект {} ({})", ref, Integer.toBinaryString(ref));
        for (int i = CACHED_OBJECT_REFERENCE_LENGTH; i >= 0; i--) {
            if (ref % 2 == 1) {
                bits.set(nextPosition + i - 1);
            }
            ref /= 2;
        }
        nextPosition += CACHED_OBJECT_REFERENCE_LENGTH;
    }

    private void writeStringValue(Object stringValue) {
        String bitsString = BinaryUtil.convertStringToBinary((String) stringValue);
        writeLength(bitsString.length());
        logger.debug("Записываем строку {} ({})", stringValue, bitsString);
        writeBitsString(bitsString);
    }

    private void writeBitsString(String bitsString) {
        for (char c : bitsString.toCharArray()) {
            if (c == '1')
                bits.set(nextPosition);
            nextPosition++;
        }
    }

    /**
     * Распаковывает объект типа Array в массив Object[]
     *
     * @param value
     * @return
     */
    private static Object[] unpackArray(final Object value) {
        if (value == null) return null;
        if (value.getClass().isArray()) {
            if (value instanceof Object[]) {
                return (Object[]) value;
            } else // box primitive arrays
            {
                final Object[] boxedArray = new Object[Array.getLength(value)];
                for (int index = 0; index < boxedArray.length; index++) {
                    boxedArray[index] = Array.get(value, index); // automatic boxing
                }
                return boxedArray;
            }
        } else throw new IllegalArgumentException("Not an array");
    }

}
