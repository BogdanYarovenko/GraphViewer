/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.graphs;

import info.iut.sae2.algorithm.Autosizing;
import info.iut.sae2.algorithm.ForceDirected;
import info.iut.sae2.properties.LayoutProperty;
import info.iut.sae2.properties.SizeProperty;
import java.util.ArrayList;

/**
 *
 * @author rbourqui
 */
public abstract class AbstractGraph implements IGraph {

    /**
     * LayoutProperty associated to the graph nodes and edges
     */
    LayoutProperty layout;
    
    
    /**
     * SizeProperty associated to the graph nodes and edges
     */
    SizeProperty size;

    /**
     * Default constructor
     */
    AbstractGraph() {
        layout = new LayoutProperty();
        size = new SizeProperty();
    }

    @Override
    public abstract IGraph createGraph();

    @Override
    public abstract INode addNode();

    @Override
    public abstract IEdge addEdge(INode src, INode tgt);

    @Override
    public abstract void delNode(INode n);

    @Override
    public abstract void delEdge(IEdge e);

    @Override
    public abstract INode addNode(INode n);

    @Override
    public abstract IEdge addEdge(IEdge e);

    @Override
    public abstract int numberOfNodes();

    @Override
    public abstract int numberOfEdges();

    @Override
    public abstract ArrayList<INode> getNeighbors(INode n);

    @Override
    public abstract ArrayList<INode> getSuccesors(INode n);

    @Override
    public abstract ArrayList<INode> getPredecessors(INode n);

    @Override
    public abstract ArrayList<IEdge> getInOutEdges(INode n);

    @Override
    public abstract ArrayList<IEdge> getInEdges(INode n);

    @Override
    public abstract ArrayList<IEdge> getOutEdges(INode n);

    @Override
    public abstract ArrayList<INode> getNodes();

    @Override
    public abstract ArrayList<IEdge> getEdges();

    @Override
    public abstract INode source(IEdge e);

    @Override
    public abstract INode target(IEdge e);

    @Override
    public abstract int inDegree(INode n);

    @Override
    public abstract int outDegree(INode n);

    @Override
    public abstract int degree(INode n);

    @Override
    public abstract boolean existEdge(INode src, INode tgt, boolean oriented);

    @Override
    public abstract IEdge getEdge(INode src, INode tgt, boolean oriented);

    @Override
    public SizeProperty getSizes() {
        return size;
    }

    @Override
    public Size getNodeSize(INode n) {
        return size.getNodeValue(n);
    }

    @Override
    public Double getEdgeWidth(IEdge e) {
        return size.getEdgeValue(e);
    }

    @Override
    public void setNodeSize(INode n, Size s) {
        size.setNodeValue(n, s);
    }

    @Override
    public void setEdgeWidth(IEdge e, Double width) {
        size.setEdgeValue(e, width);
    }

    @Override
    public void setAllNodesSizes(Size s) {
        size.setAllNodesValues(s);
    }

    @Override
    public void setAllEdgesWidths(Double width) {
        size.setAllEdgesValues(width);
    }

    @Override
    public LayoutProperty getLayout() {
        return layout;
    }

    @Override
    public Coord getNodePosition(INode n) {
        return layout.getNodeValue(n);
    }

    @Override
    public ArrayList<Coord> getEdgePosition(IEdge e) {
        return layout.getEdgeValue(e);
    }

    @Override
    public void setNodePosition(INode n, Coord c) {
        layout.setNodeValue(n, c);
    }

    @Override
    public void setEdgePosition(IEdge e, ArrayList<Coord> bends) {
        layout.setEdgeValue(e, bends);
    }

    @Override
    public void setAllNodesPositions(Coord c) {
        layout.setAllNodesValues(c);
    }

    @Override
    public void setAllEdgesPositions(ArrayList<Coord> bends) {
        layout.setAllEdgesValues(bends);
    }

    @Override
    public ArrayList<Coord> getBoundingBox() {
        double minX, minY, maxX, maxY;
        minX = minY = Double.MAX_VALUE;
        maxX = maxY = -Double.MAX_VALUE;

        for (INode n : this.getNodes()) {
            Size s = this.getNodeSize(n);
            Coord c = this.getNodePosition(n);

            minX = Math.min(minX, c.getX() - s.getW() / 2);
            minY = Math.min(minY, c.getY() - s.getH() / 2);
            maxX = Math.max(maxX, c.getX() + s.getW() / 2);
            maxY = Math.max(maxY, c.getY() + s.getH() / 2);
        }
        for (IEdge e : this.getEdges()) {
            ArrayList<Coord> bends = this.getEdgePosition(e);
            if (bends == null) {
                continue;
            }
            for (Coord c : bends) {
                minX = Math.min(minX, c.getX());
                minY = Math.min(minY, c.getY());
                maxX = Math.max(maxX, c.getX());
                maxY = Math.max(maxY, c.getY());
            }
        }

        Coord cMax = new Coord(maxX, maxY);
        Coord cMin = new Coord(minX, minY);

        ArrayList<Coord> bb = new ArrayList<>();
        bb.add(cMin);
        bb.add(cMax);
        return bb;
    }

    @Override
    public void forceDirected() {
        ForceDirected sm = new ForceDirected();
        layout = sm.apply(this, null);
    }

    @Override
    public void autoSize() {
        Autosizing as = new Autosizing();
        size = as.apply(this, null);
    }

}
