package apr.sorting;

import apr.sorting.visualization.SortingReplay;

/**
 * HeapSort
 */
public class HeapSort {

    public static <T extends Comparable<T>> SortingReplay<T> sort(T[] arr) {
        var replay = new SortingReplay<>(arr); // visualization

        heapify(arr, 0, arr.length, replay);

        int hi = arr.length;
        while (hi > 1) {
            --hi;
            swap(arr, 0, hi, replay);
            siftdown(arr, 0, hi, replay);
        }

        return replay; // visualization
    }

    static <T extends Comparable<T>> void heapify(T[] arr, int lo, int hi, SortingReplay<T> replay) {
        int root = (hi - 2) / 2;
        while (root >= 0) {
            siftdown(arr, root, hi, replay);
            root--;
        }
    }

    static <T extends Comparable<T>> void siftdown(T[] arr, int lo, int hi, SortingReplay<T> replay) {
        int root = lo;
        while (root * 2 + 1 < hi) {
            int child = root * 2 + 1;
            if (child + 1 < hi) {
                replay.storeComparison(child, child + 1);
            }
            if (child + 1 < hi && arr[child].compareTo(arr[child + 1]) < 0) {
                ++child;
            }
            replay.storeComparison(root, child);
            if (arr[root].compareTo(arr[child]) < 0) {
                swap(arr, root, child, replay);
                root = child;
            } else {
                break;
            }
        }
    }

    static <T extends Comparable<T>> void swap(T[] arr, int first, int second, SortingReplay<T> replay) {
        replay.storeSwap(first, second);
        T tmp = arr[first];
        arr[first] = arr[second];
        arr[second] = tmp;
    }
}
