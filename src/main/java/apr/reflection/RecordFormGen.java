package apr.reflection;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.UUID;

/**
 * RecordFormGen
 */
public class RecordFormGen {

    public static Element gen(Document doc, Class<?> cls) {
        Element div = doc.createElement("div");
        div.attributes().add("class", "reflection__form-div");

        Element title = doc.createElement("h1");
        title.attributes().add("class", "reflection__form-title");
        // title.text("Create new " + cls.getName());
        title.text(cls.getName());

        Element form = doc.createElement("form");
        form.attributes().add("onsubmit", "reflectionFormSubmit(event)");
        form.attributes().add("class", "reflection__form");
        form.attributes().add("method", "post");
        form.attributes().add("action", "javascript");

        Element ul = doc.createElement("ul");
        ul.attributes().add("class", "reflection__ul");

        for (var field : cls.getDeclaredFields()) {
            Element li = doc.createElement("li");
            Element label = doc.createElement("label");
            Element input = doc.createElement("input");

            label.appendText("[" + field.getType().getName() + "] " + field.getName() + ":");
            input.attributes().add("name", field.getName());
            input.attributes().add("type", "text");

            UUID uuid = UUID.randomUUID();
            label.attributes().add("for", uuid.toString());
            input.attributes().add("id", uuid.toString());

            li.appendChild(label);
            li.appendChild(input);
            ul.appendChild(li);
        }

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
}
