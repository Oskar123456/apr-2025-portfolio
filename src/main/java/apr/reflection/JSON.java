package apr.reflection;

import java.util.*;

/**
 * JSON
 */
public class JSON {

    public static String stringify(Object obj) throws IllegalArgumentException, IllegalAccessException {
        List<String> children = new ArrayList<>();
        Class<?> cls = obj.getClass();

        for (var field : cls.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType().equals(String.class)) {
                children.add(String.format("\"%s\": \"%s\"", field.getName(), field.get(obj).toString()));
            } else if (Number.class.isAssignableFrom(field.getType())) {
                children.add(String.format("\"%s\": %s", field.getName(), String.valueOf(field.get(obj))));
            } else {
                throw new IllegalArgumentException("unsupported data type " + field.getType());
            }
        }

        return "{" + String.join(",", children) + "}";
    }
}
