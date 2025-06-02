package apr.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * NQueens
 */
public class NQueens {

    static char queen = 'Q';
    static char blank = '.';

    public static List<char[][]> solve(int n) {
        List<char[][]> solutions = new ArrayList<>();

        var board = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = blank;
            }
        }

        solveRec(solutions, board, n, 0);

        return solutions;
    }

    static void solveRec(List<char[][]> solutions, char[][] board, int n, int row) {
        if (row == n) {
            solutions.add(arrayCopyOf(board));
            return;
        }
        for (int i = 0; i < n; ++i) {
            if (isSafe(board, n, i, row)) {
                board[i][row] = queen;
                solveRec(solutions, board, n, row + 1);
                board[i][row] = blank;
            }
        }
    }

    static boolean isSafe(char[][] board, int n, int col, int row) {
        // System.out.printf("NQueens.isSafe(%d,%d):%n", col, row);
        //
        // for (int i = 0; i < board.length; i++) {
        // for (int j = 0; j < board[0].length; j++) {
        // if (i == col && j == row) {
        // System.out.printf("[%c]", board[i][j]);
        // } else {
        // System.out.printf("%3c", board[i][j]);
        // }
        // }
        // System.out.println();
        // }

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; j++) {
                if (i == col && board[i][j] == queen) { // vertical
                    // System.out.println("NQueens.isSafe(): false");
                    return false;
                }
                if (j == row && board[i][j] == queen) { // horizonal
                    // System.out.println("NQueens.isSafe(): false");
                    return false;
                }
                if (j - i == row - col && board[i][j] == queen) { // diagonal
                    // System.out.println("NQueens.isSafe(): false");
                    return false;
                }
                if (j + i == row + col && board[i][j] == queen) { // reverse diagonal
                    // System.out.println("NQueens.isSafe(): false");
                    return false;
                }
            }
        }
        // System.out.println("NQueens.isSafe(): true");
        return true;
    }

    static char[][] arrayCopyOf(char[][] arr) {
        var newArr = new char[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                newArr[i][j] = arr[i][j];
            }
        }
        return newArr;
    }

}
