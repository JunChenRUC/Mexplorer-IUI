package cn.edu.ruc.Mexplore.domain;

import java.util.ArrayList;

public class Result {
	private Entity queryEntity = new Entity();
	private ArrayList<Feature> queryFeatureList = new ArrayList<>();
	private ArrayList<Entity> resultEntityList = new ArrayList<>();
	private ArrayList<Feature> resultFeatureList = new ArrayList<>();
	
	public Result(){
		
	}

	public Entity getQueryEntity() {
		return queryEntity;
	}

	public void setQueryEntity(Entity queryEntity) {
		this.queryEntity = queryEntity;
	}
	
	public ArrayList<Feature> getQueryFeatureList() {
		return queryFeatureList;
	}

	public void setQueryFeatureList(ArrayList<Feature> queryFeatureList) {
		this.queryFeatureList = queryFeatureList;
	}
	
	public void addQueryFeature(Feature queryFeature){
		queryFeatureList.add(queryFeature);
	}

	public ArrayList<Entity> getResultEntityList() {
		return resultEntityList;
	}

	public void setResultEntityList(ArrayList<Entity> resultEntityList) {
		this.resultEntityList = resultEntityList;
	}
	
	public void addResultEntity(Entity resultEntity){
		resultEntityList.add(resultEntity);
	}

	public ArrayList<Feature> getResultFeatureList() {
		return resultFeatureList;
	}

	public void setResultFeatureList(ArrayList<Feature> resultFeatureList) {
		this.resultFeatureList = resultFeatureList;
	}
	
	public void addResultFeature(Feature feature){
		resultFeatureList.add(feature);
	}
}
