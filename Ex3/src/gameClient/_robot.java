package gameClient;

import java.util.List;

import org.json.JSONException;

import dataStructure.node_data;
import utils.Point3D;

public interface _robot {
	
	public void initRobot (String json ) throws JSONException;
	
	public void setLocation (Point3D p);
	
	public Point3D getLocaiton();
	
	public  void setSrc(int src);
	
	public int getSrc();
	
	public  void setDest(int dest);
	
	public int getDest();
	
	public void addV(double value);
	
	public double getV();
	
	public int getId();
	
	public void setPath(List<node_data> Path);
	
	public 	List<node_data> getPath();

}
