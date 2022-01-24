package ru.shanalotte;

public enum PrimitiveTypeDescriptors {

    BYTE("byte"),
    SHORT("short"),
    INT("int"),
    LONG("long"),
    BOOLEAN("boolean"),
    DOUBLE("double"),
    FLOAT("float"),
    CHAR("char");

    private String primitiveTypeName;

    PrimitiveTypeDescriptors(String primitiveTypeName) {
        this.primitiveTypeName = primitiveTypeName;
    }

    public String getPrimitiveTypeName() {
        return primitiveTypeName;
    }
}
