package info.iut.sae2.algorithm;

import info.iut.sae2.graphs.IGraph;
import info.iut.sae2.properties.SizeProperty;

import java.util.Map;

public abstract class SizeAlgorithm implements Algorithm<SizeProperty> {
    @Override
    public abstract SizeProperty apply(IGraph g, Map<String, Object> parameters);
}
