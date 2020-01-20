package dataStructure;

public class Edge implements edge_data {
	
	private int src;
	private int dest;
	private double weight;
	private int tag;
	private String info;
	
	public Edge(int src, int dest, double weight, int tag, String info) {
		super();
		this.src = src;
		this.dest = dest;
		this.weight = weight;
		this.tag = tag;
		this.info = info;
	}

	public Edge(int src, int dest, double weight) {
		super();
		this.src = src;
		this.dest = dest;
		this.weight = weight;
		this.tag = -999999;
		this.info = "";
	}
	
	public Edge() {
		this.src = 0;
		this.dest = 0;
		this.weight = 0;
		this.info = "";
		this.tag = -99999;
	}

	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
	
	
	
	
	
	

}
