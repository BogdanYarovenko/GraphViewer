/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.algorithm;

import info.iut.sae2.graphs.Coord;
import info.iut.sae2.graphs.IGraph;
import info.iut.sae2.graphs.INode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;

/**
 *
 * @author rbourqui
 */ 
public class DelaunayTriangulation extends GraphAlgorithm {
    /**
     * The graph to be returned, containing the Delaunay triangulation
     */
    IGraph result; 
    
    @Override
    public IGraph apply(IGraph g, Map<String, Object> parameters){
        result = g.createGraph();
        
        Map<Coordinate, INode> coord2Nodes = new HashMap<>();
            
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for(INode n : g.getNodes()){
            result.addNode(n);

            Coord c = g.getNodePosition(n);
            Coordinate jts_c = new Coordinate(c.getX(), c.getY());
            
            result.setNodePosition(n, g.getNodePosition(n));
            result.setNodeSize(n, g.getNodeSize(n));
            coordinates.add(jts_c);
            coord2Nodes.put(jts_c, n);
        }
        
        DelaunayTriangulationBuilder delaunay = new DelaunayTriangulationBuilder();
        delaunay.setSites(coordinates);
        Geometry geom = delaunay.getEdges(new GeometryFactory());
        for(int i = 0; i < geom.getNumGeometries(); ++i){
            Coordinate [] coords = geom.getGeometryN(i).getCoordinates();
            assert coords.length == 2;
                    
            Coordinate c1 = coords[0];
            Coordinate c2 = coords[1];
            assert coord2Nodes.containsKey(c1);
            assert coord2Nodes.containsKey(c2);
            
            INode n1 = coord2Nodes.get(c1);
            INode n2 = coord2Nodes.get(c2);
            
            result.addEdge(n1,n2); 
        }
            
        return result;
    }
}
