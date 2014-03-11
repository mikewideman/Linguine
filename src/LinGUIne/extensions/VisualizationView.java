/**
 * 
 */
package LinGUIne.extensions;

import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;

/**
 * Visualizations show themselves in a view. Extending this class allows a
 * visualization to be seen within the application.
 * 
 * @author Peter Dimou
 */
public abstract class VisualizationView {

	/**
	 * Basic method to create a view within the application. This constructor
	 * will be called when the application tries to display this visualization
	 * 
	 * @param parent The container that the application will attemp to hold this view
	 */
	@PostConstruct
	public abstract void createComposite(Composite parent);

}
