package apr.reflection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * HtmlGen
 */
public class HtmlGen {

    public static Document loadDoc(String path) throws IOException {
        File input = new File(path);
        Document doc = Jsoup.parse(input, "UTF-8", "http://localhost:9999/");
        return doc;
    }

    public static void write(Document doc) {
        try (FileWriter fwriter = new FileWriter("html/index.html", false)) {
            fwriter.write(doc.toString());
            // System.out.println("wrote:");
            // System.out.println(doc.toString());
            fwriter.flush();
        } catch (Exception e) {
        }
    }
}
