package ru.shanalotte;

public enum PrimitiveTypeDescriptor {

    BYTE("byte", "java.lang.Byte"),
    SHORT("short", "java.lang.Short"),
    INT("int", "java.lang.Integer"),
    LONG("long", "java.lang.Long"),
    BOOLEAN("boolean", "java.lang.Boolean"),
    DOUBLE("double", "java.lang.Double"),
    FLOAT("float", "java.lang.Float"),
    CHAR("char", "java.lang.Character");

    
    private String primitiveTypeName;
    private String wrapperTypeName;

    PrimitiveTypeDescriptor(String primitiveTypeName, String wrapperTypeName) {
        this.primitiveTypeName = primitiveTypeName;
        this.wrapperTypeName = wrapperTypeName;
    }

    public String getPrimitiveTypeName() {
        return primitiveTypeName;
    }

    public String getWrapperTypeName() {
        return wrapperTypeName;
    }
}
