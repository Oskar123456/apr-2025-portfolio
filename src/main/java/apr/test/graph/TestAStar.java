package apr.test.graph;

import java.util.Random;

import apr.algorithms.graph.AStar;
import apr.datastructures.graph.AStarEdge;
import apr.datastructures.graph.AStarGraph;
import apr.datastructures.graph.AStarNode;
import apr.datastructures.graph.Point2DI;

/**
 * TestAstar
 */
public class TestAStar {

    public static void testAStar() {
        Random rng = new Random();
        int n = rng.nextInt(2, 10);

        AStarGraph<String> graph = new AStarGraph<>();

        for (int i = 0; i < n; i++) {
            graph.nodes.add(new AStarNode<>("N" + i, rng.nextInt(1, 10), rng.nextInt(1, 10)));
        }

        for (int i = 0; i < n; i++) {
            int srcId = i;
            int destId = rng.nextInt(0, n);
            if (destId == srcId) {
                destId = (destId + 1) % n;
            }
            var srcNode = graph.nodes.get(srcId);
            var destNode = graph.nodes.get(destId);

            int j = 0;
            while (j++ < n) {
                if (srcNode != destNode && !srcNode.isConnectedTo(destNode)) {
                    break;
                }
                destId = (destId + 1) % n;
                destNode = graph.nodes.get(destId);
            }

            if (srcNode == destNode || srcNode.isConnectedTo(destNode)) {
                continue;
            }

            var edge = new AStarEdge<>(srcNode, destNode);
            graph.edges.add(edge);
            srcNode.edges.add(edge);
        }

        System.out.println(graph);

        int srcId = rng.nextInt(0, n);
        int destId = rng.nextInt(0, n);
        if (destId == srcId) {
            destId = (destId + 1) % n;
        }

        AStar.use(graph, graph.nodes.get(srcId), graph.nodes.get(destId));

        /* GRID TESTING */
        int w = rng.nextInt(5, 10);
        int h = rng.nextInt(5, 10);

        int srcX = rng.nextInt(0, w);
        int srcY = rng.nextInt(0, h);
        int destX = rng.nextInt(0, w);
        int destY = rng.nextInt(0, h);

        if (srcX == destX && srcY == destY) {
            destX = (destX + 1) % w;
        }

        int[][] grid = new int[h][w];
        AStar.grid(grid, new Point2DI(srcX, srcY), new Point2DI(destX, destY));
    }

}
