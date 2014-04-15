package LinGUIne.utilities;

import java.util.Collection;

import org.eclipse.core.runtime.ISafeRunnable;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.events.VisualizationViewEvent;
import LinGUIne.extensions.IVisualization;
import LinGUIne.extensions.VisualizationView;
import LinGUIne.model.Result;

/**
 * Runnable which wraps the execution of an IVisualization in a safe fashion.
 * 
 * @author Peter Dimou
 */
public class SafeVisualization implements ISafeRunnable {

	private Shell shell;
	private IVisualization visualization;
	private Collection<Result> results;

	/**
	 * Constructs the object with necessary parameters for execution.
	 * 
	 * @param shell
	 *            The current shell
	 * @param visualization
	 *            The visualization to be ran
	 * @param results
	 *            The results to be used to run the visualization
	 */
	public SafeVisualization(Shell shell, IVisualization visualization,
			Collection<Result> results) {
		this.shell = shell;
		this.visualization = visualization;
		this.results = results;
	}

	/**
	 * Raises an error dialog in the event of an exception during execution.
	 */
	@Override
	public void handleException(Throwable exception) {
		MessageDialog.open(SWT.ERROR, shell, "Error",
				"An error occurred while attempting to run the visualization",
				SWT.NONE);
	}

	/**
	 * Runs the visualization with the results supplied. VisualResults and
	 * VisualResultContents are then created and saved to the project to be
	 * displayed later to the user.
	 */
	@Override
	public void run() throws Exception {
		// TODO: refactor to conform to VisualResult/VisualResultContents design
		visualization.setResults(results);
		VisualizationView view = visualization.runVisualization();

		VisualizationViewEvent viewEvent = new VisualizationViewEvent(view,
				visualization);
	}
}