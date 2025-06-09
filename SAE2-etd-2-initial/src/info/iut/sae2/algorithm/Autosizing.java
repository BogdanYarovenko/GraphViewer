package info.iut.sae2.algorithm;

import info.iut.sae2.graphs.IGraph;
import info.iut.sae2.graphs.INode;
import info.iut.sae2.graphs.Size;
import info.iut.sae2.properties.SizeProperty;

import java.util.HashMap;
import java.util.Map;

public class Autosizing extends SizeAlgorithm {
    @Override
    public SizeProperty apply(IGraph g, Map<String, Object> parameters) {
        DelaunayTriangulation dt = new DelaunayTriangulation();
        IGraph dtG = dt.apply(g, parameters);

        // Set back to default size before in case of Force Directed already applied to the graph
        dtG.setAllNodesSizes(SizeProperty.DEFAULT_NODE_SIZE); 

        boolean stop;
        do {
            Map<INode, Double> variations = getSizeVariations(dtG);
            stop = applySizeVariations(dtG, variations);
        } while (!stop);
        
        return dtG.getSizes(); // As size property object
    }

    /**
     * Apply the given size variations to the graph
     * @param dtG The Delaunay Triangulation Result Graph
     * @param variations The variations to apply
     * @return If the algorithm has to stop
     */
    private boolean applySizeVariations(IGraph dtG, Map<INode, Double> variations) {
        boolean stop = true;
        for (INode u : dtG.getNodes()) { // Apply size to each node
            double before = dtG.getNodeSize(u).w;
            double after = variations.get(u);
            dtG.setNodeSize(u, new Size(after + before, after + before));
            if (Math.abs(after) >= .001) { // Accept such a precision
                stop = false; // Continue
            }
        }
        return stop;
    }

    /**
     * Get the size variations to apply for each node of a given graph
     * @param dtG The Delaunay Triangulation Result Graph
     * @return Size variations for each node of the graph
     */
    private Map<INode, Double> getSizeVariations(IGraph dtG) {
        HashMap<INode, Double> variations = new HashMap<>();
        for (INode u : dtG.getNodes()) { // Compute available space for each Node
            variations.put(u, getMinAvailableSpace(dtG, u));
        }
        return variations;
    }

    /**
     * Computes the minimum available space for a given node
     * @param dtG The Delaunay Triangulation Result Graph
     * @param u Given Node
     * @return Its minimum available space
     */
    private double getMinAvailableSpace(IGraph dtG, INode u) {
        double after = Double.MAX_VALUE;
        for (INode v : dtG.getNeighbors(u)) {
            if (!u.equals(v)) {
                double availableSpace = getAvailableSpace(dtG, u, v);
                if (availableSpace < after) after = availableSpace; // Replace if it's less than the last minimum
            }
        }
        return after;
    }

    /**
     * Compute available space between two nodes
     * @param dtG The Delaunay Triangulation Result Graph
     * @param u First Node
     * @param v Second Node
     * @return The available space between the two nodes
     */
    private double getAvailableSpace(IGraph dtG, INode u, INode v) {
        return (dtG.getNodePosition(u).dist(dtG.getNodePosition(v)) - dtG.getNodeSize(u).w/2 - dtG.getNodeSize(v).w/2) / 2;
    }

    
}
