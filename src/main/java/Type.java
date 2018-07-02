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

    static <T extends Number> T add(T one, T two) throws TypeMismatchException {
        
        if (one.getClass().getTypeName().equals(INTEGER.toString()) && two.getClass().getTypeName().equals(INTEGER.toString())) {
            return (T) Integer.valueOf(one.intValue() + two.intValue());
        } else if (one.getClass().getTypeName().equals(DOUBLE.toString()) && two.getClass().getTypeName().equals(DOUBLE.toString())) {
            return (T) Double.valueOf(one.doubleValue() + two.doubleValue());
        } else if (one.getClass().getTypeName().equals(LONG.toString()) && two.getClass().getTypeName().equals(LONG.toString())) {
            return (T) Long.valueOf(one.longValue() + two.longValue());
        } else {
            throw new TypeMismatchException();
        }
    }
    
    static <T extends Number> boolean greaterThan(T one, T two) throws TypeMismatchException {
        if (one.getClass().getTypeName().equals(INTEGER.toString()) && two.getClass().getTypeName().equals(INTEGER.toString())) {
            return one.intValue() > two.intValue();
        } else if (one.getClass().getTypeName().equals(DOUBLE.toString()) && two.getClass().getTypeName().equals(DOUBLE.toString())) {
            return one.doubleValue() > two.doubleValue();
        } else if (one.getClass().getTypeName().equals(LONG.toString()) && two.getClass().getTypeName().equals(LONG.toString())) {
            return one.longValue() > two.longValue();
        } else {
            throw new TypeMismatchException();
        }
    }
}
