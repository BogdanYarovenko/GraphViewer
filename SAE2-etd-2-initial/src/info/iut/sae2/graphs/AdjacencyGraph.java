package info.iut.sae2.graphs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class AdjacencyGraph extends AbstractGraph {

    private final ArrayList<IEdge> edges = new ArrayList<>();
    private final ArrayList<INode> noeuds = new ArrayList<>();
    private final ArrayList<ArrayList<INode>> matrix;

    public AdjacencyGraph() {
        this.matrix = new ArrayList<>();
    }

    @Override
    public IGraph createGraph() {
        return new AdjacencyGraph();
    }

    @Override
    public INode addNode() {
        INode noeud = new Node(noeuds.size());
        noeuds.add(noeud);
        for (ArrayList<INode> row : matrix) {
            row.add(null);
        }
        ArrayList<INode> newRow = new ArrayList<>();
        for (int i = 0; i < noeuds.size(); i++) {
            newRow.add(null);
        }
        matrix.add(newRow);
        return noeud;
    }

    @Override
    public IEdge addEdge(INode src, INode tgt) {
        IEdge edge = new Edge(tgt, src, true);
        edges.add(edge);
        matrix.get(src.getId()).set(tgt.getId(), tgt);
        return edge;
    }

    @Override
    public void delNode(INode n) {

        int id = n.getId();
        if (id < 0 || id >= noeuds.size() || !noeuds.get(id).equals(n)) {
            return;
        }

        noeuds.remove(id);
        matrix.remove(id);
        for (ArrayList<INode> row : matrix) {
            row.remove(id);
        }
    }

    @Override
    public void delEdge(IEdge e) {
        edges.remove(e);
        INode src = e.source();
        INode tgt = e.target();
        matrix.get(src.getId()).set(tgt.getId(), null);
    }

    @Override
    public INode addNode(INode n) {
        for (INode existing : noeuds) {
            if (existing.getId() == n.getId()) {
                return existing;
            }
        }
        noeuds.add(n);
        for (ArrayList<INode> row : matrix) {
            row.add(null);
        }
        ArrayList<INode> newRow = new ArrayList<>();
        for (INode noeud : noeuds) {
            newRow.add(null);
        }
        matrix.add(newRow);
        return n;
    }

    @Override
    public IEdge addEdge(IEdge e) {
        edges.add(e);
        return e;
    }

    @Override
    public int numberOfNodes() {
        return noeuds.size();
    }

    @Override
    public int numberOfEdges() {
        return edges.size();
    }

    @Override
    public ArrayList<INode> getNeighbors(INode n) {
        HashSet<INode> neighbors = new HashSet<>();
        neighbors.addAll(getSuccesors(n));
        neighbors.addAll(getPredecessors(n));
        return new ArrayList<>(neighbors);
    }

    @Override
    public ArrayList<INode> getSuccesors(INode n) {
        ArrayList<INode> list = new ArrayList<>();
        int id = n.getId();
        for (int j = 0; j < noeuds.size(); j++) {
            if (matrix.get(id).get(j) != null) {
                list.add(noeuds.get(j));
            }

        }
        return list;

    }

    @Override
    public ArrayList<INode> getPredecessors(INode n) {
        ArrayList<INode> list = new ArrayList<>();
        int id = n.getId();
        for (int i = 0; i < noeuds.size(); i++) {
            if (matrix.get(i).get(id) != null) {
                list.add(noeuds.get(i));
            }
        }
        return list;
    }

    @Override
    public ArrayList<IEdge> getInOutEdges(INode n) {
        ArrayList<IEdge> list = getInEdges(n);
        list.addAll(getOutEdges(n));
        return list;
    }

    @Override
    public ArrayList<IEdge> getInEdges(INode n) {
        ArrayList<IEdge> list = new ArrayList<>();
        int id = n.getId();
        for (int i = 0; i < noeuds.size(); i++) {
            if (matrix.get(i).get(id) != null) {
                list.add(new Edge(noeuds.get(i), n, true));
            }
        }
        return list;
    }

    @Override
    public ArrayList<IEdge> getOutEdges(INode n) {
        ArrayList<IEdge> list = new ArrayList<>();
        int id = n.getId();
        for (int j = 0; j < noeuds.size(); j++) {
            if (matrix.get(id).get(j) != null) {
                list.add(new Edge(n, noeuds.get(j), true));
            }
        }
        return list;
    }

    @Override
    public ArrayList<INode> getNodes() {
        return noeuds;
    }

    @Override
    public ArrayList<IEdge> getEdges() {
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
        int srcId = src.getId();
        int tgtId = tgt.getId();

        if (oriented) {

            return matrix.get(srcId).get(tgtId) != null;
        } else {

            return matrix.get(srcId).get(tgtId) != null || matrix.get(tgtId).get(srcId) != null;
        }
    }

    @Override
    public IEdge getEdge(INode src, INode tgt, boolean oriented) {
        return new Edge(tgt, src, oriented);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.edges);
        hash = 53 * hash + Objects.hashCode(this.noeuds);
        hash = 53 * hash + Objects.hashCode(this.matrix);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AdjacencyGraph other = (AdjacencyGraph) obj;
        if (!Objects.equals(this.edges, other.edges)) {
            return false;
        }
        if (!Objects.equals(this.noeuds, other.noeuds)) {
            return false;
        }
        return Objects.equals(this.matrix, other.matrix);
    }

    @Override
    public String toString() {
        return "AdjacencyGraph{" + "edges=" + edges + ", noeuds=" + noeuds + ", matrix=" + matrix + '}';
    }

}
