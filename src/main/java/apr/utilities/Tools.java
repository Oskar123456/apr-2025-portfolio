package apr.utilities;

/**
 * Tools
 */
public class Tools {

    public static double NanoSecsToSecs(long ns) {
        return ns / 1000000000D;
    }

    public static void arrPrint(char[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.printf("%3c", arr[i][j]);
            }
            System.out.println();
        }
    }

}
