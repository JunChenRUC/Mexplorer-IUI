package cn.edu.ruc.Mexplore.run;

import java.util.ArrayList;

import cn.edu.ruc.Mexplore.domain.Entity;
import cn.edu.ruc.Mexplore.domain.Feature;
import cn.edu.ruc.Mexplore.domain.Path;
import cn.edu.ruc.Mexplore.domain.Result;

public class Test {
	private static Process process = new Process();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub	
		pathFind();
		//resultFind();
	}
	
	public static void resultFind(){
		long beginTime = System.currentTimeMillis();
		
		String queryString = "forrest gump";
		
		Result result = null;
		result = process.getResult(queryString);
		
		System.out.println("Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
		
		System.out.println(result.getQueryEntity());
		
		for(Feature feature : result.getQueryFeatureList())
			System.out.println(feature);
		
		for(Entity entity : result.getResultEntityList())
			System.out.println(entity);

		for(Feature feature : result.getResultFeatureList())
			System.out.println(feature);
	}
	
	public static void pathFind(){
		long beginTime = System.currentTimeMillis();
		
		ArrayList<Path> pathList = null;
		pathList = process.getPath("forrest gump", "jfk (film)");
		
		System.out.println("Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
		
		for(Path path : pathList)
			System.out.println(path);
	}
}
