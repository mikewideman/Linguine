package LinGUIne.utilities;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.extensions.IAnalysisPlugin;
import LinGUIne.model.IProjectData;
import LinGUIne.model.IProjectDataContents;
import LinGUIne.model.Project;
import LinGUIne.model.Project.Subdirectory;
import LinGUIne.model.Result;

/**
 * Runnable which wraps the execution of an IAnalysisPlugin in a safe fashion.
 * 
 * @author Kyle Mullins
 */
public class SafeAnalysis implements ISafeRunnable {

	private Shell shell;
	private IAnalysisPlugin analysisPlugin;
	private Collection<IProjectData> sourceData;
	private Project destProject;
	
	/**
	 * Creates a new SafeAnalysis which runs the given analysis over the given
	 * ProjectData sources and places the Results in the given Project.
	 * 
	 * @param theShell	The current Shell; used to display error dialogs.
	 * @param analysis	The IAnalysisPlugin to be used for the analysis job.
	 * @param sources	The IProjectData that are to be analyzed.
	 * @param proj		The Project into which the Analysis Result(s) are to
	 * 					be placed.
	 */
	public SafeAnalysis(Shell theShell, IAnalysisPlugin analysis,
			Collection<IProjectData> sources, Project proj){
		
		shell = theShell;
		analysisPlugin = analysis;
		sourceData = sources;
		destProject = proj;
	}
	
	/**
	 * Raises an error dialog in the event of an exception during execution.
	 */
	@Override
	public void handleException(Throwable exception) {
		MessageDialog.open(SWT.OK, shell, "Error",
				"An error occurred while performing analysis.", SWT.NONE);
	}

	/**
	 * Runs analysisPlugin over sourceData and places the Result(s) into
	 * destProject.
	 */
	@Override
	public void run() throws Exception {
		Class<? extends Result> returnedResult =
				analysisPlugin.getReturnedResultType();
		Collection<IProjectDataContents> resultContents;
		
		resultContents = analysisPlugin.runAnalysis(sourceData);
		
		String resultFileName = analysisPlugin.getName() + "_" +
				Calendar.getInstance().getTimeInMillis();
		int contentsCount = 1;
		
		for(IProjectDataContents contents: resultContents){
			if(contentsCount > 1){
				resultFileName += " (" + contentsCount + ")";
			}
			
			resultFileName += ".res";

			File resultFile = destProject.getSubdirectory(Subdirectory.Results).append(resultFileName).toFile();
			Result result = Result.createResult(returnedResult, resultFile);
			
			result.updateContents(contents);
			destProject.addResult(result, sourceData);
			
			contentsCount++;
		}
		
		//TODO: Error handling/liveness
	}
}
