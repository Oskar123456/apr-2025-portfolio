package apr.sorting.visualization;

import java.util.List;

/**
 * SortingReplay
 */
public class SortingReplay {

    List<ElementAction> actions;
    double[] data;
    int actionIdx, len;

    public SortingReplay(List<ElementAction> actions, double[] data) {
        this.actions = actions;
        this.data = data;
        this.len = data.length;
    }

    public ElementAction next() {
        if (actionIdx < actions.size()) {
            return actions.get(actionIdx++);
        }
        return null;
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
