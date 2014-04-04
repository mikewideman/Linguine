package LinGUIne.utilities;

import java.util.Collection;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

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
		MessageDialog.open(SWT.OK, shell, "Error",
				"An error occurred while attempting to run the visualization",
				SWT.NONE);
	}

	/**
	 * Runs the visualization over the results and then provides the output
	 * VisualizationWizard to <TO BE DETERMINED>
	 */
	@Override
	public void run() throws Exception {
		VisualizationView output = visualization.runVisualization();
		/*
		 * TODO: Incomplete. The view needs to be collected by a separate piece.
		 * In addition, a solution needs to be found to handle visualization
		 * settings.
		 */
	}
}
