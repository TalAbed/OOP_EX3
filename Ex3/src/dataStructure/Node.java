package dataStructure;

import utils.Point3D;

public class Node implements node_data {
	
	private int key;
	private Point3D location;
	private double weight;
	private String info;
	private int tag;
	
	public Node (Node n) {
		this.key = n.key;
		this.location = n.location;
		this.weight = n.weight;
		this.info = n.info;
		this.tag = n.tag;
	}
	public Node(int key,Point3D p,double weight)
    {
        this.key = key;
        this.location = new Point3D(p);
        this.weight = 0;
    }

	public Node(int key, Point3D location, double weight, String info, int tag) {
		super();
		this.key = key;
		this.location = location;
		this.weight = weight;
		this.info = info;
		this.tag = tag;
	}
	
	public Node(int key,Point3D Location)
	{
		this.key=key;
		this.location=Location;
		this.weight=Double.MAX_VALUE;
		tag=0;

	}

	@Override
	public int getKey() {
		return this.key;
	}

	@Override
	public Point3D getLocation() {
		return this.location;
	}

	@Override
	public void setLocation(Point3D p) {
		this.location = p;
	}

	@Override
	public double getWeight() {
		return this.weight;
	}

	@Override
	public void setWeight(double w) {
		this.weight = w;
	}

	@Override
	public String getInfo() {
		return this.info;
	}

	@Override
	public void setInfo(String s) {
		this.info = s;
	}

	@Override
	public int getTag() {
		return this.tag;
	}

	@Override
	public void setTag(int t) {
		this.tag = t;
	}
	
	
}

