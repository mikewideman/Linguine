package LinGUIne.utilities;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.events.LinGUIneEvents;
import LinGUIne.events.OpenProjectDataEvent;
import LinGUIne.extensions.IVisualization;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.Result;
import LinGUIne.model.VisualResult;
import LinGUIne.model.VisualResultContents;
import LinGUIne.model.Project.Subdirectory;

/**
 * Runnable which wraps the execution of an IVisualization in a safe fashion.
 * 
 * @author Peter Dimou
 */
public class SafeVisualization implements ISafeRunnable {

	private Shell shell;
	private IVisualization visualization;
	private Collection<Result> results;
	private Project project;
	private IEventBroker eventBroker;

	/**
	 * Constructs the object with necessary parameters for execution.
	 * 
	 * @param shell
	 *            The current shell.
	 * @param visualization
	 *            The visualization to be ran.
	 * @param results
	 *            The results to be used to run the visualization.
	 * @param project
	 *            The source and destination project.
	 * @param broker
	 *			  The broker to post the event to.
	 */
	public SafeVisualization(Shell shell, IVisualization visualization,
			Collection<Result> results, Project project, IEventBroker broker) {
		this.shell = shell;
		this.visualization = visualization;
		this.results = results;
		this.project = project;
		eventBroker = broker;
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
	 * VisualResultContents are then created and saved to the project. An event
	 * is then posted with the contents.
	 */
	@Override
	public void run() throws Exception {
		visualization.setResults(results);
		VisualResultContents contents = visualization.runVisualization();

		String resultFileName = visualization.getName() + "_" +
				Calendar.getInstance().getTimeInMillis() + ".vis";
		

		File resultFile = project.getSubdirectory(Subdirectory.Results)
				.append(resultFileName).toFile();
		VisualResult result = new VisualResult(resultFile);
		result.updateContents(contents);

		// Build a new collection since IProjectData is generic and you cannot
		// cast collections
		Collection<IProjectData> sourceResults = new LinkedList<IProjectData>();
		sourceResults.addAll(results);
		project.addResult(result, sourceResults);
		
		eventBroker.post(LinGUIneEvents.UILifeCycle.OPEN_PROJECT_DATA,
				new OpenProjectDataEvent(result, project));
	}
}
