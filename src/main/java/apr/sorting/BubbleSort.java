package apr.sorting;

import apr.sorting.visualization.SortingReplay;

/**
 * BubbleSort
 */
public class BubbleSort {

    public static <T extends Comparable<T>> SortingReplay<T> sort(T[] arr) {
        var replay = new SortingReplay<>(arr); // visualization

        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (int i = 0; i + 1 < arr.length; i++) {
                replay.storeComparison(i, i + 1); // visualization
                if (arr[i].compareTo(arr[i + 1]) > 0) {
                    T tmp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = tmp;
                    swapped = true;
                    replay.storeSwap(i, i + 1); // visualization
                }
            }
        }

        return replay; // visualization
    }

}
