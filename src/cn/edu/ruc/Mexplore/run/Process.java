package cn.edu.ruc.Mexplore.run;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.apache.jasper.tagplugins.jstl.core.If;

import cn.edu.ruc.Mexplore.data.DataManager;
import cn.edu.ruc.Mexplore.domain.Entity;
import cn.edu.ruc.Mexplore.domain.Feature;
import cn.edu.ruc.Mexplore.domain.Path;
import cn.edu.ruc.Mexplore.domain.Relation;
import cn.edu.ruc.Mexplore.domain.Result;
import cn.edu.ruc.Mexplore.utility.QuickSortEntity;
import cn.edu.ruc.Mexplore.utility.QuickSortFeature;
import cn.edu.ruc.Mexplore.utility.QuickSortPath;
import cn.edu.ruc.Mexplore.utility.QuickSortRelation;

public class Process {
	private DataManager dataManager;
	
	public Process() {
		install();
	}
	
	public void install() {
		dataManager = new DataManager();
	}

	public Result getResult(String queryString){	
		Result result = new Result();
		
		result.setQueryEntity(dataManager.getEncodedEntity(queryString));
		
		result.setQueryFeatureList(getFact(result.getQueryEntity()));

		result.setResultEntityList(getEntity(result.getQueryEntity()));
		
		result.setResultFeatureList(getFeature(result.getResultEntityList()));
	
		dataManager.decodeResult(result);
		
		return result;
	}
	
	public ArrayList<Feature> getFact(Entity queryEntity){
		ArrayList<Feature> featureList = new ArrayList<>();
		
		int count = 0;
		for(Relation relation : getRelation(queryEntity)){
			if(++ count > 4)
				continue;
			for(Entity targetEntity : rankEntity(getTarget(queryEntity, relation), relation.equals(dataManager.getEncodedRelation("subject")) ? 10 : DataManager.output_size_feature))
				featureList.add(new Feature(relation, targetEntity, targetEntity.getScore() * relation.getScore()));
		}
		return featureList;
	}
	
	public ArrayList<Relation> getRelation(Entity queryEntity){
		HashMap<Integer, Relation> relationMap = new HashMap<>();
		
		int triple_size_max = 1, triple_size_min = Integer.MAX_VALUE;	
		for(int i = 0; i < 2; i ++){
			if(dataManager.getTripleHash().get(i).containsKey(queryEntity.getId())){
				for(HashSet<Integer> entitySet : dataManager.getTripleHash().get(i).get(queryEntity.getId()).values())	{
					if(entitySet.size() > triple_size_max)
						triple_size_max = entitySet.size();
					if(entitySet.size() < triple_size_min)
						triple_size_min = entitySet.size();
				}
			}
		}
			
		for(int i = 0; i < 2; i ++){
			if(dataManager.getTripleHash().get(i).containsKey(queryEntity.getId())){
				for(Entry<Integer, HashSet<Integer>> relation2entityEntry : dataManager.getTripleHash().get(i).get(queryEntity.getId()).entrySet()){
					Relation relation = new Relation(DataManager.relation_type, relation2entityEntry.getKey(), i);
					
					double score = (double)relation2entityEntry.getValue().size() / (triple_size_min + triple_size_max);
					score = (- score * Math.log(score)) * queryEntity.getScore();
					relation.setScore(score);
					if(relation.equals(dataManager.getEncodedRelation("subject")))
						relation.setScore(1);

					if(!relationMap.containsKey(relation.getId()))
						relationMap.put(relation.getId(), relation);
					else
						relationMap.get(relation.getId()).setScore(relationMap.get(relation.getId()).getScore() + relation.getScore());
				}
			}
		}

		return rankRelation(new ArrayList<Relation>(relationMap.values()), relationMap.size());
	}

	public ArrayList<Entity> getTarget(Entity queryEntity, Relation relation){
		HashMap<Integer, Entity> targetEntityMap = new HashMap<>();
		
		double[] sourceVector = dataManager.getVector(queryEntity.getId(), queryEntity.getType());
		if(dataManager.getTripleHash().get(relation.getDirection()).containsKey(queryEntity.getId())){
			if(dataManager.getTripleHash().get(relation.getDirection()).get(queryEntity.getId()).containsKey(relation.getId())){
				for(int targetId : dataManager.getTripleHash().get(relation.getDirection()).get(queryEntity.getId()).get(relation.getId())){				
					Entity targetEntity = dataManager.getEncodedEntity(targetId);
					double[] targetVector = dataManager.getVector(targetEntity.getId(), targetEntity.getType());
					
					double score = getScoreOfEntities(sourceVector, targetVector);			
					targetEntity.setScore(score);					
					
					if(!targetEntityMap.containsKey(targetEntity.getId()))
						targetEntityMap.put(targetEntity.getId(), targetEntity);
					else
						targetEntityMap.get(targetEntity.getId()).setScore(targetEntityMap.get(targetEntity.getId()).getScore() + targetEntity.getScore());
				
				}
			}
		}
		
		return new ArrayList<Entity>(targetEntityMap.values());
	}
	
	public ArrayList<Entity> getEntity(Entity queryEntity){	
		ArrayList<Entity> entity2distanceList = new ArrayList<>();
		
		for(Entry<Integer, double[]> entity2vectorEntry : dataManager.getEntity2Vector().entrySet()){
			if(queryEntity.getId() == entity2vectorEntry.getKey())
				continue;
					
			double[] queryVector = dataManager.getVector(queryEntity.getId(), queryEntity.getType());
			
			double score = getScoreOfEntities(queryVector, entity2vectorEntry.getValue());
			
			entity2distanceList.add(new Entity(DataManager.entity_type, entity2vectorEntry.getKey(), score));
		}	
		
		return rankEntity(entity2distanceList, DataManager.output_size_entity);
	}
	
	public ArrayList<Feature> getFeature(ArrayList<Entity> queryEntityList){
		ArrayList<Feature> featureList = new ArrayList<>();
				
		int count = 0;
		for(Relation relation : getRelation(queryEntityList)){
			if( ++ count > 4)
				continue;
			double[] relationVector = dataManager.getVector(relation.getId(), relation.getType());
			
			ArrayList<Entity> entity2distanceList = new ArrayList<>();
			for(Entry<Integer, double[]> entity2vectorEntry : dataManager.getEntity2Vector().entrySet()){											
				double[] targetVector = entity2vectorEntry.getValue();
				
				double score = 0;			
				for(Entity queryEntity : queryEntityList){
					double[] sourceVector = dataManager.getVector(queryEntity.getId(), queryEntity.getType());
					
					if(relation.getDirection() == 0)
						score += getScoreOfEntities(getTail(sourceVector, relationVector), targetVector) * queryEntity.getScore();
					else
						score += getScoreOfEntities(getHead(sourceVector, relationVector), targetVector) * queryEntity.getScore();
				}
				entity2distanceList.add(new Entity(DataManager.entity_type, entity2vectorEntry.getKey(), score));
			}
		
			for(Entity target : rankEntity(entity2distanceList, relation.equals(dataManager.getEncodedRelation("subject")) ? 10 : DataManager.output_size_feature))
				featureList.add(new Feature(relation, target, target.getScore() * relation.getScore()));
		}
		
		return featureList;
	}

	public ArrayList<Relation> getRelation(ArrayList<Entity> queryEntityList){
		HashMap<Integer, Relation> relationMap = new HashMap<>();
		
		int triple_size_max = 1, triple_size_min = Integer.MAX_VALUE;	
		for(Entity queryEntity : queryEntityList){
			for(int i = 0; i < 2; i ++){
				if(dataManager.getTripleHash().get(i).containsKey(queryEntity.getId())){
					for(HashSet<Integer> entitySet : dataManager.getTripleHash().get(i).get(queryEntity.getId()).values())	{
						if(entitySet.size() > triple_size_max)
							triple_size_max = entitySet.size();
						if(entitySet.size() < triple_size_min)
							triple_size_min = entitySet.size();
					}
				}
			}
		}
		
		for(Entity queryEntity : queryEntityList){
			for(int i = 0; i < 2; i ++){
				if(dataManager.getTripleHash().get(i).containsKey(queryEntity.getId())){
					for(Entry<Integer, HashSet<Integer>> relation2entityEntry : dataManager.getTripleHash().get(i).get(queryEntity.getId()).entrySet()){
						Relation relation = new Relation(DataManager.relation_type, relation2entityEntry.getKey(), i);
						
						double score = (double)relation2entityEntry.getValue().size() / (triple_size_min + triple_size_max);
						score = (- score * Math.log(score)) * queryEntity.getScore();
						relation.setScore(score);
						if(relation.equals(dataManager.getEncodedRelation("subject")))
							relation.setScore(1);
	
						if(!relationMap.containsKey(relation.getId()))
							relationMap.put(relation.getId(), relation);
						else
							relationMap.get(relation.getId()).setScore(relationMap.get(relation.getId()).getScore() + relation.getScore());
					}
				}
			}
		}

		return rankRelation(new ArrayList<Relation>(relationMap.values()), relationMap.size());
	}
		
	//get path
	public ArrayList<Path> getPath(String headName, String tailName){
		Entity head, tail;
		head = dataManager.getEncodedEntity(headName);
		tail = dataManager.getEncodedEntity(tailName);
		
		ArrayList<Path> pathList = new ArrayList<>();
		
		//get 1 hop path		
		for(Relation relation : getRelation(head)){
			int size = getTarget(head, relation).size();
			for(Entity target : getTarget(head, relation)){
				if(target.equals(tail)){
					Path path = new Path();
					path.setHead(head);
					path.addRelation(relation);
					path.addEntity(target);
					path.setScore(target.getScore() / Math.log(size));
					pathList.add(path);
				}
			}
		}
		
		//get 2 hop path
		if(pathList.size() < DataManager.output_size_feature){
			for(Relation relation : getRelation(head)){
				int size = getTarget(head, relation).size();
				for(Entity target : getTarget(head, relation)){
					if(target.equals(tail))
						continue;
					
					for(Relation relation2 : getRelation(target)){
						int size2 = getTarget(target, relation2).size();
						for(Entity target2 : getTarget(target, relation2)){
							if(target2.equals(tail)){
								Path path = new Path();
								path.setHead(head);
								path.addRelation(relation);
								path.addEntity(target);
								path.addRelation(relation2);
								path.addEntity(target2);
								path.setScore(target.getScore() / Math.log(size) * target2.getScore() / Math.log(size2));
								pathList.add(path);
							}
						}
					}
				}
			}
		}
		
		//get 3 hop path
		if(pathList.size() < DataManager.output_size_feature){
			for(Relation relation : getRelation(head)){
				int size = getTarget(head, relation).size();
				for(Entity target : getTarget(head, relation)){
					if(target.equals(tail))
						continue;
					
					for(Relation relation2 : getRelation(target)){
						int size2 = getTarget(target, relation2).size();
						for(Entity target2 : getTarget(target, relation2)){
							if(target2.equals(tail) || target2.equals(head))
								continue;
							
							for(Relation relation3 : getRelation(target2)){
								int size3 = getTarget(target2, relation3).size();
								for(Entity target3 : getTarget(target2, relation3)){
									if(target3.equals(tail)){
										Path path = new Path();
										path.setHead(head);
										path.addRelation(relation);
										path.addEntity(target);
										path.addRelation(relation2);
										path.addEntity(target2);
										path.addRelation(relation3);
										path.addEntity(target3);
										path.setScore(target.getScore() / Math.log(size) * target2.getScore() / Math.log(size2) * target3.getScore() / Math.log(size3));
										pathList.add(path);
									}
								}
							}
						}
					}
				}
			}
		}
		
        pathList = rankPath(pathList, DataManager.output_size_feature);
        dataManager.decodePathList(pathList);
        
		return pathList;
	}
	
	public double[] getHead(double[] eVector_tail, double[] rVector){
		double[] eVector_head = new double[DataManager.D_relation];
		for(int i = 0; i < DataManager.D_relation; i ++)
			eVector_head[i] = eVector_tail[i] - rVector[i];
		return eVector_head;
	}
	
	public double[] getTail(double[] eVector_head, double[] rVector){
		double[] vector_tail = new double[DataManager.D_relation];
		for(int i = 0; i < DataManager.D_relation; i ++)
			vector_tail[i] = eVector_head[i] + rVector[i];
		return vector_tail;
	}
	
	public double getScoreOfEntities(double[] eVector1, double[] eVector2){
		double score = 0;
		int dimension = eVector1.length;
		for(int i = 0; i < dimension; i ++)
			score += Math.pow(eVector1[i] - eVector2[i], 2);
		return (Math.sqrt(dimension) - Math.sqrt(score)) / Math.sqrt(dimension);
	}
	
	public ArrayList<Entity> rankEntity(ArrayList<Entity> data, int k){  	
		k = (k > data.size() ? data.size() : k);
		
		Entity[] topk = new Entity[k];
        		
		for(int i = 0; i < k; i++)
            topk[i] = data.get(i);   
          
        QuickSortEntity quickSort = new QuickSortEntity(); 
        quickSort.initialSort(topk);
           
        for(int i = k; i < data.size();i++){   
            if(data.get(i).getScore() > quickSort.getMin().getScore())
            	quickSort.replace(data.get(i)); 
        }  
        
        ArrayList<Entity> resultList = new ArrayList<>();
        for(Entity result : topk)
        	resultList.add(result);
        
        return resultList;  
	} 
	
	public ArrayList<Relation> rankRelation(ArrayList<Relation> arrayList, int k){  	
		k = (k > arrayList.size() ? arrayList.size() : k);
		
		Relation[] topk = new Relation[k];
        		
		for(int i = 0; i < k; i++)
            topk[i] = arrayList.get(i);   
       
        QuickSortRelation quickSort = new QuickSortRelation(); 
        quickSort.initialSort(topk);
           
        for(int i = k; i < arrayList.size();i++){   
            if(arrayList.get(i).getScore() > quickSort.getMin().getScore())
            	quickSort.replace(arrayList.get(i)); 
        }  
        
        ArrayList<Relation> relationList = new ArrayList<>();
        for(Relation relation : topk)
        	relationList.add(relation);
        
        return relationList;  
	} 
	
	public ArrayList<Feature> rankFeature(ArrayList<Feature> data, int k){  	
		k = (k > data.size() ? data.size() : k);
		
		Feature[] topk = new Feature[k];
        		
		for(int i = 0; i < k; i++)
            topk[i] = data.get(i);   
          
        QuickSortFeature quickSort = new QuickSortFeature(); 
        quickSort.initialSort(topk);
           
        for(int i = k; i < data.size();i++){   
            if(data.get(i).getScore() > quickSort.getMin().getScore())
            	quickSort.replace(data.get(i)); 
        }  
        
        ArrayList<Feature> featureList = new ArrayList<>();
        for(Feature result : topk)
        	featureList.add(result);
        
        return featureList;  
	} 
	
	public ArrayList<Path> rankPath(ArrayList<Path> data, int k){  	
		k = (k > data.size() ? data.size() : k);
		
		Path[] topk = new Path[k];
        		
		for(int i = 0; i < k; i++)
            topk[i] = data.get(i);   
          
        QuickSortPath quickSort = new QuickSortPath(); 
        quickSort.initialSort(topk);
           
        for(int i = k; i < data.size();i++){   
            if(data.get(i).getScore() > quickSort.getMin().getScore())
            	quickSort.replace(data.get(i)); 
        }  
        
        ArrayList<Path> pathList = new ArrayList<>();
        for(Path result : topk)
        	pathList.add(result);
        
        return pathList;  
	} 
}
