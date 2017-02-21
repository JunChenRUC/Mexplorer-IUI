package cn.edu.ruc.Mexplore.index;

import cn.edu.ruc.Mexplore.run.Index;

public class IndexBuilder {
	private static String inputPath_abstract = "D://kb_data/instance_processed/entity2abstract.txt";
	private static String inputPath_entity = "D://kb_data/instance_processed/entity2id.txt";
	private static String outputPath = "D://kb_data/instance_processed/index";
	
	public static void main(String[] args) throws Exception {			
		buildIndex();
		searchIndex();	
	}
	
	public static void searchIndex(){
		// Now search the index:
		Index index = new Index();
		
		// Iterate through the results:
		for(String entity : index.getResult("forrest gump"))
			System.out.println(entity);	
	}
	
	public static void buildIndex(){
		IndexManager indexBuilder = new IndexManager();

		indexBuilder.buildIndexForAbstract(inputPath_abstract, outputPath);
		indexBuilder.buildIndexForEntity(inputPath_entity, outputPath);
		//indexBuilder.showIndex(outputPath);
	}

}
