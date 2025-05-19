package apr.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * RecordGen
 */
public class RecordGen {

    static Map<Class<?>, FieldExtractor> extractors = Map.of(
            Double.class, (field, json) -> json.get(field.getName()).asDouble(),
            Float.class, (field, json) -> (float) json.get(field.getName()).asDouble(),
            Long.class, (field, json) -> json.get(field.getName()).asLong(),
            Integer.class, (field, json) -> json.get(field.getName()).asInt(),
            String.class, (field, json) -> json.get(field.getName()).asText());

    public static Object fromJson(JsonNode json)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            ClassNotFoundException {

        // System.out.println("RecordGen.fromJson(): " + json.toPrettyString());

        Class<?> cls = Class.forName(json.get("className").asText());
        Object obj = extract(cls, json);

        // System.out.println("RecordGen.fromJson(): " + obj);

        return obj;
    }

    static Object extract(Class<?> cls, JsonNode json)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // System.out.println("RecordGen.extract() : " + cls.getSimpleName() + " : " +
        // json.toPrettyString());

        Constructor<?> ctor = cls.getConstructors()[0];
        List<Object> ctorArgs = new ArrayList<>();

        for (var field : cls.getDeclaredFields()) {
            if (!extractors.containsKey(field.getType())) {
                ctorArgs.add(extract(field.getType(), json.get(field.getName())));
            } else {
                ctorArgs.add(extractors.get(field.getType()).extract(field, json));
            }
        }

        // System.out.print("RecordGen.extract():\t");
        // for (var arg : ctorArgs) {
        // System.out.print(arg.toString() + " ; ");
        // }
        // System.out.println();

        return ctor.newInstance(ctorArgs.toArray());
    }

}

/**
 * FieldExtractor
 */
interface FieldExtractor {

    public Object extract(Field field, JsonNode json);
}
