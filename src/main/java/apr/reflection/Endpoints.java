package apr.reflection;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.get;

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
            Object obj = RecordGen.fromJson(json);

            System.out.printf("App.onFormSubmission(): managed to instantiate record: %s%n", obj.toString());

            ctx.status(200);
            ctx.result(JSON.stringify(obj));

        } catch (Exception e) {
            System.out.println("App.onFormSubmission(): Error: " + e.getMessage());
            throw e;
        }
    }

    static void home(Context ctx) {
        ctx.redirect("/index.html");
    }
}
