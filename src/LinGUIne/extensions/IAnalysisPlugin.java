package LinGUIne.extensions;

import java.util.Collection;

import LinGUIne.model.IProjectData;
import LinGUIne.model.Result;

/**
 * Interface defining methods for all AnalysisPlugins.
 * This package will have to be imported to any AnalysisPlugin projects.
 * 
 * @author Pete Maresca
 * @author Kyle Mullins
 */
public interface IAnalysisPlugin {
	
	/**
	 * Returns the name of the plugin.
	 */
	String getName();
	
	/**
	 * Returns the name of the associated Binary Software Module.
	 */
	String getAnalysisLibrary();
	
	/**
	 * Returns a text description of this AnalysisPlugin.
	 */
	String getAnalysisDescription();
	
	/**
	 * Returns more detailed data about this AnalysisPlugin if necessary.
	 */
	Object getPluginData();
	
	/**
	 * Runs the analysis this AnalysisPlugin is responsible for executing.
	 * 
	 * @param sourceData
	 * 
	 * @return
	 */
	Collection<Result> runAnalysis(Collection<IProjectData> sourceData);
	
	/**
	 * Returns a collection of all of the original (i.e. non-Result) ProjectData
	 * Types that this AnalysisPlugin can be run with.
	 * Note: The Types returned should be as specific as possible.
	 */
	Collection<Class<? extends IProjectData>> getSupportedSourceDataTypes();

	/**
	 * Returns a collection of all of the Result object Types that are required
	 * in order to run this AnalysisPlugin or an empty collection if no prior
	 * Results are needed.
	 * Note: The Types returned should be as specific as possible.
	 */
	Collection<Class<? extends Result>> getRequiredResultTypes();
	
	/**
	 * Returns the Type of Result object that is returned by running this
	 * AnalysisPlugin.
	 * Note: The Type returned should be as specific as possible.
	 */
	Class<? extends Result> getReturnedResultType();
}
