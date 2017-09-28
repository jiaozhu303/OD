package com.lenovo.lsc.OD;

import com.google.common.graph.*;
import org.junit.Test;

import java.util.Set;

public class GraphTest extends OdApplicationTests {

    @Test
    public void testGraph() {
        MutableGraph<Integer> graph = GraphBuilder.directed().build();
        graph.addNode(1);
        graph.putEdge(2, 3);  // also adds nodes 2 and 3 if not already present
        Set<Integer> successorsOfTwo = graph.successors(2); // returns {3}
        graph.putEdge(2, 3);  // no effect; Graph does not support parallel edges
    }

    @Test
    public void testValueGraph() {
        MutableValueGraph<Integer, Double> weightedGraph = ValueGraphBuilder.directed().build();
        weightedGraph.addNode(1);
        weightedGraph.putEdgeValue(2, 3, 1.5);  // also adds nodes 2 and 3 if not already present
        weightedGraph.putEdgeValue(3, 5, 1.5);  // edge values (like Map values) need not be unique
        weightedGraph.putEdgeValue(3, 5, 2.0);  // updates the value for (2,3) to 2.0
    }

    @Test
    public void testValueGraphUndirected() {
        MutableValueGraph<String, String> weightedGraph = ValueGraphBuilder.undirected().build();
        weightedGraph.addNode("MEM:4G");
        weightedGraph.putEdgeValue("MEM:4G", "CPU:I5", "");  // also adds nodes 2 and 3 if not already present
        weightedGraph.putEdgeValue("", "", "");  // edge values (like Map values) need not be unique
        weightedGraph.putEdgeValue("", "", "");  // updates the value for (2,3) to 2.0
    }


    @Test
    public void testNetWork() {
        MutableNetwork<Integer, String> network = NetworkBuilder.directed().build();
        network.addNode(1);
        network.addEdge(2, 3, "2->3");  // also adds nodes 2 and 3 if not already present
        Set<Integer> successorsOfTwo = network.successors(2);  // returns {3}
        Set<String> outEdgesOfTwo = network.outEdges(2);   // returns {"2->3"}
        network.addEdge(3, 2, "2->3 too");  // throws; Network disallows parallel edges
        // by default
        network.addEdge(2, 3, "2->3");  // no effect; this edge is already present
        // and connecting these nodes in this order
        Set<String> inEdgesOfFour = network.inEdges(4); // throws; node not in graph
    }

}
