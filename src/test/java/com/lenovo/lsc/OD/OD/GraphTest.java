package com.lenovo.lsc.OD.OD;

import com.google.common.collect.*;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collector;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GraphTest {

    @Test
    public void testOD() {

        //covert graph
        MutableValueGraph<String, String> graph = ValueGraphBuilder.undirected().build();


        //load data
        ImmutableMultimap.Builder<String, String> variantBuilder = ImmutableMultimap.builder();
        variantBuilder.put("VT1:1", "MEM:4G");
        variantBuilder.put("VT1:1", "CPU:I5");
        variantBuilder.put("VT1:1", "CPU:I7");
        variantBuilder.put("VT2:1", "CPU:I5");
        variantBuilder.put("VT2:1", "HDD:256G");
        variantBuilder.put("VT2:2", "CPU:I7");
        variantBuilder.put("VT2:2", "HDD:128G");
        variantBuilder.put("VT2:2", "HDD:256G");
        variantBuilder.put("VT3:1", "MEM:4G");
        variantBuilder.put("VT3:1", "CPU:I5");
        variantBuilder.put("VT3:1", "Card:NV810");

        variantBuilder.put("VT3:1", "MEM:8G");
        variantBuilder.put("VT1:1", "MEM:8G");


        ImmutableMultimap<String, String> variantMultmap = variantBuilder.build();

        ImmutableSet<String> variantMultmapkey = variantMultmap.keySet();
        for (String key : variantMultmapkey) {

            log.info("key=>>" + key);

            log.info("value=>" + variantMultmap.get(key));

            ImmutableCollection<String> variantValues = variantMultmap.get(key);
            Multimap<String, String> variantValueMultimap = variantValues.stream().collect(Collector.of(ArrayListMultimap::create, (multimap, vt) -> {
                multimap.put(vt.split(":")[0], vt.split(":")[1]);
            }, (multi1, multi2) -> {
                multi1.putAll(multi2);
                return multi1;
            }));

            log.info("cvMultimap => " + variantValueMultimap);

            for (int i = 0; i < variantValueMultimap.keySet().size() - 1; i++) {

                for (int j = i + 1; j < variantValueMultimap.keySet().size(); j++) {
                    String charI = (String) variantValueMultimap.keySet().toArray()[i];
                    Collection<String> valueI = variantValueMultimap.get(charI);
                    String charJ = (String) variantValueMultimap.keySet().toArray()[j];
                    Collection<String> valueJ = variantValueMultimap.get(charJ);

                    valueI.forEach(vI -> {
                                valueJ.forEach(vJ -> {
                                    graph.putEdgeValue(charI.concat(":").concat(vI), charJ.concat(":").concat(vJ), (graph.edgeValueOrDefault(charI.concat(":").concat(vI), charJ.concat(":").concat(vJ), "").equals("") ? "" : (graph.edgeValue(charI.concat(":").concat(vI), charJ.concat(":").concat(vJ)) + "|")) + key);
                                });
                            }
                    );
                }
            }
        }

        log.info("graph => " + graph);

//        Multimap<String, String> traversal = new Traversal2("MEM:4G", graph).traversal();
//        Multimap<String, String> traversal = new Traversal2("CPU:I5", graph).traversal();
//        Multimap<String, String> traversal = new Traversal2("HDD:256G", graph).traversal();

        Multimap<String, String> traversalOne = new Traversal2("MEM:4G", graph).traversal();
        Multimap<String, String> traversalTwo = new Traversal2("CPU:I5", graph).traversal();
        Multimap<String, String> traversal = intersectionCollection(traversalOne, traversalTwo);

        log.info(traversal.toString());

    }

    private Multimap<String,String> intersectionCollection(Multimap<String, String> traversalOne, Multimap<String, String> traversalTwo) {
        Multimap<String, String> scoreMultimap = HashMultimap.create();
        Collection<Map.Entry<String, String>> entriesOne = traversalOne.entries();
        Collection<Map.Entry<String, String>> entriesTwo = traversalTwo.entries();

        entriesOne.forEach( one -> {
            entriesTwo.forEach(two -> {
                if(one.getKey().equals(two.getKey()) && one.getValue().equals(two.getValue())){
                    scoreMultimap.put(one.getKey(),one.getValue());
                }
            });
        });

       return scoreMultimap;
    }
}
