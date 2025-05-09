package apr.sorting;

import apr.sorting.visualization.SortingReplay;

/**
 * SortFn
 */
public interface SortFn<T extends Comparable<T>> {

    public SortingReplay<T> sort(T[] arr);
}
