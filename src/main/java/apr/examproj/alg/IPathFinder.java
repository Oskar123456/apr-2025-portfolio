package apr.examproj.alg;

import apr.examproj.ds.Graph;

/**
 * PathFinder
 */
public interface IPathFinder {

    public <T> Path<T> search(Graph<T> graph);

}
