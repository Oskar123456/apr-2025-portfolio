package apr.backtracking;

import apr.utilities.Tools;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {

        int N = 8;

        var NQueensSolutions = NQueens.solve(N);

        System.out.printf("NQueensSolutions(%d): %n", N);
        for (var solution : NQueensSolutions) {
            Tools.arrPrint(solution);
            System.out.println("  ----------------------");
        }
        System.out.println();

        System.out.println("Main.main(): " + NQueensSolutions.size() + " solutions found for N = " + N);

    }

}
