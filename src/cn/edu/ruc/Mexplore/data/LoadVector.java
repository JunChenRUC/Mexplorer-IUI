package cn.edu.ruc.Mexplore.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class LoadVector {
	private HashMap<Integer, double[]> entity2vector = new HashMap<>();
	private HashMap<Integer, double[]> relation2vector = new HashMap<>();
	private HashMap<Integer, double[][]> relation2transposition = new HashMap<>();
	
	public void loadEntityVector(String inputPath_eid2vector, HashMap<Integer, String> id2entity){
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPath_eid2vector)), "UTF-8"));
        	String tmpString = null;
            int count = 0;
            while((tmpString = reader.readLine()) != null) {
            	String[] tokens = tmpString.split("\t");

            	if(id2entity.containsKey(count)){
            		entity2vector.put(count, new double[DataManager.D_entity]);
            		for(int i = 0; i < DataManager.D_entity; i++)
                		entity2vector.get(count)[i] = Double.parseDouble(tokens[i]);
            	}
            	else 
            		System.out.println("Wrong entity dictionary");
            	
            	count ++;
            }
            reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadRelationVector(String inputPath_rid2vector, HashMap<Integer, String> id2relation){
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPath_rid2vector)), "UTF-8"));
        	String tmpString = null;
            int count = 0;
            while((tmpString = reader.readLine()) != null) {
            	String[] tokens = tmpString.split("\t");
            	
            	if(id2relation.containsKey(count)){
            		relation2vector.put(count, new double[DataManager.D_relation]);
    	        	for(int i = 0; i < DataManager.D_relation; i++)
    	        		relation2vector.get(count)[i] = Double.parseDouble(tokens[i]);
            	}
            	else 
    				System.out.println("Id: " + count +"\tWrong relation dictionary");
            	
            	count ++;
            }
            reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadTransposition(String inputPath_relation2transposition, HashMap<Integer, String> id2relation){
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPath_relation2transposition)), "UTF-8"));
        	String tmpString = null;
            int count = 0;
            while((tmpString = reader.readLine()) != null) {
            	String[] tokens = tmpString.split("\t");
            	
            	if(id2relation.containsKey(count)){
            		relation2transposition.put(count, new double[DataManager.D_entity][DataManager.D_relation]);
            		for(int i = 0; i < DataManager.D_entity; i ++)
            			for(int j = 0; j < DataManager.D_relation; j++)
            				relation2transposition.get(count)[i][j] = Double.parseDouble(tokens[i * DataManager.D_entity + j]);
            	}
            	else 
    				System.out.println("Id: " + count +"\tWrong relation dictionary");
            	
            	count ++;
            }
            reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<Integer, double[]> getEntity2Vector(){
		return entity2vector;
	}
	
	public HashMap<Integer, double[]> getRelation2Vector(){
		return relation2vector;
	}
	
	public HashMap<Integer, double[][]> getRelation2Transposition(){
		return relation2transposition;
	}
}
