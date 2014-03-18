package LinGUIne.extensions;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * Visualizations are able to have a separate settings pane within the
 * application. Extending this class allows a a visualization to be edited by
 * the user via this pane.
 * 
 * @author Peter Dimou
 */
public abstract class VisualizationSettings extends ViewPart {

	/**
	 * Basic method to create a view within the application. This constructor
	 * will be called when the application tries to display this settings pane.
	 * 
	 * @param parent
	 *            The container that the application will attempt to hold this
	 *            view
	 */
	public abstract void createPartControl(Composite parent);

}
