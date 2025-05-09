package apr.sorting.visualization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SortingReplay
 */
public class SortingReplay<T> {

    List<ElementAction> actions;
    T[] dataInit;
    T[] data;
    int actionIdx, len;

    public boolean isDone;

    public SortingReplay(T[] data) {
        this.actions = new ArrayList<>();
        this.dataInit = Arrays.copyOf(data, data.length);
        this.data = data;
        this.len = data.length;
        actionIdx = -1;
    }

    public SortingReplay(List<ElementAction> actions, T[] data) {
        this.actions = actions;
        this.dataInit = Arrays.copyOf(data, data.length);
        this.data = data;
        this.len = data.length;
        actionIdx = -1;
    }

    public void storeSwap(int first, int second) {
        actions.add(new ElementAction(first, second, ActionType.SWAP));
    }

    public void storeComparison(int first, int second) {
        actions.add(new ElementAction(first, second, ActionType.COMPARE));
    }

    public ElementAction last() {
        if (actionIdx >= 0 && actionIdx < actions.size()) {
            return actions.get(actionIdx);
        }
        return null;
    }

    public ElementAction next() {
        ElementAction a = null;
        if (actionIdx < actions.size() - 1) {
            a = actions.get(++actionIdx);
            if (a.action == ActionType.SWAP) {
                T tmp = dataInit[a.first];
                dataInit[a.first] = dataInit[a.second];
                dataInit[a.second] = tmp;
            }
        } else {
            isDone = true;
        }
        return a;
    }

    public static class ElementAction {
        int first, second;
        ActionType action;

        public ElementAction(int first, int second, ActionType action) {
            this.first = first;
            this.second = second;
            this.action = action;
        }
    }

    public enum ActionType {
        COMPARE, SWAP
    }
}
