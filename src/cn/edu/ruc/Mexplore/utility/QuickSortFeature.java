package cn.edu.ruc.Mexplore.utility;

import cn.edu.ruc.Mexplore.domain.Feature;

public class QuickSortFeature {
	private Feature[] data; 
	private int length;
	
	public QuickSortFeature(){
		
    } 
	
	public void initialSort(Feature[] data){
		this.data = data;  
        this.length = data.length;
        buildSort(); 
	}
	
	public void buildSort(){
		while(true) {
	        boolean isEnd = true;
	        for(int i = 0; i < length - 1 ; i++) {
	        	Feature before = data[i];
	        	Feature behind = data[i + 1];
	        	
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
	
	public Feature getMin(){
		return data[length - 1];
	}
	
	public void replace(Feature tmp){
		data[length - 1] = tmp;
		buildSort();
	}
}
