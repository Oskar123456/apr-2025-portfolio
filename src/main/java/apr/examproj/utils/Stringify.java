package apr.examproj.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Stringify
 */
public class Stringify {

    @SuppressWarnings("rawtypes")
    public static String toString(Object obj) {
        Class<?> cls = obj.getClass();
        String name = cls.getSimpleName();
        try {
            String content = "";

            for (var field : cls.getDeclaredFields()) {
                field.setAccessible(true);
                if (Collection.class.isAssignableFrom(field.getType())) {
                    content += String.format("(%s: %s elements)", field.getName(),
                            ((Collection) field.get(obj)).size());
                } else if (Map.class.isAssignableFrom(field.getType())) {
                    content += String.format("(%s: %s elements)", field.getName(),
                            ((Map) field.get(obj)).size());
                } else {
                    content += String.format("(%s: %s)", field.getName(), field.get(obj).toString());
                }
            }

            return String.format("%s[%s]", name, content);
        } catch (Exception e) {
            e.printStackTrace();
            return name + "[]";
        }
    }

}
