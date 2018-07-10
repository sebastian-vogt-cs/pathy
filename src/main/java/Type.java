public enum Type {
    INTEGER("Integer"),
    DOUBLE("Double"),
    LONG("Long");

    private final String fieldDescription;

    Type(String value) {
        fieldDescription = value;
    }

    public String toString() {
        return fieldDescription;
    }

    @SuppressWarnings("unchecked")
    static <T extends Number> T add(T one, T two) {
        
        if (one.getClass().getTypeName().equals(INTEGER.toString())) {
            return (T) Integer.valueOf(one.intValue() + two.intValue());
        } else if (one.getClass().getTypeName().equals(DOUBLE.toString())) {
            return (T) Double.valueOf(one.doubleValue() + two.doubleValue());
        } else {
            return (T) Long.valueOf(one.longValue() + two.longValue());
        }
    }
    
    static <T extends Number> boolean greaterThan(T one, T two) {
        if (one.getClass().getTypeName().equals(INTEGER.toString())) {
            return one.intValue() > two.intValue();
        } else if (one.getClass().getTypeName().equals(DOUBLE.toString())) {
            return one.doubleValue() > two.doubleValue();
        } else {
            return one.longValue() > two.longValue();
        }
    }

    static <T extends Number> boolean lessThan(T one, T two) {
        return greaterThan(two, one);
    }

    static <T extends Number> int compare(T one, T two) {
        if (greaterThan(one, two)) {
            return 1;
        } else if (lessThan(one, two)) {
            return -1;
        } else {
            return 0;
        }
    }
}
