package LinGUIne.extensions;

import java.util.Collection;

import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.Result;

/**
 * Visualizations can provide wizards. Extending this class allows this
 * visualization's wizard to be called from within the application.
 * 
 * @author Peter Dimou
 */
public abstract class VisualizationWizard extends Wizard {
	
	private Collection<Result> results;

	/**
	 * Provides the wizard with result objects.
	 * 
	 * @param results The results to build this wizard with
	 */
	public VisualizationWizard(Collection<Result> results) {
		this.results = results;
	}
	
	/**
	 * Gets the visualization view to be shown to the user.
	 * 
	 * @return The view to be shown to the user.
	 */
	public abstract VisualizationView getView();

}
