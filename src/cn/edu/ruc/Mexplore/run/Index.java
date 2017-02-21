package cn.edu.ruc.Mexplore.run;

import java.net.URLDecoder;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import cn.edu.ruc.Mexplore.configuration.ConfigFactory;
import cn.edu.ruc.Mexplore.index.IndexManager;

public class Index {
	private static IndexManager indexBuilder;
	private static IndexSearcher searcher;
	private static int K;
	
	public Index(){
		K = Integer.parseInt(ConfigFactory.getInstance().get("output_size_entity"));
		indexBuilder = new IndexManager();
		String inputPath = ConfigFactory.getInstance().get("dir") + "index";
		searcher = new IndexSearcher(indexBuilder.getIndexReader(inputPath));
	}
	
	public ArrayList<String> getResult(String keywords){
		ArrayList<String> entityList = new ArrayList<>();
		QueryParser parser_name = new QueryParser("name", new StandardAnalyzer());	
		Query query_name;
		
		try {
			query_name = parser_name.parse(keywords);
			for (ScoreDoc scoreDoc : searcher.search(query_name, K).scoreDocs){
				Document document = searcher.doc(scoreDoc.doc);
				entityList.add(URLDecoder.decode(document.get("name"), "UTF-8"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		if(entityList.size() < K) {
			QueryParser parser_context = new QueryParser("context", new StandardAnalyzer());
			Query query_context;
			try {
				query_context = parser_context.parse("forrest gump");
				for (ScoreDoc scoreDoc : searcher.search(query_context, K).scoreDocs){
					Document document = searcher.doc(scoreDoc.doc);
					entityList.add(URLDecoder.decode(document.get("name"), "UTF-8"));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return entityList;
	}
}
