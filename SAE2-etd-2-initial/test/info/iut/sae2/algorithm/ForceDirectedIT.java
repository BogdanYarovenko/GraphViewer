package info.iut.sae2.algorithm;

import info.iut.sae2.graphs.Coord;
import info.iut.sae2.graphs.Graph;
import info.iut.sae2.graphs.IGraph;
import info.iut.sae2.graphs.INode;
import info.iut.sae2.properties.LayoutProperty;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author byarovenko
 */
public class ForceDirectedIT {

    private IGraph g;
    private INode n1, n2, n3;
    private LayoutProperty layout;
    private ForceDirected algo;

    @Before
    @Test
    public void setUp() {
        g = new Graph();
        n1 = g.addNode();
        n2 = g.addNode();
        n3 = g.addNode();
        g.addEdge(n1, n2); // edge for attractive force

        layout = new LayoutProperty();
        layout.setNodeValue(n1, new Coord(0, 0));
        layout.setNodeValue(n2, new Coord(2, 0));
        layout.setNodeValue(n3, new Coord(0, 3));

        algo = new ForceDirected();
    }

    @Test
    public void testComputeRepulsive() {
        Coord result = algo.computeRepulsive(g, layout, n1);

        double fR2 = algo.IDEAL / Math.pow(2, 3);
        Coord rep2 = new Coord(-2 * fR2, 0);

        double fR3 = algo.IDEAL / Math.pow(3, 3);
        Coord rep3 = new Coord(0, -3 * fR3);

        Coord expected = Coord.add(rep2, rep3);

        assertEquals(expected.getX(), result.getX(), 0.0001);
        assertEquals(expected.getY(), result.getY(), 0.0001);
    }

    @Test
    public void testComputeGravityForce() {
        Coord barycenter = new Coord(1, 1);
        Coord result = algo.computeGravityForce(g, layout, n1, barycenter);

        double dx = (barycenter.getX() - layout.getNodeValue(n1).getX()) * algo.GRAVITY;
        double dy = (barycenter.getY() - layout.getNodeValue(n1).getY()) * algo.GRAVITY;

        assertEquals(dx, result.getX(), 0.0001);
        assertEquals(dy, result.getY(), 0.0001);
    }

    @Test
    public void testComputeForces() {
        Coord barycenter = new Coord(1, 1);
        Coord result = algo.computeForces(g, layout, n1, barycenter);

        // Attractive
        double fA = 2 / Math.pow(algo.IDEAL, 2);
        Coord attractive = new Coord(2 * fA, 0);

        // Repulsive
        double fR2 = algo.IDEAL / Math.pow(2, 3);
        Coord rep2 = new Coord(-2 * fR2, 0);

        double fR3 = algo.IDEAL / Math.pow(3, 3);
        Coord rep3 = new Coord(0, -3 * fR3);
        Coord repulsive = Coord.add(rep2, rep3);

        // Gravity
        Coord gravity = new Coord(1 * algo.GRAVITY, 1 * algo.GRAVITY);

        // Total
        Coord expected = Coord.add(Coord.add(attractive, repulsive), gravity);

        assertEquals(expected.getX(), result.getX(), 0.0001);
        assertEquals(expected.getY(), result.getY(), 0.0001);
    }
}
