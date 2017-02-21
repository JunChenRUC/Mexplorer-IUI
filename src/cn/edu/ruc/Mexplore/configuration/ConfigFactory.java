package cn.edu.ruc.Mexplore.configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class ConfigFactory{
	private static ConfigFactory instance = null;

	private Map<String, String> itemMap = new HashMap<String, String>();

	public static ConfigFactory getInstance(){
		if (instance == null)
			instance = new ConfigFactory();
		return instance;
	}

	private ConfigFactory(){
		String path = ConfigFactory.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		if(path.contains("WEB-INF"))
			path = path.substring(0, path.lastIndexOf("WEB-INF"));
		else
			path = path.substring(0, path.lastIndexOf("build")) + "WebContent";
		path = path + "/conf/configuration.xml";
		System.out.println("Read configuration from " + path);
		
		File xmlFile = new File(path);		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			doc = dBuilder.parse(xmlFile);
			
			itemMap.put("dir", doc.getElementsByTagName("dir").item(0).getTextContent());
			itemMap.put("model", doc.getElementsByTagName("model").item(0).getTextContent());
			itemMap.put("method", doc.getElementsByTagName("method").item(0).getTextContent());
			itemMap.put("D_entity", doc.getElementsByTagName("D_entity").item(0).getTextContent());
			itemMap.put("D_relation", doc.getElementsByTagName("D_relation").item(0).getTextContent());
			itemMap.put("output_size_entity", doc.getElementsByTagName("output_size_entity").item(0).getTextContent());
			itemMap.put("output_size_feature", doc.getElementsByTagName("output_size_feature").item(0).getTextContent());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String get(String itemName){
		return this.itemMap.get(itemName);
	}
}