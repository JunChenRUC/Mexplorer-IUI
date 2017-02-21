package cn.edu.ruc.Mexplore.domain;

public class Relation extends Entity{
	private int direction;
	private String range;
	
	public Relation(){
		
	}
	
	public Relation(int type, int id){
		setType(type);
		setId(id);
		setScore(1);
	}
	
	public Relation(int type, int id, String name){
		setType(type);
		setId(id);
		setName(name);
		setScore(1);
	}
	
	public Relation(int type, int id, String name, int direction){
		setType(type);
		setId(id);
		setName(name);
		setDirection(direction);
		setScore(1);
	}
	
	public Relation(int type, int id, int direction){
		setType(type);
		setId(id);
		setDirection(direction);
		setScore(1);
	}
	
	public Relation(int type, int id, int direction, double score){
		setType(type);
		setId(id);
		setDirection(direction);
		setScore(score);;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}
}
