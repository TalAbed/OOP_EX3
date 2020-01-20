package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;

public class AutoGame implements _game {
	
	private game_service game;
	private graph g;
	ArrayList <Fruit> fruits;
	HashMap <Integer, Robot> robots;
	private int movingRobot;
	boolean b = false;
	private double x;
	private double y;
	

	@Override
	public void initLevel(int level) {
		game = Game_Server.getServer(level);
		String s = game.getGraph();
		DGraph dg = new DGraph();
		dg.init(s);
		this.g = dg;
		Iterator <String> fi = game.getFruits().iterator();
		if (fruits==null)
			fruits = new ArrayList<Fruit>();
		else
			fruits.clear();
		while (fi.hasNext()) {
			try {
				Fruit f = new Fruit (g);
				f.initFruit(fi.next());
				fruits.add(f);
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		String info = game.toString();
		JSONObject obj;
		try {
			obj = new JSONObject (info);
			JSONObject jo = obj.getJSONObject("GameServer");
			int r = jo.getInt("robots");
			if (robots==null)
				robots = new HashMap<Integer, Robot>();
			else
				robots.clear();
			int i=0;
			Iterator <edge_data> ir = setRobots().iterator();
			while (i<r) {
				if (ir.hasNext())
					game.addRobot(ir.next().getSrc());
				else 
					game.addRobot((int)Math.random()*g.nodeSize());
				i++;
			}
			for (String str : game.getRobots()) {
				Robot robot = new Robot();
				robot.initRobot(str);
				robots.put(robot.getId(), robot);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<edge_data> setRobots() {
		Iterator <Fruit> fi = fruits.iterator();
		ArrayList <edge_data> AL = new ArrayList <edge_data>();
		while(fi.hasNext())
			AL.add(fi.next().getEdge());
		return AL;
	}

	private int i;
	@Override
	public void moveRobot() {
		try {
			fruits = new ArrayList <Fruit>();
			fruits.clear();
			Iterator <String> fi = game.getFruits().iterator();
			while(fi.hasNext()) {
				Fruit f = new Fruit (g);
				f.initFruit(fi.next());
				fruits.add(f);
			}
			for (Robot r : robots.values()) {
				if (r.dest==-1) {
					movingRobot = r.getId();
					i = i+setPath();
					Iterator <node_data> it = r.getPath().iterator();
					while (it.hasNext())
						game.chooseNextEdge(r.getId(), it.next().getKey());
					r.setPath(null);
					r.setDest(-1);
				}
			}
			robots.clear();
			for (String s : game.getRobots()) {
				Robot robot = new Robot();
				robot.initRobot(s);
				robots.put(robot.getId(), robot);
			}
			game.move();
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int setPath() {
		Fruit f = null;
		Fruit fruit = null;
		graph_algorithms ga = new Graph_Algo(g);
		double min = Double.MAX_VALUE;
		Iterator <Fruit> fi = fruits.iterator();
		_robot r = robots.get(movingRobot);
		while (fi.hasNext()) {
			f = fi.next();
			if(f.getEdge().getSrc()==r.getSrc()) {
				double d = f.getEdge().getWeight();
				if (d<min) {
					min = d;
					fruit = f;
					ArrayList <node_data> path = new ArrayList<node_data>();
					path.add(g.getNode(f.getEdge().getSrc()));
					path.add(g.getNode(f.getEdge().getDest()));
					r.setPath(path);
				}
			}
			else {
				double d = ga.shortestPathDist(r.getSrc(), f.getEdge().getSrc());
				d = d + f.getEdge().getWeight();
				if (d<min) {
					min = d;
					fruit = f;
					r.setPath(ga.shortestPath(r.getSrc(), f.getEdge().getSrc()));
					r.getPath().add(g.getNode(f.getEdge().getDest()));
				}
			}
		}
		fruits.remove(fruit);
		return r.getPath().size();
	}

	@Override
	public graph getGraph() {
		return this.g;
	}

	@Override
	public ArrayList<Fruit> getFruits() {
		return this.fruits;
	}

	@Override
	public Collection<Robot> getRobots() {
		return this.robots.values();
	}

	@Override
	public game_service getGame() {
		return this.game;
	}
	
	

}
