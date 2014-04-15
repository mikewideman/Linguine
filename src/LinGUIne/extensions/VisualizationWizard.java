package LinGUIne.extensions;

import org.eclipse.jface.wizard.Wizard;

/**
 * Visualizations can provide wizards. Extending this class allows this
 * visualization's wizard to be called from within the application.
 * 
 * @author Peter Dimou
 */
public abstract class VisualizationWizard extends Wizard {
	
	private IVisualization visualization;
	
	/**
	 * Provides the wizard with the corresponding IVisualization
	 * 
	 * @param visualization The visualization to build this wizard with
	 */
	public VisualizationWizard(IVisualization visualization) {
		this.visualization = visualization;
	}

}