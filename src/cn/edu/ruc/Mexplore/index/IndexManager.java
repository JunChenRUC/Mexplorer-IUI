package cn.edu.ruc.Mexplore.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.HashSet;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

public class IndexManager {
	private HashSet<String> entitySet = new HashSet<>();
	
	public IndexManager(){
		
	}
	
	public Document createAbstract(String tmpString) throws IOException{
		String[] tokens = tmpString.split("\t");	
    	Document document = new Document();
    	
    	String name = URLDecoder.decode(tokens[0].replaceAll("_", " "), "UTF-8");
    	document.add(new Field("name", name, TextField.TYPE_STORED));
    	document.add(new Field("context", tokens[1], TextField.TYPE_STORED));
    	
    	return document;
	}
	
	public Document createEntity(String tmpString) throws IOException{
		String[] tokens = tmpString.split("\t");	
    	Document document = new Document();
    	
    	String name = URLDecoder.decode(tokens[0].replaceAll("_", " "), "UTF-8");
    	document.add(new Field("name", name, TextField.TYPE_STORED));
    	
    	return document;
	}
	
	public void buildIndexForAbstract(String inputPath, String outputPath){
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
	    indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
	    
		BufferedReader reader;
		IndexWriter indexWriter;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPath)), "UTF-8"));
			indexWriter = new IndexWriter(FSDirectory.open(Paths.get(outputPath)), indexWriterConfig);
        	
			String tmpString = null;
        	while((tmpString = reader.readLine()) != null){
        		entitySet.add(tmpString.split("\t")[0]);
            	indexWriter.addDocument(createAbstract(tmpString));
        	}
			reader.close();
			indexWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void buildIndexForEntity(String inputPath, String outputPath){
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
	    indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
	    
		BufferedReader reader;
		IndexWriter indexWriter;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPath)), "UTF-8"));
			indexWriter = new IndexWriter(FSDirectory.open(Paths.get(outputPath)), indexWriterConfig);
        	
			String tmpString = null;
        	while((tmpString = reader.readLine()) != null){
        		if(!entitySet.contains(tmpString.split("\t")[0]))
        			indexWriter.addDocument(createEntity(tmpString));
        	}
        	
			reader.close();
			indexWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showIndex(String inputPath) {  
        System.out.println("*****************Read index**********************");
        DirectoryReader directoryReader;
		try {
			directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(inputPath)));
			int docLength = directoryReader.maxDoc();  
			Document document;
			 for (int i = 0; i < docLength; i++) { 
				document = directoryReader.document(i);
				System.out.println("No. " + (i + 1) + "\tid: " + document.get("id") + "\tname: " + document.get("name") + "\tcontext: " + document.get("context"));
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println("*****************Finish**********************\n");  
    } 
	
	public DirectoryReader getIndexReader(String inputPath) {
		DirectoryReader directoryReader = null;
		
		try {
			directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(inputPath)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return directoryReader;
	}
}
