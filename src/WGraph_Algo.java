package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {

    private weighted_graph theGraph;

    /**
     * constructor
     */
    public WGraph_Algo()
    {
        theGraph = new WGraph_DS();
    }

    /**
     * Shallow copy constructor
     */
    public WGraph_Algo(weighted_graph g)
    {
        theGraph = g;
    }

    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        this.theGraph=g;
    }

    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public weighted_graph getGraph() {
        return this.theGraph;
    }

    /**
     * Compute a deep copy of this weighted graph-call the copy constructor of WGraph_DS
     */
    @Override
    public weighted_graph copy() {

        return new WGraph_DS(this.theGraph);
    }

    /**
     *help function that calculates for each node the weight of the shortestPath between it and src and save it in the node tag
     * @param src
     * @return list of nodes with their parents so that we can reconstruct the path
     */
    private HashMap<Integer, node_info> Dijkstra(int src)
    {
        for(node_info nodes:theGraph.getV())//update all the nodes tag to be the MAX_VALUE
            nodes.setTag(Double.MAX_VALUE);
        PriorityQueue<node_info> help=new PriorityQueue<>();
        HashMap<Integer,node_info> parents=new HashMap<Integer, node_info>();//To save the parents of the nodes to ce the path
        node_info n;
        double m;
        theGraph.getNode(src).setTag(0);//the shortPath node to himself is 0
        parents.put(src,null);//add src to the HashMap that save the parents with parent null
        help.add(theGraph.getNode(src));
        while(!help.isEmpty())
        {
          n=help.poll();//take the node with the smallest tag in the PriorityQueue
          for(node_info ni:theGraph.getV(n.getKey()))//go over his neighbors
          {
                  m = n.getTag() + theGraph.getEdge(n.getKey(), ni.getKey());
                  //if this neighbor have bigger tag, update his tag and his parent(There is a shorter path from src to him)
                  if (ni.getTag() > m) {
                      ni.setTag(m);
                      parents.put(ni.getKey(),n);
                      help.add(ni);
                  }
              }
          }
        return parents;
        }

    /**
     *Returns true if and only if there is a valid path from EVREY node to each other node
     */
    @Override
    public boolean isConnected() {
        if(theGraph==null)
            return false;
        if (theGraph.nodeSize() == 0 || theGraph.nodeSize() == 1)//If the graph is empty or there is one node return true
            return true;
        Iterator<node_info> iter1 = theGraph.getV().iterator();//take node from the graph nodes list
        node_info node1 = iter1.next();
        Dijkstra(node1.getKey());//Updates for each node its tag to be the distance from node1
        //Checks if there is a path between node1 and to all the other nodes in the graph
        while (iter1.hasNext()) {
            node1 = iter1.next();
            if (node1.getTag() ==Double.MAX_VALUE)
                return false;
        }
        return true;
    }

    /**
     * returns the length of the shortest path between src to dest if no such path returns -1
     * @param src - start node
     * @param dest - end (target) node
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if(theGraph==null)
            return -1;
        if((theGraph.getV().contains(theGraph.getNode(src)) == false) || (theGraph.getV().contains(theGraph.getNode(dest)) == false))//if src or dest are not in the graph
            return -1;
        Dijkstra(src);//Updates the tag of each node in the graph to be the distance from src
        double dist=theGraph.getNode(dest).getTag();
        if(dist==Double.MAX_VALUE)//no such path
            return -1;
        return dist;
    }

    /**
     *returns list of nodes of the the shortest path between src to dest
     * @param src - start node
     * @param dest - end (target) node
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if(theGraph==null)
            return null;
        if (theGraph.getV().contains(theGraph.getNode(src)) == false || theGraph.getV().contains(theGraph.getNode(dest)) == false)////if src or dest are not in the graph
            return null;
        Stack<node_info> path = new Stack<node_info>();
        HashMap <Integer,node_info> g = this.Dijkstra(dest);//the HashMap that save the parent of each node in the short path(send dest so that the path will be in good order)
        if(this.theGraph.getNode(src).getTag()==Double.MAX_VALUE)//no such path
            return path;
        node_info parent = g.get(src);
        path.push(theGraph.getNode(src));//Insert src to the stack
        //while we dont get to dest insert the node and save his parent
        while (parent != null) {
            path.push(parent);
            parent = g.get(parent.getKey());
        }
        return path;
    }

    /**
     * Saves this weighted graph to the given file name
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        File file_obj=new File(file);
        try{
            if(theGraph==null)
                return false;
            FileWriter file_witer=new FileWriter(file);
            file_witer.write(theGraph.toString());
            file_witer.close();
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
      //  FileOutputStream file=new FileOutputStream()
    }

    /**
     * This method load a graph to this graph algorithm. if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the graph was not loaded the original graph should remain "as is".
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        WGraph_DS g=new WGraph_DS();
        File file_obj=new File(file);
        String s,s1,s2;
        int index;
        int index2;
        try{
            Scanner file_scanner=new Scanner(file_obj);
            //add the nodes to the graph
            while(file_scanner.hasNext()) {
                String line = file_scanner.nextLine();
                String[] help = line.split(" ");//split the line with " "
                s2 = help[0].substring(5);//in each line the node key is in place 0 in the array of split place 5 till the end
                g.addNode(Integer.parseInt(s2));
            }
          Scanner file_scanner1=new Scanner(file_obj);
            //add the edges
            while(file_scanner1.hasNext())
            {
                String line = file_scanner1.nextLine();
                String[] help = line.split(" ");
                s2 = help[0].substring(5);//the node key
                //in place 1 there is "edges:" and the edges started in place 2 in each line
                for(int i=2;i<help.length;i++)
                {
                    index=help[i].indexOf(',');//[node of the neighbor,the wight with him]
                    s=help[i].substring(1,index);//grt the key of the neighbor
                    index2=help[i].indexOf(']');
                    s1=help[i].substring(index+1,index2);//the wight
                    g.connect(Integer.parseInt(s),Integer.parseInt(s2),Double.parseDouble(s1));
                }
            }
        }catch(FileNotFoundException e){
            System.out.println("File not exist");
            return false;
        }
        this.init(g);//Update graph of this class
        return true;
    }
}