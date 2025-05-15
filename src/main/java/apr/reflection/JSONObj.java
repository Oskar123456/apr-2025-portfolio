package apr.reflection;

import java.util.Map;
import java.util.HashMap;

/**
 * JSONObj
 */
public class JSONObj {

    Map<String, Object> children;
    Boolean isLeaf;
    JSONType type;
    Object value;

    public JSONObj(String jsonStr) throws Exception {
        jsonStr = jsonStr.trim();
        if (!jsonStr.endsWith("}") || !jsonStr.startsWith("{")) {
            throw new Exception("JSONObj: json object has to start and end with {}");
        }
        if (!jsonStr.endsWith("]") || !jsonStr.startsWith("[")) {
            throw new Exception("JSONObj: json object has to start and end with {}");
        }

        children = new HashMap<>();
    }

    public enum JSONType {
        NUMBER, ARRAY, BOOLEAN, NULL, OBJECT, STRING
    }
}
