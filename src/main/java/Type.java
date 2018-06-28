public enum Type {
    INTEGER("Integer"),
    DOUBLE("Double"),
    LONG("Long");

    private final String fieldDescription;

    private Type(String value) {
        fieldDescription = value;
    }

    public String toString() {
        return fieldDescription;
    }

}
