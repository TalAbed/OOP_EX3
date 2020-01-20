package dataStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.Point3D;

public class DGraph implements graph{
	
	private HashMap <Integer, HashMap<Integer, edge_data>> edgeMap = new HashMap <Integer, HashMap<Integer, edge_data>>();
	private HashMap <Integer, node_data> nodeMap = new HashMap <Integer, node_data>();
	private int McCounter = 0;
	private int edgeCounter = 0;

	@Override
	public node_data getNode(int key) {
		return nodeMap.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		if (src==dest || nodeMap.get(src)==null || nodeMap.get(dest)==null)
			return null;
		if (edgeMap.get(nodeMap.get(src))!=null)
			return edgeMap.get(nodeMap.get(src)).get(dest);
		return null;
	}

	@Override
	public void addNode(node_data n) {
		nodeMap.put(n.getKey(), n);
		
	}

	@Override
	public void connect(int src, int dest, double w) {
		boolean b = true;
		if (src==dest) {
			System.out.println("src and dest are equals");
			b = false;
		}
		if (b || ((nodeMap.get(src)!=null) || (nodeMap.get(dest)!=null))) {
			if (!nodeMap.containsKey(src)||nodeMap.containsKey(dest)) {
				if (edgeMap.containsKey(src)&&edgeMap.get(src).get(dest) != null)
					throw new RuntimeException ("this edge is already exist");
				else {
					edge_data ed = new Edge (src, dest, w);
					if(edgeMap.containsKey(src)) {
						this.edgeMap.get(src).put(dest, ed);
					}
					else {
						HashMap<Integer, edge_data> value=new HashMap<Integer, edge_data>();
						value.put(dest, ed);
						this.edgeMap.put(src, value);
					}
					McCounter++;
					edgeCounter++;
				}
			}
			else if (b)
				throw new RuntimeException ("src or dest is not exist");
		}
	}

	public Collection<node_data> getV() {
		Collection<node_data> list = new ArrayList<node_data>(nodeMap.values());
		return list;
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		if (edgeMap.get(node_id) != null) {
			return edgeMap.get(node_id).values();
		}
		return null;
	}

	@Override
	public node_data removeNode(int key) {
		node_data n1 = this.getNode(key);
		if (n1!=null) {
			for (Iterator<node_data> it = this.getV().iterator();it.hasNext();) {
				node_data n2 = (node_data) it.next();
				this.removeEdge(n2.getKey(), key);
			}
			if (edgeMap.get(key)!=null) {
				edgeCounter -= edgeMap.get(key).size();
				McCounter += edgeMap.get(key).size();
			}
			nodeMap.remove(key);
			edgeMap.remove(key);
			McCounter++;
		}
		return n1;
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		McCounter++;
		if (edgeMap.get(src).get(dest)!=null) {
			Edge e = new Edge ();
			e = (Edge) edgeMap.get(src).get(dest);
			edgeMap.get(src).remove(dest);
			return e;
		}
		return null;
	}

	@Override
	public int nodeSize() {
		return nodeMap.size();
	}

	@Override
	public int edgeSize() {
		return this.edgeCounter;
	}

	@Override
	public int getMC() {
		return this.McCounter;
	}
	
	public void init (String file) {
		DGraph dg = new DGraph();
		try {
			JSONObject o = new JSONObject (file);
			JSONArray edges = o.getJSONArray("Edges");
			JSONArray nodes = o.getJSONArray("Nodes");
			
			for (int i=0;i<nodes.length();i++) {
				String pos = nodes.getJSONObject(i).getString("pos");
				String sa[] = pos.split(",");
				int id=nodes.getJSONObject(i).getInt("id");
				Point3D p=new Point3D(Double.parseDouble(sa[0]),Double.parseDouble(sa[1]),Double.parseDouble(sa[2]));
				node_data n =new Node(id,p); 
				dg.addNode(n);
			}
			
			for(int i = 0; i < edges.length(); i++)
			{
				int  src = edges.getJSONObject(i).getInt("src");
				int dst = edges.getJSONObject(i).getInt("dest");
				double w = edges.getJSONObject(i).getDouble("w");
				dg.connect(src, dst, w);
			} 
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		this.nodeMap = dg.nodeMap;
		this.edgeMap = dg.edgeMap;
	}

}

