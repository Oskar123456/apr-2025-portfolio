package apr.reflection;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

/**
 * RecordFormGen
 */
public class RecordFormGen {

    static Set<Class<?>> primitives = Set.of(Integer.class, Long.class, Float.class, Double.class, String.class);

    public static Element gen(Document doc, Class<?> cls) {
        Element div = doc.createElement("div");
        div.attributes().add("class", "reflection__form-div");

        Element title = doc.createElement("h1");
        title.attributes().add("class", "reflection__form-title");
        title.text(cls.getName());

        Element form = doc.createElement("form");
        form.attributes().add("onsubmit", "reflectionFormSubmit(event)");
        form.attributes().add("class", "reflection__form");
        form.attributes().add("method", "post");
        form.attributes().add("action", "javascript");

        Element ul = doc.createElement("ul");
        ul.attributes().add("class", "reflection__ul");
        ul.appendChildren(Arrays.stream(cls.getDeclaredFields()).map(f -> makeFormField(doc, f, "")).toList());

        Element className = doc.createElement("input");
        className.attributes().add("type", "hidden");
        className.attributes().add("name", "className");
        className.attributes().add("value", cls.getName());

        Element submit = doc.createElement("input");
        submit.attributes().add("type", "submit");
        submit.attributes().add("value", "Submit");

        form.appendChild(className);
        form.appendChild(ul);
        form.appendChild(submit);

        div.appendChild(title);
        div.appendChild(form);

        return div;
    }

    static Element makeFormField(Document doc, Field field, String qualifier) {
        if (primitives.contains(field.getType())) {
            Element li = doc.createElement("li");
            Element label = doc.createElement("label");
            Element input = doc.createElement("input");

            String className = qualifier + (qualifier != null && qualifier.length() > 0 ? "." : "")
                    + field.getName();

            label.appendText("[" + field.getType().getSimpleName() + "] " + field.getName() + ":");
            input.attributes().add("name", className);
            input.attributes().add("type", "text");

            UUID uuid = UUID.randomUUID();
            label.attributes().add("for", uuid.toString());
            input.attributes().add("id", uuid.toString());

            li.appendChild(label);
            li.appendChild(input);
            return li;

        } else {
            Element div = doc.createElement("div");
            Element ul = doc.createElement("ul");
            for (var subField : field.getType().getDeclaredFields()) {
                String className = qualifier + (qualifier != null && qualifier.length() > 0 ? "." : "")
                        + field.getName();
                Element li = makeFormField(doc, subField, className);
                li.attributes().add("className", className);
                ul.appendChild(li);
            }
            Element title = doc.createElement("h2");
            title.attributes().add("class", "reflection__form-element-subtitle");
            title.text("[" + field.getType().getSimpleName() + "] " + field.getName());
            div.appendChild(title);
            div.appendChild(ul);
            return div;
        }
    }

}
