package LinGUIne.events;

import LinGUIne.extensions.IVisualization;
import LinGUIne.extensions.VisualizationView;

/**
 * Event that contains the visualization view for whichever part wants to
 * display it.
 * 
 * @author Peter Dimou
 */
public class VisualizationViewEvent {

	private VisualizationView view;
	private IVisualization visualization;

	/**
	 * Creates the event with the required data
	 * 
	 * @param view
	 *            The view to be broadcasted
	 * @param visualization
	 *            The visualization that generated the view
	 */
	public VisualizationViewEvent(VisualizationView view,
			IVisualization visualization) {
		this.view = view;
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
}
