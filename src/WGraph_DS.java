package ex1.src;


import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class WGraph_DS implements weighted_graph {

    private HashMap<Integer, node_info> NodesGraph;
    private HashMap<Integer, HashMap<Integer,Double>> ni;//HashMap of nodes in the graph and in each node there is HashMap of his neighbors
    private int edgeCounter;
    private int McCounter;
    /**
     * private class of node each node have key,info,tag
     */
    private class NodeInfo implements node_info, Comparable<node_info> {

        private int key;
        private String info;
        private double tag;

        /**
         * constructor that creates a node with a key number he gets
         * @param key
         */
        public NodeInfo(int key) {
            this.key = key;
            this.info = "";
            this.tag = Double.MAX_VALUE;
        }

        /**
         * copy constructor
         * @param node
         */
        public NodeInfo(node_info node) {
            key = node.getKey();
            info = node.getInfo();
            tag = node.getTag();
        }

        /**
         * Return the key (id) associated with this node.
         * @return key
         */
        @Override
        public int getKey(){
            return this.key;
        }

        /**
         * return the remark (meta data) associated with this node.
         * @return info
         */
        @Override
        public String getInfo() {
            return this.info;
        }

        /**
         *changing the remark (meta data) associated with this node.
         * @param s-the new string for info
         */
        @Override
        public void setInfo(String s) {
            this.info=s;
        }

        /**
         *used by algorithms to save the distance
         * @return tag
         */
        @Override
        public double getTag() {
            return this.tag;
        }

        /**
         * Updating the tag to the value that he get
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            this.tag=t;
        }

        /**
         * Checks if the key are the same in the 2 nodes
         * @param obj
         * @return true if the node are equals
         */
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof NodeInfo))
                return false;
            node_info n=(node_info)obj;
            if(n.getKey()!=this.getKey())
                return false;
            return true;
        }
        public String toString()
        {
            return "key:" + key + ",tag:" + tag + ",info:" + info ;

        }

        /**
         * Checks which tag is bigger
         * @param o
         * @return 0 if equal 1 if this is bigger and -1 if o is bigger
         */
        @Override
        public int compareTo(node_info o) {
            if(this.getTag()>o.getTag())
                return 1;
            else if(this.getTag()==o.getTag())
                return 0;
            return -1;
        }
    }
    /**
     *constructor
     */
    public WGraph_DS()
    {
        ni=new HashMap<Integer,HashMap<Integer,Double>>();
        NodesGraph=new HashMap<Integer,node_info>();
        edgeCounter=0;
        McCounter=0;
    }

    /**
     * copy constructor
     * @param  graph
     */
    public WGraph_DS(weighted_graph graph) {
        if (graph != null) {
            this.NodesGraph = new HashMap<Integer, node_info>();
            this.ni = new HashMap<Integer, HashMap<Integer, Double>>();
            if (graph.nodeSize() != 0) {
                for (node_info nodeOfGraph : graph.getV()) {
                    NodesGraph.put(nodeOfGraph.getKey(), new NodeInfo(nodeOfGraph)); //do deep copy for each node of graph and add it to the list of nodes of the new graph
                    ni.put(nodeOfGraph.getKey(), new HashMap<Integer, Double>());//add the new node key to ni HashMap with empty HashMap of neighbors
                    for (node_info nodesNi : graph.getV(nodeOfGraph.getKey()))
                        ni.get(nodeOfGraph.getKey()).put(nodesNi.getKey(), graph.getEdge(nodeOfGraph.getKey(), nodesNi.getKey()));//add all the neighbors of node to his HashMap
                }
                this.edgeCounter = graph.edgeSize();
                this.McCounter = graph.getMC();
            }
        }
    }


    /**
     * return the node_info by the key
     * @param key - the node_id
     * @return node_info null if none
     */
    @Override
    public node_info getNode(int key) {
        return this.NodesGraph.get(key);
    }

    /**
     * return true iff (if and only if) there is an edge between node1 and node2
     * @param node1,node2
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if(NodesGraph.containsKey(node1)==false||NodesGraph.containsKey(node2)==false)////if node1 or node2 not in the graph
            return false;
        return ni.get(node1).containsKey(node2);
    }

    /**
     * return the weight of the edge (node1, node1). In case there is no such edge - should return -1
     * @param node1,node2
     * @return weight
     */
    @Override
    public double getEdge(int node1, int node2) {
        if(this.hasEdge(node1,node2)==false)
            return -1;
        return ni.get(node1).get(node2);//return the weight of the edge
    }

    /**
     * add node to the graph-add the node to the NodesGraph HashMap and to the ni HashMap with empty HshMap
     * @param key
     */
    @Override
    public void addNode(int key) {
        if(NodesGraph.containsKey(key))//If the node is already in the nodes list of the graph do not put it again
            return;
        node_info n=new NodeInfo(key);
        this.NodesGraph.put(key,n);
        HashMap<Integer,Double> temp=new HashMap<Integer,Double>();
        this.ni.put(key,temp);//put the node in the ni HashMap with empty hashMap of neighbors
        McCounter++;
    }

    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * Adds each node to the neighbor's list(that is in the ni HashMap) of the other.
     * @param node1,node2
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if(w<0)//weight can not be -
            return;
        if (node1 == node2)//There is no edge between a node and itself
            return;
        if (NodesGraph.containsKey((node1)) == false || NodesGraph.containsKey(node2) == false)//if node1 or node2 not in the graph
            return;
        double edge=this.getEdge(node1,node2);
        if (edge==-1)//check that there is no edge already
        {
            ni.get(node1).put(node2, w);
            ni.get(node2).put(node1, w);
            McCounter++;
            edgeCounter++;
            return;
        }
        if (edge != w)//If there is a edge but want to change weight
        {
            ni.get(node1).put(node2, w);
            ni.get(node2).put(node1, w);
            McCounter++;
        }
    }

    /**this method return a pointer (shallow copy) for a Collection representing all the nodes in the graph.
     * @return collection pf the graph nodes
     */
    @Override
    public Collection<node_info> getV() {
        return NodesGraph.values();
    }

    /**This method returns a Collection containing all the nodes connected to node_id
     * @param node_id
     * @return collection of neighbors
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        if(!NodesGraph.containsKey(node_id))//if the node not in the graph
            return null;
        LinkedList<node_info> AllNi=new LinkedList<node_info>();
        for(Integer keyNode:ni.get(node_id).keySet())//go throw the hashMap of the neighbors of node_id and add them to the list
        {
            AllNi.add(getNode(keyNode));
        }
        return AllNi;
    }
    /**
     * Delete the node (with the given ID) from the graph -and removes all edges which starts or ends at this node.
     * @param key
     * @return the node that was remove
     */
    @Override
    public node_info removeNode(int key) {
        if(NodesGraph.containsKey(key)==false)//if the node not in the graph
            return null;
        for(Integer node:ni.get(key).keySet())//go over the neighbors of key and remove from the list of neighbors of each neighbor the node key
        {
            ni.get(getNode(node).getKey()).remove(key);
            edgeCounter--;
            McCounter++;
        }
        node_info temp=getNode(key);//save the node needs to be deleted
        NodesGraph.remove(key);//delete the node from the list of the graph nodes
        ni.remove(key);//delete the node from the ni HashMap
        McCounter++;
        return temp;
    }

    /**Delete the edge from the graph-delete from the neighbor list of each node the other node
     * @param node1,node 2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (this.hasEdge(node1,node2))//if they are not neighbors do nothing
        {
            ni.get(node1).remove(node2);
            ni.get(node2).remove(node1);
            edgeCounter--;
            McCounter++;
        }
    }

    /**
     * return the number of nodes in the graph.
     */
    @Override
    public int nodeSize() {
        return NodesGraph.size();
    }

    /**
     *  return the number of edges
     */
    @Override
    public int edgeSize() {
        return edgeCounter;
    }

    /**
     *  return the Mode Count-Any change in the inner state of the graph should cause an increment in the ModeCount
     */
    @Override
    public int getMC() {
        return McCounter;
    }

    /**
     * check if the graphs aare equals-have the same nodes and edges
     * @param obj
     * @return ture if the graphs are equals
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof WGraph_DS))
            return false;
        WGraph_DS g=( WGraph_DS)obj;
        if(g.nodeSize()!=this.nodeSize()||g.edgeCounter!=this.edgeCounter)//if the number of the node or the number of the edges not the same return false
            return false;
        //check that all the nodes are the same
        for (node_info node : g.getV()) {
            if (!this.NodesGraph.containsKey(node.getKey()))
                return false;
        }
        //check that all the edges are the same
        for (node_info node : g.getV()) {
            for (node_info nodeNi : g.getV(node.getKey())) {
                if (this.getEdge(node.getKey(),nodeNi.getKey())!=g.getEdge(node.getKey(),nodeNi.getKey()))
                    return false;
            }
        }
        return true;
    }

    /**
     * return string with all the nodes of the graph and the edges
     * @return
     */
    public String toString()
    {
        String s="";
        for(node_info temp:getV())
        {
            // temp=iter.next();
            s = s+"node:"+ temp.getKey() + " edges: ";
            for(node_info ni:this.getV(temp.getKey()))
            {
                s=s+"["+ni.getKey()+","+this.getEdge(ni.getKey(),temp.getKey())+"] ";
            }
            s=s+"\n";
        }
        return s;
    }
}