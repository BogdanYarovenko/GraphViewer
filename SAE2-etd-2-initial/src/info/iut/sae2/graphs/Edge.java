package info.iut.sae2.graphs;

import java.util.Objects;

public class Edge implements IEdge{
    private final INode target;
    private final INode source;
    private final boolean directed;

    public Edge(INode target, INode source, boolean oriented) {
        this.target = target;
        this.source = source;
        this.directed = oriented;
    }

    @Override
    public INode source() {
        return source;
    }

    @Override
    public INode target() {
        return target;
    }

    public boolean isDirected() {
         return directed;
    }

    @Override
    public String toString() {
        return "Edge(" + source.toString() + " -> " + target.toString() + ")" + (directed ? "Directed" : "Undirected");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Edge edge)) return false;
        return Objects.equals(target, edge.target) && Objects.equals(source, edge.source) && Objects.equals(directed, edge.directed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, source);
    }
}
