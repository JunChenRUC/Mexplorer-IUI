package cn.edu.ruc.Mexplore.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import cn.edu.ruc.Mexplore.configuration.ConfigFactory;
import cn.edu.ruc.Mexplore.domain.Entity;
import cn.edu.ruc.Mexplore.domain.Feature;
import cn.edu.ruc.Mexplore.domain.Path;
import cn.edu.ruc.Mexplore.domain.Relation;
import cn.edu.ruc.Mexplore.domain.Result;;

public class DataManager {
	public static int error_type = 0, entity_type = 1, relation_type = 2;
	
	public static int D_entity, D_relation, output_size_entity, output_size_feature;
	
	private String commonPath, model, method;
	
	private HashMap<String, Integer> entity2id = new HashMap<>();
	private HashMap<Integer, String> id2entity = new HashMap<>();
	private HashMap<String, Integer> relation2id = new HashMap<>();
	private HashMap<Integer, String> id2relation = new HashMap<>();
	
	private HashMap<Integer, double[]> feature2vector = new HashMap<>();
	private HashMap<Integer, double[]> entity2vector = new HashMap<>();
	private HashMap<Integer, double[]> relation2vector = new HashMap<>();	
	private HashMap<Integer, double[][]> relation2transposition = new HashMap<>();
	private HashMap<Integer, HashMap<Integer,HashMap<Integer, HashSet<Integer>>>> tripleHash = new HashMap<>();

	
	public DataManager(){
		initializeParameters();
		initializeDictionary();
		initializeVector();
		initializeTriple();
	}	
	
	private void initializeParameters(){
		commonPath = ConfigFactory.getInstance().get("dir");
		model = ConfigFactory.getInstance().get("model");
		method = ConfigFactory.getInstance().get("method");
		D_entity = Integer.parseInt(ConfigFactory.getInstance().get("D_entity"));
		D_relation = Integer.parseInt(ConfigFactory.getInstance().get("D_relation"));
		output_size_entity = Integer.parseInt(ConfigFactory.getInstance().get("output_size_entity"));
		output_size_feature = Integer.parseInt(ConfigFactory.getInstance().get("output_size_feature"));
	}

	private void initializeDictionary(){
		String inputPath_entity2id = commonPath + "entity2id.txt";
		String inputPath_relation2id = commonPath + "relation2id.txt";
		String inputPath_relation2concept = commonPath + "relation2concept.txt";
		
		LoadDictionary loadDictionary = new LoadDictionary();		
		loadDictionary.loadEntity2Id(inputPath_entity2id);
		loadDictionary.loadRelation2Id(inputPath_relation2id);
		loadDictionary.loadRelation2Concept(inputPath_relation2concept);
		
		entity2id = loadDictionary.getEntity2Id();
		id2entity = loadDictionary.getId2Entity();
		relation2id = loadDictionary.getRelation2Id();
		id2relation = loadDictionary.getId2Relation();
		//relation2concept = loadDictionary.getRelation2Concept();
	}
	
	private void initializeVector(){
		String inputPath_eid2vector = commonPath + model + "/entity2vec." + method;
		String inputPath_rid2vector = commonPath + model + "/relation2vec." + method;
		
		//load vectors
		LoadVector loadVector = new LoadVector();
		loadVector.loadEntityVector(inputPath_eid2vector, id2entity);;
		loadVector.loadRelationVector(inputPath_rid2vector, id2relation);
		//if(!model.equals("TransE"))
			//loadVector.loadTransposition(inputPath_relation2transposition, id2relation);
				
		entity2vector = loadVector.getEntity2Vector();
		relation2vector = loadVector.getRelation2Vector();	
		if(!model.equals("TransE"))
			relation2transposition = loadVector.getRelation2Transposition();
	}
	
	private void initializeTriple(){
		String inputPath_triples = commonPath + "train.txt";
		
		//load triples
		LoadTriple loadTriple = new LoadTriple();
		loadTriple.loadTriples(inputPath_triples, entity2id, relation2id);
		
		tripleHash = loadTriple.getTripleHash();
	}
	
	public Entity getEncodedEntity(String name){
		if(!name.contains("_")){
			if(entity2id.containsKey(name))
				return new Entity(entity_type, entity2id.get(name), name);
			else 
				return new Entity(error_type, Integer.MAX_VALUE, name);
		}
		else
			return null;
	}
	
	public Relation getEncodedRelation(String name){
		if(!name.contains("_")){
			if(relation2id.containsKey(name))
				return new Relation(relation_type, relation2id.get(name), name);
			else 
				return new Relation(error_type, Integer.MAX_VALUE, name);
		}
		else
			return null;
	}
	
	public Relation getEncodedRelation(String name, int direction){
		if(!name.contains("_")){
			if(relation2id.containsKey(name))
				return new Relation(relation_type, relation2id.get(name), name, direction);
			else 
				return new Relation(error_type, Integer.MAX_VALUE, name);
		}
		else
			return null;
	}
	
	public Feature getEncodedFeature(String feature){
		if(feature.contains("_")){
			String tokens[] = feature.split("_");
			return new Feature(getEncodedRelation(tokens[1], Integer.parseInt(tokens[2])), getEncodedEntity(tokens[0]));
		}
		else 
			return null;
	}
	
	public Entity getEncodedEntity(int id){
		if(id2entity.containsKey(id))
			return new Entity(entity_type, id);
		else 
			return new Entity(error_type, Integer.MAX_VALUE);
	}
	
	public Relation getEncodedRelation(int id){
		if(id2relation.containsKey(id))
			return new Relation(relation_type, id);
		else 
			return new Relation(error_type, Integer.MAX_VALUE);
	}
	
	public int getEncodedId(String name, int type){
		if(type == relation_type)
			return relation2id.get(name);
		else if(type == entity_type)
			return entity2id.get(name);
		else 
			return Integer.MAX_VALUE;
	}

	public void decodeResult(Result result){
		decodeEntity(result.getQueryEntity());

		decodeFeatureList(result.getQueryFeatureList());
		
		decodeEntityList(result.getResultEntityList());
		
		decodeFeatureList(result.getResultFeatureList());
	}
	
	public void decodeEntityList(ArrayList<Entity> queryList){
		for(Entity query : queryList)
			decodeEntity(query);
	}
	
	public void decodeFeatureList(ArrayList<Feature> featureList){
		for(Feature feature : featureList)
			decodeFeature(feature);
	}
	
	public void decodePathList(ArrayList<Path> pathList){
		for(Path path : pathList)
			decodePath(path);
	}
	
	public void decodePath(Path path){
		for(Relation relation : path.getRelationList())
			decodeRelation(relation);
		for(Entity entity : path.getEntityList())
			decodeEntity(entity);
	}
	
	public void decodeFeature(Feature feature){
		decodeRelation(feature.getRelation());
		decodeEntity(feature.getTarget());
		for(Entity entity : feature.getEntitySet())
			decodeEntity(entity);
	}
	
	public void decodeEntity(Entity entity){
		entity.setName(id2entity.get(entity.getId()));
		for(Feature feature : entity.getFeatureSet())
			decodeFeature(feature);
	}
	
	public void decodeRelation(Relation relation){
		relation.setName(id2relation.get(relation.getId()));
	}
	
	public double[] getVector(int id, int type){
		if(type == relation_type)
			return relation2vector.get(id);
		else if(type == entity_type)
			return entity2vector.get(id);
		else 
			return null;
	}
	
	public double[][] getTransposition(int id){
		return relation2transposition.get(id);
	}
	
	public HashMap<Integer, double[]> getFeature2vector(){
		return feature2vector;
	}
	
	public HashMap<Integer, double[]> getEntity2Vector(){
		return entity2vector;
	}
	
	public HashMap<Integer, double[]> getRelation2Vector(){
		return relation2vector;
	}

	public HashMap<Integer, HashMap<Integer,HashMap<Integer, HashSet<Integer>>>> getTripleHash(){
		return tripleHash;
	}
}
