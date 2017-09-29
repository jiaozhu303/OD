package com.lenovo.lsc.OD.OD;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.graph.MutableValueGraph;

import java.util.*;

public class TraversalDJ {

    private final String sourceNode;
    private final MutableValueGraph<String, String> graph;

    public TraversalDJ(String sourceNode, MutableValueGraph<String, String> graph) {
        this.sourceNode = sourceNode;
        this.graph = graph;
    }

    public Set<String> traversal() {

        Set<String> nodes = graph.nodes();
        if(!nodes.contains(sourceNode)) {
            throw new IllegalArgumentException(sourceNode +  " is not in this graph!");
        }

        Map<String,Integer> visitedNodes = new HashMap<>();
        Queue<String> queue = new LinkedList<String>();

        Set<String> twoHopNeighbors = new HashSet<>();
        for (String neighbor : graph.adjacentNodes(sourceNode)) {
            twoHopNeighbors.addAll(graph.adjacentNodes(neighbor));
        }
        twoHopNeighbors.remove(sourceNode);
        return twoHopNeighbors;



    }


}
