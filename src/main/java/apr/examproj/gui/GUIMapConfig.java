package apr.examproj.gui;

/**
 * GUIMapConfig
 */
public class GUIMapConfig {

    static GUIMapConfig cfg;

    double addrRadius = 0.015;
    double nodeRadius = 0.005;
    double addrTextSize = 12;

    public static GUIMapConfig getInstance() {
        if (cfg == null) {
            cfg = new GUIMapConfig();
        }
        return cfg;
    }

    public double getAddrRadius() {
        return addrRadius;
    }

    public double getAddrTextSize() {
        return addrTextSize;
    }

    public class Builder { // TODO: MAYBE JUST TO HAVE DESIGN PATTERN

    }

}
