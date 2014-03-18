package LinGUIne.events;

import LinGUIne.extensions.IVisualization;
import LinGUIne.extensions.VisualizationSettings;
import LinGUIne.extensions.VisualizationView;

/**
 * Event that contains the visualization view for whichever part wants to
 * display it.
 * 
 * @author Peter Dimou
 */
public class VisualizationViewEvent {

	private VisualizationView view;
	private VisualizationSettings settings;
	private IVisualization visualization;

	/**
	 * Creates the event with the required data
	 * 
	 * @param view
	 *            The view to be broadcasted
	 * @param settings
	 *            The settings that are tied to the view/visualization
	 * @param visualization
	 *            The visualization that generated the view
	 */
	public VisualizationViewEvent(VisualizationView view,
			VisualizationSettings settings, IVisualization visualization) {
		this.view = view;
		this.settings = settings;
		this.visualization = visualization;
	}

	/**
	 * Returns the broadcasted VisualizationView
	 * 
	 * @return The broadcasted VisualizationView
	 */
	public VisualizationView getView() {
		return view;
	}

	/**
	 * Returns the broadcasted IVisualization
	 * 
	 * @return The broadcasted IVisualization
	 */
	public IVisualization getVisualization() {
		return visualization;
	}

	/**
	 * Returns the broadcasted VisualizationSettings
	 * 
	 * @return The broadcasted VisualizationSettings
	 */
	public VisualizationSettings getSettings() {
		return settings;
	}
}
