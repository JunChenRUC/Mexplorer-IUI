package cn.edu.ruc.Mexplore.utility;

import cn.edu.ruc.Mexplore.domain.Path;

public class QuickSortPath {
	private Path[] data; 
	private int length;
	
	public QuickSortPath(){
		
    } 
	
	public void initialSort(Path[] data){
		this.data = data;  
        this.length = data.length;
        buildSort(); 
	}
	
	public void buildSort(){
		while(true) {
	        boolean isEnd = true;
	        for(int i = 0; i < length - 1 ; i++) {
	        	Path before = data[i];
	        	Path behind = data[i + 1];
	        	
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
	
	public Path getMin(){
		return data[length - 1];
	}
	
	public void replace(Path tmp){
		data[length - 1] = tmp;
		buildSort();
	}
}
