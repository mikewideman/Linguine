package LinGUIne.extensions;

import java.util.Collection;
import LinGUIne.model.Result;

/**
 * Represents an individual visualization (such as a bar graph or pie chart).
 * 
 * @author Peter Dimou
 */
public interface IVisualization {

	/**
	 * Returns this visualization's name.
	 * 
	 * @return The name of this visualization
	 */
	String getName();

	/**
	 * Returns the description for the visualization
	 * 
	 * @return The description of the visualization
	 */
	String getVisualizationDescription();

	/**
	 * Runs the specified visualization and gets the view.
	 * 
	 * @return The view to be displayed to the user.
	 */
	VisualizationView runVisualization();

	/**
	 * Returns the Result types that this visualization supports.
	 * 
	 * @return A collection of result types that this visualization can be ran
	 *         with
	 */
	Collection<Class<? extends Result>> getSupportedResultTypes();

	/**
	 * Returns whether this visualization has a wizard.
	 * 
	 * @return True if this visualization can provide a wizard to run, false
	 *         otherwise
	 */
	boolean hasWizard();

	/**
	 * Returns the wizard this visualization provides. If no wizard is
	 * available, (hasWizard is false) then this method should return null.
	 * 
	 * @return The wizard this visualization provides
	 */
	VisualizationWizard getWizard();
}
