package cn.edu.ruc.Mexplore.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;

public class LoadTriple {
	private HashMap<Integer, HashMap<Integer,HashMap<Integer, HashSet<Integer>>>> tripleHash = new HashMap<>();
	
	public LoadTriple(){
		tripleHash.put(0, new HashMap<>());
		tripleHash.put(1, new HashMap<>());
	}
	
	public void loadTriples(String inputPath_triples, HashMap<String, Integer> entity2id, HashMap<String, Integer> relation2id){
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPath_triples)), "UTF-8"));
        	String tmpString = null;
            while((tmpString = reader.readLine()) != null) {
            	String[] tokens = tmpString.split("\t");
            	
            	String subject = URLDecoder.decode(tokens[0], "UTF-8").replaceAll("_", " ");
            	String predicate = URLDecoder.decode(tokens[2], "UTF-8");
            	String object = URLDecoder.decode(tokens[1], "UTF-8").replaceAll("_", " ");
            	//if(object.startsWith("category:"))
            		//object = object.replaceAll("category:", "");
            	
            	if(relation2id.containsKey(predicate)){
            		int relationId = relation2id.get(predicate);
            		if(entity2id.containsKey(subject) && entity2id.containsKey(object)){
                		int subjectId = entity2id.get(subject);
                		int objectId = entity2id.get(object);
                		
                		//Put out direction
                		HashMap<Integer,HashMap<Integer, HashSet<Integer>>> tripleHash_out = tripleHash.get(0);
                		if(!tripleHash_out.containsKey(subjectId)){
                			tripleHash_out.put(subjectId , new HashMap<>());
                			tripleHash_out.get(subjectId).put(relationId, new HashSet<>());
                		}
                		else if(!tripleHash_out.get(subjectId).containsKey(relationId))
                			tripleHash_out.get(subjectId).put(relationId, new HashSet<>());

                		tripleHash_out.get(subjectId).get(relationId).add(objectId);
                		
                		HashMap<Integer,HashMap<Integer, HashSet<Integer>>> tripleHash_in = tripleHash.get(1);               		
                		if(!tripleHash_in.containsKey(objectId)){
                			tripleHash_in.put(objectId , new HashMap<>());
                			tripleHash_in.get(objectId).put(relationId, new HashSet<>());
                		}
                		else if(!tripleHash_in.get(objectId).containsKey(relationId))
                			tripleHash_in.get(objectId).put(relationId, new HashSet<>());

                		tripleHash_in.get(objectId).get(relationId).add(subjectId);
            		}
                	else 
                		System.out.println("Loading triples! Wrong entity dictionary\t" + subject + "\t" + object);	             
            	}
            	else 
    				System.out.println("Loading triples! Wrong relation dictionary");
            }
            reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<Integer, HashMap<Integer,HashMap<Integer, HashSet<Integer>>>> getTripleHash(){
		return tripleHash;
	}
}
