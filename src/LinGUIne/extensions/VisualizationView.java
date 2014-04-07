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
	
	/**
	 * Returns whether this visualization has a settings pane.
	 * 
	 * @return True if this visualization can provide a settings pane to show to
	 *         the user, false otherwise
	 */
	public abstract boolean hasSettings();

	/**
	 * Returns the settings pane this visualization provides. If no settings
	 * pane is available, (hasSettings is false) then this method should return
	 * null.
	 * 
	 * @return The settings pane this visualization provides
	 */
	public abstract VisualizationSettings getSettings();

}