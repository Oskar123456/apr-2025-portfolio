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

        Class<?> cls = Class.forName(json.get("className").asText());
        Constructor<?> ctor = cls.getConstructors()[0]; // record has exactly 1 constructor
        List<Object> ctorArgs = new ArrayList<>();

        for (var field : cls.getDeclaredFields()) {
            if (!extractors.containsKey(field.getType())) {
                System.out.println("RecordGen.fromJson(): Error: enrecognized field type " + field.getType().getName());
                continue;
            }
            ctorArgs.add(extractors.get(field.getType()).extract(field, json));
        }

        return ctor.newInstance(ctorArgs.toArray());
    }

    public static Object gen(String query) throws InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException,
            ClassNotFoundException {

        String className = query.split("\\?")[0];
        String[] qParams = query.split("\\?")[1].split("&");
        Class<?> cl = Class.forName(className);

        List<String> names = new ArrayList<>();
        List<String> values = new ArrayList<>();
        List<Object> args = new ArrayList<>();

        extractNamesAndValue(qParams, names, values);

        for (int i = 0; i < names.size(); i++) {
            for (var field : cl.getDeclaredFields()) {
                if (field.getName().equals(names.get(i))) {
                    if (field.getType().equals(Double.class)) {
                        args.add(Double.valueOf(values.get(i)));
                    } else if (field.getType().equals(Float.class)) {
                        args.add(Float.valueOf(values.get(i)));
                    } else if (field.getType().equals(Integer.class)) {
                        args.add(Integer.valueOf(values.get(i), 10));
                    } else if (field.getType().equals(Long.class)) {
                        args.add(Long.valueOf(values.get(i), 10));
                    } else if (field.getType().equals(String.class)) {
                        args.add(values.get(i));
                    } else {
                        System.out.println("RecordGen.gen(): unsupported type " + field.getType().getName());
                    }
                }
            }
        }

        return cl.getDeclaredConstructors()[0].newInstance(args.toArray());
    }

    public static void extractNamesAndValue(String[] qParams, List<String> names, List<String> values) {
        for (var qParam : qParams) {
            String fieldName = qParam.split("=")[0];
            String fieldValue = qParam.split("=")[1];
            names.add(fieldName);
            values.add(fieldValue);
        }
    }

}

/**
 * FieldExtractor
 */
interface FieldExtractor {

    public Object extract(Field field, JsonNode json);
}
