package LinGUIne.extensions;

/**
 * A Visualization Plugin consists of the provider (usually a library or 
 * software module such as JFreeChart or BIRT) and visualizations that 
 * the provider can produce (e.g., bar graphs, pie charts, etc.)
 * 
 * @author Peter Dimou
 */
public interface IVisualizationPlugin {

	/**
	 * Get the visualization provider's name
	 * 
	 * @return The visualization provider's name
	 */
	String getName();
	
	/**
	 * Get the list of all visualizations that this provider can produce.
	 * 
	 * @return The list of all visualizations that this plugin provides.
	 */
	String[] getVisualizations();
	
	/**
	 * Returns the description for the requested visualization
	 * 
	 * @param visualizationName The name of the visualization as provided by
	 * the method getVisualizations.
	 * @return The description of the visualization provided by the library
	 */
	String getVisualizationDescription(String visualizationName);
	
	/**
	 * Runs the specified visualization. The name of the visualization comes
	 * from the provider by using the getVisualizations method.
	 * 
	 * @param visualizationName The name of the visualization as provided by
	 * the method getVisualizations.
	 */
	void runVisualization(String visualizationName);
	
}
