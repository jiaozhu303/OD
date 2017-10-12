package com.lenovo.lsc.OD.OD;

import com.google.common.collect.*;
import com.google.common.graph.MutableValueGraph;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/*
Queue
队列特性：

add        增加一个元索                    如果队列已满，则抛出一个IIIegaISlabEepeplian异常
remove      移除并返回队列头部的元素         如果队列为空，则抛出一个NoSuchElementException异常
element     返回队列头部的元素              如果队列为空，则抛出一个NoSuchElementException异常
offer       添加一个元素并返回true          如果队列已满，则返回false
poll         移除并返问队列头部的元素        如果队列为空，则返回null
peek       返回队列头部的元素               如果队列为空，则返回null
put         添加一个元素                   如果队列满，则阻塞
take        移除并返回队列头部的元素         如果队列为空，则阻塞

 */
@Slf4j
public class Traversal2 {
    private final String sourceNode;
    private final MutableValueGraph<String, String> graph;
    private Multimap<String, String> scoreMultimap = HashMultimap.create();


    public Traversal2(String sourceNode, MutableValueGraph<String, String> graph) {
        this.sourceNode = sourceNode;
        this.graph = graph;
    }

    public Multimap<String, String> traversal() {

        Set<String> nodes = graph.nodes();
        if (!nodes.contains(sourceNode)) {
            throw new IllegalArgumentException(sourceNode + " is not in this graph!");
        }

        //Visited node
        Map<String, Integer> visitedNodes = new HashMap<>();

        Queue<String> queue = new LinkedList<String>();

        Set<String> queueUsed = Sets.newHashSet();

        queue.offer(sourceNode);
        queueUsed.add(sourceNode);

        while (!queue.isEmpty()) {
            //The first visit is still the source node, remove and get the first element from the queue
            String currentVisitNode = queue.poll();

            Table<String, String, String> table = HashBasedTable.create();

            graph.predecessors(currentVisitNode).forEach(node -> {
                if (!visitedNodes.keySet().contains(node)) {
                    String edgeValue = graph.edgeValue(currentVisitNode, node);
                    table.put(node.split(":")[0], node.split(":")[1], edgeValue);
                }
            });

            log.info(table.toString());
            Set<String> tableKeySet = table.rowKeySet();
            tableKeySet.forEach(row -> {

                log.info("row=>" + row);
                Map<String, String> row1 = table.row(row);
                Set<String> rowKeySet = row1.keySet();
                Set<Temp> tempSets = Sets.newHashSet();
                for (String cell : rowKeySet) {
                    String vt = table.get(row, cell);
                    tempSets = findMoreLineVTs(tempSets, cell, vt, row, visitedNodes);
                }
                addScoreMultiMap(scoreMultimap, row, tempSets);
                addQueueUsed(queueUsed, queue, row, tempSets);
            });
            visitedNodes.put(currentVisitNode, 1);
        }
        String[] sorceNodeSplit = sourceNode.split(":");
        scoreMultimap.put(sorceNodeSplit[0], sorceNodeSplit[1]);
        return scoreMultimap;
    }

    private void addQueueUsed(Set<String> queueUsed, Queue<String> queue, String row, Set<Temp> tempSets) {
        tempSets.forEach(temp -> {
            if (sourceNode.split(":")[0].equals(row)) {
                if (!queueUsed.contains(row + ":" + temp.getTempCell())) {
                    queueUsed.add(row + ":" + temp.getTempCell());
                }
            } else {
                if (!queueUsed.contains(row + ":" + temp.getTempCell())) {
                    queue.offer(row + ":" + temp.getTempCell());
                    queueUsed.add(row + ":" + temp.getTempCell());
                }
            }
        });
    }


    private void addScoreMultiMap(Multimap<String, String> scoreMultimap, String row, Set<Temp> tempSets) {
        tempSets.forEach(temp -> scoreMultimap.put(row, temp.getTempCell()));
    }

    private Set<Temp> findMoreLineVTs(Set<Temp> tempSets, String cell, String vt, String row, Map<String, Integer> visitedNodes) {
        if (tempSets.isEmpty()) {
            Temp temp = new Temp();
            tempSets.add(temp.builder().temVT(vt).tempCell(cell).build());
        } else {
            Set<Temp> tempSetsBack = Sets.newHashSet();
            for (Temp temp : tempSets) {
                if (vt.split("|").length > temp.getTemVT().split("|").length) {
                    visitedNodes.put(row + ":" + temp.getTempCell(), 1);
                    temp.builder().temVT(vt).tempCell(cell).build();
                    tempSetsBack.add(temp);
                } else if (vt.split("|").length == temp.getTemVT().split("|").length) {
                    tempSetsBack.add(temp);
                    tempSetsBack.add(Temp.builder().temVT(vt).tempCell(cell).build());
                } else if (vt.split("|").length < temp.getTemVT().split("|").length) {
                    visitedNodes.put(row + ":" + cell, 1);
                    tempSetsBack.add(temp);
                }
            }
            tempSets = tempSetsBack;
        }
        return tempSets;
    }

}
