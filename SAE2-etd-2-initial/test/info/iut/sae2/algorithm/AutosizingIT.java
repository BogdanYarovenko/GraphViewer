package info.iut.sae2.algorithm;

import info.iut.sae2.graphs.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author byarovenko
 */
public class AutosizingIT {

    protected IGraph g;
    protected IGraph dtG;

    @Before
    @Test
    public void init() {
        g = GraphLoader.loadFromFile("dataset/testEB_nodes.csv", "dataset/testEB_edges.csv", GraphDataStructure.ADJACENCY_MATRIX);
        dtG = new DelaunayTriangulation().apply(g, null);
        g.autoSize();
    }

    @Test
    public void testApply() {

        double eps = 0.001; // Tolerance used to account for floating point precision

        {
            

            for (INode v : g.getNodes()) {
                // First block: ensure that no pair of nodes is overlapping
                for (INode u : g.getNodes()) {
                    if (!u.equals(v)) {

                        double sizes = g.getNodeSize(u).w / 2 + g.getNodeSize(v).w / 2;
                        double distance = dtG.getNodePosition(u).dist(dtG.getNodePosition(v));

                        // Assert that nodes are not overlapping (distance must be greater than the sum of radii)
//                        assert (distance + eps) > sizes;
                        assertTrue((distance + eps) > sizes);
                    }
                }
                
                // Second Block: ensure that each couple of nodes in the triangulation are touching
                for (INode u : g.getNeighbors(v)) {
                    if (!u.equals(v)) {
                        double sizes = g.getNodeSize(u).w / 2 + g.getNodeSize(v).w / 2;
                        double distance = dtG.getNodePosition(u).dist(dtG.getNodePosition(v));

                        // Assert that they are almost touching (distance is approximately equal to the sum of radii)
//                        assert (Math.abs((sizes - distance)) < eps);
                        assertTrue(Math.abs((sizes - distance)) < eps);
                    }
                }
            }
        }
    }

}
