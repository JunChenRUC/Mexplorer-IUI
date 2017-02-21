package cn.edu.ruc.Mexplore.utility;

import cn.edu.ruc.Mexplore.domain.Entity;

public class QuickSortEntity {
	private Entity[] data; 
	private int length;
	
	public QuickSortEntity(){
		
    } 
	
	public void initialSort(Entity[] data){
		this.data = data;  
        this.length = data.length;
        buildSort(); 
	}
	
	public void buildSort(){
		while(true) {
	        boolean isEnd = true;
	        for(int i = 0; i < length - 1 ; i++) {
	        	Entity before = data[i];
	        	Entity behind = data[i + 1];
	        	
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
	
	public Entity getMin(){
		return data[length - 1];
	}
	
	public void replace(Entity tmp){
		data[length - 1] = tmp;
		buildSort();
	}
}
