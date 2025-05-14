package apr.reflection;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * ClassFinder
 */
public class ClassFinder {

    public static List<Class<?>> find(String packageName) throws URISyntaxException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();

        String classFolderName = packageName.replace(".", "/");
        URL url = ClassFinder.class.getClassLoader().getResource(classFolderName);
        File classFolder = new File(url.toURI());

        for (var file : classFolder.listFiles()) {
            String fName = file.getName();
            if (!fName.endsWith(".class")) {
                continue;
            }
            classes.add(Class.forName(packageName + "." + fName.substring(0, fName.lastIndexOf("."))));
        }

        return classes;
    }
}
