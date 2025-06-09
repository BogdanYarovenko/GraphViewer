package info.iut.sae2.graphs;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractGraphTest {

    /**
     * The graph instance used in tests. Must be initialized by subclasses.
     */
    protected IGraph g;

    /**
     * Initializes a specific graph implementation. This method must be
     * implemented by each subclass.
     */
    public abstract void setGraph();

    /**
     * Initializes the graph instance before each test.
     */
    @Before
    public void init() {
        setGraph();
    }

    /**
     * Tests that createGraph returns a new empty graph.
     */
    @Test
    public void createGraphTest() {
        IGraph newGraph = g.createGraph();
        assertNotNull(newGraph);
        assertEquals(0, newGraph.numberOfNodes());
        assertEquals(0, newGraph.numberOfEdges());
    }

    /**
     * Tests adding nodes to the graph, including nodes with custom and
     * duplicate IDs.
     */
    @Test
    public void addNodeTest() {
        {
            assertEquals(0, g.numberOfNodes());
            g.addNode();
            assertEquals(0, g.getNodes().getFirst().getId());
            assertEquals(1, g.numberOfNodes());
        }
        {
            INode node = new Node(200);
            g.addNode(node);
            assertEquals(200, g.getNodes().get(1).getId());
            assertEquals(2, g.numberOfNodes());
        }
        {
            // ID of the node can be negative but not duplicated 
            INode negativeNode = new Node(-1);
            g.addNode(negativeNode);
            assertTrue(g.getNodes().contains(negativeNode));
            // Have the same ID

            INode duplicateNode = new Node(-1);

            assertEquals(3, g.numberOfNodes());

            g.addNode(duplicateNode);

            assertEquals(3, g.numberOfNodes());
        }
    }

    /**
     * Tests adding an edge between two nodes.
     */
    @Test
    public void addEdgeTest() {
        {
            assertEquals(0, g.numberOfNodes());
            assertEquals(0, g.numberOfEdges());
            INode node1 = g.addNode();
            INode node2 = g.addNode();
            g.addEdge(node1, node2);
            assertEquals(1, g.numberOfEdges());
            assertTrue(g.existEdge(node1, node2, false));
        }

    }

    /**
     * Tests retrieving nodes after insertion.
     */
    @Test
    public void getNodesTest() {
        assertEquals(0, g.numberOfNodes());
        INode firstNode = g.addNode();
        g.addNode();
        assertEquals(2, g.numberOfNodes());
        
        INode negativeNode = new Node(-1);
        INode duplicateNode = new Node(-1);
        
        g.addNode(negativeNode);
        assertEquals(3, g.numberOfNodes());

        g.addNode(duplicateNode);
        assertEquals(3, g.numberOfNodes());
        
        g.delNode(firstNode);
        assertEquals(2, g.numberOfNodes());
    }

    /**
     * Tests retrieving edges, both created via addEdge and manually.
     */
    @Test
    public void getEdgesTest() {
        assertEquals(0, g.numberOfEdges());
        INode node1 = g.addNode();
        INode node2 = g.addNode();
        g.addEdge(node1, node2);
        
        IEdge edge2 = new Edge(node2, node1, false);
        g.addEdge(edge2);
        assertEquals(2, g.numberOfEdges());
        
        g.addEdge(node1, node2);
        assertEquals(3, g.numberOfEdges()); // We can have multiple edges for a pair of nodes
    }

    /**
     * Tests the behavior of the getNeighbors method in a graph implementation.
     *
     */
    @Test
    public void getNeighbors() {
        // Create initial nodes
        INode node1 = g.addNode();
        INode node2 = g.addNode();
        INode node3 = g.addNode();
        INode node4 = g.addNode(); // will remain unconnected
        INode node5 = g.addNode();

        // Create edges
        g.addEdge(node1, node2);
        g.addEdge(node1, node3);
        g.addEdge(node2, node5);

        // Check neighbor counts
        assertEquals(2, g.getNeighbors(node1).size());
        assertEquals(2, g.getNeighbors(node2).size());
        assertEquals(1, g.getNeighbors(node3).size());
        assertEquals(0, g.getNeighbors(node4).size());
        assertEquals(1, g.getNeighbors(node5).size());

        // Check specific neighbors
        assertTrue(g.getNeighbors(node1).contains(node2));
        assertTrue(g.getNeighbors(node1).contains(node3));

        assertTrue(g.getNeighbors(node2).contains(node1));
        assertTrue(g.getNeighbors(node2).contains(node5));

        assertTrue(g.getNeighbors(node3).contains(node1));
        assertTrue(g.getNeighbors(node5).contains(node2));

        // Check that node4 has no neighbors
        assertTrue(g.getNeighbors(node4).isEmpty());

        // Ensure no unintended neighbors
        assertFalse(g.getNeighbors(node1).contains(node5));
        assertFalse(g.getNeighbors(node3).contains(node2));
        assertFalse(g.getNeighbors(node5).contains(node1));
    }

    /**
     * Tests removing a node from the graph.
     */
    @Test
    public void deleteNodeTest() {
        assertEquals(0, g.numberOfNodes());
        
        INode n = g.addNode();
        assertEquals(1, g.numberOfNodes());
        
        g.delNode(n);
        assertTrue(g.getNodes().isEmpty());
        assertEquals(0, g.numberOfNodes());
        
        g.delNode(n); // Trying to remove a node that is already removed
        assertTrue(g.getNodes().isEmpty());
        assertEquals(0, g.numberOfNodes());
    }

    /**
     * Tests removing an edge from the graph.
     */
    @Test
    public void deleteEdgeTest() {
        {
            INode node1 = g.addNode();
            INode node2 = g.addNode();
            IEdge edge = g.addEdge(node1, node2);
            assertEquals(1, g.numberOfEdges());
            
            g.delEdge(edge);
            assertTrue(g.getEdges().isEmpty());
            assertEquals(0, g.numberOfEdges());

            g.delEdge(edge);
            assertTrue(g.getEdges().isEmpty());
            assertEquals(0, g.numberOfEdges());
        }
    }

    /**
     * Tests retrieving successors and predecessors of nodes.
     */
    @Test
    public void getSuccesorsTest() {
        INode node1 = g.addNode();
        INode node2 = g.addNode();
        INode node3 = g.addNode();

        g.addEdge(node1, node2);
        g.addEdge(node1, node3);

        assertEquals(2, g.getSuccesors(node1).size());
        assertEquals(0, g.getPredecessors(node1).size());
        assertTrue(g.getSuccesors(node1).contains(node2));
        assertTrue(g.getSuccesors(node1).contains(node3));
        assertTrue(g.getPredecessors(node2).contains(node1));
        assertTrue(g.getPredecessors(node3).contains(node1));
    }
    
    @Test
    public void getPredecessorsTest() {
        INode node1 = g.addNode();
        INode node2 = g.addNode();
        INode node3 = g.addNode();

        g.addEdge(node2, node1);
        g.addEdge(node3, node1);

        assertEquals(2, g.getPredecessors(node1).size());
        assertEquals(0, g.getSuccesors(node1).size());
        assertTrue(g.getPredecessors(node1).contains(node2));
        assertTrue(g.getPredecessors(node1).contains(node3));
        assertTrue(g.getSuccesors(node2).contains(node1));
        assertTrue(g.getSuccesors(node3).contains(node1));
    }

    /**
     * Tests retrieving incoming and outgoing edges of nodes.
     */
    @Test
    public void getInOutEdgesTest() {
        {
            INode node1 = g.addNode();
            INode node2 = g.addNode();
            INode node3 = g.addNode();
            INode node4 = g.addNode();
            g.addEdge(node2, node1);
            g.addEdge(node3, node1);
            g.addEdge(node4, node1);

            assertEquals(3, g.getInEdges(node1).size());
            assertEquals(0, g.getOutEdges(node1).size());
        }
        {
            INode node4 = g.addNode();
            INode node5 = g.addNode();
            INode node6 = g.addNode();
            g.addEdge(node5, node4);
            g.addEdge(node4, node5);
            g.addEdge(node6, node4);
            g.addEdge(node4, node4);
            assertEquals(3, g.getInEdges(node4).size());
            assertEquals(2, g.getOutEdges(node4).size());
        }
        {
            // There is no edge to this node
            INode node7 = g.addNode();
            assertEquals(0, g.getInEdges(node7).size());
            assertEquals(0, g.getOutEdges(node7).size());
        }
    }

    /**
     * Tests the existence of edges in directed and undirected scenarios.
     */
    @Test
    public void existEdgeTest() {
        {
            // Directed case
            INode node1 = g.addNode();
            INode node2 = g.addNode();
            assertFalse(g.existEdge(node1, node2, true));
            assertFalse(g.existEdge(node2, node1, true));
            g.addEdge(node1, node2);
            assertTrue(g.existEdge(node1, node2, true));
            assertFalse(g.existEdge(node2, node1, true)); // opposite directed

        }
        {
            // Undirected case
            INode node3 = g.addNode();
            INode node4 = g.addNode();
            assertFalse(g.existEdge(node3, node4, false));
            assertFalse(g.existEdge(node4, node3, false));
            g.addEdge(node3, node4);
            assertTrue(g.existEdge(node3, node4, false));
            assertTrue(g.existEdge(node4, node3, false));
        }
    }

    /**
     * Tests source and target node retrieval from an edge.
     */
    @Test
    public void sourceAndTargetTest() {
        {
            INode node1 = g.addNode();
            INode node2 = g.addNode();
            IEdge edge = g.addEdge(node1, node2);

            assertEquals(node1, g.source(edge));
            assertEquals(node2, g.target(edge));
        }
        {
            // Loop
            INode node3 = g.addNode();
            IEdge edge2 = g.addEdge(node3, node3);
            assertEquals(node3, g.source(edge2));
            assertEquals(node3, g.target(edge2));
        }
    }

    /**
     * Tests the degree of nodes in directed and undirected graphs.
     */
    @Test
    public void degreeTest() {
        // Undirected graph
        {
            INode node1 = g.addNode();
            INode node2 = g.addNode();
            g.addEdge(node1, node2);
            g.addEdge(node2, node1);
            assertEquals(2, g.degree(node1));
            assertEquals(2, g.degree(node2));
        }
        // Directed graph
        {
            INode node3 = g.addNode();
            INode node4 = g.addNode();
            g.addEdge(node3, node4);
            assertEquals(1, g.degree(node3));
            assertEquals(1, g.degree(node4));
        }
    }
}
