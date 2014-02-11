package LinGUIne.model;
/**
 * 
 * @author Pete Maresca
 * Interface defining methods for all analysis plugins
 * This package will have to be imported to any analysis plugin projects
 * 
 */
public interface IAnalysisPlugin {
	
	//Return the plugin name
	String getName();
	
	//Return the associated Binary Library
	String getAnalysisLibrary();
	
	//Returns more detailed data about the plugin- Not sure if necessary may be able to 
	//leverage Eclipse Runtime
	Object getPluginData();
	
	//The Analysis this plugin is responsible for executing
	Result runAnalysis();
	
	
}
