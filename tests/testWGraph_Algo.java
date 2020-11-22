package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class testWGraph_Algo {


    @Test
    public void  init()
    {
        weighted_graph graph=graph_creator(3);
        graph.connect(0,1,1);
        graph.connect(1,2,2);
        weighted_graph_algorithms g=new WGraph_Algo();
        g.init(graph);
        graph.connect(0,2,2);
        Assertions.assertEquals(3,g.getGraph().edgeSize());

    }
    //Checks that the graph and its copy are equals
    @Test
    public void Copy()
    {
        weighted_graph graph=graph_creator(3);
        graph.connect(0,1,1);
        graph.connect(1,2,2);
        weighted_graph_algorithms g1=new WGraph_Algo(graph);
        weighted_graph g2=g1.copy();
        Assertions.assertTrue(g2.equals(g1.getGraph()));
    }
    //Checks that a change in the graph does do nothing in the copied graph
    @Test
    public void anotherCopy()
    {
        weighted_graph graph=graph_creator(3);
        graph.connect(0,1,1);
        graph.connect(1,2,2);
        weighted_graph_algorithms g1=new WGraph_Algo(graph);
        weighted_graph g2=g1.copy();
        graph.removeNode(0);
        Assertions.assertEquals(2,graph.nodeSize());
        Assertions.assertEquals(3,g2.nodeSize());
    }


    @Test
    public void isConnected()
    {
        weighted_graph graph=new WGraph_DS();
        weighted_graph_algorithms g=new WGraph_Algo();
        g.init(graph);
        Assertions.assertTrue(g.isConnected());//empty graph is connected
        g.getGraph().addNode(0);
        Assertions.assertTrue(g.isConnected());//graph with 1 node is connected
        g.getGraph().addNode(1);
        g.getGraph().addNode(2);
        graph.connect(0,1,1);
        graph.connect(1,2,1);
        Assertions.assertTrue(g.isConnected());
        graph.removeEdge(1,2);
        Assertions.assertFalse(g.isConnected());
    }
    @Test
    public void shortestPathDist()
    {
        weighted_graph graph=graph_creator(5);
        graph.connect(0,1,2);
        graph.connect(0,2,2);
        graph.connect(2,1,6);
        graph.connect(2,3,7);
        graph.connect(3,4,1.1);
        graph.connect(0,4,20);
        weighted_graph_algorithms g=new WGraph_Algo(graph);
        Assertions.assertEquals(10.1,g.shortestPathDist(0,4));
        graph.connect(0,4,1.5);
        Assertions.assertEquals(1.5,g.shortestPathDist(0,4));

    }
    //check shortestPathDist between the same node,node that not in the graph and 2 node that in the graph without path
    @Test
    public void anotherShortestPathDist()
    {
        weighted_graph graph=graph_creator(3);
        graph.connect(0,1,2);
        weighted_graph_algorithms g=new WGraph_Algo(graph);
        Assertions.assertEquals(0,g.shortestPathDist(0,0));
        Assertions.assertEquals(-1,g.shortestPathDist(0,4));
        Assertions.assertEquals(-1,g.shortestPathDist(0,2));

    }
   @Test
    public void shortPath()
    {
        weighted_graph graph=graph_creator(5);
        graph.connect(0,1,2);
        graph.connect(0,2,2);
        graph.connect(2,1,6);
        graph.connect(2,3,7);
        graph.connect(3,4,1.1);
        graph.connect(0,4,20);
        weighted_graph_algorithms g=new WGraph_Algo(graph);
        List<node_info> path=g.shortestPath(0,4);
        node_info [] answer={graph.getNode(0),graph.getNode(2),graph.getNode(3),graph.getNode(4)};
        Assertions.assertEquals(answer.length,path.size());
        int i=0;
        for(node_info node:path)
        {
            if(!node.equals(answer[i]))
                Assertions.fail();
            i++;
        }
    }
    @Test
    public void shortPath2()
    {
        weighted_graph graph=graph_creator(3);
        weighted_graph_algorithms g=new WGraph_Algo(graph);
        graph.connect(0,1,2);
        graph.connect(0,2,2);
        List<node_info> path=g.shortestPath(0,4);//if src/dest not in the graph return null
        Assertions.assertNull(path);
        graph.addNode(3);
        path=g.shortestPath(0,3);//if there is not path return empty list
    }

    @Test
    public void shortPathSameNode()
    {
        weighted_graph graph=graph_creator(3);
        weighted_graph_algorithms g=new WGraph_Algo(graph);
        graph.connect(0,1,2);
        graph.connect(0,2,2);
        List<node_info> path=g.shortestPath(0,0);//path from node to himself is list with the node
        Assertions.assertEquals(1,path.size());
        Assertions.assertTrue(path.contains(g.getGraph().getNode(0)));
    }

    @Test
    public void saveAndLoad()
    {
        weighted_graph graph=graph_creator(3);
        graph.connect(0,1,2);
        graph.connect(0,2,2);
        weighted_graph_algorithms g=new WGraph_Algo(graph);
        g.save("noa");
        g.load("noa");
        Assertions.assertTrue(g.getGraph().equals(graph));
        graph.addNode(3);
        Assertions.assertEquals(g.getGraph().nodeSize()+1,graph.nodeSize());
    }
    @Test
    public void creatBigGraphAndCopy()
    {
        Assertions.assertTimeoutPreemptively(Duration.ofMillis(10000),() ->{
            weighted_graph g=graph_creator(1000000);
            for(int i=0;i<1000000;i++)
                for(int j=0;j<=3;j++)
                    g.connect(i,j,2);
           weighted_graph_algorithms g1=new WGraph_Algo(g);
           g1.copy();
        });
    }
    @Test
    public void creatBigGraphAndPath()
    {
        Assertions.assertTimeoutPreemptively(Duration.ofMillis(10000),() ->{
            weighted_graph g=graph_creator(1000000);
            for(int i=0;i<g.nodeSize();i++)
                for(int j=0;j<=4;j++)
                    g.connect(i,j,2);
            weighted_graph_algorithms g1=new WGraph_Algo(g);
            Assertions.assertEquals(2,g1.shortestPathDist(0,100));

        });
    }
    //load from non-existent file
    @Test
     public void load()
    {
        weighted_graph graph=graph_creator(3);
        weighted_graph_algorithms g=new WGraph_Algo(graph);
        Assertions.assertFalse(g.load("abcde"));
    }

    // //help function to creat a graph with nodeSize nodes
    public static weighted_graph graph_creator(int nodeSize) {
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < nodeSize; i++) {
            g.addNode(i);
        }
        return g;
    }
}
