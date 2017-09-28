package com.lenovo.lsc.OD.OD;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.graph.MutableValueGraph;

import java.util.*;

public class Traversal {
    private final String sourceNode;
    private final MutableValueGraph<String, String> graph;

    public Traversal(String sourceNode, MutableValueGraph<String, String> graph) {
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

        queue.offer(sourceNode);

        while (!queue.isEmpty()) {
            String currentVisitNode = queue.poll();
            if (!visitedNodes.keySet().contains(currentVisitNode)) {

                Table<String, String, String> table = HashBasedTable.create();

                graph.predecessors(currentVisitNode).forEach(node->{
                    if (!visitedNodes.keySet().contains(node)) {
                        String edgeValue = graph.edgeValue(currentVisitNode, node);
                        table.put(node.split(":")[0], node.split(":")[1], edgeValue);
                    }

                });

                System.out.println(table);

                table.rowKeySet().forEach(row -> {
                    System.out.println("row=>" + row);

                    table.row(row).keySet().forEach(cell ->{

                        System.out.println(cell);
                        System.out.println(table.get(row,cell));

                    });

                });



            }
        }

        return null;

    }



}
