package info.iut.sae2.graphs;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph extends AbstractGraph {

    HashMap<INode, ArrayList<INode>> g;
    
    public Graph() {
        super();
        g = new HashMap<>();
    }

    @Override
    public IGraph createGraph() {
        return new Graph();
    }

    @Override
    public INode addNode() {
        return addNode(new Node(g.size()));
    }

    @Override
    public IEdge addEdge(INode src, INode tgt) {
        return addEdge(new Edge(tgt, src, true));
    }

    @Override
    public void delNode(INode n) {
        g.remove(n);
        g.values().forEach((iNodes) -> iNodes.remove(n));
    }

    @Override
    public void delEdge(IEdge e) {
        g.get(e.source()).remove(e.target());
    }

    @Override
    public INode addNode(INode n) {
        g.put(n, new ArrayList<>());
        return n;
    }

    @Override
    public IEdge addEdge(IEdge e) {
        g.get(e.source()).add(e.target());
        return e;
    }

    @Override
    public int numberOfNodes() {
        return g.size();
    }

    @Override
    public int numberOfEdges() {
        int sum = 0;
        for (ArrayList<INode> nodes : g.values()) {
            sum += nodes.size();
        }
        return sum;
    }

    @Override
    public ArrayList<INode> getNeighbors(INode n) {
        ArrayList<INode> neighbors = new ArrayList<>(getSuccesors(n));
        neighbors.addAll(getPredecessors(n));
        return neighbors;
    }

    @Override
    public ArrayList<INode> getSuccesors(INode n) {
        return g.get(n);
    }

    @Override
    public ArrayList<INode> getPredecessors(INode n) {
        ArrayList<INode> predecessors = new ArrayList<>();
        g.forEach((iNode, iNodes) -> {
            if (iNodes.contains(n)) predecessors.add(iNode);
        });
        return predecessors;
    }

    @Override
    public ArrayList<IEdge> getInOutEdges(INode n) {
        ArrayList<IEdge> edges = getInEdges(n);
        edges.addAll(getOutEdges(n));
        return edges;
    }

    @Override
    public ArrayList<IEdge> getInEdges(INode n) {
        ArrayList<IEdge> edges = new ArrayList<>();
        g.forEach((iNode, iNodes) -> {
            if (iNodes.contains(n)) edges.add(new Edge(iNode, n, true));
        });
        return edges;
    }

    @Override
    public ArrayList<IEdge> getOutEdges(INode n) {
        ArrayList<IEdge> edges = new ArrayList<>();
        g.get(n).forEach(iNode -> edges.add(new Edge(iNode, n, true)));
        return edges;
    }

    @Override
    public ArrayList<INode> getNodes() {
        return new ArrayList<>(g.keySet());
    }

    @Override
    public ArrayList<IEdge> getEdges() {
        ArrayList<IEdge> edges = new ArrayList<>();
        g.keySet().forEach(iNode -> edges.addAll(getOutEdges(iNode)));
        return edges;
    }

    @Override
    public INode source(IEdge e) {
        return e.source();
    }

    @Override
    public INode target(IEdge e) {
        return e.target();
    }

    @Override
    public int inDegree(INode n) {
        return getInEdges(n).size();
    }

    @Override
    public int outDegree(INode n) {
        return getOutEdges(n).size();
    }

    @Override
    public int degree(INode n) {
        return inDegree(n) + outDegree(n);
    }

    @Override
    public boolean existEdge(INode src, INode tgt, boolean oriented) {
        ArrayList<IEdge> edges = getEdges();
        return
            edges.contains(new Edge(tgt, src, true)) || 
            edges.contains(new Edge(tgt, src, false)) || 
            (!oriented && edges.contains(new Edge(src, tgt, true)));
    }

    @Override
    public IEdge getEdge(INode src, INode tgt, boolean oriented) {
        return new Edge(tgt, src, oriented);
    }
}
