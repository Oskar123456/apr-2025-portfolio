package apr;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import apr.algorithms.graph.visualization.Node;
import apr.datastructures.Hashmap;
import apr.reflection.HtmlGen;
import apr.reflection.RecordGen;
import apr.reflection.TestRecord;
import apr.reflection.TestRecord2;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * App
 */
public class App {

    static Faker faker = new Faker();

    public static void main(String[] args) {

        String doc = HtmlGen.document();
        doc = HtmlGen.setBody(doc, "LOOLOLO");

        Class<?> cl = TestRecord2.class;

        List<String> names = new ArrayList<>();
        List<String> types = new ArrayList<>();

        for (var field : cl.getDeclaredFields()) {
            names.add(field.getName());
            types.add(field.getType().getSimpleName());
        }

        String form = HtmlGen.form(cl.getName(), names, types);
        String title = HtmlGen.title(cl.getSimpleName() + ":", 1);

        String body = HtmlGen.combine("form1", title, form);

        doc = HtmlGen.setBody(doc, body);

        try (var pw = new FileWriter("html/index.html", false)) {
            pw.write(doc);
            pw.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(9999), 0);
            server.createContext("/", new MyHandler());
            server.setExecutor(null);
            server.start();
        } catch (Exception e) {

        }

        // System.out.println(doc);

        // for (var e : hm) {
        //
        // }

        // AStarGUI.Maze maze;
        // try {
        // maze = AStarGUI.readMaze("data/maze2.txt");
        // AStarGUI.solve(maze);
        // // maze.DrawMe(gc);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // TestDijkstra.testDijkstra();
        // TestAStar.testAStar();
    }

}

class MyHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";

        try {
            String query = exchange.getRequestURI().toString();
            var clInstance = RecordGen.gen(query.substring(query.lastIndexOf("/") + 1));

            ObjectMapper om = new ObjectMapper();
            response = om.writeValueAsString(clInstance);
            exchange.getResponseHeaders().add("content-type", "application/json");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            exchange.sendResponseHeaders(500, 0);
            return;
        }

        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

        System.out.printf("[%s] MyHandler.handle(): %s%n",
                DateTimeFormatter.ISO_TIME.format(LocalDateTime.now()),
                response);
    }

}
