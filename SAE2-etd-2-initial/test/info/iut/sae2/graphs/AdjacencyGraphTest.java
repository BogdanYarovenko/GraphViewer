package info.iut.sae2.graphs;
import org.junit.Before;

public class AdjacencyGraphTest extends AbstractGraphTest {

    @Before
    @Override
    public void setGraph() {
        g = new AdjacencyGraph();
    }

}
