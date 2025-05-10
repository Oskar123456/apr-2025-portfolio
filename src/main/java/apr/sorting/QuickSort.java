package apr.sorting;

import apr.sorting.visualization.SortingReplay;

/**
 * QuickSort
 */
public class QuickSort {

    public static <T extends Comparable<T>> SortingReplay<T> sort(T[] arr) {
        var replay = new SortingReplay<>(arr); // visualization

        rec(arr, 0, arr.length - 1, replay);

        return replay; // visualization
    }

    static <T extends Comparable<T>> void rec(T[] arr, int lo, int hi, SortingReplay<T> replay) {
        if (lo >= hi || lo < 0) {
            return;
        }

        int divider = partition(arr, lo, hi, replay);

        rec(arr, lo, divider - 1, replay);
        rec(arr, divider + 1, hi, replay);
    }

    static <T extends Comparable<T>> int partition(T[] arr, int lo, int hi, SortingReplay<T> replay) {
        int pivot = lo;

        for (int i = lo; i < hi; i++) {
            replay.storeComparison(i, hi);
            if (arr[i].compareTo(arr[hi]) <= 0) {
                swap(arr, i, pivot, replay);
                ++pivot;
            }
        }

        swap(arr, pivot, hi, replay);
        return pivot;
    }

    static <T extends Comparable<T>> void swap(T[] arr, int first, int second, SortingReplay<T> replay) {
        replay.storeSwap(first, second);
        T tmp = arr[first];
        arr[first] = arr[second];
        arr[second] = tmp;
    }
}
