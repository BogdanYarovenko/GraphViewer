/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.algorithm;

import info.iut.sae2.graphs.Coord;
import info.iut.sae2.graphs.IGraph;
import info.iut.sae2.graphs.INode;
import info.iut.sae2.properties.LayoutProperty;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * @author rbourqui
 */
public class ForceDirected extends LayoutAlgorithm {

    /**
     * Constants to be used in the algorithm
     */
    final int WIDTH = 400;
    final int HEIGHT = 400;

    final double MAX_ATTRACTIVE = 300.;
    final double MAX_REPULSIVE = 60.;
    final double IDEAL = 30.;
    final double GRAVITY = .01;

    /**
     * Maximal allowed displacement
     */
    double maxDisplacement = 20.;

    /**
     * The property to store and return the result of the algorithm
     */
    LayoutProperty layout;

    @Override
    public LayoutProperty apply(IGraph g, Map<String, Object> parameters) {
        layout = new LayoutProperty(g.getLayout());

        initDrawing(g, layout, WIDTH, HEIGHT);
        maxDisplacement = 20;

        for (int i = 0; i < g.numberOfNodes(); i++) {
            Coord barycenter = getBarycenter(g, layout);
            g.getNodes().forEach(n -> move(g, layout, n, computeForces(g, layout, n, barycenter), maxDisplacement));
            maxDisplacement = maxDisplacement <= 0 ? 0 : maxDisplacement - 1;
        }

        g.getEdges().forEach(e -> {
            ArrayList<Coord> coords = new ArrayList<>();
            coords.add(layout.getNodeValue(e.source()));
            coords.add(layout.getNodeValue(e.target()));
            layout.setEdgeValue(e, coords);
            });
        
        return layout;
    }

    /**
     * Compute the barycenter of the drawing
     *
     * @param g IGraph
     * @param layout The property storing the computed random coordinates
     * @return The barycenter
     */
    public Coord getBarycenter(IGraph g, LayoutProperty layout) {
        Coord barycenter = new Coord();
        g.getNodes().forEach(n -> barycenter.add(layout.getNodeValue(n)));
        return Coord.mult(barycenter, 1. / g.numberOfNodes());
    }

    /**
     * Initialize the drawing with random coordinates between (0,0) and (width,
     * height)
     *
     * @param g IGraph whose nodes' positions will be initialized
     * @param layout The property storing the computed random coordinates
     * @param width maximal value of the x-coordinates
     * @param height maximal value of the y-coordinates
     */
    void initDrawing(IGraph g, LayoutProperty layout, double width, double height) {
        Random random = new Random();
        g.getNodes().forEach(n -> 
                layout.setNodeValue(n, new Coord(random.nextDouble(width), random.nextDouble(height))));
    }

    /**
     * Compute the attractive forces on a given node
     *
     * @param g IGraph containing the node
     * @param layout The property storing the nodes'coordinates
     * @param n Node on which the forces will be applied to
     * @return the total attractive force
     */
    Coord computeAttractive(IGraph g, LayoutProperty layout, INode n) {
        Coord sommeAttraction = new Coord();
        Coord coordV = layout.getNodeValue(n);

        g.getNeighbors(n).forEach(u -> {
            Coord coordU = layout.getNodeValue(u);

            Coord vector = Coord.minus(coordU, coordV);
            vector.mult(coordV.dist(coordU) / Math.pow(IDEAL, 2)); // Apply Attractive formula
            sommeAttraction.add(vector);
        });

        if (sommeAttraction.norm() > MAX_ATTRACTIVE) {
            // Compute unit vector
            double dist = sommeAttraction.norm();
            sommeAttraction.setX(sommeAttraction.getX() / dist);
            sommeAttraction.setY(sommeAttraction.getY() / dist);

            // Vector of norm MAX_ATTRACTIVE
            sommeAttraction.mult(MAX_ATTRACTIVE);
        }

        return sommeAttraction;
    }

    /**
     * Compute the repulsive forces on a given node
     *
     * @param g IGraph containing the node of interest
     * @param layout The property storing the nodes'coordinates
     * @param n Node on which the forces will be applied to
     * @return the total repulsive force
     */
    Coord computeRepulsive(IGraph g, LayoutProperty layout, INode n) {
        Coord sommeRepulsive = new Coord();
        Coord coordV = layout.getNodeValue(n);

        g.getNodes().forEach(u -> {
            Coord coordU = layout.getNodeValue(u);
            if (!coordU.equals(coordV)) {
                Coord vector = Coord.minus(coordV, coordU);
                vector.mult(IDEAL / Math.pow(coordV.dist(coordU), 3)); // Apply Repulsive formula
                sommeRepulsive.add(vector);
            }
        });

        if (sommeRepulsive.norm() > MAX_REPULSIVE) {
            // Compute unit vector
            double dist = sommeRepulsive.norm();
            sommeRepulsive.setX(sommeRepulsive.getX() / dist);
            sommeRepulsive.setY(sommeRepulsive.getY() / dist);

            // Vector of norm MAX_REPULSIVE
            sommeRepulsive.mult(MAX_REPULSIVE);
        }

        return sommeRepulsive;
    }

    /**
     * Compute the total force on a given node
     *
     * @param g IGraph containing the node of interest
     * @param layout The property storing the nodes' coordinates
     * @param n Node on which the forces will be applied to
     * @param barycenter The barycenter
     * @return the total (attractive and repulsive force
     */
    Coord computeForces(IGraph g, LayoutProperty layout, INode n, Coord barycenter) {
        return Coord.add(Coord.add(computeAttractive(g, layout, n), computeRepulsive(g, layout, n)), computeGravityForce(g, layout, n, barycenter));
    }

    /**
     * Compute the gravity force on a given node
     *
     * @param g IGraph containing the node of interest
     * @param layout The property storing the nodes'coordinates
     * @param n Node on which the forces will be applied to
     * @param barycenter Coordinates of the barycenter of the nodes' positions
     */
    Coord computeGravityForce(IGraph g, LayoutProperty layout, INode n, Coord barycenter) {
        return Coord.mult(Coord.minus(barycenter, layout.getNodeValue(n)), GRAVITY);
    }

    /**
     * Move a given node according to a given position and a displacement
     *
     * @param g IGraph containing the node of interest
     * @param layout The property storing the nodes'coordinates
     * @param n Node to be moved
     * @param disp the desired displacement
     * @param maxMove the maximal allowed displacement
     */
    void move(IGraph g, LayoutProperty layout, INode n, Coord disp, double maxMove) {
        Coord coordN = layout.getNodeValue(n);
        Coord dispCopy = new Coord(disp); // Make a copy

        if (disp.norm() > maxMove) { // in the case the displacement is longer than maxMove
            dispCopy.mult(1 / disp.norm());
            dispCopy.mult(maxMove);
        }

        coordN.add(dispCopy);
        g.setNodePosition(n, coordN); // Should not be needed but for safety purpose
    }

}
