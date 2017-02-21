package cn.edu.ruc.Mexplore.utility;

import cn.edu.ruc.Mexplore.domain.Relation;

public class QuickSortRelation {
	private Relation[] data; 
	private int length;
	
	public QuickSortRelation(){
		
    } 
	
	public void initialSort(Relation[] data){
		this.data = data;  
        this.length = data.length;
        buildSort(); 
	}
	
	public void buildSort(){
		while(true) {
	        boolean isEnd = true;
	        for(int i = 0; i < length - 1 ; i++) {
	        	Relation before = data[i];
	        	Relation behind = data[i + 1];
	        	
	        	if(before.getScore() < behind.getScore()) {
	        		data[i] = behind;
	        		data[i + 1] = before;
	        		isEnd = false; 
	        		continue;
	        	} else if (i == length - 1) {
	        		isEnd = true;
	        	}
	        }
	        if(isEnd) {
	        	break;
        	}
		}
	}
	
	public Relation getMin(){
		return data[length - 1];
	}
	
	public void replace(Relation tmp){
		data[length - 1] = tmp;
		buildSort();
	}
}
