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
        this.nextPosition = 0;
        bits = new BitSet();
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields){
            serializeField(field, bits, target);
        }
        return bits;
    }

    private void writeTypeDescriptor(int descriptor){
        logger.debug("Writing descriptor {}", descriptor);

        for (int i = DESCRIPTOR_LENGTH - 1; i >= 0; i--) {
            if ((descriptor % 2) == 1){
                bits.set(nextPosition + i);
            }
            descriptor = descriptor / 2;
        }
        nextPosition += DESCRIPTOR_LENGTH;
    }

    private void writeLongValue(long value){
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
        writeLength(len + 1);
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

    private void writeLength(int length) {
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
                writeLongValue(longValue);
            }
            if (value instanceof Byte){
                longValue = number.byteValue();
                writeLongValue(longValue);
            }
            if (value instanceof Long){
                longValue = number.longValue();
                writeLongValue(longValue);
            }
            if (value instanceof Short){
                longValue = number.shortValue();
                writeLongValue(longValue);
            }
            if (value instanceof Double || value instanceof Float){
                Double doubleValue = number.doubleValue();
                writeDoubleValue(doubleValue);
            }
        }
        if (value instanceof Character){
            long longValue = (Character) value;
            writeLongValue(longValue);
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
        writeLength(valueString.length());
        for (char c : valueString.toCharArray()){
            if (c == '1')
                bits.set(nextPosition);
            nextPosition++;
        }
    }

    private void serializeField(Field field, BitSet bits, Object target) throws IllegalAccessException {
        if (field.getName().equals("__$lineHits$__")) return;
        field.setAccessible(true);
        int descriptor = 0;
        if (field.getType().isPrimitive()){
            //записать тип
            descriptor = Arrays.stream(DescriptorType.values()).filter(value -> value.getPrimitiveTypeName().equals(field.getType().getTypeName())).findFirst().orElse(null).ordinal();
        }
        else if (isWrapperType(field.getType())){
           descriptor = Arrays.stream(DescriptorType.values()).filter(value -> value.getWrapperTypeName().equals(field.getType().getTypeName())).findFirst().orElse(null).ordinal();
        }
        else if (field.getType().getName().equals("java.lang.String")){
            descriptor = DescriptorType.STRING.ordinal();
        }
        logger.debug("Descriptor {}", descriptor);
        writeTypeDescriptor(descriptor);
        if (field.get(target) == null){
            bits.set(nextPosition);
            nextPosition++;
            logger.debug("Writing null flag 1");
            return;
        }
        logger.debug("Writing null flag 0");
        nextPosition++;
        //записать длину значения
        //записать значение
        if (descriptor == DescriptorType.STRING.ordinal()){
            writeStringValue(field.get(target));
        }
        else
        writePrimitiveValue(field.get(target));
    }

    private void writeStringValue(Object stringValue) {
        String bitsString = BinaryUtil.convertStringToBinary((String)stringValue);
        writeLength(bitsString.length());
        writeBitsString(bitsString);
    }

    private void writeBitsString(String bitsString) {
        for (char c : bitsString.toCharArray()){
            if (c == '1')
                bits.set(nextPosition);
            nextPosition++;
        }
    }


}
