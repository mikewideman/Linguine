package LinGUIne.extensions;

import java.util.Collection;

import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.Result;
import LinGUIne.model.VisualResultContents;

/**
 * Represents an individual visualization.
 * 
 * @author Peter Dimou
 */
public interface IVisualization {

	/**
	 * Returns this visualization's name.
	 * 
	 * @return The name of this visualization.
	 */
	String getName();

	/**
	 * Returns the description for the visualization.
	 * 
	 * @return The description of the visualization.
	 */
	String getVisualizationDescription();

	/**
	 * Runs the specified visualization and returns the corresponding contents.
	 * 
	 * @return The contents to be displayed to the user.
	 */
	VisualResultContents runVisualization();

	/**
	 * Returns the Result types that this visualization supports.
	 * 
	 * @return A collection of result types that this visualization can be ran
	 *         with.
	 */
	Collection<Class<? extends Result>> getSupportedResultTypes();

	/**
	 * Set the results that this visualization needs in order to run.
	 * 
	 * @return True if the results were accepted, false otherwise.
	 */
	boolean setResults(Collection<Result> results);

	/**
	 * Returns the results that this visualization is currently holding on to.
	 * 
	 * @return A collection of results if the visualization has results, null
	 *         otherwise.
	 */
	Collection<Result> getResults();
	
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
	Wizard getWizard();
}
