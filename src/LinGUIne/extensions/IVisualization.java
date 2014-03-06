package LinGUIne.extensions;

/**
 * Represents an individual visualization (such as a bar graph or pie chart).
 * 
 * @author Peter Dimou
 */
public interface IVisualization {
	
	/**
	 * Returns the description for the visualization
	 * 
	 * @return The description of the visualization
	 */
	String getVisualizationDescription();
	
	/**
	 * Runs the specified visualization.
	 */
	void runVisualization();

}
