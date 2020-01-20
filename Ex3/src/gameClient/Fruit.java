package gameClient;

import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;

import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

public class Fruit implements _fruit {
	
	private int type;
	private double value;
	private Point3D locaiton;
	private edge_data edge ;
	private graph g;



	public Fruit ( int type,Point3D locaiton, double value,graph g) {
		this.type = type;
		this.locaiton = locaiton;
		this.value = value;
		this.edge = null;
	}


	public Fruit(graph g) {
		this.g = g;
		this.edge = null;
		this.locaiton = null;
		this.type = 0;
		this.value = -1;
	}

	public void initFruit (String json) throws JSONException {
		JSONObject obj = new JSONObject(json);
		JSONObject fruit = obj.getJSONObject("Fruit");
		String pos = fruit.getString("pos");
		String str[] = pos.split(",");
		this.type = fruit.getInt("type");
		this.value = fruit.getDouble("value");
		Point3D p = new Point3D(Double.parseDouble(str[0]),Double.parseDouble(str[1]),Double.parseDouble(str[2]));
		this.locaiton = p;
		setEdge();
	}

	public void setType(int type) {
		this.type=type;
	}

	public int getType() {
		return this.type;
	}

	public void setLocaiton(Point3D l) {
		this.locaiton=l;
	}

	public  Point3D getlocaiton() {
		return this.locaiton; 
	}

	public void setvalue(double value) {
		this.value=value;
	}

	public double getValue() {
		return this.value;
	}
	
	public void setEdge() {
		Collection<node_data> s =g.getV();
		for (node_data node : s) 
		{
			Collection<edge_data> edges =g.getE(node.getKey());
			for(edge_data e : edges)
			{
				if(pointOn(g.getNode(e.getSrc()).getLocation(),g.getNode(e.getDest()).getLocation(),this.locaiton))
					this.edge=e;
			}
		}
		if (this.edge==null) throw new RuntimeException("The point is not on the graph");


	}
	
	public edge_data getEdge() {
		if (this.edge !=null) return edge;
		return null;
	}

	
	//return T if point C is on the line between A and B
	public boolean pointOn(Point3D a , Point3D b , Point3D c) {
		if (Math.abs(distance(a, c) + distance(b, c) - distance(a, b))<=0.000001)
			return true;
		return false; 
	}
	
	//return the distance between two points
	public double distance(Point3D a , Point3D b) {
		return Math.sqrt( Math.pow((a.x() - b.x()), 2) + Math.pow((a.y() - b.y()), 2) );
	}
}
