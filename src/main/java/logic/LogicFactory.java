package logic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class LogicFactory {

    private static final String PACKAGE = "logic.";
    private static final String SUFFIX = "Logic";

    private LogicFactory() {
    }

    public static < T> T getFor(String entityName) {
        try {
            return getFor((Class< T>) Class.forName(PACKAGE + entityName + SUFFIX));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static < T> T getFor(Class<T> type) {
        try {
            Constructor<T> declaredConstructor = type.getDeclaredConstructor();
            return declaredConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new IllegalStateException(e);

        }
    }
}
