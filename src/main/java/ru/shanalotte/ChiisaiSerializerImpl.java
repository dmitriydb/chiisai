package ru.shanalotte;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

import static ru.shanalotte.Chiisai.*;

public class ChiisaiSerializerImpl implements ChiisaiSerializer{

    private BitSet bits = new BitSet();
    private int nextPosition;
    private static final Logger logger = LoggerFactory.getLogger(ChiisaiSerializerImpl.class);

    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    public static boolean isWrapperType(Class<?> clazz)
    {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes()
    {
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

    public BitSet serialize(Object target) throws IllegalAccessException {
        bits = new BitSet();
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields){
            serializeField(field, bits, target);
        }
        return bits;
    }

    private void writeTypeDescriptor(BitSet bits, int descriptor){
        logger.debug("Writing descriptor {}", descriptor);

        for (int i = DESCRIPTOR_LENGTH - 1; i >= 0; i--) {
            if ((descriptor % 2) == 1){
                bits.set(nextPosition + i);
            }
            descriptor = descriptor / 2;
        }
        nextPosition += DESCRIPTOR_LENGTH;
    }

    private void writeLongValue(BitSet bits, long value){
        logger.debug("Writing long value {}", value);

        boolean isNegative = false;
        if (value < 0){
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
        writePrimitiveTypeLength(bits, len + 1);
        if (isNegative){
            bits.set(nextPosition);
        }
        nextPosition++;
        for (int position : positions){
            int pos = nextPosition + len - 1 - position;
            bits.set(pos);
        }
        nextPosition += len;
    }

    private void writePrimitiveTypeLength(BitSet bits, int length) {
        logger.debug("Writing length {}", length);
        for (int i = VALUE_LENGTH; i >= 0; i--) {
            if (length % 2 == 1){
                bits.set(nextPosition + i - 1 );
            }
            length /= 2;
        }
        nextPosition += VALUE_LENGTH;
    }

    private void writePrimitiveValue(Object value){
        if (value instanceof Number){
            Number number = (Number)value;
            long longValue = 0L;
            if (value instanceof Integer){
                longValue = number.intValue();
                writeLongValue(bits, longValue);
            }
            if (value instanceof Byte){
                longValue = number.byteValue();
                writeLongValue(bits, longValue);
            }
            if (value instanceof Long){
                longValue = number.longValue();
                writeLongValue(bits, longValue);
            }
            if (value instanceof Short){
                longValue = number.shortValue();
                writeLongValue(bits, longValue);
            }
            if (value instanceof Double || value instanceof Float){
                Double doubleValue = number.doubleValue();
                writeDoubleValue(doubleValue);
            }
        }
        if (value instanceof Character){
            long longValue = (Character) value;
            writeLongValue(bits, longValue);
        }
        if (value instanceof Boolean){
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
        writePrimitiveTypeLength(bits, valueString.length());
        for (char c : valueString.toCharArray()){
            if (c == '1')
                bits.set(nextPosition);
            nextPosition++;
        }
    }

    private void serializeField(Field field, BitSet bits, Object target) throws IllegalAccessException {
        field.setAccessible(true);
        if (field.getType().isPrimitive()){
            //записать тип
            int descriptor = Arrays.stream(PrimitiveTypeDescriptor.values()).filter(value -> value.getPrimitiveTypeName().equals(field.getType().getTypeName())).findFirst().orElse(null).ordinal();
            writeTypeDescriptor(bits, descriptor);
            //записать длину значения
            writePrimitiveValue(field.get(target));
            //записать значение
        }
        else if (isWrapperType(field.getType())){
            System.out.println(field.getType().getTypeName());
            int descriptor = Arrays.stream(PrimitiveTypeDescriptor.values()).filter(value -> value.getWrapperTypeName().equals(field.getType().getTypeName())).findFirst().orElse(null).ordinal();
            writeTypeDescriptor(bits, descriptor);
            //записать длину значения
            writePrimitiveValue(field.get(target));
        }

    }
}