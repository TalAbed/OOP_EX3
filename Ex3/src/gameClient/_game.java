package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Server.game_service;
import dataStructure.edge_data;
import dataStructure.graph;

public interface _game {
	
	public void initLevel (int level);
	
	public List <edge_data> setRobots();
	
	public void moveRobot();
	
	public int setPath();
	
	public graph getGraph();
	
	public ArrayList <Fruit> getFruits();
	
	public Collection <Robot> getRobots();
	
	public game_service getGame();
	
	

}
