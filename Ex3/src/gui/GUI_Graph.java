package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

public class GUI_Graph extends JFrame implements ActionListener, MouseListener, MouseMotionListener, KeyListener{
	
	graph gui_graph;
	private double minX = Double.MIN_VALUE;
	private double maxX = Double.MAX_VALUE;
	private double minY = Double.MIN_VALUE;
	private double maxY = Double.MAX_VALUE;
	
	public GUI_Graph (graph g) {
		this.gui_graph = g;
		set (this.gui_graph);
		init();
	}
	
	public GUI_Graph() {
		init();
	}
	
	public void init() {
		this.setSize(600, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MenuBar mb = new MenuBar();
		Menu file = new Menu ("file");
		Menu graph_menu = new Menu ("graph_menu");
		mb.add(file);
		mb.add(graph_menu);
		this.setMenuBar(mb);
		MenuItem save = new MenuItem ("save");
		save.addActionListener(this);
		MenuItem load = new MenuItem ("load");
		load.addActionListener(this);
		MenuItem isConnected = new MenuItem ("isConnected");
		isConnected.addActionListener(this);
		MenuItem shortestPath = new MenuItem ("shortestPath");
		shortestPath.addActionListener(this);
		MenuItem shortestPathDist = new MenuItem ("shortestPathDist");
		shortestPathDist.addActionListener(this);
		MenuItem TSP = new MenuItem ("TSP");
		TSP.addActionListener(this);
		MenuItem addEdge = new MenuItem ("addEdge");
		addEdge.addActionListener(this);
		MenuItem removeEdge = new MenuItem ("removeEdge");
		removeEdge.addActionListener(this);
		file.add(load);
		file.add(save);
		graph_menu.add(isConnected);
		graph_menu.add(shortestPath);
		graph_menu.add(shortestPathDist);
		graph_menu.add(TSP);
		graph_menu.add(addEdge);
		graph_menu.add(removeEdge);
		this.addMouseListener(this);
		}
	
	public void init (graph g) {
		this.gui_graph = g;
		set(this.gui_graph);
		init();
	}
	
	private void set (graph g) {
		Collection <node_data> nodes = g.getV();
		double [] x = new double [nodes.size()];
		double [] y = new double [nodes.size()];
		int i=0;
		for (node_data n : nodes) {
			x[i] = n.getLocation().x();
			y[i] = n.getLocation().y();
			i++;
		}
		Arrays.sort(x);
		Arrays.sort(y);
		this.minX = x[0];
		this.maxX = x[nodes.size()-1];
		this.minY = y[0];
		this.maxY = y[nodes.size()-1];
	}
	
	private double scale (double data, double min1, double max1, double min2, double max2) {
		double r = ((data - min1)/(max1 - min1)) * (max2-min2) + max2;
		return r;
	}
	
	public void paint (Graphics g) {
		super.paint(g);
		if(gui_graph==null)
			return;
		Collection <node_data> nodes = gui_graph.getV();
		for (node_data n : nodes) {
			double x = scale(n.getLocation().x(), this.minX, this.maxX, 10, getWidth()-20);
			double y = scale (n.getLocation().y(), this.minY, this.maxY, 70, getHeight()-20);
			Point3D p = new Point3D (x,y);
			g.setColor(Color.BLUE);
			g.fillOval(p.ix(), p.iy(), 15, 15);
			g.drawString(""+n.getKey(), p.ix()+1, p.iy()+1);
			Collection <edge_data> edges = gui_graph.getE(n.getKey());
			for (edge_data e : edges) {
				if (e.getTag() == Double.MIN_EXPONENT) {
					g.setColor(Color.RED);
					e.setTag(0);
				}
				else
					g.setColor(Color.YELLOW);
				x = scale (gui_graph.getNode(e.getDest()).getLocation().x(), this.minX, this.maxX, 10, getWidth()-20);
				y = scale (gui_graph.getNode(e.getDest()).getLocation().y(), this.minY, this.maxY, 70, getHeight()-20);
				Point3D p2 = new Point3D (x,y);
				g.drawLine(p.ix(), p.iy(), p2.ix(), p2.iy());
				g.drawString(""+e.getWeight(), (int)(((p.x()*3+p2.x())/4)), (int)((p.y()*3+p2.y())/4));
				g.setColor(Color.WHITE);
				g.fillOval((int)(((p.x()*3+p2.x())/4)),(int)((p.y()*3+p2.y())/4),10,10);
			}
		}
	}
	
	public void save () {
		graph_algorithms ga = new Graph_Algo();
		ga.init(this.gui_graph);
		JFileChooser chooser = new JFileChooser (FileSystemView.getFileSystemView().getHomeDirectory());
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				ga.save(chooser.getSelectedFile() + ".txt");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void load() {
		Graph_Algo ga = new Graph_Algo();
		JFileChooser chooser = new JFileChooser (FileSystemView.getFileSystemView().getHomeDirectory());
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				File selectedFile = chooser.getSelectedFile();
				ga.init(selectedFile.getAbsolutePath());
				this.gui_graph = ga.copy();
				repaint();
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void isConnected() {
		graph_algorithms ga = new Graph_Algo();
		ga.init(this.gui_graph);
		boolean flag = ga.isConnected();
		if (flag)
			JOptionPane.showMessageDialog(null, "the graph is connected", "isConnected", JOptionPane.QUESTION_MESSAGE);
		else
			JOptionPane.showMessageDialog(null, "the graph is not connected", "isConnected", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void shortestPath () {
		String src = JOptionPane.showInputDialog("enter start point");
		String dest = JOptionPane.showInputDialog("enter end point");
		graph_algorithms ga = new Graph_Algo();
		ga.init(this.gui_graph);
		List <node_data> nodes = ga.shortestPath(Integer.parseInt(src), Integer.parseInt(dest));
		if (nodes == null) {
			JOptionPane.showMessageDialog(null, "there is no path between the points", "shortest path points", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		int s = 0;
		for (int d=1;d<nodes.size();d++,s++)
			this.gui_graph.getEdge(nodes.get(s).getKey(), nodes.get(d).getKey()).setTag(Double.MIN_EXPONENT);
		repaint();
	}
	
	public void shortestPathDist() {
		String src = JOptionPane.showInputDialog("enter start point");
		String dest = JOptionPane.showInputDialog("enter end point");
		graph_algorithms ga = new Graph_Algo();
		ga.init(this.gui_graph);
		double ans = ga.shortestPathDist(Integer.parseInt(src), Integer.parseInt(dest));
		if (ans != Double.MAX_VALUE)
			JOptionPane.showMessageDialog(null,"The shortest path dist is "+ans,"shortest path dist", JOptionPane.INFORMATION_MESSAGE);
		else 
			JOptionPane.showMessageDialog(null,"The shortest path dist is "+Double.POSITIVE_INFINITY, "shortest path:", JOptionPane.INFORMATION_MESSAGE);	
	}
	
	public void TSP() {
		List <Integer> l = new ArrayList <Integer>();
		int goAgain;
		String input = "";
		do {
			input = JOptionPane.showInputDialog("enter points \n");
			try {
				l.add(Integer.parseInt(input));
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(null, "", "error", JOptionPane.INFORMATION_MESSAGE);
				e.printStackTrace();
				return;
			}
			goAgain = JOptionPane.showConfirmDialog(null, "type YES to go again or NO to stop", "confirm", JOptionPane.YES_NO_OPTION);
		}
		while (goAgain == JOptionPane.YES_OPTION);
		graph_algorithms ga = new Graph_Algo();
		ga.init(this.gui_graph);
		List <node_data> nodes = ga.TSP(l);
		if (nodes == null) {
			JOptionPane.showMessageDialog(null, "there is no path between the points", "TSP", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		int src = 0;
		String path = ""+nodes.get(src).getKey();
		for (int dest=1; dest<nodes.size();src++, dest++) {
			this.gui_graph.getEdge(nodes.get(src).getKey(), nodes.get(dest).getKey()).setTag(Double.MAX_EXPONENT);
			path = path + "->" + nodes.get(dest).getKey();
		}
		JOptionPane.showMessageDialog(null, path, "path:", JOptionPane.INFORMATION_MESSAGE);
		repaint();
	}
	
	public void addEdge() {
		String src = JOptionPane.showInputDialog("enter start point");
		String dest = JOptionPane.showInputDialog("enter end point");
		String w = JOptionPane.showInputDialog("enter weight");
		int wi = Integer.parseInt(w);
		try {
			if(wi<0) {
				JOptionPane.showMessageDialog(null, "weight can't be negative", "", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			this.gui_graph.connect(Integer.parseInt(src), Integer.parseInt(dest), wi);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "src or dest does not exist", "", JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
			return;
		}
		JOptionPane.showMessageDialog(null, "edge number = "+ this.gui_graph.edgeSize(), "", JOptionPane.INFORMATION_MESSAGE);
		repaint();
	}
	
	public void removeEdge() {
		String src = JOptionPane.showInputDialog("enter start point");
		String dest = JOptionPane.showInputDialog("enter end point");
		edge_data ed = null;
		try {
			ed = this.gui_graph.removeEdge(Integer.parseInt(src), Integer.parseInt(dest));
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "the edge you want to remove does not exist", "", JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}
		if (ed!=null)
			JOptionPane.showMessageDialog(null, "edge number = "+ this.gui_graph.edgeSize(), "", JOptionPane.INFORMATION_MESSAGE);
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		switch(s) {
		case "save":
			save();
			break;
		case "load":
			load();
			break;
		case "isConnected":
			isConnected();
			break;
		case "shortestPath":
			shortestPath();
			break;
		case "shortestPathDist":
			shortestPathDist();
			break;
		case "TSP":
			TSP();
			break;
		case "addEdge":
			addEdge();
			break;
		case "removeEdge":
			removeEdge();
			break;
		}
	}
	
	public static void main (String []args) {
		GUI_Graph g = new GUI_Graph();
		g.setVisible(true);
	}
}
