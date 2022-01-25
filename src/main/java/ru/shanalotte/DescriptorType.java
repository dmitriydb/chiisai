package ru.shanalotte;

public enum DescriptorType {

    BYTE("byte", "java.lang.Byte"),
    SHORT("short", "java.lang.Short"),
    INT("int", "java.lang.Integer"),
    LONG("long", "java.lang.Long"),
    BOOLEAN("boolean", "java.lang.Boolean"),
    DOUBLE("double", "java.lang.Double"),
    FLOAT("float", "java.lang.Float"),
    CHAR("char", "java.lang.Character"),
    STRING("string", "java.lang.String"),
    OBJECT("object", "java.lang.Object"),
    ARRAY("array", "java.lang.Array"),
    NULL("null", "null"),
    CACHED_OBJECT_REFENCE("reference", "ref");

    private String primitiveTypeName;
    private String wrapperTypeName;

    DescriptorType(String primitiveTypeName, String wrapperTypeName) {
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
