package apr.examproj.alg;

import apr.examproj.ds.Graph;
import apr.examproj.ds.GraphNode;

/**
 * PathFinder
 */
public interface IPathFinder {

    public <T> boolean search(Graph<T> graph, GraphNode<T> src, GraphNode<T> dest);

}
