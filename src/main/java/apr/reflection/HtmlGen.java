package apr.reflection;

import java.util.List;
import java.util.UUID;

/**
 * HtmlGen
 */
public class HtmlGen {

    public static String document() {
        return """
                            <!DOCTYPE html>
                            <html>

                            <head>
                                <meta charset="utf-8" />
                                <title data-l10n-id="newtab-page-title"></title>
                <link rel="stylesheet" href="index.css" />
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT" crossorigin="anonymous">
                            </head>

                            <body>

                                <h1>TESTE</h1>

                            </body>

                            </html>""";
    }

    public static String setBody(String document, String body) {
        return document.substring(0, document.indexOf("<body>"))
                + "<body>" + System.lineSeparator() + body + System.lineSeparator()
                + document.substring(document.lastIndexOf("</body>"));
    }

    public static String form(String path, List<String> names, List<String> types) {
        String str = String.format("<form action=\"http://localhost:9999/%s\">", path);

        for (int i = 0; i < names.size(); i++) {
            String n = names.get(i);
            String t = types.get(i);
            UUID uuid = UUID.randomUUID();
            str += "<div>";
            str += String.format("<label for=\"%s\">%s:</label>", uuid.toString(), n);
            if (t.toLowerCase().equals("double") || t.toLowerCase().equals("int")) {
                str += String.format("%n    <input id=\"%s\" name=\"%s\" placeholder=\"%s\" type=\"number\">",
                        uuid.toString(), n, t);
            }
            if (t.toLowerCase().equals("string")) {
                str += String.format("%n    <input id=\"%s\" name=\"%s\" placeholder=\"%s\" type=\"text\">",
                        uuid.toString(), n, t);
            }
            str += "</div>";
        }

        str += String.format("%n<input type=\"submit\" value=\"Submit\">");
        return str + String.format("%n</form>");
    }

    public static String title(String content, int tier) {
        return String.format("<h%d>%s</h%d>", tier, content, tier);
    }

    public static String combine(String id, String... contents) {
        return String.format("<div id=\"%s\">", id) +
                String.join(System.lineSeparator(), contents)
                + String.format("</div>");
    }
}
