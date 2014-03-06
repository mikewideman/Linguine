package LinGUIne.extensions;

/**
 * A Visualization Plugin consists of the provider (usually a library or 
 * software module such as JFreeChart or BIRT) and visualizations that 
 * the provider can produce (e.g., bar graphs, pie charts, etc.)
 * 
 * @author Peter Dimou
 */
public interface IVisualizationProvider {

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
	IVisualization[] getVisualizations();	
}
