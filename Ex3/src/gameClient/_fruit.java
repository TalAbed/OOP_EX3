package gameClient;

import org.json.JSONException;

import dataStructure.edge_data;
import utils.Point3D;

public interface _fruit {
	
	public void initFruit (String json) throws JSONException;
	
	public void setType(int type);
	
	public int getType();
	
	public void setLocaiton(Point3D l);
	
	public  Point3D getlocaiton();
	
	public void setvalue(double value);
	
	public double getValue();
	
	public void setEdge();
	
	public edge_data getEdge();
	
}
