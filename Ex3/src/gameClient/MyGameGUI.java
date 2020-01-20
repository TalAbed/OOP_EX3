package gameClient;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Server.game_service;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;
import utils.StdDraw;

public class MyGameGUI {
	
	private game_service gs;
	private graph gui_graph;
	private _game game;
	private long time = 0;
	private double minX = Double.MIN_VALUE;
	private double maxX = Double.MAX_VALUE;
	private double minY = Double.MIN_VALUE;
	private double maxY = Double.MAX_VALUE;
	private double x = 0;
	private double y = 0;

	
	
	public MyGameGUI (graph g) {
		this.gui_graph = g;
		game = null;
		set(this.gui_graph);
		init();
	}
	
	public MyGameGUI () {
		this.gui_graph = null;
		this.game = null;
		init();
	}
	
	public void paint() {
		StdDraw.clear();
		if (this.gui_graph == null)
			return;
		for (node_data nd : this.gui_graph.getV()) {
			StdDraw.setFont();
			Point3D p = nd.getLocation();
			StdDraw.setPenColor(Color.BLACK);
			StdDraw.circle(p.x(), p.y(), 0.0000);
			StdDraw.text(p.x()+0.001, p.y()+0.001, ""+nd.getKey());
			for (edge_data ed : this.gui_graph.getE(nd.getKey())) {
				if (ed.getTag() == Double.MIN_EXPONENT) {
					ed.setTag(0);
					StdDraw.setPenColor(Color.WHITE);
				}
				else
					StdDraw.setPenColor(Color.RED);
				Point3D p2 = this.gui_graph.getNode(ed.getDest()).getLocation();
				StdDraw.line(p.x(), p.y(), p2.x(), p2.y());
				StdDraw.text((p.x()+p2.x())/2, (p.y()+p2.y())/2, ""+ed.getWeight());
			}
		}
		
		//draw fruits
		ArrayList <Fruit> fruits = game.getFruits();
		if (!fruits.isEmpty()) {
			Iterator <Fruit> itfr = fruits.iterator();
			while (itfr.hasNext()) {
				Fruit f = itfr.next();
				if (f.getType()==1)
					StdDraw.setPenColor(Color.GREEN);
				else
					StdDraw.setPenColor(Color.BLUE);
				StdDraw.circle(f.getlocaiton().x(), f.getlocaiton().y(), 0.005);
			}
		}
		
		//draw robots
		Collection <Robot> robots = game.getRobots();
		if(!robots.isEmpty()) {
			for (Robot r : robots) {
				StdDraw.setPenColor(Color.YELLOW);
				StdDraw.circle(r.getLocaiton().x(), r.getLocaiton().y(), 0.005);
			}
			StdDraw.show();
		}
		
		StdDraw.setPenColor(Color.BLACK);
		StdDraw.setFont();
		StdDraw.textLeft(50, 50, "time left: " + time);
	}
	
	public void init() {
		if (!StdDraw.isPainted()) {
			StdDraw.setCanvasSize(1000, 1000);
			StdDraw.enableDoubleBuffering();
			StdDraw.paint();
		}
		if (game!=null)
			this.gui_graph = game.getGraph();
		if (this.gui_graph!=null)
			set (this.gui_graph);
		StdDraw.setXscale(minX, maxX);
		StdDraw.setYscale(minY, maxY);
		StdDraw.set_GUI(this);
		StdDraw.show();
		paint();
	}
	
	public void set (graph g) {
		double [] x = new double [g.getV().size()];
		double [] y = new double [g.getV().size()];
		int i=0;
		for (node_data nd : g.getV()) {
			x[i] = nd.getLocation().x();
			y[i] = nd.getLocation().y();
			i++;
		}
		Arrays.sort(x);
		Arrays.sort(y);
		this.minX = x[0];
		this.maxX = x[g.getV().size()-1];
		this.minY = y[0];
		this.maxY = y[g.getV().size()-1];
	}
	
	public void manualGame (int level) {
		try {
			//int l = Integer.parseInt(level);
			if (level>=0 && level<24) {
				game = new ManualGame();
				game.initLevel(level);
				this.gs = game.getGame();
				init();
				user();
			}
			else {
				JFrame j = new JFrame();
				JOptionPane.showMessageDialog(j, "this level does not exist");
				j.dispose();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void user() {
		gs.startGame();
		while(gs.isRunning()) {
			game.moveRobot();
			paint();
			time = gs.timeToEnd()/1000;
			if(time<=10)
				break;
		}
		String s = gs.toString();
		System.out.println(s);
	}
	
	public void autoGame (int level) {
		try {
			//int l = Integer.parseInt(level);
			if (level>=0 && level<24) {
				game = new AutoGame();
				game.initLevel(level);
				this.gs = game.getGame();
				init();
				computer();
			}
			else {
				JFrame j = new JFrame();
				JOptionPane.showMessageDialog(j, "this level does not exist");
				j.dispose();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void computer() {
		gs.startGame();
		while (gs.isRunning()) {
			game.moveRobot();
			paint();
			time = gs.timeToEnd()/1000;
			if(time<=10)
				break;
		}
		String s = gs.toString();
		System.out.println(s);
	}
	
	
	
	public static void main(String[] args) {
		MyGameGUI maze= new MyGameGUI();
		maze.autoGame(0);
	}
}
