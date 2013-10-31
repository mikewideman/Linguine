package LinGUIne.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *MockLibrary represents for the time being, the library plugin which will reside
 *in the analysis view for the sake of progress until library plugins are implemented 
 * @author Pete Maresca
 *
 */
public class MockLibrary {

	String libName;
	Map<String, String> featureFunctionMap =  new HashMap<String,String>();
	
	public MockLibrary(String libName, List<String> libFeatures, Map<String, String> featFunctions){
		this.libName = libName;
		this.featureFunctionMap = featFunctions;
	}
	
	public MockLibrary(){
		this.featureFunctionMap = new HashMap<String, String>();
	}
	
	public void setLibName(String libName) {
		this.libName = libName;
	}
	
	public String getLibName(){
		return this.libName;
	}
	
	public  Map<String,String> getFeatureFunctionMap(){
		return this.featureFunctionMap;
	}
	
	public void setFeatureFunctionMap(Map<String,String> feats){
		this.featureFunctionMap = feats;
	}


	
	public static ArrayList<MockLibrary> generateDemoLibs(){
		ArrayList<MockLibrary> demoLibs = new ArrayList<MockLibrary>();
		MockLibrary stan = new MockLibrary();
		MockLibrary other = new MockLibrary();
		stan.setLibName("Stanford NLP");
		other.setLibName("Library X");
		HashMap<String,String> stanFeatsAndScripts = new HashMap<String,String>();
		stanFeatsAndScripts.put("Part of Speech Analysis", "Stanford NLP PoS Algorithm");
		stanFeatsAndScripts.put("Normalization", "Stanford NLP Normalization Algorithm");
		stanFeatsAndScripts.put("Tokenization", "Stanford NLP Tokenization Algorithm, will separate file into tokens specified by delimiter");
		stanFeatsAndScripts.put("Sentiment Analysis", "Stanford NLP Sentiment Analysis");
		
		stan.setFeatureFunctionMap(stanFeatsAndScripts);
		
		HashMap<String,String> oFeatsAndScripts = new HashMap<String,String>();
		oFeatsAndScripts.put("Part of Speech Analysis", "Library X PoS Algorithm");
		oFeatsAndScripts.put("Normalization", "Library X Normalization Algorithm");
		oFeatsAndScripts.put("Tokenization", "Library X Tokenization Algorithm, will separate file into tokens specified by delimiter");
		oFeatsAndScripts.put("Sentiment Analysis", "Library X Sentiment Analysis");
		oFeatsAndScripts.put("New and Exciting Analysis", "Library X's New and Exciting Analysis!");
		
		other.setFeatureFunctionMap(oFeatsAndScripts);
		demoLibs.add(stan);
		demoLibs.add(other);
		
		return demoLibs;
		
	}




	

}
