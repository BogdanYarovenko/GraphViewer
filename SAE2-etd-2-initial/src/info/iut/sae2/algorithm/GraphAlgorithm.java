/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.algorithm;

import info.iut.sae2.graphs.IGraph;
import java.util.Map;

/**
 *
 * @author rbourqui
 */
public abstract class GraphAlgorithm implements Algorithm<IGraph> {
    @Override
    public abstract IGraph apply(IGraph g, Map<String, Object> parameters);
}
