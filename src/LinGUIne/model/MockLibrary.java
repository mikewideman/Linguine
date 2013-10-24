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
	List<String> libFeatures;
	Map<String, List<String>> featureFunctionMap =  new HashMap<String,List<String>>();
	
	public MockLibrary(String libName, List<String> libFeatures, Map<String, List<String>> featFunctions){
		this.libName = libName;
		this.featureFunctionMap = featFunctions;
	}
	
	public MockLibrary(){
		this.libFeatures = new ArrayList<String>();
		this.featureFunctionMap = new HashMap<String, List<String>>();
	}
	
	public void setLibName(String libName) {
		this.libName = libName;
	}

	public List<String> getLibFeatures() {
		return libFeatures;
	}

	public void setLibFeatures(List<String> libFeatures) {
		this.libFeatures = libFeatures;
	}

	public Map<String, List<String>> getFeatureFunctionMap() {
		return this.featureFunctionMap;
	}

	public void setFeatureFunctionMap(Map<String, List<String>> featureFunctions) {
		this.featureFunctionMap = featureFunctions;
	}
	
	public void addLibFeature(String nFeat){
		sortAdd(nFeat, this.libFeatures) ;
	}
	/**
	 * list add function sorted by index where i 0,1,2 etc. = Aaron, Abel, Bob etc.
	 * If two words are equivalent according to String.compareTo, the newest word takes precedence
	 * @param input the string being added to the list.
	 * @param libFeatures - the list being added to.
	 */
	public void sortAdd(String input, List<String> libFeatures){
		for(String libFeature : libFeatures){
			int compareValue = input.compareTo(libFeature);
			if(compareValue >= 0){
				libFeatures.add(libFeatures.indexOf(libFeature), input);
				return;
			}
		}
		libFeatures.add(libFeatures.size(), input);
	}
	

}
