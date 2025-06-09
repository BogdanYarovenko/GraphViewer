/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package info.iut.sae2.viewer;

import info.iut.sae2.graphs.GraphDataStructure;
import info.iut.sae2.graphs.GraphLoader;
import info.iut.sae2.graphs.IGraph;
import info.iut.sae2.graphs.Size;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import static java.lang.Math.min;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

public class GraphViewer extends JFrame {

    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    final int FRAME_WIDTH = 800;

    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    final int FRAME_HEIGHT = 800;

    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    final int SLIDER_WIDTH = 100;

    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    final int BUTTON_HEIGHT = 40;

    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    IGraph currentGraph;

    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    IGraph graph;

    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    GraphDataStructure graphDataStructure;

    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    GraphCanvas gc;
    
    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    public GraphViewer(String title) {
        super(title);
        graph = null;
        currentGraph = null;
        // spanningTree = null;
        graphDataStructure = GraphDataStructure.ADJACENCY_LISTS;
        buildUI();
    }

    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    private void buildUI() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(null);

        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel filenameLabel = new JLabel("Graph Filename:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 20, 0, 0);
        contentPane.add(filenameLabel, c);

        String filenames[] = {"Simple sample", "Simple sample 2","Simple sample 3","Simple sample 4", "Complete", "Zachary Karate Club", "Hollywood", "US Migration", "UBC CS website", "Air traffic 2000"};
        JComboBox fileChooser = new JComboBox<>(filenames);
        fileChooser.setSelectedIndex(-1);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0, 3, 0, 0);
        contentPane.add(fileChooser, c);

        JLabel infos = new JLabel(String.format("%d Nodes and %d Edges", 0, 0));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 1.;
        c.insets = new Insets(0, 3, 0, 0);
        contentPane.add(infos, c);

        String[] dataStructure = {"Adjacency Lists", "AdjacencyMatrix"};
        JComboBox dataStructureChooser = new JComboBox<>(dataStructure);
        dataStructureChooser.setSelectedIndex(0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 0;
        c.insets = new Insets(0, 3, 0, 0);
        contentPane.add(dataStructureChooser, c);

        
        JLabel timeLabel = new JLabel("Computation Time");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 0;
        c.weightx = 1.;
        c.insets = new Insets(0, 10, 0, 0);
        contentPane.add(timeLabel, c);
        
        JLabel computationTime = new JLabel("---- (ms)");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 0;
        c.weightx = 1.;
        contentPane.add(computationTime, c);
        
        JLabel nodeLabel = new JLabel("Node Size");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        c.insets = new Insets(0, 10, 0, 0);
        contentPane.add(nodeLabel, c);

        JSlider nodeSize = new JSlider(JSlider.HORIZONTAL, 1, 500, 10);
        nodeSize.setMinimumSize(new Dimension(SLIDER_WIDTH, BUTTON_HEIGHT));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 1;
        contentPane.add(nodeSize, c);

        JLabel edgeLabel = new JLabel("Edge transparency");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 1;
        contentPane.add(edgeLabel, c);

        JSlider edgeTransp = new JSlider(JSlider.HORIZONTAL, 0, 255, 60);
        edgeTransp.setMinimumSize(new Dimension(SLIDER_WIDTH, BUTTON_HEIGHT));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 1;
        contentPane.add(edgeTransp, c);

        JCheckBox smooth = new JCheckBox("Smooth", true);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 6;
        c.gridy = 1;
        contentPane.add(smooth, c);

        JButton FDButton = new JButton();
        FDButton.setText("Redraw");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        contentPane.add(FDButton, c);

        JButton ASButton = new JButton();
        ASButton.setText("Autosizing");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        contentPane.add(ASButton, c);

        gc = new GraphCanvas();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1.;
        c.weighty = 1.;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 0, 0);
        contentPane.add(gc, c);

        fileChooser.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String item = (String) e.getItem();
                long startTime = System.nanoTime();
                loadGraph(item);
                long stopTime = System.nanoTime();
                displayComputationTime(computationTime, startTime, stopTime);
                infos.setText(String.format("%d Nodes and %d Edges", currentGraph.numberOfNodes(), currentGraph.numberOfEdges()));
            }
        });

        dataStructureChooser.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String item = (String) e.getItem();
                changeDataStructure(item);
                if (fileChooser.getSelectedIndex() != -1) {
                    long startTime = System.nanoTime();
                    loadGraph((String) fileChooser.getSelectedObjects()[0]);
                    long stopTime = System.nanoTime();
                    displayComputationTime(computationTime, startTime, stopTime);
                }
            }
        });

        smooth.addItemListener((ItemEvent e) -> {
            gc.setSmooth(e.getStateChange() == ItemEvent.SELECTED);
            gc.repaint();
        });

        edgeTransp.addChangeListener((ChangeEvent e) -> {
            gc.setEdgeTransparency(edgeTransp.getValue());
            gc.repaint();
        });

        nodeSize.addChangeListener((ChangeEvent e) -> {
            double diameter = nodeSize.getValue() / 10.;
            graph.setAllNodesSizes(new Size(diameter, diameter));
            gc.setRadius(nodeSize.getValue());
            gc.repaint();
        });

        FDButton.addActionListener((ActionEvent e) -> {
            long startTime = System.nanoTime();
            forceDirected();
            long stopTime = System.nanoTime();
            displayComputationTime(computationTime, startTime, stopTime);
        });

        ASButton.addActionListener((ActionEvent e) -> {
            long startTime = System.nanoTime();
            autoSizing();
            long stopTime = System.nanoTime();
            displayComputationTime(computationTime, startTime, stopTime);
        });
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                updateGraphView();
            }
        });
    }

    
    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    private void displayComputationTime(JLabel computationTime, long startTime, long stopTime){
        String time = String.format("%f", (stopTime-startTime)/1000000.);
        StringBuilder timeBuilder = new StringBuilder(time.substring(0, min(6, time.length())));
        timeBuilder.append(" (ms)");
        computationTime.setText(timeBuilder.toString());
    }
    
    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    public void changeDataStructure(String dataStructure) {
        if (dataStructure.equals("Adjacency Lists")) {
            graphDataStructure = GraphDataStructure.ADJACENCY_LISTS;
        } else if (dataStructure.equals("AdjacencyMatrix")) {
            graphDataStructure = GraphDataStructure.ADJACENCY_MATRIX;
        }
    }

    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    public void forceDirected() {
        if (graph == null) {
            return;
        }
        graph.forceDirected();
        currentGraph = graph;
        updateGraphView();
    }

    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    public void autoSizing() {
        if (graph == null) {
            return;
        }
        graph.autoSize();
        currentGraph = graph;
        updateGraphView();
    }

    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    public void clearGraph() {
        if (graph == null) {
            return;
        }
        currentGraph = graph;
        currentGraph.setAllEdgesPositions(new ArrayList<>());
        updateGraphView();
    }

    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    public void loadGraph(String filename) {
        //spanningTree = null;
        switch (filename) {
            case "Simple sample" ->
                graph = GraphLoader.loadFromFile("dataset/simple_nodes.csv", "dataset/simple_edges.csv", graphDataStructure);
            case "Simple sample 2" ->
                graph = GraphLoader.loadFromFile("dataset/testCJ_nodes.csv", "dataset/testCJ_edges.csv", graphDataStructure);
            case "Simple sample 3" ->
                graph = GraphLoader.loadFromFile("dataset/testEB_nodes.csv", "dataset/testEB_edges.csv", graphDataStructure);
            case "Simple sample 4" ->
                graph = GraphLoader.loadFromFile("dataset/testEBB_nodes.csv", "dataset/testEBB_edges.csv", graphDataStructure);
            case "Complete" ->
                graph = GraphLoader.loadFromFile("dataset/k9_nodes.csv", "dataset/k9_edges.csv", graphDataStructure);
            case "Zachary Karate Club" ->
                graph = GraphLoader.loadFromFile("dataset/zachary_karate_club_nodes.csv", "dataset/zachary_karate_club_edges.csv", graphDataStructure);
            case "Hollywood" ->
                graph = GraphLoader.loadFromFile("dataset/hollywood_nodes.csv", "dataset/hollywood_edges.csv", graphDataStructure);
            case "US Migration" ->
                graph = GraphLoader.loadFromFile("dataset/us_migrations_nodes.csv", "dataset/us_migrations_edges.csv", graphDataStructure);
            case "UBC CS website" ->
                graph = GraphLoader.loadFromFile("dataset/ubc2K_nodes.csv", "dataset/ubc2K_edges.csv", graphDataStructure);
            default ->
                graph = GraphLoader.loadFromFile("dataset/air_traffic_nodes.csv", "dataset/air_traffic_edges.csv", graphDataStructure);
        }
        currentGraph = graph;

        updateGraphView();
    }

    /**
     * No javadoc is provided, there is no need to understand/modify this part
     * of the code
     */
    private void updateGraphView() {
        gc.setGraph(currentGraph);
        gc.repaint();
    }
}
