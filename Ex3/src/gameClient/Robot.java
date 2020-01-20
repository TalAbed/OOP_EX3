package gameClient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

public class Robot implements _robot{
	
	int src;
	int dest;
	int id;
	int speed;
	Point3D locaiton ;
	double value;
	List<node_data> Path = new ArrayList<node_data>();

	public Robot() {
		src=-1;
		dest=-1;
		id=-1;
		speed=-1;
		this.locaiton=null;
		this.value=0;
		Path =null;
	}

	public void initRobot (String json ) throws JSONException {
		JSONObject obj = new JSONObject(json);
		JSONObject robot = obj.getJSONObject("Robot");
		this.id = robot.getInt("id");
		this.speed = robot.getInt("speed");
		this.src = robot.getInt("src");
		this.dest = robot.getInt("dest");
		String pos = robot.getString("pos");
		String str[] = pos.split(",");
		this.locaiton=new Point3D(Double.parseDouble(str[0]),Double.parseDouble(str[1]),Double.parseDouble(str[2]));
	}

	public void setLocation (Point3D p) {
		this.locaiton = p;
	}

	public Point3D getLocaiton() {
		return this.locaiton;
	}

	public  void setSrc(int src) {
		this.src=src;
	}

	public int getSrc() {
		return this.src;
	}

	public  void setDest(int dest) {
		this.dest=dest;
	}

	public int getDest() {
		return this.dest;
	}

	public void addV(double value) {
		this.value = this.value + value;
	}

	public double getV() {
		return this.value;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setPath(List<node_data> Path) {
		this.Path=Path;
	}
	
	public 	List<node_data> getPath() {
		return this.Path;
	}

	



}
