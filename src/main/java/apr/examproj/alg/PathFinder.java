package apr.examproj.alg;

import apr.examproj.ds.Graph;

/**
 * PathFinder
 */
public interface PathFinder<T> {

    public boolean search(Graph<T> graph);

}
