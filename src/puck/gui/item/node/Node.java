package puck.gui.item.node;



import java.util.HashMap;

import puck.gui.item.edge.Edge;

public class Node {
	private String name ; 
	private String id;
	private String type ;
	private HashMap<String, Edge> relation;
	
	
	public Node(){
		this.relation = new HashMap<String,Edge>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public HashMap<String, Edge> getRelation() {
		return relation;
	}
	public void setRelation(HashMap<String, Edge> relation) {
		this.relation = relation;
	}
	
	
}
