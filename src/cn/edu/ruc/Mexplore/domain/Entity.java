package cn.edu.ruc.Mexplore.domain;

import java.text.DecimalFormat;
import java.util.HashSet;

public class Entity {
	private int type;
	private int id;
	private String name;
	private boolean nature;
	private double score;
	private HashSet<Feature> featureSet = new HashSet<>();
	
	public Entity(){
		setNature(true);
	}
	
	public Entity(int type, int id){
		setType(type);
		setId(id);
		setNature(true);
		setScore(1);
	}
	
	public Entity(int type, int id, String name){
		setType(type);
		setId(id);
		setName(name);
		setNature(true);
		setScore(1);
	}
	
	public Entity(int type, int id, double score){
		setType(type);
		setId(id);
		setNature(true);
		setScore(score);
	}
	
	@Override
	public boolean equals(Object object){
		if(!(object instanceof Entity))
			return false;
		if(object == this)
			return true;
		return (id == ((Entity) object).getId());
	}
	
	public int hashCode(){
		return new Integer(id).hashCode();
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getType(){
		return this.type;
	}
	
	public void setNature(boolean nature){
		this.nature = nature;
	}
	
	public boolean getNature(){
		return nature;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public HashSet<Feature> getFeatureSet() {
		return featureSet;
	}

	public void setFeatureSet(HashSet<Feature> featureSet) {
		this.featureSet = featureSet;
	}
	
	public void addFeature(Feature feature){
		featureSet.add(feature);
	}
	
	public String toString(){
		String string = "";
		
		DecimalFormat df = new DecimalFormat("0.0000" ); 
		string += name + "\t" + df.format(score);
		
		//string += "\t";
		//for(Feature feature : featureSet)
			//string += "["+ feature.getRelation().getName() + "_" + feature.getTarget().getName() + " " + df.format(feature.getScore()) + "]";
		
		return string;
	}
}
