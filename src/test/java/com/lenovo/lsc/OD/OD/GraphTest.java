package com.lenovo.lsc.OD.OD;

import com.google.common.collect.*;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.stream.Collector;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GraphTest {

    @Test
    public void test() {

        //load data
        ImmutableMultimap.Builder<String, String> builder = ImmutableMultimap.builder();
        builder.put("VT1:1", "MEM:4G");
        builder.put("VT1:1", "CPU:I5");
        builder.put("VT1:1", "CPU:I7");
        builder.put("VT2:1", "CPU:I5");
        builder.put("VT2:1", "HDD:256G");
        builder.put("VT2:2", "CPU:I7");
        builder.put("VT2:2", "HDD:128G");
        builder.put("VT2:2", "HDD:256G");
        builder.put("VT3:1", "MEM:4G");
        builder.put("VT3:1", "CPU:I5");
        builder.put("VT3:1", "Card:NV810");


        //covert graph
        MutableValueGraph<String, String> graph = ValueGraphBuilder.undirected().build();

        ImmutableMultimap<String, String> ods = builder.build();

        ImmutableSet<String> key_ods = ods.keySet();
        for (String key : key_ods) {

            System.out.println("key=>>" + key);

            String t = key.split(":")[0];
            String s = key.split(":")[1];

            System.out.println("value=>" + ods.get(key));

            ImmutableCollection<String> odsKey = ods.get(key);
            Multimap<String, String> cvMultimap = odsKey.stream().collect(Collector.of(ArrayListMultimap::create, (multimap, vt) -> {
                multimap.put(vt.split(":")[0], vt.split(":")[1]);
            }, (multi1, multi2) -> {
                multi1.putAll(multi2);
                return multi1;
            }));

            System.out.println("cvMultimap => " + cvMultimap);

            for (int i = 0; i < cvMultimap.keySet().size() - 1; i++) {

                for (int j = i + 1; j < cvMultimap.keySet().size(); j++) {
                    String charI = (String) cvMultimap.keySet().toArray()[i];
                    Collection<String> valueI = cvMultimap.get(charI);
                    String charJ = (String) cvMultimap.keySet().toArray()[j];
                    Collection<String> valueJ = cvMultimap.get(charJ);

                    valueI.forEach(vI -> {
                                valueJ.forEach(vJ -> {
                                    graph.putEdgeValue(charI.concat(":").concat(vI), charJ.concat(":").concat(vJ), (graph.edgeValueOrDefault(charI.concat(":").concat(vI), charJ.concat(":").concat(vJ), "").equals("") ? "" : (graph.edgeValue(charI.concat(":").concat(vI), charJ.concat(":").concat(vJ)) + "|")) + key);
                                });

                            }
                    );

                }
            }

        }

        System.out.println("graph => " + graph);

//        graph.adjacentNodes("MEM:4G").forEach(
//                cv -> {
//                    System.out.println(cv);
//                    System.out.println(graph.edgeValue("MEM:4G",cv));
//
//                }
//
//        );

//        Traversal traversal = new Traversal("EPCOUNTRY:MALAYSIA", graph);
        Traversal traversal = new Traversal("MEM:4G", graph);
        System.out.println(traversal.traversal());

    }
}
