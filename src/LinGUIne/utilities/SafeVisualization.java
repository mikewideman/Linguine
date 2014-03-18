package LinGUIne.utilities;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.events.LinGUIneEvents;
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

	@Inject
	private IEventBroker eventBroker;

	private Shell shell;
	private IVisualization visualization;
	private Collection<Result> results;

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
	 * Runs the visualization over the results and then provides the output
	 * VisualizationWizard through a broadcasted event that can be picked up by
	 * any part listening for the event
	 */
	@Override
	public void run() throws Exception {
		VisualizationView view = visualization.runVisualization();

		VisualizationViewEvent viewEvent = new VisualizationViewEvent(view,
				visualization.getSettings(), visualization);

		eventBroker.post(LinGUIneEvents.UILifeCycle.VISUALIZATION_VIEW,
				viewEvent);
	}
}
