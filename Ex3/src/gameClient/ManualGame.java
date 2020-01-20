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
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import utils.Point3D;

public class ManualGame implements _game {
	
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

	@Override
	public void moveRobot() {
		List <String> L = game.move();
		if (L!=null) {
			try {
				int dest = setPath();
				if (dest!=-1) {
					Robot r = robots.get(movingRobot);
					if (r!=null) {
						game.chooseNextEdge(r.getId(), dest);
						game.move();
					}
				}
				fruits.clear();
				fruits = new ArrayList <Fruit>();
				Iterator <String> fi = game.getFruits().iterator();
				while (fi.hasNext()) {
					Fruit f = new Fruit (g);
					f.initFruit(fi.next());
					fruits.add(f);
				}
				robots.clear();
				List <String> robotsList = game.getRobots();
				for (String s : robotsList) {
					Robot r = new Robot();
					r.initRobot(s);
					robots.put(r.getId(), r);
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int setPath() {
		if (!b) {
			for (Robot r : robots.values()) {
				Point3D p = r.getLocaiton();
				double dist = p.distance2D(new Point3D(x,y));
				if (dist<=0.005) {
					x=y=0;
					b = true;
					movingRobot = r.getId();
					return -1;
				}
			}
			return -1;
		}
		else {
			int s = robots.get(movingRobot).getSrc();
			for (edge_data ed : g.getE(s)) {
				Point3D p = g.getNode(ed.getDest()).getLocation();
				double dist = p.distance2D(new Point3D(x,y));
				x=y=0;
				if (dist<=0.005) {
					b = false;
					return ed.getDest();
				}
			}
		}
		return -1;
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
