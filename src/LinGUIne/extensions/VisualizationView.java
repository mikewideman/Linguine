package LinGUIne.extensions;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * Visualizations show themselves in a view. Extending this class allows a
 * visualization to be seen within the application.
 * 
 * @author Peter Dimou
 */
public abstract class VisualizationView extends ViewPart {

	/**
	 * Basic method to create a view within the application. This constructor
	 * will be called when the application tries to display this visualization
	 * 
	 * @param parent The container that the application will attempt to hold this view
	 */
	public abstract void createPartControl(Composite parent);

}
