package apr.reflection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

/**
 * Main
 */
public class App {

    static Logger logger = LoggerFactory.getLogger(App.class);
    static ObjectMapper jsonMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        try {

            Document index = HtmlGen.loadDoc("html/index.html");
            index.body().children().clear();

            List<Class<?>> classes = ClassFinder.find("apr.reflection.records");

            Element title = index.createElement("h1");
            title.text("APR REFLECTION EXAMPLE");
            title.attributes().add("class", "reflection__title");

            Element subTitle = index.createElement("h2");
            subTitle.text("Here you can create new classes/records");
            subTitle.attributes().add("class", "reflection__sub-title");

            Element container = index.createElement("div");
            container.attributes().add("class", "reflection__container");
            container.appendChildren(classes.stream().map(cl -> RecordFormGen.gen(index, cl)).toList());

            index.body().appendChild(title);
            index.body().appendChild(subTitle);
            index.body().appendChild(container);
            index.body().attributes().add("class", "reflection__body");

            HtmlGen.write(index);

            startServer();

        } catch (

        Exception e) {
            System.out.println("App.main(): Error: " + e.getMessage());
        } finally {
            // clean();
        }
    }

    static void startServer() {
        PrintStream sout = new PrintStream(System.out);
        PrintStream serr = new PrintStream(System.err);

        System.setOut(new PrintStream(PrintStream.nullOutputStream()));
        System.setErr(new PrintStream(PrintStream.nullOutputStream()));

        File f = new File("html");

        var jav = Javalin.create(config -> {
            config.staticFiles.add(f.getAbsolutePath(), Location.EXTERNAL);
            config.router.apiBuilder(Endpoints.getAll());
        });
        jav.afterMatched("*", ctx -> logger.info(ctx.statusCode() + ""));

        jav.start(9999);

        System.setOut(sout);
        System.setErr(serr);

        System.out.println("App.main(): serving " + f.getAbsolutePath());
    }

    static void clean() throws IOException {
        Document document = HtmlGen.loadDoc("html/index.html");
        document.body().children().clear();

        try (var fwriter = new FileWriter("html/index.html", false)) {
            fwriter.write(document.toString());
            fwriter.flush();
        } catch (Exception e) {
            System.out.println("App.clean(): Error: " + e.getMessage());
        }
    }
}
