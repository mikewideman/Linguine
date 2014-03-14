package LinGUIne.wizards;

import java.util.Collection;
import java.util.LinkedList;

import LinGUIne.extensions.IVisualization;
import LinGUIne.model.Project;
import LinGUIne.model.Result;

/**
 * Used to carry data between pages in the VisualizationWizard.
 * 
 * @author Peter Dimou
 */
public class VisualizationData {
	private Project chosenProject;
	private Collection<Result> chosenProjectResults;
	private IVisualization chosenVisualization;

	/**
	 * Creates a default empty instance.
	 */
	public VisualizationData() {
		chosenProject = null;
		chosenProjectResults = new LinkedList<Result>();
	}

	/**
	 * Sets the Project that was chosen.
	 * 
	 * @param chosenProject
	 *            The project to set this object to
	 */
	public void setChosenProject(Project chosenProject) {
		this.chosenProject = chosenProject;
	}

	/**
	 * Returns the currently chosen Project.
	 * 
	 * @return The project currently chosen.
	 */
	public Project getChosenProject() {
		return chosenProject;
	}

	/**
	 * Returns a list of the currently chosen Results (if any).
	 * 
	 * @return The list of results associated with this object.
	 */
	public Collection<Result> getChosenResults() {
		return chosenProjectResults;
	}

	/**
	 * Returns the result classes that the user chose. Duplicates are allowed.
	 * 
	 * @return A collection of classes of results based on the result types that
	 *         the user selected.
	 */
	public Collection<Class<? extends Result>> getChosenResultTypes() {
		Collection<Class<? extends Result>> retVal = new LinkedList<Class<? extends Result>>();

		for (Result resultClass : chosenProjectResults) {
			retVal.add(resultClass.getClass());
		}

		return retVal;
	}

	/**
	 * Sets the currently chosen Results to the given list. List may be empty.
	 * 
	 * @param chosenResults
	 *            The results to associate with this object.
	 */
	public void setChosenResults(Collection<Result> chosenResults) {
		chosenProjectResults = chosenResults;
	}

	/**
	 * Returns the currently chosen Visualization.
	 * 
	 * @return The current visualization associated with this object.
	 */
	public IVisualization getChosenVisualization() {
		return chosenVisualization;
	}

	/**
	 * Sets the currently chosen Visualization to a given one.
	 * 
	 * @param visualization
	 *            The visualization to associate with this object
	 */
	public void setChosenVisualization(IVisualization visualization) {
		chosenVisualization = visualization;
	}
}
