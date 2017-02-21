package cn.edu.ruc.Mexplore.domain;

import java.text.DecimalFormat;
import java.util.LinkedHashSet;

public class Feature{
	private Relation relation;
	private Entity target;
	private boolean nature;
	private double score;
	private LinkedHashSet<Entity> entitySet = new LinkedHashSet<>();

	public Feature(Relation relation, Entity target) {
		setRelation(relation);
		setTarget(target);
		setNature(true);
	}
	
	public Feature(Relation relation, Entity target, double score) {
		setRelation(relation);
		setTarget(target);
		setScore(score);
		setNature(true);
	}
	
	@Override
	public boolean equals(Object object){
		if(!(object instanceof Feature))
			return false;
		if(object == this)
			return true;
		return (relation.getId() == ((Feature) object).getRelation().getId()) && (target.getId() == ((Feature) object).getTarget().getId());
	}
	
	public int hashCode(){
		return new Integer(relation.getId() + target.getId()).hashCode();
	}
	
	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}
	
	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}	
	
	public LinkedHashSet<Entity> getEntitySet() {
		return entitySet;
	}

	public void setEntitySet(LinkedHashSet<Entity> entitySet) {
		this.entitySet = entitySet;
	}
	
	public void addEntity(Entity entity){
		entitySet.add(entity);
	}
	
	public void addEntitySet(LinkedHashSet<Entity> entitySet){
		for(Entity entity : entitySet)
			this.entitySet.add(entity);
	}
	
	public void setNature(boolean nature){
		this.nature = nature;
	}
	
	public boolean getNature(){
		return nature;
	}
	
	public String toString(){
		String string = "";
		
		DecimalFormat df = new DecimalFormat("0.000" ); 
		
		string += "relation: [" + relation.getName() + " " + df.format(relation.getScore()) + "]";
		string += "; target: [" + target.getName() + " " + df.format(target.getScore()) + "]";
		string += "; score: " + df.format(score);
		
		//string += "\nentity set: ";
		//for(Entity entity : entitySet)
			//string += "["+ (entity.getNature() == true ? "pos" : "neg") + " " +entity.getName() + " " + df.format(entity.getScore()) + "]";
		
		return string;
	}
}
