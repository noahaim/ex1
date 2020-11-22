This project is about developing a data structure of an weighted graph.
The project contains 3 interfaces: node_info,weighted_graph, weighted_graph_algorithms
and 3 classes(1 private class): WGraph_DS  with private class inside-NodeInfo ,WGraph_Algo.

WGraph_DS implements weighted_graph
Includes the private class -NodeInfo that  implements  node_info, Comparable<node_info>,each NoseInfo have key,info and tag,the Node Info have copy constructor and constructor that get key.

the NodeInfo method:
-getKey()-return the id of the node
-getInfo()/setInfo()-return/change the String info of the key.
-getTag()/setTag()-return/set the tag of the NodeInfo(the tag is  save distanse when we call to the  Dijkstra algorithm)
-equals(Object obj)-check if in the 2 NodeInfo the key is the same.
-comperTo(node_info o)-Checks which NodeInfo has a bigger tag(this is for the PriorityQueue in the Dijkstra algorithm)

each WGraph_DS have: 	
-  edgeCounter
- McCounter(that count the changes that made on the graph)
-HashMap calld NodesGraph which includes the nodes in the graph 
-HashMap calld ni that have the keys of all the nodes in the graph and in each node key there is HashMap of his neighbors and the weight between them.
WGraph_DS have copy constructor.
implement the list of nodes that in the graph in HashMap let us add node to the graph,get list of all the nodes in the graph,get node that in the graph by the key and get how meny nodes there is in the graph in  o(1)
implement the list of neighbors in HashMap with the nodes key and each key has inside HashMap which includes the neighbors' keys and the size of the edge with it, let us connect nodes,get the edge and remove edge in o(1)
also it let us to get Collection of the neighbors of node in o(k) (k is the number of neighbors) and remove node and all the edges that he part wuth in o(1)
more methods in WGraph_DS-get edgeSize,getMc and equals.

WGraph_Algo implements weighted_graph_algorithms
each WGraph_Algo have  weighted graph calld theGraph .
the method init()- Init the weighted graph on which this set of algorithms operates on.
In this class we can make a deep copy by calling for the copy constructor of a WGraph_DS that calls for a deep copy of NodeInfo.
Make a shallow copy,and get the graph on which this set of algorithms operates on.
Dijkstra algorithm-get src and set all the tag in the weighted graph to the shortest distance between them and src.This algorithm used PriorityQueue which compares by tag.
the running time complexity is o((|e|+|v|)*log|v|)-e the number of edge and v the number of nodes.
There are 3 more  methods that used the  Dijkstra algorithm:
1.Check that the graph is connected-call to Dijkstra algorithm with some node in the graph and then go over the nodes of the graph and check if there is node with tag of infinity(that meen that there is no path between this node and the node that send to the algorithm)
2.Return the size of distance  between 2 node-send the src node to the  Dijkstra algorithm and in the algoritm all the tag of the nodes in the graph save the distance from src. 
3.return path itself between 2 node-the Dijkstra algorithm return HashMap contains the node key and the node that comes before them(in the short path)
in WGraph_Algo we can also saves  weighted graph to the given file name and also load weighted graph from  given file name and init it to theGraph.




