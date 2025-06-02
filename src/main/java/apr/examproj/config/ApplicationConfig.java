package apr.examproj.config;

import java.util.Observable;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;

/**
 * ApplicationConfig
 */
public class ApplicationConfig {

    static double walkSpeed = 5;
    static double bikeSpeed = 5;

    public static double mapNodeRadius = 8;
    public static double mapPathNodeRadius = 4;
    public static double mapAddressRadius = 0.025;
    public static double addressSignRadius = 12;
    public static double mapAddressRadiusMax = 14;
    public static String cssIdMapNode = "street-map__node";

    public static BooleanProperty showLinkPaths = new SimpleBooleanProperty();
    public static BooleanProperty showPathNodes = new SimpleBooleanProperty();

    public static double getWalkingSpeed() {
        return walkSpeed;
    }

    public static double getBikingSpeed() {
        return bikeSpeed;
    }

}
