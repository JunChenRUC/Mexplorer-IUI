package cn.edu.ruc.Mexplore.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;


public class LoadDictionary {
	private HashMap<String, Integer> entity2id = new HashMap<>();
	private HashMap<Integer, String> id2entity = new HashMap<>();
	private HashMap<String, Integer> relation2id = new HashMap<>();
	private HashMap<Integer, String> id2relation = new HashMap<>();
	private HashMap<Integer, HashMap<Integer, String>> relation2concept = new HashMap<>();
	
	public void loadEntity2Id(String inputPath_entity2id){
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPath_entity2id)), "UTF-8"));
			String tmpString = null;
			while((tmpString = reader.readLine()) != null) {
				String[] tokens = tmpString.split("\t");
				
				String name = URLDecoder.decode(tokens[0].replaceAll("_", " "), "UTF-8");
				//if(tokens[0].startsWith("category:"))
					//name = name.replaceAll("category:", "");
				
				entity2id.put(name, Integer.parseInt(tokens[1]));
				id2entity.put(Integer.parseInt(tokens[1]), name);	
			}
			reader.close();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadRelation2Id(String inputPath_relation2id){
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPath_relation2id)), "UTF-8"));		
        	String tmpString = null;
            while((tmpString = reader.readLine()) != null) {
            	String[] tokens = tmpString.split("\t");
            	
            	relation2id.put(tokens[0], Integer.parseInt(tokens[1]));
            	id2relation.put(Integer.parseInt(tokens[1]), tokens[0]);
            }
            reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadRelation2Concept(String inputPath_relation2concept){
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPath_relation2concept)), "UTF-8"));		
        	String tmpString = null;
            while((tmpString = reader.readLine()) != null) {
            	String[] tokens = tmpString.split("\t");
            	
            	String domain = tokens[0];
            	String relation = tokens[1];
            	String range = tokens[2];
            	
            	relation2concept.put(relation2id.get(relation), new HashMap<>());
				relation2concept.get(relation2id.get(relation)).put(0, range);
				relation2concept.get(relation2id.get(relation)).put(1, domain);
            }
            reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<String, Integer> getEntity2Id(){
		return entity2id;
	}
	
	public HashMap<String, Integer> getRelation2Id(){
		return relation2id;
	}
	
	public HashMap<Integer, String> getId2Entity(){
		return id2entity;
	}
	
	public HashMap<Integer, String> getId2Relation(){
		return id2relation;
	}
	
	public HashMap<Integer, HashMap<Integer, String>> getRelation2Concept(){
		return relation2concept;
	}
}
