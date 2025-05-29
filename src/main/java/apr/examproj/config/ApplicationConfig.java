package apr.examproj.config;

/**
 * ApplicationConfig
 */
public class ApplicationConfig {

    static double walkSpeed = 5;
    static double bikeSpeed = 5;

    public static double mapNodeRadius = 0.005;
    public static double mapAddressRadius = 0.025;
    public static double mapAddressRadiusMax = 14;
    public static String cssIdMapNode = "street-map__node";

    public static double getWalkingSpeed() {
        return walkSpeed;
    }

    public static double getBikingSpeed() {
        return bikeSpeed;
    }

}
