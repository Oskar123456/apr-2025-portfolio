package apr.reflection;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;

/**
 * Endpoints
 */
public class Endpoints {

    static Logger logger = LoggerFactory.getLogger(Endpoints.class);
    static ObjectMapper jsonMapper = new ObjectMapper();

    public static EndpointGroup getAll() {
        return () -> {
            path("/", () -> {
                get("/", Endpoints::home);
                post("/records", Endpoints::onFormSubmission);
            });
        };
    }

    static void onFormSubmission(Context ctx) throws Exception {
        try {
            String form = new String(ctx.bodyAsBytes());
            System.out.println("App.onFormSubmission(): " + form);

            JsonNode json = jsonMapper.readTree(form);

            List<Object> objs = new ArrayList<>();

            for (var node : json) {
                objs.add(RecordGen.fromJson(node));
            }

            System.out.printf("App.onFormSubmission(): managed to instantiate records: %s%n", objs.toString());

            ctx.status(200);
            ctx.json(objs);

            FileWriter fwriter = new FileWriter("data/tmprecords.txt", false);

            String finalStr = "";
            List<String> finalStrs = new ArrayList<>();

            for (var obj : objs) {
                String objStr = jsonMapper.writeValueAsString(obj);
                objStr = String.format("{\"className\": \"%s\", %s", obj.getClass().getName(),
                        objStr.substring(objStr.indexOf("{") + 1));
                finalStrs.add(objStr);
            }

            finalStr = "[" + String.join(",", finalStrs) + "]";

            fwriter.write(finalStr);
            fwriter.flush();
            fwriter.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("App.onFormSubmission(): Error: " + e.getMessage());
            throw e;
        }
    }

    static void home(Context ctx) {
        ctx.redirect("/index.html");
    }
}
