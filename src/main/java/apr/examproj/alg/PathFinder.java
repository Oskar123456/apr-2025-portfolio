package apr.examproj.alg;

import apr.examproj.ds.Graph;
import apr.examproj.ds.GraphNode;

/**
 * PathFinder
 */
public interface PathFinder<T> {

    public boolean search(Graph<T> graph);

}
