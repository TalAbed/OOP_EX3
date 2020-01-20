package algorithms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms{
	
	private DGraph graph;
	
	public Graph_Algo() {
		
	}
	public Graph_Algo(graph g) {
        init(g);
    }
	
	@Override
	public void init(graph g) {
		this.graph = (DGraph) g;
	}

	@Override
	public void init(String file_name) {
		Graph_Algo GA = new Graph_Algo();
		try {
			FileInputStream file = new FileInputStream (file_name);
			ObjectInputStream input = new ObjectInputStream (file);
			GA = (Graph_Algo)input.readObject();
			graph = GA.graph;
			input.close();
			file.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void save(String file_name) {
		try {
			FileOutputStream file = new FileOutputStream (file_name);
			ObjectOutputStream output = new ObjectOutputStream (file);
			output.writeObject(graph);
			output.close();
			file.close();
		}
		catch (IOException e) {
			System.out.println("exception");
		}
	}

	
	@Override
	public boolean isConnected() {
		for (node_data n : graph.getV()) {
			clearData();
			int c = connectedCounter (n);
			if (c<graph.nodeSize())
				return false;
		}
		return true;
	}

	private int connectedCounter(node_data nd) {
		if (nd.getTag()==1)
			return 0;
		nd.setTag(1);
		int c = 1;
		for (edge_data e : graph.getE(nd.getKey())) {
			c += connectedCounter (graph.getNode(e.getDest()));
		}
		return c;
	}

	private void clearData() {
		for (node_data n : graph.getV()) {
			n.setTag(0);
			n.setWeight(Double.MAX_VALUE);
		}
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		dijkstra(src);
		return graph.getNode(dest).getWeight();
	}
	
	private void dijkstra (int src) {
		clearData();
		PriorityQueue <node_data> PQ = new PriorityQueue<node_data>();
		graph.getNode(src).setWeight(0);
		PQ.add(graph.getNode(src));
		while (!PQ.isEmpty()) {
			node_data nd = PQ.poll();
			for (edge_data ed : graph.getE(nd.getKey())) {
				double nodeWeight = nd.getWeight();
				node_data n = graph.getNode(ed.getDest());
				double edgeWeight = ed.getWeight();
				if (graph.getNode(ed.getDest()).getTag()!=1) {
					if (edgeWeight + nodeWeight < graph.getNode(ed.getDest()).getWeight()) {
						PQ.remove(n);
						n.setWeight(nodeWeight+edgeWeight);
						n.setInfo(""+nd.getKey());
						PQ.add(n);
					}
				}
			}
		}
	}
	
	private boolean connectionTwoNodes (int src, int dest) {
		double path = shortestPathDist(src,dest);
		if (path<Double.MAX_VALUE)
			return true;
		return false;
	}
	
	@Override
	public List<node_data> shortestPath(int src, int dest) {
		if (!connectionTwoNodes(src,dest))
			throw new RuntimeException ("the nodes are't connect");
		else {
			ArrayList <node_data> path = new ArrayList<node_data>();
			if (src == dest) {
				path.add(0, graph.getNode(src));
				return path;
			}
			clearData();
			graph.getNode(src).setWeight(0);
			dijkstra(src);
			String info = graph.getNode(dest).getInfo();
			int k = Integer.parseInt(info);
			path.add(0, graph.getNode(dest));
			path.add(0, graph.getNode(k));
			while (!path.contains(graph.getNode(src))) {
				info = graph.getNode(k).getInfo();
				k = Integer.parseInt(info);
				path.add(0, graph.getNode(k));
			}
			return path;
		}
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		LinkedList <node_data> path = new LinkedList <node_data>();
		LinkedList <node_data> temp;
		if (targets.size()==1)
			return null;
		for (int i=0;i<targets.size();i++) {
			try {
				temp = (LinkedList<node_data>) shortestPath(targets.get(i),targets.get(i+1));
			}
			catch (RuntimeException e) {
				System.out.println("out of index");
				return null;
			}
			if (temp==null)
				return null;
			else if (temp.size()>1 && path.contains(graph.getNode(targets.get(i))))
				temp.remove(graph.getNode(targets.get(i)));
			path.addAll(temp);
		}
		return path;
	}

	@Override
	public graph copy() {
		graph g = new DGraph();
		for (node_data nd : this.graph.getV()) {
			Node n = (Node)nd;
			g.addNode(n);
		}
		for (node_data nd : this.graph.getV()) {
			if (this.graph.getE(nd.getKey())!=null) {
				for (edge_data ed : this.graph.getE(nd.getKey()))
					g.connect(ed.getSrc(), ed.getDest(), ed.getWeight());
			}
		}
		return g;
	}
	public DGraph getGraph() {
		return (DGraph)this.graph;
	}
	public void setGraph(DGraph gr) {
        this.graph = gr;		
	}

}


