package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class testWGraph_DS {

    //get node that noy in the graph return null
    @Test
    public void getNode() {
        weighted_graph g = graph_creator(2);
        Assertions.assertNull(g.getNode(3));
    }

    //check that if we add the same node or delete the same node nothing change and if we remove node that not in the graph
    @Test
    public void checkNode() {
        weighted_graph g=new WGraph_DS();
        g.addNode(0);
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        Assertions.assertEquals(3,g.nodeSize());
        g.removeNode(4);//not in the graph
        g.removeNode(2);
        g.removeNode(2);
        Assertions.assertEquals(2,g.nodeSize());
    }
    //check that if we add the same edge or delete the same edge nothing change and if we remove edge that not in the graph
    @Test
    public void checkEdges() {
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < 5; i++)
            g.addNode(i);
        g.connect(0, 1, 2);
        g.connect(0, 2, 2);
        g.connect(0, 3, 2);
        g.connect(3, 4, 2);
        g.connect(3, 4, 6);
        Assertions.assertEquals(6, g.getEdge(3, 4));//check if the weight of the edge Updated
        Assertions.assertEquals(4, g.edgeSize());//Check that did not add a edge that already exists
        g.removeEdge(3, 4);
        g.removeEdge(3, 4);
        g.removeEdge(3, 19);
        Assertions.assertEquals(3, g.edgeSize());//Checks that only edge that belong to the graph are deleted
        Assertions.assertEquals(5,g.nodeSize());
    }
    //check that not creat edge between the same node
    @Test
    public void edgeSameNode()
    {
        weighted_graph g=graph_creator(2);
        g.connect(0,0,2);
        Assertions.assertFalse(g.hasEdge(0,0));
        Assertions.assertEquals(-1,g.getEdge(0,0));
    }
    @Test
    public void getV()
    {
        weighted_graph g=graph_creator(5);
        g.removeNode(2);
        ArrayList<node_info> answer=new ArrayList<node_info>();
        answer.add(g.getNode(0));
        answer.add(g.getNode(1));
        answer.add(g.getNode(3));
        answer.add(g.getNode(4));
        Collection<node_info> help=g.getV();
        Assertions.assertEquals(answer.size(),help.size());
        for(node_info n:answer)
        {
            if(!help.contains(n))
                Assertions.fail();
        }
    }
    //check getV(node_info id)
    @Test
    public void getNi()
    {
        weighted_graph g=graph_creator(4);
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(1,2,1);
        g.connect(0,3,1);
        ArrayList<node_info> answer=new ArrayList<node_info>();
        answer.add(g.getNode(1));
        answer.add(g.getNode(2));
        answer.add(g.getNode(3));
        Collection<node_info> help=g.getV(0);
        Assertions.assertEquals(help.size(),answer.size());
        for(node_info n:answer)
        {
            if(!help.contains(n))
                Assertions.fail();
        }
    }

    //creat big graph and check that its done after 10 second
    @Test
    public void creatBigGraph()
    {
        Assertions.assertTimeoutPreemptively(Duration.ofMillis(10000),() ->{
        weighted_graph g=graph_creator(1000000);
        for(int i=0;i<g.nodeSize();i++)
            for(int j=0;j<=10;j++)
                g.connect(i,j,2);
        });
    }
    //check that if removeNode remove all the edges that the node is part of
    @Test
    public void removeNode()
    {
        weighted_graph g=graph_creator(4);
        g.connect(0,1,2);
        g.connect(1,2,2);
        g.connect(2,3,2);
        g.removeNode(2);
        Assertions.assertEquals(1,g.edgeSize());
    }
    //Checks that it mc is updated only it's a new edge or a new weight
    @Test
    public void mcSize()
    {
        weighted_graph g=graph_creator(5);
        g.connect(0,1,1);
        g.connect(1,2,3.14);
        g.connect(3,4,1);
        g.connect(1,0,1);
        g.connect(3,8,1);
        g.connect(1,0,5);
        Assertions.assertEquals(9,g.getMC());
        Assertions.assertEquals(3,g.edgeSize());
    }
    //check that it make deep copy
    @Test
    public void copyGraph() {
        WGraph_DS g = new WGraph_DS();
        for (int i = 0; i < 5; i++)
            g.addNode(i);
        g.connect(0, 1, 1);
        g.connect(1, 2, 1);
        g.connect(3, 4, 1);
        g.connect(4, 2, 1);
        WGraph_DS deepCopyGraph = new WGraph_DS(g);
        WGraph_DS shaloowCopyGraph = g;
        g.removeNode(1);
        Assertions.assertEquals(4, shaloowCopyGraph.nodeSize());
        Assertions.assertEquals(5, deepCopyGraph.nodeSize());
    }
    //help function to creat a graph with nodeSize nodes
    public static weighted_graph graph_creator(int nodeSize) {
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < nodeSize; i++) {
            g.addNode(i);
        }
        return g;
    }
}
