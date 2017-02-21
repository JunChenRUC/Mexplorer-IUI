package cn.edu.ruc.Mexplore.domain;

import java.util.ArrayList;

public class Path {
	private Entity head;
	private ArrayList<Relation> relationList;
	private ArrayList<Entity> entityList;
	private double score;
	
	public Path(){
		relationList = new ArrayList<>();
		entityList = new ArrayList<>();
		score = 0;
	}

	public void setScore(double score){
		this.score = score;
	}
	
	public double getScore(){
		return score;
	}
	
	public Entity getHead() {
		return head;
	}

	public void setHead(Entity head) {
		this.head = head;
	}
	
	public ArrayList<Relation> getRelationList() {
		return relationList;
	}

	public void setRelationList(ArrayList<Relation> relationList) {
		this.relationList = relationList;
	}
	
	public void addRelation(Relation relation){
		this.relationList.add(relation);
	}

	public ArrayList<Entity> getEntityList() {
		return entityList;
	}

	public void setEntityList(ArrayList<Entity> entityList) {
		this.entityList = entityList;
	}
	
	public void addEntity(Entity entity) {
		this.entityList.add(entity);
	}
	
	public String toString(){
		String string = head.getName() + " ";
		for(int i = 0; i < relationList.size(); i ++)
			string += relationList.get(i).getName() + " " + relationList.get(i).getDirection() + " " + entityList.get(i).getName();
		
		string += score;
		
		return string;
	}


}
